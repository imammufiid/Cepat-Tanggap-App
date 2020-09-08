package com.example.cepattanggapapp.fragment

import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import com.example.cepattanggapapp.R
import com.example.cepattanggapapp.utils.Constants
import com.example.cepattanggapapp.utils.helpers.SenderMessage
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.fragment_item_list_dialog_list_dialog.*
import kotlinx.android.synthetic.main.fragment_item_list_dialog_list_dialog_item.view.*

// TODO: Customize parameter argument names
const val ARG_ITEM_COUNT = "item_count"

class BottomSheetFragment : BottomSheetDialogFragment() {
    private var sender: SenderMessage? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root =  inflater.inflate(R.layout.fragment_item_list_dialog_list_dialog, container, false)

        // init
        val message = root.findViewById<View>(R.id.et_pesan_anda) as EditText
        val in_message = root.findViewById<View>(R.id.in_mesasage) as TextInputLayout
        val button_send = root.findViewById<View>(R.id.btn_kirim)
        sender = SenderMessage(context)
        button_send.setOnClickListener {
            val msg = message.text.toString()
            if(msg == "") {
                in_message.error = "Mohon isi pesan anda"
            } else {
                sender?.senderMessage(msg)
                dismiss()
            }
        }
        return root
    }


    companion object {

        // TODO: Customize parameters
        fun newInstance(itemCount: Int): BottomSheetFragment =
            BottomSheetFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_ITEM_COUNT, itemCount)
                }
            }


    }
}