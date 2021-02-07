package com.example.androidinterviewapp.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.androidinterviewapp.R
import com.example.androidinterviewapp.data.model.User
import com.example.androidinterviewapp.ui.mvvm.UsersFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.bottom_sheet_login.*
import java.util.*

class CreateUserBottomSheetDialog(val usersFragment: UsersFragment) : BottomSheetDialogFragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_login, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        var gender = "Male"
        var active = "Active"

        statusSwitch.setOnCheckedChangeListener { _, isChecked ->
            active = if (isChecked)
                "Active"
            else
                "Inactive"
        }

        statusGender.setOnCheckedChangeListener { _, isChecked ->
            gender = if (isChecked)
                "Male"
            else
                "Female"
        }

        okBtn.setOnClickListener {
            val user = User(name = name.text.toString(),email = email.text.toString(),gender = gender,status = active,id = null,created_at = null,updated_at = null)
            usersFragment.saveUser(user)
        }
    }
}