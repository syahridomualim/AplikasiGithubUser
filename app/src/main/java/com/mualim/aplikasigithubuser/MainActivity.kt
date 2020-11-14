package com.mualim.aplikasigithubuser

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mualim.aplikasigithubuser.detail.DetailActivity
import com.mualim.aplikasigithubuser.model.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: UserAdapter
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter = UserAdapter()
        adapter.notifyDataSetChanged()

        rvUsers.layoutManager = LinearLayoutManager(this)
        rvUsers.adapter = adapter

        adapter.setOnClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(user: User) {
                val intents = Intent(this@MainActivity, DetailActivity::class.java)
                intents.putExtra(DetailActivity.USER_NAME, user.userName)
                startActivity(intents)
            }
        })
        mainViewModel = ViewModelProvider(this@MainActivity, ViewModelProvider.NewInstanceFactory())
                .get(MainViewModel::class.java)
        mainViewModel.getUser().observe(this@MainActivity,{
            if (it != null){
                adapter.setData(it)
                showLoading(false)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu?.findItem(R.id.search)?.actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.searchHint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String): Boolean {
                mainViewModel = ViewModelProvider(this@MainActivity, ViewModelProvider.NewInstanceFactory())
                        .get(MainViewModel::class.java)
                mainViewModel.getUserData(this@MainActivity, query)
                showLoading(true)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.action_change_settings){
            val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }
    private fun showLoading(state: Boolean) {
        if (state) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }
}