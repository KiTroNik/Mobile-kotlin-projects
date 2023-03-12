package com.example.calc

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)

        aboutButton.setOnClickListener {
            val intent = Intent(this, AboutActivity::class.java)
            startActivity(intent)
        }

        simpleButton.setOnClickListener {
            val intent = Intent(this, SimpleCalcActivity::class.java)
            startActivity(intent)
        }

        advencedButton.setOnClickListener {
            val intent = Intent(this, AdvancedCalcActivity::class.java)
            startActivity(intent)
        }

        exitButton.setOnClickListener {
            exitProcess(-1)
        }
    }
}
