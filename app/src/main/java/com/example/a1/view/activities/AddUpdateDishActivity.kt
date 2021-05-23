package com.example.a1.view.activities

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.graphics.drawable.toBitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.a1.R
import com.example.a1.databinding.ActivityAddUpdateDishBinding
import com.example.a1.databinding.DialogImageSelectionBinding
import com.example.a1.util.Util
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Exception
import java.util.*

class AddUpdateDishActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var viewBinding: ActivityAddUpdateDishBinding
    private lateinit var dialog: Dialog
    private var imagePath = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityAddUpdateDishBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        initActionBar()
        viewBinding.dishAdd.setOnClickListener(this)
    }

    private fun initActionBar() {
        setSupportActionBar(viewBinding.actionBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        viewBinding.actionBar.setNavigationOnClickListener {
            onBackPressed()
        }

    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.dish_add -> {
                    Timber.d("Add dish image")
                    customSelectionDialog()
                    return
                }

                else -> {
                    Toast.makeText(this, "Unknown", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                CAMERA_CODE ->
                    data?.extras?.get("data")?.let {
                        loadImage(it)
                        dialog.dismiss()
                    }
                GALLERY_CODE ->
                    data?.data?.let {
                        Timber.d(it.toString())
                        loadImage(it)
                        dialog.dismiss()
                    }
            }
        }
    }

    private fun customSelectionDialog() {
        dialog = Dialog(this)
        val binding = DialogImageSelectionBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)
        binding.cameraSelect.setOnClickListener {
            Toast.makeText(this, "Camera select", Toast.LENGTH_SHORT).show()
            Dexter.withContext(this)
                .withPermissions(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        if (report.areAllPermissionsGranted()) {
                            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                            startActivityForResult(intent, CAMERA_CODE)

                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        p0: MutableList<PermissionRequest>?,
                        p1: PermissionToken?
                    ) {
                        Util.showRationalPermissionDialog(applicationContext, packageName)
                    }

                }
                ).onSameThread().check()
        }
        binding.gallerySelect.setOnClickListener {
            Toast.makeText(this, "Gallery select", Toast.LENGTH_SHORT).show()
            Dexter.withContext(this)
                .withPermissions(
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        if (report.areAllPermissionsGranted()) {
                            val intent = Intent(
                                Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                            )
                            startActivityForResult(intent, GALLERY_CODE)
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        p0: MutableList<PermissionRequest>?,
                        p1: PermissionToken?
                    ) {
                        Util.showRationalPermissionDialog(applicationContext, packageName)
                    }

                }
                ).onSameThread().check()
        }
        dialog.show()
    }

    private fun loadImage(bitmap: Any) {
        Glide.with(this)
            .load(bitmap)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    e?.printStackTrace()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    resource?.let { it -> saveBitmapImage(it.toBitmap()) }
                    viewBinding.dishImage.setImageDrawable(resource)
                    return true
                }
            })
            .centerCrop()
            .into(viewBinding.dishImage)
    }

    private fun saveBitmapImage(bitmap: Bitmap): Boolean {
        try {
            var file = applicationContext.getFileStreamPath("favDish")
            file.mkdirs()
            file = File(applicationContext.getFileStreamPath("favDish"), "${UUID.randomUUID()}.jpg")
            val stream = FileOutputStream(file, true)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
            imagePath = file.absolutePath
            Timber.e("ImagePath: $imagePath")
            return true
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
        return false

    }


    companion object {
        const val CAMERA_CODE = 1000
        const val GALLERY_CODE = 1001
    }
}