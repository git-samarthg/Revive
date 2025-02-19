package com.example.revive

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import com.example.revive.utils.ToastHelper // Import the ToastHelper
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class AddItemActivity : AppCompatActivity() {

    private lateinit var itemName: EditText
    private lateinit var itemDescription: EditText
    private lateinit var itemPrice: EditText
    private lateinit var itemImage: ImageView
    private lateinit var uploadButton: Button
    private lateinit var submitButton: Button

    private var imageUri: Uri? = null
    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)

        itemName = findViewById(R.id.item_name)
        itemDescription = findViewById(R.id.item_description)
        itemPrice = findViewById(R.id.item_price)
        itemImage = findViewById(R.id.item_image)
        uploadButton = findViewById(R.id.upload_button)
        submitButton = findViewById(R.id.submit_button)

        uploadButton.setOnClickListener {
            openImagePicker()
        }

        submitButton.setOnClickListener {
            uploadItem()
        }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICK_CODE && resultCode == RESULT_OK) {
            imageUri = data?.data
            itemImage.setImageURI(imageUri)
        }
    }

    private fun uploadItem() {
        val name = itemName.text.toString()
        val description = itemDescription.text.toString()
        val price = itemPrice.text.toString().toDoubleOrNull()

        if (name.isEmpty() || description.isEmpty() || price == null || imageUri == null) {
            Log.d("AddItemActivity", "Name: $name, Description: $description, Price: $price, ImageUri: $imageUri")
            ToastHelper.showToastAndLog(this, "Please fill all fields") // Modified here
            return
        }

        val itemId = UUID.randomUUID().toString()
        val imageRef = storage.reference.child("item_images/$itemId")

        Log.d("AddItemActivity", "Uploading image to: ${imageRef.path}")

        imageRef.putFile(imageUri!!)
            .addOnSuccessListener {
                Log.d("AddItemActivity", "Image uploaded successfully")
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    val itemData = hashMapOf(
                        "name" to name,
                        "description" to description,
                        "price" to price,
                        "imageUrl" to uri.toString(),
                        "createdAt" to System.currentTimeMillis(),
                        "userId" to "userId123", // Replace with actual user ID
                        "category" to "Default",
                        "status" to "available"
                    )

                    db.collection("items").document(itemId)
                        .set(itemData)
                        .addOnSuccessListener {
                            Log.d("AddItemActivity", "Item added successfully to Firestore")
                            ToastHelper.showToastAndLog(this, "Item listed successfully!") // Modified here
                            finish()
                        }
                        .addOnFailureListener { e ->
                            Log.w("AddItemActivity", "Error adding document", e)
                            ToastHelper.showToastAndLog(this, "Error adding item to Firestore") // Modified here
                        }
                }
            }
            .addOnFailureListener { e ->
                Log.w("AddItemActivity", "Error uploading image", e)
                ToastHelper.showToastAndLog(this, "Error uploading image") // Modified here
            }
    }

    companion object {
        private const val IMAGE_PICK_CODE = 1000
    }
}
