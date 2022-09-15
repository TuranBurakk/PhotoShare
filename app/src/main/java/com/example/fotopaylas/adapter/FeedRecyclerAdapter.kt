package com.example.fotopaylas.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fotopaylas.R
import com.example.fotopaylas.model.Post
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.rec_row.view.*

class FeedRecyclerAdapter(val postList : ArrayList<Post>) : RecyclerView.Adapter<FeedRecyclerAdapter.PostHolder>() {

    class PostHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.rec_row,parent,false)
        return  PostHolder(view)
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {
       holder.itemView.rec_row_useremail.text = postList[position].userEmail
       holder.itemView.rec_row_usercomment.text = postList[position].userComment
       Picasso.get().load(postList[position].gorselUrl).into(holder.itemView.rec_row_imageview)

    }

    override fun getItemCount(): Int {
        return postList.size
    }
}