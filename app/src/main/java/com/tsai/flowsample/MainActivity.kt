package com.tsai.flowsample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import com.tsai.flowsample.databinding.MainActivityBinding
import com.tsai.flowsample.ui.main.MainFragment
import com.tsai.flowsample.util.Logger

class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Logger.d("activity onCreate")

        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        Logger.d("activity onStart")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val testStr = savedInstanceState.get("tsai")
        Logger.d("activity onRestoreInstanceState get String -> $testStr")
    }

    override fun onResume() {
        super.onResume()
        Logger.d("activity onResume")

    }

    override fun onPause() {
        super.onPause()
        Logger.d("activity onPause")

    }

    override fun onStop() {
        super.onStop()
        Logger.d("activity onStop")

    }

    override fun onRestart() {
        super.onRestart()
        Logger.d("activity onRestart")

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("tsai", "string from Activity onSaveInstanceState ")
        Logger.d("activity onSaveInstanceState")

    }

    override fun onDestroy() {
        super.onDestroy()
        Logger.d("activity onDestroy")

    }

}