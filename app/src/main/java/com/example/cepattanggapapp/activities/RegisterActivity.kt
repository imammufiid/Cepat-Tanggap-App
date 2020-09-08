package com.example.cepattanggapapp.activities

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.cepattanggapapp.R
import com.example.cepattanggapapp.utils.Constants
import com.example.cepattanggapapp.viewmodels.UserState
import com.example.cepattanggapapp.viewmodels.UserViewModel
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.et_password

@Suppress("DEPRECATION")
class RegisterActivity : AppCompatActivity() {
    private lateinit var userViewModel: UserViewModel
    private var dialog : ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = resources.getColor(R.color.colorWhite)

        // init progress dialog
        dialog = ProgressDialog(this)

        // init user view model
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        userViewModel.getState().observer(this, Observer {
            handlerUIState(it)
        })

        text_to_login.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        in_password.addOnEditTextAttachedListener {
            et_password.transformationMethod = HideReturnsTransformationMethod.getInstance()
            // et_password.transformationMethod = PasswordTransformationMethod.getInstance()
        }

        doRegister()
    }

    private fun doRegister() {
        btn_register.setOnClickListener {
            val nama_lengkap = et_nama_lengkap.text.toString()
            val email = et_username.text.toString()
            val alamat = et_alamat.text.toString()
            val password = et_password.text.toString()
            val password2 = et_password2.text.toString()

            if(userViewModel.validate(nama_lengkap, email, alamat, password)) {
                userViewModel.register(nama_lengkap, email, alamat, password)
            }
        }
    }

    private fun handlerUIState(it: UserState?) {
        when (it) {
            is UserState.Reset -> {
                setEmailError(null)
                setPasswordError(null)
                setAlamatError(null)
                setNamaLengkapError(null)
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
            is UserState.SuccessRegister -> isSuccess(it.status, it.message)
            is UserState.Validation -> {
                it.email?.let {
                    setEmailError(it)
                }
                it.password?.let {
                    setPasswordError(it)
                }
                it.alamat?.let {
                    setAlamatError(null)
                }
                it.nama_lengkap?.let {
                    setNamaLengkapError(null)
                }
            }
        }

    }

    private fun isSuccess(status: Int?, message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        Handler().postDelayed({
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }, 2000)
    }

    private fun isLoading(state: Boolean) {
        if(state) {
            btn_register.isEnabled = false
            dialog?.setMessage("Memuat...")
            dialog?.show()
        } else {
            btn_register.isEnabled = true
            dialog?.dismiss()
        }
    }

    private fun setPasswordError(err: String?) {
        et_password.error = err
    }

    private fun setNamaLengkapError(err: String?) {
        et_nama_lengkap.error = err
    }

    private fun setAlamatError(err: String?) {
        et_alamat.error = err
    }

    private fun setEmailError(err: String?) {
        et_username.error = err
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}