package com.mualim.aplikasigithubuser.detail

import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.mualim.aplikasigithubuser.R
import com.mualim.aplikasigithubuser.User
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_detail.*
import org.json.JSONObject
import java.lang.Exception


class DetailActivity : AppCompatActivity() {

    companion object {
        const val USER_NAME = "user_name"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        val user = intent.getStringExtra(USER_NAME)

        supportActionBar?.title = user

        val mBundle = Bundle()
        mBundle.putString(SectionPagerAdapter.USER_NAME, user)

        getDetailUser(user!!)

        val sectionPagerAdapter = SectionPagerAdapter(this, supportFragmentManager)
        sectionPagerAdapter.userName = user
        viewPager.adapter = sectionPagerAdapter
        tabs.setupWithViewPager(viewPager)

        supportActionBar?.elevation = 0f
    }

    private fun getDetailUser(user: String){
        progressBar1.visibility = View.VISIBLE

        val urlDetail = "https://api.github.com/users/$user"
        val client = AsyncHttpClient()
        client.addHeader("Authorization","token #########################")
        client.addHeader("User-Agent", "request")

        client.get(urlDetail, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseBody: ByteArray?
            ) {
                val result = String(responseBody!!)
                try {
                    progressBar1.visibility = View.INVISIBLE
                    val responseObject = JSONObject(result)
                    val username = responseObject.getString("login")
                    val avatar = responseObject.getString("avatar_url")
                    val repositories = responseObject.getInt("public_repos")
                    val company = responseObject.getString("company")
                    val name = responseObject.getString("name")
                    val location = responseObject.getString("location")

                    val userItems = User(
                            username,
                            name,
                            location,
                            repositories,
                            avatar,
                            company
                    )

                    Glide.with(this@DetailActivity)
                            .load(userItems.avatar)
                            .into(profile_image1)
                    tv_userName.text = userItems.userName
                    tv_name.text = userItems.name
                    tv_location.text = userItems.location
                    tv_repositories.text = userItems.repository.toString()
                    tv_company.text = userItems.company

                } catch (e: Exception) {
                    progressBar1.visibility = View.INVISIBLE
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseBody: ByteArray?,
                    error: Throwable?
            ) {
                progressBar1.visibility = View.INVISIBLE
                Log.d(DetailActivity::class.java.simpleName, error?.message.toString())
            }
        })
    }
}