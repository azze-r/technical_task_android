package com.example.androidinterviewapp.ui.mvvm


import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.androidinterviewapp.data.api.APIService
import com.example.androidinterviewapp.data.api.ApiClient
import com.example.androidinterviewapp.data.model.Base
import com.example.androidinterviewapp.data.model.BaseAnswerCreate
import com.example.androidinterviewapp.data.model.User
import com.example.androidinterviewapp.data.repository.NetworkState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers


class UsersViewModel : ViewModel() {

    val liveUsers = MutableLiveData<List<User>>()
    val liveCode = MutableLiveData<Int>()

    val networkState: MutableLiveData<NetworkState> = MutableLiveData()

    private val disposable = CompositeDisposable()

    fun getUSers(){
        networkState.value = NetworkState.LOADING

        val apiService: APIService = ApiClient.getClient()
            .create(APIService::class.java)


        disposable.add(
            apiService
                .getLastPage()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<Base?>() {
                    override fun onSuccess(base: Base) {
                        disposable.add(
                            apiService
                                .getUsers(base.meta.pagination.pages)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeWith(object : DisposableSingleObserver<Base?>() {
                                    override fun onSuccess(base: Base) {
                                        networkState.value = NetworkState.LOADED
                                        liveUsers.value = base.data

                                    }

                                    override fun onError(e: Throwable) {
                                        networkState.value = NetworkState.ERROR
                                    }
                                }))

                    }

                    override fun onError(e: Throwable) {
                        networkState.value = NetworkState.ERROR
                    }
                }))


    }

    fun deleteUSer(id:Int){
        networkState.value = NetworkState.LOADING

        val apiService: APIService = ApiClient.getClient()
            .create(APIService::class.java)


        apiService
            .deleteUser(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).let {
                disposable.add(
                    it
                        .subscribeWith(object : DisposableSingleObserver<Base?>() {
                            override fun onSuccess(base: Base) {
                                liveCode.value = base.code
                                networkState.value = NetworkState.LOADED
                            }

                            override fun onError(e: Throwable) {
                                networkState.value = NetworkState.ERROR
                            }
                        }))
            }
    }

    fun createUSer(user: User){
        networkState.value = NetworkState.LOADING

        val apiService: APIService = ApiClient.getClient()
            .create(APIService::class.java)

        apiService
            .createUser(user)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).let {
                disposable.add(
                    it
                        .subscribeWith(object : DisposableSingleObserver<BaseAnswerCreate?>() {
                            override fun onSuccess(base: BaseAnswerCreate) {
                                networkState.value = NetworkState.LOADED
                                liveCode.value = base.code

                            }

                            override fun onError(e: Throwable) {
                                Log.i("tryhard",e.localizedMessage)
                                networkState.value = NetworkState.ERROR
                            }
                        }))
            }
    }
}