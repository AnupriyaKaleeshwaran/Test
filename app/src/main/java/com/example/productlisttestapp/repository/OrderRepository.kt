import com.example.productlisttestapp.apiutils.ApiClient
import com.example.productlisttestapp.model.ApiResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class OrderRepository {

    private val api = ApiClient.getApi()

    suspend fun getOrderByTableId(tableId: Int): Result<ApiResponse> {
        return try {
            val jsonBody = """
                {
                    "apiKey":"3444CED48713B3DA2EC075CDDE7D562F",
                    "tableId":$tableId
                }
            """.trimIndent()

            val requestBody = jsonBody.toRequestBody("text/plain".toMediaTypeOrNull())
            val response = api.getOrderByTableId(requestBody)

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("API Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

