package com.example.ParkinsonApp.Firebase

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.ParkinsonApp.DataTypes.DoctorData
import com.example.ParkinsonApp.DataTypes.DoctorDataWithId
import com.example.ParkinsonApp.DataTypes.Emotion
import com.example.ParkinsonApp.DataTypes.EmotionState
import com.example.ParkinsonApp.DataTypes.Hour
import com.example.ParkinsonApp.DataTypes.Medication
import com.example.ParkinsonApp.DataTypes.MedicationSchedule
import com.example.ParkinsonApp.DataTypes.PatientAction
import com.example.ParkinsonApp.DataTypes.PatientData
import com.example.ParkinsonApp.DataTypes.PatientDataWithId
import com.example.ParkinsonApp.DataTypes.User
import com.example.ParkinsonApp.DataTypes.UserType
import com.example.ParkinsonApp.DataTypes.Water
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore


class FirebaseRepository {

    private val context: Context? = null

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    private val actionsCollection = db.collection("patient_actions")



    fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }

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
                            val newDoctor = UserType.DoctorUser(email, DoctorData(additionalInfo["firstName"]!!, additionalInfo["lastName"]!!))
                            db.collection("users").document(uid!!).set(newDoctor).addOnSuccessListener {
                                Log.d("FirebaseRepository", "User info successfully written to Firestore")
                                _authState.value = AuthState.Authenticated
                            }
                                .addOnFailureListener { e ->
                                    Log.e("FirebaseRepository", "Error writing user info to Firestore", e)
                                    _authState.value = AuthState.Error(e.message ?: "Failed to save user info")
                                }
                        }

                        "patient" -> {
                            val newPatient = UserType.PatientUser(email, PatientData(additionalInfo["firstName"]!!, additionalInfo["lastName"]!!))
                            db.collection("users").document(uid!!).set(newPatient).addOnSuccessListener {
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
                    } else {
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

    fun getPatientData(userId: String, onComplete: (PatientDataWithId?) -> Unit) {
        db.collection("users").document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val user = document.toObject(User::class.java)
                    if (user != null && user.userType == "patient" && user.patientData != null) {
                        val patientDataWithId = PatientDataWithId(
                            id = userId,
                            data = user.patientData
                        )
                        onComplete(patientDataWithId)
                    } else {
                        // Handle if user is not a patient
                        onComplete(null)
                    }
                } else {
                    // Document doesn't exist
                    onComplete(null)
                }
            }
            .addOnFailureListener { e ->
                Log.e("FirebaseRepository", "Error getting patient data", e)
                onComplete(null)
            }
    }

    fun getDoctorData(userId: String, onComplete: (DoctorDataWithId?) -> Unit) {
        db.collection("users").document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val user = document.toObject(User::class.java)
                    if (user != null && user.userType == "doctor" && user.doctorData != null) {
                        val doctorDataWithId = DoctorDataWithId(
                            id = userId,
                            data = user.doctorData
                        )
                        onComplete(doctorDataWithId)
                    } else {
                        // Handle if user is not a doctor
                        onComplete(null)
                    }
                } else {
                    // Document doesn't exist
                    onComplete(null)
                }
            }
            .addOnFailureListener { e ->
                Log.e("FirebaseRepository", "Error getting doctor data", e)
                onComplete(null)
            }
    }

    fun getPatientsData(patientIds: List<String>, onComplete: (List<PatientDataWithId>) -> Unit) {
        if (patientIds.isEmpty()) {
            onComplete(emptyList())
            return
        }

        db.collection("users")
            .whereIn(FieldPath.documentId(), patientIds)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val patients = querySnapshot.documents.mapNotNull { doc ->
                    val user = doc.toObject(User::class.java)
                    if (user != null && user.userType == "patient" && user.patientData != null) {
                        PatientDataWithId(
                            id = doc.id,
                            data = user.patientData
                        )
                    } else {
                        null
                    }
                }
                onComplete(patients)
            }.addOnFailureListener { e ->
                Log.e("FirebaseRepository", "Error retrieving patients", e)
                onComplete(emptyList())
            }
    }

    sealed class AuthState {
        object Authenticated : AuthState()
        object Unauthenticated : AuthState()
        object Loading : AuthState()
        data class Error(val message: String) : AuthState()
    }

    fun updateWaterIntake(patientId: String, newAmount: Int, onComplete: (Boolean) -> Unit) {
        db.collection("users").document(patientId)
            .update("patientData.waterIntake.amount", newAmount)
            .addOnSuccessListener {
                onComplete(true)
            }
            .addOnFailureListener { e ->
                Log.e("FirebaseRepository", "Error updating water intake", e)
                onComplete(false)
            }
    }

    fun updateEmotions(patientId: String, emotions: List<Emotion>, onComplete: (Boolean) -> Unit) {
        val emotionsData = emotions
        db.collection("users").document(patientId)
            .update("emotions", emotionsData)
            .addOnSuccessListener {
                onComplete(true)
            }
            .addOnFailureListener { e ->
                Log.e("FirebaseRepository", "Error updating emotions", e)
                onComplete(false)
            }
    }

}


