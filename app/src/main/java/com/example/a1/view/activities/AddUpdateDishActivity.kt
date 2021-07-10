package com.example.a1.view.activities

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.a1.MainApplication
import com.example.a1.R
import com.example.a1.databinding.ActivityAddUpdateDishBinding
import com.example.a1.databinding.DialogCustomListBinding
import com.example.a1.databinding.DialogImageSelectionBinding
import com.example.a1.model.entities.FavDish
import com.example.a1.utils.Constants
import com.example.a1.utils.SelectedItem
import com.example.a1.utils.Util
import com.example.a1.utils.Util.Companion.showDialog
import com.example.a1.view.adapers.CustomListItemAdapter
import com.example.a1.viewmodel.FavDishViewModel
import com.example.a1.viewmodel.FavDishViewModelFactory
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import timber.log.Timber
import java.util.UUID

class AddUpdateDishActivity : AppCompatActivity(), View.OnClickListener, SelectedItem {
    private var editDish: FavDish? = null
    private lateinit var viewBinding: ActivityAddUpdateDishBinding
    private lateinit var customSelectionDialog: Dialog
    private lateinit var customListDialog: Dialog
    private var isEdit = false
    private val mViewModel: FavDishViewModel by viewModels {
        FavDishViewModelFactory((application as MainApplication).repository)
    }
    private var mImagePath = ""

    private val cameraResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
            it.data?.extras?.get("data")?.let { image ->
                val imageName = UUID.randomUUID().toString()
                mImagePath = "${applicationContext.filesDir.absolutePath}/favDish/$imageName.jpg"
                Timber.e(mImagePath)
                Util.loadImage(this, image, viewBinding.dishImage, imageName)
                customSelectionDialog.dismiss()
            }
        }
    }

    private val galleryResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        it.data?.data?.let { imageUri ->
            val imageName = UUID.randomUUID().toString()
            mImagePath = "${applicationContext.filesDir.absolutePath}/favDish/$imageName.jpg"
            Timber.e(mImagePath)
            Util.loadImage(this, imageUri, viewBinding.dishImage, imageName)
            customSelectionDialog.dismiss()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        customListDialog = Dialog(this)
        viewBinding = ActivityAddUpdateDishBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        if (intent.hasExtra(Constants.DISH_UPDATE)) {
            isEdit = true
            editDish = intent.getParcelableExtra(Constants.DISH_UPDATE)
        }
        initActionBar()
        initData()
    }

    private fun initData() {
        viewBinding.dishAddImage.setOnClickListener(this)
        viewBinding.dishType.setOnClickListener(this)
        viewBinding.dishCategory.setOnClickListener(this)
        viewBinding.cookingTime.setOnClickListener(this)
        viewBinding.addDish.setOnClickListener(this)
        editDish?.let {
            viewBinding.apply {
                addDish.text = getString(R.string.update_dish)
                dishTitle.setText(it.title)
                dishType.setText(it.type)
                dishCategory.setText(it.category)
                directionToCook.setText(it.directionToCook)
                cookingTime.setText(it.cookingTime)
                dishIngredient.setText(it.ingredients)
                Glide.with(this@AddUpdateDishActivity).load(it.imagePath).into(dishImage)
                mImagePath = it.imagePath
            }

        }
    }

    private fun initActionBar() {
        setSupportActionBar(viewBinding.actionBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        if (isEdit) supportActionBar?.title = getString(R.string.edit_dish)
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
                    customListDialog.showDialog(this, resources.getString(R.string.dish_type), Constants.getDishTypes(), Constants.DISH_TYPE)
                    return
                }
                R.id.dish_category -> {
                    Timber.d("Add dish_category")
                    customListDialog.showDialog(this, resources.getString(R.string.dish_category), Constants.getDishCategories(), Constants.DISH_CATEGORY)
                    return
                }
                R.id.cooking_time -> {
                    Timber.d("Add cooking_time")
                    customListDialog.showDialog(this, resources.getString(R.string.select_cooking_time), Constants.getDishCookTime(), Constants.DISH_COOKING_TIME)
                    return
                }
                R.id.add_dish -> {
                    var id = 0
                    var isFavorite = false
                    var imageSource = Constants.DISH_IMAGE_SOURCE_LOCAL
                    editDish?.let {
                        id = it.id
                        isFavorite = it.favoriteDish
                        imageSource = it.imageSource
                    }

                    val resultDish = FavDish(
                        mImagePath,
                        imageSource,
                        viewBinding.dishTitle.text.toString(),
                        viewBinding.dishType.text.toString(),
                        viewBinding.dishCategory.text.toString(),
                        viewBinding.dishIngredient.text.toString(),
                        viewBinding.cookingTime.text.toString(),
                        viewBinding.directionToCook.text.toString(),
                        isFavorite,
                        id
                    )
                    try {
                        if (isValidate(resultDish)) {
                            if (!isEdit) {
                                mViewModel.insert(resultDish)
                                Timber.d("Add dish")
                                Toast.makeText(this, "Add dish $resultDish", Toast.LENGTH_SHORT).show()
                            } else {
                                mViewModel.update(resultDish)
                                Timber.d("Update dish")
                                Toast.makeText(this, "Update dish $resultDish", Toast.LENGTH_SHORT).show()
                            }
                            finish()
                            return

                        } else {
                            Timber.e("Dish validate fail, please update missing or wrong value. $resultDish")
                            Toast.makeText(this, "Dish validate fail, please update missing or wrong value. $resultDish", Toast.LENGTH_SHORT).show()
                        }
                    } catch (ex: Exception) {
                        Timber.e("Add/Update dish error $ex")
                        Toast.makeText(this, "Add/Update dish error $ex", Toast.LENGTH_SHORT).show()
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
        return favDish.category != "" && favDish.cookingTime != "" && favDish.directionToCook != "" && favDish.imagePath != "" && favDish.ingredients != "" && favDish.type != "" && favDish.title != ""
    }

    override fun onSelected(item: String, selection: String) {
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

    private fun customSelectionDialog() {
        customSelectionDialog = Dialog(this)
        val binding = DialogImageSelectionBinding.inflate(layoutInflater)
        customSelectionDialog.setContentView(binding.root)
        binding.cameraSelect.setOnClickListener {
            Toast.makeText(this, "Camera select", Toast.LENGTH_SHORT).show()
            Dexter.withContext(this).withPermissions(
                Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted()) {
                        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        cameraResultLauncher.launch(intent)

                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: MutableList<PermissionRequest>?, p1: PermissionToken?
                ) {
                    Util.showRationalPermissionDialog(this@AddUpdateDishActivity, packageName)
                }

            }).onSameThread().check()
        }
        binding.gallerySelect.setOnClickListener {
            Toast.makeText(this, "Gallery select", Toast.LENGTH_SHORT).show()
            Dexter.withContext(this).withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted()) {
                        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                        galleryResultLauncher.launch(intent)
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: MutableList<PermissionRequest>?, p1: PermissionToken?
                ) {
                    Util.showRationalPermissionDialog(this@AddUpdateDishActivity, packageName)
                }

            }).onSameThread().check()
        }
        customSelectionDialog.show()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}