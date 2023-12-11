package com.example.yyyyy

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.core.graphics.drawable.toBitmap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val db = Db.getDatabase(this)
        val image = findViewById<ImageView>(R.id.image)
        findViewById<Button>(R.id.save).setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val imageEntity = TestEntity(imageData = bitmapToByteArray(image.drawable.toBitmap()))
                db.imageDao().insertImage(imageEntity)
            }
        }
//        findViewById<Button>(R.id.load).setOnClickListener {
//            image.setImageResource(R.drawable.ic_launcher_foreground)
//            CoroutineScope(Dispatchers.IO).launch {
//                val im = db.imageDao().getImageById(1)
//                image.setImageBitmap(byteArrayToBitmap(im!!.imageData))
//            }
//        }



    }
    fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }
    fun byteArrayToBitmap(byteArray: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }

}