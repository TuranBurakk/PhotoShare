package com.example.fotopaylas.view

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.fotopaylas.R
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_photo_share.*
import java.util.*

@Suppress("DEPRECATION")
class PhotoShareActivity : AppCompatActivity() {
    var  secilenGorsel : Uri? = null
    var secilenBitmap : Bitmap? = null
    private lateinit var storage : FirebaseStorage
    private lateinit var auth : FirebaseAuth
    private lateinit var database : FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_share)
        storage = FirebaseStorage.getInstance()
        database = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
    }
    fun share(view: View){
        val uuid = UUID.randomUUID()
        val gorselIsmi = "${uuid}.jpg"
        val reference = storage.reference
        val gorselReferance = reference.child("images").child(gorselIsmi)
        if (secilenGorsel != null){
            gorselReferance.putFile(secilenGorsel!!).addOnSuccessListener {
                val yuklenenGorselReference = FirebaseStorage.getInstance().reference.child("images").child(gorselIsmi)
                yuklenenGorselReference.downloadUrl.addOnSuccessListener { uri ->
                    val downloadUrl = uri.toString()
                    val currentUserEmail = auth.currentUser!!.email.toString()
                    val userComment = commenttext.text.toString()
                    val date = Timestamp.now()
                    val postHashMap = hashMapOf<String,Any>()
                    postHashMap.put("gorselurl",downloadUrl)
                    postHashMap.put("useremail",currentUserEmail)
                    postHashMap.put("usercomment",userComment)
                    postHashMap.put("date",date)
                    database.collection("Post").add(postHashMap).addOnCompleteListener {
                        if (it.isSuccessful){
                            val intent = Intent(this,FeedActivity::class.java)
                            startActivity(intent)
                            finish()

                        }
                    }.addOnFailureListener { exception ->
                        Toast.makeText(applicationContext,exception.localizedMessage,Toast.LENGTH_LONG).show()

                    }
                }
            }
        }

    }
    fun photoSelect(view: View){
    if (ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),1)

    }else{
        val galeriIntent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galeriIntent,2)

    }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode == 1){
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                val galeriIntent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galeriIntent,2)
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == 2 && resultCode == Activity.RESULT_OK && data != null ){
          secilenGorsel = data.data
          if (secilenGorsel != null){
              if (Build.VERSION.SDK_INT >= 28){
                  val source = ImageDecoder.createSource(this.contentResolver,secilenGorsel!!)
                  secilenBitmap = ImageDecoder.decodeBitmap(source)
                  imageView.setImageBitmap(secilenBitmap)
              }else{
                  secilenBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver,secilenGorsel)
                  imageView.setImageBitmap(secilenBitmap)
              }
          }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}