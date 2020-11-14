package com.mualim.aplikasigithubuser.model

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.mualim.aplikasigithubuser.User
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

class MainViewModel : ViewModel() {

    val listUserItems = MutableLiveData<ArrayList<User>>()

    fun getUserData(context: Context, userName: String) {
        Log.d("getData", "Loading...")

        val listUser = ArrayList<User>()
        val urlSearch = "https://api.github.com/search/users?q=$userName"
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token #########################")
        client.addHeader("User-Agent", "request")

        client.get(urlSearch, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseBody: ByteArray?
            ) {
                val result = String(responseBody!!)
                try {
                    Log.d(context.toString(), result)
                    val responseObject = JSONObject(result)
                    val list = responseObject.getJSONArray("items")

                    for (i in 0 until list.length()) {
                        val item = list.getJSONObject(i)
                        val userItems = User()
                        val username = item.getString("login")
                        val avatar = item.getString("avatar_url")
                        val type = item.getString("type")

                        userItems.userName = username
                        userItems.avatar = avatar
                        userItems.type = type

                        listUser.add(userItems)
                    }

                    listUserItems.postValue(listUser)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseBody: ByteArray?,
                    error: Throwable?
            ) {
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error?.message}"
                }
                Log.d("onFailure", errorMessage)
            }
        })
    }

    fun getDataFollowerOrFollowing(context: Context, user: String, followerOrFollowing: String){
        val listUser = ArrayList<User>()
        val url = "https://api.github.com/users/$user/$followerOrFollowing"
        val client = AsyncHttpClient()
        client.addHeader("Authorization","token #########################")
        client.addHeader("User-Agent", "request")

        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseBody: ByteArray?
            ) {
                val result = String(responseBody!!)
                Log.d(context.toString(), result)
                try {
                    val responseArray = JSONArray(result)

                    for (i in 0 until responseArray.length()) {
                        val item = responseArray.getJSONObject(i)
                        val userItems = User()
                        val username = item.getString("login")
                        val avatar = item.getString("avatar_url")
                        val type = item.getString("type")

                        userItems.userName = username
                        userItems.avatar = avatar
                        userItems.type = type

                        listUser.add(userItems)
                    }
                    listUserItems.postValue(listUser)
                } catch (e: Exception) {
                    Log.d(context.toString(), e.message.toString())
                    e.printStackTrace()
                }
            }
            override fun onFailure(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseBody: ByteArray?,
                    error: Throwable?
            ) {
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error?.message}"
                }
                Log.d("onFailure", error?.message.toString())
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun getUser(): MutableLiveData<ArrayList<User>>{
        return listUserItems
    }
}