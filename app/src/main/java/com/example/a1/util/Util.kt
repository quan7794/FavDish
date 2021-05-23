package com.example.a1.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.startActivity
import timber.log.Timber
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
                    startActivity(context,intent,null)
                } catch (ex: Exception) {
                    Timber.e(ex)
                }
            }
            .setNegativeButton("CANCEL") { dialog, _ ->
                dialog.dismiss()
            }.show()
    }
    }

}