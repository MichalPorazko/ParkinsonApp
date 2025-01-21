package com.example.ParkinsonApp.DataTypes

data class PatientAction(
    val id: String = "",
    val patientId: String = "",
    val patientFirstName: String = "",
    val patientLastName: String = "",
    val actionDescription: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val actionType: ActionType = ActionType.UNKNOWN
) {
    fun toMap(): Map<String, Any> {
        return mapOf(
            "patientId" to patientId,
            "patientFirstName" to patientFirstName,
            "patientLastName" to patientLastName,
            "actionDescription" to actionDescription,
            "timestamp" to timestamp,
            "actionType" to actionType.name
        )
    }

    companion object {
        fun fromMap(id: String, data: Map<String, Any>): PatientAction {
            return PatientAction(
                id = id,
                patientId = data["patientId"] as? String ?: "",
                patientFirstName = data["patientFirstName"] as? String ?: "",
                patientLastName = data["patientLastName"] as? String ?: "",
                actionDescription = data["actionDescription"] as? String ?: "",
                timestamp = data["timestamp"] as? Long ?: System.currentTimeMillis(),
                actionType = ActionType.valueOf(data["actionType"] as? String ?: "UNKNOWN")
            )
        }
    }
}

enum class ActionType {
    MEDICATION_TAKEN,
    WATER_INTAKE_UPDATED,
    EMOTION_UPDATED,
    UNKNOWN
}
