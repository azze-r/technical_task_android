package com.example.androidinterviewapp.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.androidinterviewapp.R
import com.example.androidinterviewapp.data.model.User
import com.example.androidinterviewapp.ui.mvvm.UsersFragment
import com.example.androidinterviewapp.utils.BaseListAdapter
import kotlinx.android.synthetic.main.view_article.view.*


class UsersAdapter(val usersFragment: UsersFragment) : BaseListAdapter<User>(){

    override fun onCreateChildViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_article, parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindChildViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val repo = items[position]

        holder.itemView.apply {
            cv_name.text = repo.name
            cv_creation_date.text = repo.created_at
            cv_email.text = repo.email
        }

        holder.itemView.setOnLongClickListener {
            repo.id?.let { it1 -> usersFragment.displayDeleteUserPopUpConfirmation(it1) }
            return@setOnLongClickListener true
        }
    }
}