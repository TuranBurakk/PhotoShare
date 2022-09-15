package com.example.fotopaylas.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fotopaylas.model.Post
import com.example.fotopaylas.R
import com.example.fotopaylas.adapter.FeedRecyclerAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_feed.*

class FeedActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseFirestore
    private lateinit var recyclerViewAdapter : FeedRecyclerAdapter

    var postList = ArrayList<Post>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)
        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()
        getData()
        val layoutManager = LinearLayoutManager(this)
        recyclerview.layoutManager = layoutManager
        recyclerViewAdapter = FeedRecyclerAdapter(postList)
        recyclerview.adapter = recyclerViewAdapter

    }
    fun getData(){
        database.collection("Post").orderBy("date",com.google.firebase.firestore.Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, exception ->
            if(exception != null){
                Toast.makeText(this,exception.localizedMessage,Toast.LENGTH_LONG).show()
            }else{
                if (snapshot != null){
                    if (!snapshot.isEmpty){
                        val documents = snapshot.documents
                        postList.clear()
                        for (document in documents){
                            val userEmail = document.get("useremail") as String
                            val userComment = document.get("usercomment") as String
                            val gorselUrl = document.get("gorselurl") as String
                            val downloadPost = Post(userEmail,userComment,gorselUrl)
                            postList.add(downloadPost)
                        }
                        recyclerViewAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.options_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.share_photo){
            val intent = Intent(this, PhotoShareActivity::class.java)
            startActivity(intent)
        }else if (item.itemId == R.id.sing_out){
            auth.signOut()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}