package com.mualim.aplikasigithubuser.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mualim.aplikasigithubuser.R
import com.mualim.aplikasigithubuser.UserAdapter
import com.mualim.aplikasigithubuser.model.MainViewModel
import kotlinx.android.synthetic.main.fragment_following.*

class FollowingFragment : Fragment() {

    companion object {
        private const val ARG_USER_NAME = "user_name"
    }

    private lateinit var adapter: UserAdapter
    private lateinit var mainViewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_following, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userName = activity?.intent?.getStringExtra(ARG_USER_NAME)

        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
                .get(MainViewModel::class.java)

        mainViewModel.getDataFollowerOrFollowing(view.context, userName!!, "followers")
        mainViewModel.getUser().observe(this, {
            adapter.setData(it)
        })

        adapter = UserAdapter()
        adapter.notifyDataSetChanged()

        rvUsersFollowing.setHasFixedSize(true)
        rvUsersFollowing.layoutManager = LinearLayoutManager(view.context)
        rvUsersFollowing.adapter = adapter

    }
}