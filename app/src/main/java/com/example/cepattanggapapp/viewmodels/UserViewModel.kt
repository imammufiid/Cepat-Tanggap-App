package com.example.cepattanggapapp.viewmodels

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.cepattanggapapp.api.ApiClient
import com.example.cepattanggapapp.utils.Constants
import com.example.cepattanggapapp.utils.SingleLiveEvent
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody

class UserViewModel : ViewModel() {
    private var state: SingleLiveEvent<UserState> = SingleLiveEvent()
    private var api = ApiClient.instance()

    @SuppressLint("CheckResult")
    fun login(email: String, password: String) {
        state.value = UserState.IsLoading(true)
        api.login(email, password)
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.status == 200) {
                    Log.d("DATA", it.result.toString())
                    state.value = UserState.IsSuccess(
                        it.result?.name,
                        it.result?.email,
                        it.result?.alamat,
                        it.result?.status,
                        it.result?.api_token
                    )
                } else if (it.status == 400) {
                    Log.d("ERROR", it.message!!)
                    state.value = UserState.Error(it.message!!)
                }
                state.value = UserState.IsLoading()
            }, {
                state.value = UserState.Error(it.message!!)
            })


    }

    @SuppressLint("CheckResult")
    fun register(nama_lengkap: String?, email: String, alamat: String?, password: String?) {
        state.value = UserState.IsLoading(true)
        api.register(nama_lengkap, email, alamat, password)
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.status == 201) {
                    //state.value = UserState.IsSuccess()
                    state.value = UserState.SuccessRegister(it.status, it.message)
                } else {
                    state.value = UserState.Error(it.message!!)
                }
                state.value = UserState.IsLoading()
            }, {
                state.value = UserState.Error(it.message!!)
            })
    }

    @SuppressLint("CheckResult")
    fun editProfile(token: String?, nama_lengkap: String?, email: String?, alamat: String?) {
        state.value = UserState.IsLoading(true)
        api.editProfile(token, nama_lengkap, email, alamat)
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.status == 201) {
                    //state.value = UserState.IsSuccess()
                    state.value = UserState.SuccessEdit(it.message)
                    Log.d("result", it.result.toString())
                } else {
                    state.value = UserState.Error(it.message!!)
                }
                state.value = UserState.IsLoading()
            }, {
                state.value = UserState.Error(it.message!!)
            })
    }

    @SuppressLint("CheckResult")
    fun uploadProfile(token: String?, part: MultipartBody.Part) {
        state.value = UserState.IsLoading(true)
        api.uploadProfile(token, part)
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.status == 201) {
                    //state.value = UserState.IsSuccess()
                    state.value = UserState.SuccessEdit(it.message)
//                    Log.d("result", it.result.toString())
                } else {
                    state.value = UserState.Error(it.message!!)
                    Log.d("result", it.message.toString())
                }
                state.value = UserState.IsLoading()
            }, {
                state.value = UserState.Error(it.message!!)
                Log.d("result", it.message.toString())
            })
    }

    fun validate(
        nama_lengkap: String?,
        email: String?,
        alamat: String?,
        password: String?
    ): Boolean {
        state.value = UserState.Reset
        if (nama_lengkap != null) {
            if (nama_lengkap.isEmpty()) {
                state.value = UserState.ShowToast("Nama tidak boleh kosong!")
                return false
            }
        }

        if (email!!.isEmpty() || password!!.isEmpty()) {
            state.value = UserState.ShowToast("Email atau Password tidak boleh kosong!")
            return false
        }
        if (!Constants.isValidEmail(email)) {
            state.value = UserState.Validation(email = "Email anda tidak valid!")
            return false
        }
        if (!Constants.isValidPassword(password)) {
            state.value = UserState.Validation(password = "Password minimal 8 karakter!")
            return false
        }

        return true
    }

    fun getState() = state
}

sealed class UserState {
    data class ShowToast(var message: String) : UserState()
    data class IsLoading(var state: Boolean = false) : UserState()
    data class Error(var err: String) : UserState()
    data class SuccessRegister(var status: Int?, var message: String?) : UserState()
    data class SuccessEdit(var message: String?) : UserState()
    data class IsSuccess(
        var nama: String?,
        var email: String?,
        var alamat: String?,
        var status: Int?,
        var token: String? = null
    ) : UserState()

    data class IsFailed(var message: String? = null) : UserState()
    data class Validation(
        var nama_lengkap: String? = null,
        var email: String? = null,
        var alamat: String? = null,
        var password: String? = null
    ) : UserState()

    object Reset : UserState()
}