package com.example.ParkinsonApp.Firebase

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.widget.Toast
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
import com.example.ParkinsonApp.DataTypes.UserType
import com.example.ParkinsonApp.DataTypes.Water
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldPath
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
                            val newPatient = UserType.PatientUser(email, PatientData(additionalInfo["firstName"]!!, additionalInfo["lastName"]!!))
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

    fun getPatientData(patientId: String, onComplete: (PatientDataWithId?) -> Unit) {
        db.collection("patients").document(patientId)
            .get()
            .addOnSuccessListener { document ->
                val patientData = document.toObject(PatientDataWithId::class.java)
                onComplete(patientData)
            }
            .addOnFailureListener { e ->
                // Handle error
                onComplete(null)
            }
    }

    fun getDoctorData(doctorId: String, onComplete: (DoctorDataWithId?) -> Unit) {
        db.collection("doctors").document(doctorId)
            .get()
            .addOnSuccessListener { document ->
                val doctorData = document.toObject(DoctorDataWithId::class.java)
                onComplete(doctorData)
            }
            .addOnFailureListener { e ->
                // Handle error
                }
    }

    fun getPatientsData(doctorId: String, onComplete: (List<PatientDataWithId>) -> Unit) {
        db.collection("doctors").document(doctorId).addSnapshotListener{ snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }
            if (snapshot != null) {
                val doctorDataMap = snapshot.toObject(DoctorData::class.java)
                val patientIds = doctorDataMap?.patients ?: emptyList()
                if (patientIds.isNotEmpty()){
                    db.collection("patients")
                        .whereIn(FieldPath.documentId(), patientIds)
                        .get()
                        .addOnSuccessListener { querySnapshot ->
                            val patients = querySnapshot.documents.mapNotNull { doc ->
                                doc.toObject(PatientDataWithId::class.java)
                            }
                            onComplete(patients)
                        }.addOnFailureListener { e ->
                            Log.e("FirebaseRepository", "Error retrieving patients", e)
                            onComplete(emptyList())
                        }
                }

            } else {
                Log.d(TAG, "Current data: null")
            }

        }
    }

    sealed class AuthState {
        object Authenticated : AuthState()
        object Unauthenticated : AuthState()
        object Loading : AuthState()
        data class Error(val message: String) : AuthState()
    }

    private fun parsePatientData(data: Map<*, *>?): PatientData {
        if (data == null) return PatientData()

        val firstName = data["firstName"] as? String ?: ""
        val lastName = data["lastName"] as? String ?: ""

        // Parse medications
        val medicationsMap = data["medications"] as? Map<*, *>
        val medicationSchedule = parseMedicationSchedule(medicationsMap)

        // Parse water intake
        val waterIntakeMap = data["waterIntake"] as? Map<*, *>
        val waterIntake = parseWater(waterIntakeMap)

        // Parse emotions
        val emotionsList = data["emotions"] as? List<*>
        val emotions = emotionsList?.mapNotNull { parseEmotion(it as? Map<*, *>) } ?: emptyList()

        return PatientData(
            firstName = firstName,
            lastName = lastName,
            medications = medicationSchedule,
            waterIntake = waterIntake,
            emotions = emotions
        )
    }

    private fun parseMedicationSchedule(data: Map<*, *>?): MedicationSchedule {
        if (data == null) return MedicationSchedule()

        val medicationsList = data["medications"] as? List<*>
        val medications = medicationsList?.mapNotNull { parseMedication(it as? Map<*, *>) } ?: emptyList()
        return MedicationSchedule(medications = medications)
    }

    private fun parseMedication(data: Map<*,*>?): Medication? {
        if (data == null) return null

        val name = data["name"] as? String ?: ""
        val dose = data["dose"] as? String ?: ""
        val timesList = data["times"] as? List<*>
        val times = timesList?.mapNotNull {parseHour(it as? Map<*, *>) } ?: emptyList()

        return Medication(
            name = name,
            dose = dose,
            times = times
        )
    }

    private fun parseHour(data: Map<*, *>?): Hour? {
        if (data == null) return null

        val hour = (data["hour"] as? Long)?.toInt() ?: 0
        val minute = (data["minute"] as? Long)?.toInt() ?: 0
        val taken = data["taken"] as? Boolean ?: false

        return Hour(
            hour = hour,
            minute = minute,
            taken = taken
        )
    }

    private fun parseWater(data: Map<*, *>?): Water {
        if (data == null) return Water()

        val amount = (data["amount"] as? Long)?.toInt() ?: 0
        return Water(amount = amount)
    }

    private fun parseEmotion(data: Map<*, *>?): Emotion? {
        if (data == null) return null

        val name = data["name"] as? String ?: ""
        val stateString = data["state"] as? String ?: ""
        val state = EmotionState.entries.find { it.name == stateString } ?: EmotionState.NEUTRAL

        return Emotion(
            name = name,
            state = state
        )
    }

    private fun parseDoctorData(data: Map<*, *>?): DoctorData {
        if (data == null) return DoctorData()
        val firstName = data["firstName"] as? String ?: ""
        val lastName = data["lastName"] as? String ?: ""

        val pwzNumber = (data["pwzNumber"] as? Long)?.toInt() ?: 0

        // Assuming patients are stored as a list of patient IDs (strings)
        val patientsListAny = data["patients"] as? List<*>
        val patients = patientsListAny?.mapNotNull { it as? PatientData } ?: emptyList()

        return DoctorData(
            firstName = firstName,
            lastName = lastName,
            pwzNumber = pwzNumber,
            patients = patients
        )
    }

    private fun parseUserDocument(document: DocumentSnapshot): UserType? {
        val userTypeString = document.getString("userType")
        val email = document.getString("email") ?: ""

        return when (userTypeString) {
            "patient" -> {
                val patientDataMap = (document.get("patientData") as? Map<*, *>)
                val patientData = parsePatientData(patientDataMap)
                UserType.PatientUser(email, patientData)
            }
            "doctor" -> {
                val doctorDataMap = document.get("doctorData") as? Map<*, *>
                val doctorData = parseDoctorData(doctorDataMap)
                UserType.DoctorUser(email,  doctorData)
            }
            else -> null
        }
    }

    fun updateWaterIntake(patientId: String, newAmount: Int, onComplete: (Boolean) -> Unit) {
        db.collection("patients").document(patientId)
            .update("waterIntake.amount", newAmount)
            .addOnSuccessListener {
                onComplete(true)
            }
            .addOnFailureListener { e ->
                Log.e("FirebaseRepository", "Error updating water intake", e)
                onComplete(false)
            }
    }

    fun updateEmotions(patientId: String, emotions: List<Emotion>, onComplete: (Boolean) -> Unit) {
        val emotionsData = emotions.map { it.toMap() }
        db.collection("patients").document(patientId)
            .update("emotions", emotionsData)
            .addOnSuccessListener {
                onComplete(true)
            }
            .addOnFailureListener { e ->
                Log.e("FirebaseRepository", "Error updating emotions", e)
                onComplete(false)
            }
    }

    fun updatePatientMedication(patientId: String, medicationSchedule: MedicationSchedule) {
        val medicationData = medicationSchedule.toMap()
        db.collection("patients").document(patientId)
            .update("medications", medicationData)
            .addOnSuccessListener {
                // Update was successful
            }
            .addOnFailureListener { e ->
                Log.e("FirebaseRepository", "Error updating medications", e)
                // Handle failure
            }
    }

    fun logPatientAction(patientAction: PatientAction, onComplete: (Boolean) -> Unit) {
        actionsCollection.add(patientAction.toMap())
            .addOnSuccessListener { documentReference ->
                // Optionally update the action with the document ID
                onComplete(true)
            }
            .addOnFailureListener { e ->
                // Handle failure
                onComplete(false)
            }
    }

    fun getRecentPatientActions(doctorId: String, onComplete: (List<PatientAction>) -> Unit) {
        getDoctorData(doctorId) { doctorDataWithId ->
            doctorDataWithId?.data?.let { doctorData ->
                val patientIds = doctorData.patients
                if (patientIds.isNotEmpty()) {
                    actionsCollection
                        .whereIn("patientId", patientIds)
                        .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
                        .limit(10)
                        .get()
                        .addOnSuccessListener { querySnapshot ->
                            val actions = querySnapshot.documents.mapNotNull { doc ->
                                val data = doc.data ?: return@mapNotNull null
                                PatientAction.fromMap(doc.id, data)
                            }
                            onComplete(actions)
                        }
                        .addOnFailureListener { e ->
                            // Handle failure
                            onComplete(emptyList())
                        }
                } else {
                    onComplete(emptyList())
                }
            } ?: run {
                onComplete(emptyList())
            }
        }
    }
}


