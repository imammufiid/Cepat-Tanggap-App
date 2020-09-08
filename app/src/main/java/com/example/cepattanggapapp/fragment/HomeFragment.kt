package com.example.cepattanggapapp.fragment

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.example.cepattanggapapp.R
import com.example.cepattanggapapp.utils.Constants
import com.example.cepattanggapapp.utils.MySimpleLocation
import com.example.cepattanggapapp.utils.helpers.SenderMessage

private const val MY_ADDRESS = "myAddress"
private const val ARG_PARAM2 = "param2"

@Suppress("DEPRECATION")
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var myAddress: String? = null
    private var param2: String? = null
    private var sender: SenderMessage? = null
    private val REQUST_PHONE_CALL = 1

    private var content : TextView? = null

    // pertama
    lateinit var mySimpleLocation: MySimpleLocation

    // kedua
    private val REQUEST_CODE_PERMISSION = 2
    val mPermission: String = android.Manifest.permission.ACCESS_FINE_LOCATION


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            myAddress = it.getString(MY_ADDRESS)
            param2 = it.getString(ARG_PARAM2)
        }
        sender = SenderMessage(context)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        // init element
        val qs = root.findViewById<View>(R.id.quick_send) as Button
        val sm = root.findViewById<View>(R.id.send_message) as Button
        val crs = root.findViewById<View>(R.id.contact_rs) as ImageButton
        val cp = root.findViewById<View>(R.id.contact_polisi) as ImageButton
        val crt = root.findViewById<View>(R.id.contact_rt) as ImageButton
        content = root.findViewById<TextView>(R.id.content) as TextView
        val showButtonSheet = BottomSheetFragment()

        content!!.text = Constants.getAddress(context!!)
        qs.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Proses Kirim Cepat?")
            builder.setPositiveButton("Iya") { dialog, which ->
                sender?.senderMessage("Pesan Darurat dari Kijang 1")
            }
            builder.setNegativeButton("Tidak") { dialog, which ->
                dialog.dismiss();
            }
            builder.show()

        }

        sm.setOnClickListener {
            showButtonSheet.show(fragmentManager!!, "show sheet")
        }

        crs.setOnClickListener {
            val phone = "801118"

            startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone")))
        }

        cp.setOnClickListener {
            val phone = "807544"
            startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone")))
        }
        crt.setOnClickListener {
            val phone = "08159019374"
            startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone")))
        }

        return root
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUST_PHONE_CALL) startCall()
    }

    private fun startCall() {
        val callIntent = Intent(Intent.ACTION_CALL)
        callIntent.data = Uri.parse("tel:081459019374")
        startActivity(callIntent)
    }


    companion object {
        @JvmStatic
        fun newInstance(myAddress: String, param2: String?) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(MY_ADDRESS, myAddress)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

}