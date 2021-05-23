package com.example.a1.view.activities

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.example.a1.R
import com.example.a1.databinding.ActivityAddUpdateDishBinding
import com.example.a1.databinding.DialogImageSelectionBinding
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import timber.log.Timber
import java.lang.Exception

class AddUpdateDishActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var viewBinding: ActivityAddUpdateDishBinding
    private lateinit var dialog: Dialog
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
                    data?.extras?.let {
                        val bitmap =  it.get("data")
                        Glide.with(this)
                            .load(bitmap)
                            .centerCrop()
                            .into(viewBinding.dishImage)
                        dialog.dismiss()
                    }
                GALLERY_CODE ->
                    data?.data?.let {
                        Timber.d(it.toString())
                        Glide.with(this)
                            .load(it)
                            .centerCrop()
                            .into(viewBinding.dishImage)
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
                        showRationalPermissionDialog()
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
                        showRationalPermissionDialog()
                    }

                }
                ).onSameThread().check()
        }
        dialog.show()
    }

    private fun showRationalPermissionDialog() {
        AlertDialog.Builder(this)
            .setMessage(
                "Look like you have turned off permissions. Tu use this feature, you have" +
                        " to enable permissions manual. Click GO TO SETTINGS to continue."
            )
            .setPositiveButton("GO TO SETTINGS")
            { _, _ ->
                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    intent.data = Uri.fromParts("package", packageName, null)
                    startActivity(intent)
                } catch (ex: Exception) {
                    Timber.e(ex)
                }
            }
            .setNegativeButton("CANCEL") { dialog, _ ->
                dialog.dismiss()
            }.show()
    }

    private fun saveBitmapImage(bitmap: Bitmap?) : String {
        Timber.e("sadfa ${applicationContext.packageCodePath}")
        Timber.e("sadfa ${applicationContext.getDatabasePath("á")}")
        Timber.e("sadfa ${applicationContext.getFileStreamPath("á")}")
        Timber.e("sadfa ${applicationContext.packageResourcePath}")
        return ""
    }

    companion object {
        const val CAMERA_CODE = 1000
        const val GALLERY_CODE = 1001
    }
}