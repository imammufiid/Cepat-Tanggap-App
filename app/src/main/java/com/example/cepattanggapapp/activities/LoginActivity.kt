package com.example.cepattanggapapp.activities

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.cepattanggapapp.R
import com.example.cepattanggapapp.utils.Constants
import com.example.cepattanggapapp.viewmodels.UserState
import com.example.cepattanggapapp.viewmodels.UserViewModel
import kotlinx.android.synthetic.main.activity_login.*

@Suppress("DEPRECATION")
class LoginActivity : AppCompatActivity() {
    private lateinit var userViewModel: UserViewModel
    private var dialog: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = resources.getColor(R.color.colorWhite)

        dialog = ProgressDialog(this)

        // init userViewModel
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        userViewModel.getState().observe(this, Observer {
            handlerUIState(it)
        })

        // init

        doLogin()

        text_to_register.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }


    }

    private fun doLogin() {
        btn_login.setOnClickListener {
            val username = et_email.text.toString()
            val password = et_password.text.toString()
            if(userViewModel.validate(null, username,null, password)) {
                userViewModel.login(username, password)
            }
        }
    }

    private fun handlerUIState(it: UserState?) {
        when (it) {
            is UserState.Reset -> {
                setEmailError(null)
                setPasswordError(null)
            }
            is UserState.ShowToast -> showToast(it.message)
            is UserState.Error -> {
                isLoading(false)
                showToast(it.err)
            }
            is UserState.IsLoading -> isLoading(false)
            is UserState.IsFailed -> {
                isLoading(false)
                showToast(it.message!!)
            }
            is UserState.IsSuccess -> isSuccess(it.nama, it.email, it.alamat, it.status, it.token)
            is UserState.Validation -> {
                it.email?.let {
                    setEmailError(it)
                }
                it.password?.let {
                    setPasswordError(it)
                }
            }
        }

    }

    private fun isSuccess(nama: String?, email: String?, alamat: String?, status: Int?, token: String?) {
        if(status != 1) {
            Toast.makeText(this, "Akun anda tidak aktif\n Silahkan hubungi admin!", Toast.LENGTH_SHORT).show()
        } else {
            Constants.setToken(this, token)
            Constants.setNamaUser(this, nama)
            Constants.setEmail(this, email)
            Constants.setAlamat(this, alamat)
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

    }

    private fun isLoading(state: Boolean) {
        if(state) {
            btn_login.isEnabled = false
            dialog?.setMessage("Memuat...")
            dialog?.show()
        } else {
            btn_login.isEnabled = true
            dialog?.dismiss()
        }
    }

    private fun setPasswordError(err: String?) {
        et_password.error = err
    }

    private fun setEmailError(err: String?) {
       in_email.error = err
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}