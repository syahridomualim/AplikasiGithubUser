package com.mualim.aplikasigithubuser

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_users.view.*
import java.util.ArrayList

class UserAdapter : RecyclerView.Adapter<UserAdapter.UserViewAdapter>() {

    val mData = ArrayList<User>()
    var onItemClickCallback: OnItemClickCallback? = null

    fun setOnClickCallback(onItemClickCallback: OnItemClickCallback){
       this.onItemClickCallback = onItemClickCallback
    }

    fun setData(items: ArrayList<User>){
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewAdapter {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_users, parent, false)
        return UserViewAdapter(view)
    }

    override fun onBindViewHolder(holder: UserViewAdapter, position: Int) {
        holder.bind(mData[position])
    }

    override fun getItemCount(): Int = mData.size

    inner class UserViewAdapter (itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(user: User){
            with(itemView){
                Glide.with(itemView.context)
                    .load(user.avatar)
                    .apply(RequestOptions().override(55,55))
                    .into(profile_image)
                userName.text = user.userName
                type.text = user.type

                itemView.setOnClickListener {
                    onItemClickCallback?.onItemClicked(user)
                }
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(user: User)
    }
}