package com.mapr.credibanco.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mapr.credibanco.repositories.TransactionsRepository
import com.mapr.credibanco.services.requests.RequestAuthorization

class AuthorizationViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = TransactionsRepository.getInstance(application)

    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text


    /**
     *
     */
    fun makeAuthorization(
        auth: String,
        request: RequestAuthorization,
        listener: TransactionsRepository.OnListenerResponseAuthorization
    ) {
        repository.makeAuthorizationRequest(auth, request, listener)
    }
}