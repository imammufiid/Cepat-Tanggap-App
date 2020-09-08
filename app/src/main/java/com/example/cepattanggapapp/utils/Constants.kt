package com.example.cepattanggapapp.utils

import android.content.Context

class Constants {
    companion object {
        const val BASE_URL = "http://cepattanggap.sanscoding.com/api/"
//        const val BASE_URL = "http://cepattanggap.sanscoding.com/api/"
        const val FCM_KEY =
            "AAAAU3lW6Zw:APA91bFd0qgzUvX7oZ3eaojFeN0LYYS0ZweS_BBq2uh5IRZsI_HzbmmZdcmKTvBom5VgXIkfRQ6Ym633_iWZVUnXgewZchK0tPGR2psAG9ZLqJmMrv2SmcCR-ACjEIPECgXRpvtmLPJu"



        fun setTokenFCM(context: Context?, token: String) {
            val pref = context?.getSharedPreferences("USER", Context.MODE_PRIVATE)
            pref!!.edit().apply {
                putString("TOKEN_FCM", token)
                apply()
            }
        }

        fun getTokenFCM(context: Context?): String {
            val pref = context?.getSharedPreferences("USER", Context.MODE_PRIVATE)
            return pref?.getString("TOKEN_FCM", "Mencari...")!!
        }

        fun getToken(context: Context): String? {
            val pref = context.getSharedPreferences("USER", Context.MODE_PRIVATE)
            return pref.getString("TOKEN", "undefined")
        }

        fun setToken(context: Context?, token: String?) {
            val pref = context?.getSharedPreferences("USER", Context.MODE_PRIVATE)
            pref!!.edit().apply {
                putString("TOKEN", token)
                apply()
            }
        }

        fun getAddress(context: Context): String? {
            val pref = context.getSharedPreferences("USER", Context.MODE_PRIVATE)
            return pref.getString("ADDRESS", "undefined")
        }

        fun setAddress(context: Context?, address: String?) {
            val pref = context?.getSharedPreferences("USER", Context.MODE_PRIVATE)
            pref!!.edit().apply {
                putString("ADDRESS", address)
                apply()
            }
        }

        fun getNamaUser(context: Context?): String? {
            val pref = context?.getSharedPreferences("USER", Context.MODE_PRIVATE)
            return pref?.getString("NAMAUSER", "undefined")
        }

        fun setNamaUser(context: Context?, nama: String?) {
            val pref = context?.getSharedPreferences("USER", Context.MODE_PRIVATE)
            pref!!.edit().apply {
                putString("NAMAUSER", nama)
                apply()
            }
        }

        fun getEmail(context: Context?): String? {
            val pref = context?.getSharedPreferences("USER", Context.MODE_PRIVATE)
            return pref?.getString("EMAIL", "undefined")
        }

        fun setEmail(context: Context?, email: String?) {
            val pref = context?.getSharedPreferences("USER", Context.MODE_PRIVATE)
            pref!!.edit().apply {
                putString("EMAIL", email)
                apply()
            }
        }

        fun getAlamat(context: Context?): String? {
            val pref = context?.getSharedPreferences("USER", Context.MODE_PRIVATE)
            return pref?.getString("ALAMAT", "undefined")
        }

        fun setAlamat(context: Context?, alamat: String?) {
            val pref = context?.getSharedPreferences("USER", Context.MODE_PRIVATE)
            pref!!.edit().apply {
                putString("ALAMAT", alamat)
                apply()
            }
        }



        fun clearToken(context: Context?) {
            val pref = context?.getSharedPreferences("USER", Context.MODE_PRIVATE)
            pref?.edit()?.clear()?.apply()
        }

        fun isValidEmail(email: String) =
            android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()

        fun isValidPassword(passwd: String) = passwd.length >= 8

    }


}