package com.rkpandey.mymemory

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rkpandey.mymemory.models.BoardSize
import com.rkpandey.mymemory.utils.EXTRA_BOARD_SIZE
import com.rkpandey.mymemory.utils.isPermissionGranted
import com.rkpandey.mymemory.utils.requestPermission
import java.net.URI

class CreateActivity : AppCompatActivity() {
    companion object {
        private const val PICK_PHOTO_CODE = 655
        private const val READ_EXTERNAL_PHOTOS_CODE = 248
        private const val READ_PHOTOS_PERMISSION = android.Manifest.permission.READ_EXTERNAL_STORAGE
    }
    private lateinit var imagePickerAdapter: ImagePickerAdapter
    private lateinit var boardSize:BoardSize
    private var numImagesRequired = -1
    private lateinit var rvImagePicker : RecyclerView
    private lateinit var etGameName : EditText
    private lateinit var btnSave : Button
    private val chosenImagesUris = mutableListOf<Uri>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create)

        rvImagePicker = findViewById(R.id.rvImagePicker)
        etGameName = findViewById(R.id.etGameName)
        btnSave = findViewById(R.id.btnSave)


        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        boardSize = intent.getSerializableExtra(EXTRA_BOARD_SIZE) as BoardSize
        numImagesRequired = boardSize.getNumPairs()
        supportActionBar?.title = "Choose pics (0 / $numImagesRequired)"

        val intent = Intent(this, CreateActivity::class.java)
        intent.putExtra(EXTRA_BOARD_SIZE, BoardSize.MEDIUM)
        startActivity(intent)

        imagePickerAdapter =ImagePickerAdapter(this, chosenImagesUris,boardSize, object : ImagePickerAdapter.ImageClickListener{
            override fun onPlaceHolderClick() {
                if (isPermissionGranted(this@CreateActivity,READ_PHOTOS_PERMISSION)) {
                    launchIntentForPhotos()
                }else {
                    requestPermission(this@CreateActivity, READ_PHOTOS_PERMISSION , READ_EXTERNAL_PHOTOS_CODE)
                }
            }
        })
        rvImagePicker.adapter = imagePickerAdapter
        rvImagePicker.setHasFixedSize(true)
        rvImagePicker.layoutManager = GridLayoutManager(this, boardSize.getWidth())
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == READ_EXTERNAL_PHOTOS_CODE){
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                launchIntentForPhotos()
            }else {
                Toast.makeText(this, "Please give us the permission to access for your photos" , Toast.LENGTH_SHORT).show()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home){
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
        
    private fun launchIntentForPhotos() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        startActivityForResult(Intent.createChooser(intent, "Choose pics"),PICK_PHOTO_CODE)
    }

}