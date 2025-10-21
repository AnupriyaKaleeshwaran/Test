import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.productlisttestapp.model.ApiResponse
import kotlinx.coroutines.launch

class OrderViewModel : ViewModel() {

    private val repository = OrderRepository()

    val orderResponse = MutableLiveData<ApiResponse?>()
    val error = MutableLiveData<String?>()
    val isLoading = MutableLiveData<Boolean>()
    fun fetchOrder(tableId: Int) {
        viewModelScope.launch {
            isLoading.value = true
            val result = repository.getOrderByTableId(tableId)
            result.onSuccess {
                orderResponse.value = it
            }.onFailure {
                error.value = it.message
            }
            isLoading.value = false
        }
    }
}

