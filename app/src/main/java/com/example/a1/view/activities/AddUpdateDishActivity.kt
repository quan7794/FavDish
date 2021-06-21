package com.example.a1.view.activities

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a1.MainApplication
import com.example.a1.R
import com.example.a1.databinding.ActivityAddUpdateDishBinding
import com.example.a1.databinding.DialogCustomListBinding
import com.example.a1.databinding.DialogImageSelectionBinding
import com.example.a1.model.entities.FavDish
import com.example.a1.utils.Constants
import com.example.a1.utils.Util
import com.example.a1.view.adapers.CustomListItemAdapter
import com.example.a1.viewmodel.FavDishViewModel
import com.example.a1.viewmodel.FavDishViewModelFactory
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import timber.log.Timber
import java.util.*

class AddUpdateDishActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var viewBinding: ActivityAddUpdateDishBinding
    private lateinit var customSelectionDialog: Dialog
    private lateinit var customListDialog: Dialog
    private val mViewModel: FavDishViewModel by viewModels {
        FavDishViewModelFactory((application as MainApplication).repository)
    }

    private var imagePath = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityAddUpdateDishBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        initActionBar()
        viewBinding.dishAddImage.setOnClickListener(this)
        viewBinding.dishType.setOnClickListener(this)
        viewBinding.dishCategory.setOnClickListener(this)
        viewBinding.cookingTime.setOnClickListener(this)
        viewBinding.addDish.setOnClickListener(this)
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
                R.id.dish_add_image -> {
                    Timber.d("Add dish image")
                    customSelectionDialog()
                    return
                }
                R.id.dish_type -> {
                    Timber.d("Add dish_type")
                    customItemsDialog(
                        resources.getString(R.string.dish_type),
                        Constants.getDishTypes(),
                        Constants.DISH_TYPE
                    )
                    return
                }
                R.id.dish_category -> {
                    Timber.d("Add dish_category")
                    customItemsDialog(
                        resources.getString(R.string.dish_category),
                        Constants.getDishCategories(),
                        Constants.DISH_CATEGORY
                    )
                    return
                }
                R.id.cooking_time -> {
                    Timber.d("Add cooking_time")
                    customItemsDialog(
                        resources.getString(R.string.select_cooking_time),
                        Constants.getDishCookTime(),
                        Constants.DISH_COOKING_TIME
                    )
                    return
                }
                R.id.add_dish -> {
                    val newDish = FavDish(
                        imagePath,
                        Constants.DISH_IMAGE_SOURCE_LOCAL,
                        viewBinding.dishTitle.text.toString(),
                        viewBinding.dishType.text.toString(),
                        viewBinding.dishCategory.text.toString(),
                        viewBinding.dishIngredient.text.toString(),
                        viewBinding.cookingTime.text.toString(),
                        viewBinding.directionToCook.text.toString(),
                        false
                    )
                    try {
                        if (isValidate(newDish)) {
                            mViewModel.insert(newDish)
                            Timber.d("Add dish")
                            Toast.makeText(this, "Add dish $newDish", Toast.LENGTH_SHORT).show()
                            finish()
                            return

                        } else {
                            Timber.e("Add dish error $newDish")
                            Toast.makeText(this, "Add dish error $newDish", Toast.LENGTH_SHORT).show()
                        }
                    } catch (ex: Exception) {
                        Timber.e("Add dish error $ex")
                        Toast.makeText(this, "Add dish error $ex", Toast.LENGTH_SHORT).show()
                    }

                    return
                }
                else -> {
                    Toast.makeText(this, "Unknown", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun isValidate(favDish: FavDish): Boolean {
        return favDish.category != "" &&
                favDish.cookingTime != "" &&
                favDish.directionToCook != "" &&
                favDish.imagePath != "" &&
                favDish.ingredients != "" &&
                favDish.type != "" &&
                favDish.title != ""
    }

    fun selectedListItem(item: String, selection: String) {
        when (selection) {
            Constants.DISH_TYPE -> {
                customListDialog.dismiss()
                viewBinding.dishType.setText(item)
            }
            Constants.DISH_CATEGORY -> {
                customListDialog.dismiss()
                viewBinding.dishCategory.setText(item)
            }
            Constants.DISH_COOKING_TIME -> {
                customListDialog.dismiss()
                viewBinding.cookingTime.setText(item)
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                CAMERA_CODE ->
                    data?.extras?.get("data")?.let {
                        val imageName = UUID.randomUUID().toString()
                        imagePath =
                            "${applicationContext.filesDir.absolutePath}/favDish/$imageName.jpg"
                        Timber.e(imagePath)
                        Util.loadImage(this, it, viewBinding.dishImage, imageName)
                        customSelectionDialog.dismiss()
                    }
                GALLERY_CODE ->
                    data?.data?.let {
                        val imageName = UUID.randomUUID().toString()
                        imagePath =
                            "${applicationContext.filesDir.absolutePath}/favDish/$imageName.jpg"
                        Timber.e(imagePath)
                        Util.loadImage(this, it, viewBinding.dishImage, imageName)
                        customSelectionDialog.dismiss()
                    }
            }
        }
    }

    private fun customSelectionDialog() {
        customSelectionDialog = Dialog(this)
        val binding = DialogImageSelectionBinding.inflate(layoutInflater)
        customSelectionDialog.setContentView(binding.root)
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
                        Util.showRationalPermissionDialog(this@AddUpdateDishActivity, packageName)
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
                        Util.showRationalPermissionDialog(this@AddUpdateDishActivity, packageName)
                    }

                }
                ).onSameThread().check()
        }
        customSelectionDialog.show()
    }

    private fun customItemsDialog(title: String, itemsList: List<String>, selection: String) {
        customListDialog = Dialog(this)
        val binding: DialogCustomListBinding = DialogCustomListBinding.inflate(layoutInflater)
        customListDialog.setContentView(binding.root)
        binding.dialogTitle.text = title
        binding.dialogContent.layoutManager = LinearLayoutManager(this)
        val adapter = CustomListItemAdapter(this, itemsList, selection)
        binding.dialogContent.adapter = adapter
        customListDialog.show()
    }

    companion object {
        const val CAMERA_CODE = 1000
        const val GALLERY_CODE = 1001
    }
}