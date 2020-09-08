package com.example.cepattanggapapp.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.cepattanggapapp.R
import com.example.cepattanggapapp.activities.EditProfileActivity
import com.example.cepattanggapapp.activities.LoginActivity
import com.example.cepattanggapapp.utils.Constants

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
class ProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_profile, container, false)
        val btn_logout = root.findViewById<View>(R.id.btn_logout) as Button
        val btn_edit = root.findViewById<View>(R.id.btn_edit_profile) as Button
        val nama_user = root.findViewById<View>(R.id.account_name) as TextView
        val email = root.findViewById<View>(R.id.account_email) as TextView
        val alamat = root.findViewById<View>(R.id.address_account) as TextView

        // set
        nama_user.setText(Constants.getNamaUser(context))
        email.setText(Constants.getEmail(context))
        alamat.setText(Constants.getAlamat(context))

        btn_logout.setOnClickListener {
            Constants.clearToken(context)
            startActivity(Intent(context, LoginActivity::class.java))
            activity?.finish()
        }
        btn_edit.setOnClickListener {
            startActivity(Intent(context, EditProfileActivity::class.java))
        }
        return root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}