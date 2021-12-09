package com.example.filmsearch.ui.main.utils

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import com.example.filmsearch.R

class AlertDialogs(context: Context) : AlertDialog(context) {

    fun showAlertDialog(title: String, message: String) {
        androidx.appcompat.app.AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(R.string.AD_access_to_contacts_pos_btn) { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)

                val uri: Uri =
                    Uri.fromParts("package", context.packageName, null)
                intent.data = uri
                context.startActivity(intent)
            }
            .setNegativeButton(R.string.AD_access_to_contacts_neg_btn) { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }
}