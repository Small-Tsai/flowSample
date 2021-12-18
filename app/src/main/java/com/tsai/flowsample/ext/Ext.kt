package com.tsai.flowsample.ext

import android.widget.Toast
import com.tsai.flowsample.MyApplication

fun String.toast() {
    Toast.makeText(MyApplication.instance, this, Toast.LENGTH_SHORT).show()
}