package com.example.calc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_simple_calc.*
import net.objecthunter.exp4j.Expression
import net.objecthunter.exp4j.ExpressionBuilder
import java.text.DecimalFormat


class SimpleCalcActivity : AppCompatActivity() {
    private var backClicked = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
        setContentView(R.layout.activity_simple_calc)

        btn0.setOnClickListener {
            resultsTV.text = addToInputText("0")
        }

        btn1.setOnClickListener {
            resultsTV.text = addToInputText("1")
        }

        btn2.setOnClickListener {
            resultsTV.text = addToInputText("2")
        }

        btn3.setOnClickListener {
            resultsTV.text = addToInputText("3")
        }

        btn4.setOnClickListener {
            resultsTV.text = addToInputText("4")
        }

        btn5.setOnClickListener {
            resultsTV.text = addToInputText("5")
        }

        btn6.setOnClickListener {
            resultsTV.text = addToInputText("6")
        }

        btn7.setOnClickListener {
            resultsTV.text = addToInputText("7")
        }

        btn8.setOnClickListener {
            resultsTV.text = addToInputText("8")
        }

        btn9.setOnClickListener {
            resultsTV.text = addToInputText("9")
        }

        btnMinus.setOnClickListener {
            resultsTV.text = addToInputText("-")
        }

        btnDiv.setOnClickListener {
            makeTextOrToast("/")
        }

        btnMult.setOnClickListener {
            makeTextOrToast("*")
        }

        btnDot.setOnClickListener {
            makeTextOrToast(".")
        }

        btnPlus.setOnClickListener {
            resultsTV.text = addToInputText("+")
        }

        btnEquals.setOnClickListener {
            showResult()
        }

        btnC.setOnClickListener {
            resultsTV.text = ""
        }

        btnBack.setOnClickListener {
            if (backClicked == 0) {
                val length = resultsTV.length()
                if(length > 0)
                    resultsTV.text = resultsTV.text.subSequence(0, length - 1)
                backClicked = 1
            } else {
                backClicked = 0
                resultsTV.text = ""
            }
        }

        btnChngSign.setOnClickListener {
            val length = resultsTV.length()
            if (length > 1) {
                if (resultsTV.text.toString()[length-2] != '-') {
                    resultsTV.text = StringBuffer(resultsTV.text).insert(length - 1, "-")
                } else{
                    resultsTV.text = StringBuffer(resultsTV.text).insert(length - 1, "+")
                }
            }
        }
    }

    private fun makeTextOrToast(symbol: String) {
        if (isLastSybmolDigit()) {
            resultsTV.text = addToInputText(symbol)
        } else {
            Toast.makeText(this, "Invalid operation", Toast.LENGTH_SHORT).show()
        }
    }

    private fun addToInputText(buttonValue: String): String {
        return "${resultsTV.text}$buttonValue"
    }

    private fun isLastSybmolDigit(): Boolean {
        val length = resultsTV.length()
        if (length > 0) {
            if (resultsTV.text.toString()[length - 1].isDigit())
               return true
            return false
        }
        return false
    }

    private fun showResult() {
        try {
            val result = ExpressionBuilder(resultsTV.text.toString()).build().evaluate()
            if (result.isNaN()) {
                resultsTV.text = "Error"
            } else {
                resultsTV.text = DecimalFormat("0.######").format(result).toString()
            }
        } catch (e: Exception) {
            resultsTV.text = "Error"
        }
    }
}
