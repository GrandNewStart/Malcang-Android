package com.malcang.malcang.config

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import com.malcang.malcang.R

class CustomProgressDialog(context: Context) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window!!.setBackgroundDrawable(ColorDrawable())
        window!!.setDimAmount(0.2f)
        setContentView(R.layout.layout_progress_bar)
    }

}
