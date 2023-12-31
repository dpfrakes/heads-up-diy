package com.dpfrakes.headsupdiy

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AlertDialog

object ErrorDialog {

    fun showErrorDialog(context: Context, message: String) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Error")
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ ->
                // Handle the OK button click if needed
                dialog.dismiss()
                val intent = Intent(context, GameMenuActivity::class.java)
                context.startActivity(intent)
            }

        val dialog = builder.create()
        dialog.show()
    }
}
