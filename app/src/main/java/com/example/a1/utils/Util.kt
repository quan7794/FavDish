package com.example.a1.utils

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.provider.Settings
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.getDrawable
import androidx.core.content.ContextCompat.startActivity
import androidx.core.graphics.drawable.toBitmap
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.a1.BuildConfig
import com.example.a1.R
import com.example.a1.databinding.DialogCustomListBinding
import com.example.a1.view.adapers.CustomListItemAdapter
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Exception


class Util {
    companion object {

        fun Dialog.showDialog(activity: Activity, title: String, itemsList: List<String>, selection: String, callback: SelectedItem? = null) {
            val binding: DialogCustomListBinding = DialogCustomListBinding.inflate(layoutInflater)
            this.setContentView(binding.root)
            binding.dialogTitle.text = title
            binding.dialogContent.layoutManager = LinearLayoutManager(activity)
            val adapter = CustomListItemAdapter(activity, itemsList, selection, callback)
            binding.dialogContent.adapter = adapter
            this.show()
        }

        fun showRationalPermissionDialog(context: Context, packageName: String) {
            AlertDialog.Builder(context).setMessage(
                "Look like you have turned off permissions. Tu use this feature, you have" + " to enable permissions manual. Click GO TO SETTINGS to continue."
            ).setPositiveButton("GO TO SETTINGS") { _, _ ->
                    try {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        intent.data = Uri.fromParts("package", packageName, null)
                        startActivity(context, intent, null)
                    } catch (ex: Exception) {
                        Timber.e(ex)
                    }
                }.setNegativeButton("CANCEL") { dialog, _ ->
                    dialog.dismiss()
                }.show()
        }

        fun loadImage(context: Context, bitmap: Any, des: ImageView, fileName: String = "") {
            Glide.with(context).load(bitmap).centerCrop().listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean
                ): Boolean {
                    e?.printStackTrace()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean
                ): Boolean {
                    resource?.let {
                        saveBitmapImage(context, it.toBitmap(), fileName)
                    }
//                        des.setImageDrawable(resource)
                    return false
                }
            }).into(des)
        }

        private fun saveBitmapImage(context: Context, bitmap: Bitmap, fileName: String): Boolean {
            try {
                var file = context.getFileStreamPath("favDish")
                file.mkdirs()
                file = File(context.getFileStreamPath("favDish"), "$fileName.jpg")
                val stream = FileOutputStream(file, true)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                stream.flush()
                stream.close()
                Timber.e("saveBitmapImage() - ImagePath: ${file.absolutePath}")
                return true
            } catch (ex: IOException) {
                ex.printStackTrace()
            }
            return false
        }
    }

}

@BindingAdapter("setFavoriteIcon")
fun setFavoriteIcon(view: ImageView, isFavorite: Boolean) {
    Timber.e("setFavoriteIcon: isFavorite: $isFavorite")
    if (isFavorite) view.setImageResource(R.drawable.ic_favorite_selected)
    else view.setImageResource(R.drawable.ic_favorite_unselected)
}

enum class Status {
    SUCCESS, ERROR, LOADING
}

data class Resource<out T>(val status: Status, val data: T? = null, val message: String? = null) {
    companion object {
        fun <T> success(data: T): Resource<T> = Resource(Status.SUCCESS, data)
        fun <T> error(data: T? = null, message: String) = Resource(Status.ERROR, data, message)
        fun <T> loading(data: T? = null) = Resource(Status.LOADING, data)
    }
}