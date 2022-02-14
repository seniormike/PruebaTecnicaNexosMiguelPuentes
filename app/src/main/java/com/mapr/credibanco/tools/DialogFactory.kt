package com.mapr.credibanco.tools

import android.content.Context
import android.view.Gravity
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import com.mapr.credibanco.R

class DialogFactory {

    /**
     * Method that return an AlertDialog with loading screen.
     */
    fun setProgress(context: Context): AlertDialog {
        val linearLayout = LinearLayout(context)
        linearLayout.orientation = LinearLayout.VERTICAL
        linearLayout.gravity = Gravity.CENTER
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        params.gravity = Gravity.CENTER
        linearLayout.layoutParams = params

        val progressBar = ProgressBar(context)
        progressBar.isIndeterminate = true
        linearLayout.addView(progressBar)

        val builder = AlertDialog.Builder(context, R.style.CustomDialogLoader)
        builder.setCancelable(false)
        builder.setView(linearLayout)

        val dialog = builder.create()
        val window = dialog.window
        if (window != null) {
            val layoutParams = WindowManager.LayoutParams()
            layoutParams.copyFrom(dialog.window?.attributes)
            layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT
            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
            dialog.window?.attributes = layoutParams
        }
        return dialog
    }
    /**
     * Method that return an AlertDialog with loading screen.
     */
    fun getDialog(msg:String,context: Context): AlertDialog {
        val builder = AlertDialog.Builder(context)
        val yes = "Entendido"
        val no = "Cancelar"
        builder.setMessage(msg)
            .setCancelable(false)
            .setPositiveButton(yes) { dialog, _ ->
                //listener.onAddToCart(item)
                dialog.dismiss()
            }
            /*.setNegativeButton(no) { dialog, _ ->
                dialog.dismiss()
            }*/
        return builder.create()
    }

}