package com.example.androidinterviewapp.ui.mvvm

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidinterviewapp.R
import com.example.androidinterviewapp.data.model.User
import com.example.androidinterviewapp.data.repository.NetworkState
import com.example.androidinterviewapp.databinding.FragmentMainBinding
import com.example.androidinterviewapp.ui.adapter.UsersAdapter
import com.example.androidinterviewapp.ui.dialog.CreateUserBottomSheetDialog

class UsersFragment : Fragment() {

    private lateinit var viewModel: UsersViewModel

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    var loginFragment = CreateUserBottomSheetDialog(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        viewModel = ViewModelProvider(this).get(UsersViewModel::class.java)

        viewModel.networkState.observe(viewLifecycleOwner,{
            when (it) {
                NetworkState.LOADING -> {
                    binding.progressBarPopular.visibility = View.VISIBLE
                    binding.txtErrorPopular.visibility = View.GONE
                }
                NetworkState.LOADED -> {
                    binding.progressBarPopular.visibility = View.GONE
                    binding.txtErrorPopular.visibility = View.GONE
                }
                NetworkState.ERROR -> {
                    binding.progressBarPopular.visibility = View.GONE
                    binding.txtErrorPopular.visibility = View.VISIBLE

                    Toast.makeText(context, "Probleme, try again",Toast.LENGTH_LONG).show()

                }
            }
        })

        viewModel.liveUsers.observe(viewLifecycleOwner,{
            bindUI(it)
        })

        viewModel.liveCode.observe(viewLifecycleOwner,{
            Log.i("tryhard", it.toString())
            when (it) {
                204 -> viewModel.getUSers()
                201 -> {
                    viewModel.getUSers()
                    loginFragment.dismiss()
                }
                else -> {
                    Toast.makeText(context, "Probleme, try again",Toast.LENGTH_LONG).show()
                }
            }


        })


        viewModel.getUSers()

        binding.fab.setOnClickListener {
            displayCreateUserPopUp()
        }
    }

    fun bindUI(users:List<User>){
        val articleAdapter = UsersAdapter(this)
        articleAdapter.addAll(users)
        articleAdapter.notifyDataSetChanged()
        binding.recycler.adapter?.notifyDataSetChanged()
        binding.recycler.adapter = articleAdapter
        binding.recycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    fun deleteUSer(id:Int){
        viewModel.deleteUSer(id)
    }

    fun saveUser(user:User){
        viewModel.createUSer(user)
    }

    fun displayDeleteUserPopUpConfirmation(id:Int){
        val alertDialog: AlertDialog? = activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setTitle(R.string.title)
                setPositiveButton(
                    R.string.ok
                ) { _, _ ->
                    deleteUSer(id)
                }
                setNegativeButton(R.string.cancel
                ) { _, _ ->

                }
            }
            builder.create()
        }

        alertDialog?.show()
    }

    fun displayCreateUserPopUp() {
        loginFragment = CreateUserBottomSheetDialog(this)
        fragmentManager?.let { loginFragment!!.show(it, loginFragment!!.tag) }
    }

}
