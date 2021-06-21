package com.example.a1.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.provider.Settings
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.startActivity
import androidx.core.graphics.drawable.toBitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Exception

class Util {
    companion object {

        fun showRationalPermissionDialog(context: Context, packageName: String) {
            AlertDialog.Builder(context)
                .setMessage(
                    "Look like you have turned off permissions. Tu use this feature, you have" +
                            " to enable permissions manual. Click GO TO SETTINGS to continue."
                )
                .setPositiveButton("GO TO SETTINGS")
                { _, _ ->
                    try {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        intent.data = Uri.fromParts("package", packageName, null)
                        startActivity(context, intent, null)
                    } catch (ex: Exception) {
                        Timber.e(ex)
                    }
                }
                .setNegativeButton("CANCEL") { dialog, _ ->
                    dialog.dismiss()
                }.show()
        }

        fun loadImage(context: Context, bitmap: Any, des: ImageView, fileName: String = "") {
            Glide.with(context)
                .load(bitmap)
                .centerCrop()
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
                        resource?.let {
                            saveBitmapImage(context, it.toBitmap(), fileName)
                        }
                        des.setImageDrawable(resource)
                        return true
                    }
                })
                .into(des)
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