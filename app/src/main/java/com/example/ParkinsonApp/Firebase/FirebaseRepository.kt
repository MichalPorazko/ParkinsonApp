package com.example.ParkinsonApp.Firebase

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.ParkinsonApp.DataTypes.Patient
import com.example.ParkinsonApp.DataTypes.UserType
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User


class FirebaseRepository {

    private val context: Context? = null

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    fun login(email: String, password: String) {

        _authState.value = AuthState.Loading
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _authState.value = AuthState.Authenticated
            } else {
                _authState.value = AuthState.Error(
                    task.exception?.message ?: "An error occurred during login"
                )
            }
        }
    }

    fun signup(
        email: String,
        password: String,
        userType: String,
        additionalInfo: Map<String, String>
    ) {
        _authState.value = AuthState.Loading
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = task.result.user?.uid
                    if (uid == null) {
                        Log.e("FirebaseRepository", "User UID is null.")
                        // Handle the error
                    }
                    Log.d("FirebaseRepository", "User UID: $uid")
                    Log.d("FirebaseRepository", "Signup successful. User UID: $uid")

                    when(userType){
                        "doctor" -> {
                            val newDoctor = UserType.DoctorUser(email, password, additionalInfo["firstName"]!!, additionalInfo["lastName"]!!)
                            db.collection(userType).document(uid!!).set(newDoctor).addOnSuccessListener {
                                Log.d("FirebaseRepository", "User info successfully written to Firestore")
                                _authState.value = AuthState.Authenticated
                            }
                                .addOnFailureListener { e ->
                                    Log.e("FirebaseRepository", "Error writing user info to Firestore", e)
                                    _authState.value = AuthState.Error(e.message ?: "Failed to save user info")
                                }
                        }

                        "patient" -> {
                            val newPatient = UserType.PatientUser(email, password, additionalInfo["firstName"]!!, additionalInfo["lastName"]!!)
                            db.collection(userType).document(uid!!).set(newPatient).addOnSuccessListener {
                                Log.d("FirebaseRepository", "User info successfully written to Firestore")
                                _authState.value = AuthState.Authenticated
                            }
                                .addOnFailureListener { e ->
                                    Log.e("FirebaseRepository", "Error writing user info to Firestore", e)
                                    _authState.value = AuthState.Error(e.message ?: "Failed to save user info")
                                }
                        }
                    }
                } else {
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    if (task.exception is FirebaseAuthWeakPasswordException) {
                        Toast.makeText(context, "Password should be at least 6 characters",
                            Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    }
                    Log.e("FirebaseRepository", "Signup failed", task.exception)
                    _authState.value = AuthState.Error(
                        task.exception?.message ?: "An error occurred during signup"
                    )
                }
            }
    }

    

    fun signOut() {
        auth.signOut()
        _authState.value = AuthState.Unauthenticated
    }

    fun refreshData() {
        db.collection("users").get()
    }

    suspend fun getPatients(): List<Patient> {
        // Implement logic to fetch patients from Firebase
        // Return a list of Patient objects
        return listOf(
            Patient(id = "1", name = "John Doe", age = 65),
            Patient(id = "2", name = "Jane Smith", age = 70)
            // Add more patients as needed
        )
    }

    fun getPatientData(uid: String, onComplete: (UserType?) -> Unit) {
        db.collection("users").addSnapshotListener{ snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }
            if (snapshot != null) {
                for (doc in snapshot) {
                    Log.d(TAG, "${doc.id} => ${doc.data}")
                }
            } else {
                Log.d(TAG, "Current data: null")
            }

        }
    }

    suspend fun getPatientById(patientId: String): Patient? {
        // Implement logic to fetch a patient by ID from Firebase
        // Return the Patient object or null if not found
        return Patient(id = "1", name = "John Doe", age = 65)
    }

    sealed class AuthState {
        object Authenticated : AuthState()
        object Unauthenticated : AuthState()
        object Loading : AuthState()
        data class Error(val message: String) : AuthState()
    }

   /* fun getUserData(uid: String, onComplete: (UserType?) -> Unit) {
        db.collection("users").document(uid)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val userTypeString = document.getString("userType")
                    val email = document.getString("email") ?: ""
                    val password = "" // You typically won't retrieve passwords
                    val firstName = document.getString("firstName") ?: ""
                    val lastName = document.getString("lastName") ?: ""

                    val userType = when (userTypeString) {
                        "patient" -> {
                            val patientDataMap = document.get("patientData") as? Map<*, *>
                            val patientData = parsePatientData(patientDataMap)
                            UserType.PatientUser(email, password, firstName, lastName, patientData)
                        }
                        "doctor" -> {
                            val doctorDataMap = document.get("doctorData") as? Map<*, *>
                            val doctorData = parseDoctorData(doctorDataMap)
                            UserType.DoctorUser(email, password, firstName, lastName, doctorData)
                        }
                        else -> null
                    }
                    onComplete(userType)
                } else {
                    onComplete(null)
                }
            }
            .addOnFailureListener { e ->
                Log.e("FirebaseRepository", "Error retrieving user data", e)
                onComplete(null)
            }
    }*/
}


