package com.example.androidinterviewapp.data.api

import android.provider.ContactsContract.CommonDataKinds.Note
import com.example.androidinterviewapp.data.model.Base
import com.example.androidinterviewapp.data.model.BaseAnswerCreate
import com.example.androidinterviewapp.data.model.User
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.*


interface APIService {

    @GET("users")
    fun getLastPage(): Single<Base>

    @GET("users")
    fun getUsers(@Query("page") page: Int): Single<Base>

    @DELETE("users/{id}")
    fun deleteUser(@Path("id") noteId: Int): Single<Base>

    @POST("users")
    fun createUser(@Body user: User): Single<BaseAnswerCreate>

}