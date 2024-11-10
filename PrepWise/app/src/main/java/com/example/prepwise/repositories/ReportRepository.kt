package com.example.prepwise.repositories

import android.util.Log
import com.example.prepwise.dataClass.ComplaintRequest
import com.example.prepwise.utils.RetrofitInstance

object ReportRepository {
    suspend fun submitComplaint(userIdCompl: Int? = null, resourcesId: Int? = null, setId: Int? = null, context: String): Boolean {
        return try {
            val complaintRequest = ComplaintRequest(userIdCompl, resourcesId, setId, context)
            val response = RetrofitInstance.api().submitComplaint(complaintRequest)

            if (response.isSuccessful) {
                Log.d("ComplaintRepository", "Complaint submitted successfully")
                true
            } else {
                Log.e("ComplaintRepository", "Response Code: ${response.code()}, Body: ${response.errorBody()?.string()}")
                false
            }
        } catch (e: Exception) {
            Log.e("ComplaintRepository", "Exception: ${e.message}")
            false
        }
    }
}