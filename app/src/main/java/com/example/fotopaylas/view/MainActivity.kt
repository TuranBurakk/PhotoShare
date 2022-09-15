package com.example.fotopaylas.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.fotopaylas.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth = FirebaseAuth.getInstance()
        val guncelKullanici = auth.currentUser
        if (guncelKullanici != null){
            val intent = Intent(this, FeedActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    fun register(view: View) {
        auth.createUserWithEmailAndPassword(emailtext.text.toString(),sifretext.text.toString()).addOnCompleteListener {
            if (it.isSuccessful){
                val intent = Intent(this, FeedActivity::class.java)
                startActivity(intent)
                finish()
            }
        }.addOnFailureListener {
            Toast.makeText(applicationContext,it.localizedMessage,Toast.LENGTH_LONG).show()
        }
    }
    fun login(view: View){
        auth.signInWithEmailAndPassword(emailtext.text.toString(),sifretext.text.toString()).addOnCompleteListener {
            if (it.isSuccessful){
                val guncelKullanici = auth.currentUser?.email.toString()
                Toast.makeText(this,"Hoşgeldin ${guncelKullanici}",Toast.LENGTH_LONG).show()
                val intent = Intent(this, FeedActivity::class.java)
                startActivity(intent)
                finish()
            }
        }.addOnFailureListener {
            Toast.makeText(this,it.localizedMessage,Toast.LENGTH_LONG).show()
        }
    }
}