package com.example.news_backend.ui.base

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.news_backend.network.ApiResponse
import com.example.news_backend.network.AuthenticationResponse
import com.example.news_backend.utils.Resource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

open class BaseViewModelImpl : BaseViewModel, ViewModel() {
    override fun <T> performAction(
        resultLiveData: MutableLiveData<Resource<T>>,
        apiCall: () -> Call<T>
    ) {
        resultLiveData.value = Resource.Loading()
        val call = apiCall()
        call.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        resultLiveData.value = Resource.Success(body)
                    } else {
                        resultLiveData.value = Resource.Error("Empty response body")
                    }
                } else {
                    resultLiveData.value = Resource.Error("Request failed: ${response.code()} ${response.message()}")
                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                Log.d("Network", "Đăng nhập thành công")
                resultLiveData.value = Resource.Error("Đăng nhập thành công")
            }
        })
    }
}