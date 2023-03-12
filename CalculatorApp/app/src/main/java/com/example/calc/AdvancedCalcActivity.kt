package com.example.calc

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_advenced_calc.*
import kotlinx.android.synthetic.main.activity_simple_calc.*
import kotlinx.android.synthetic.main.activity_simple_calc.btn0
import kotlinx.android.synthetic.main.activity_simple_calc.btn1
import kotlinx.android.synthetic.main.activity_simple_calc.btn2
import kotlinx.android.synthetic.main.activity_simple_calc.btn3
import kotlinx.android.synthetic.main.activity_simple_calc.btn4
import kotlinx.android.synthetic.main.activity_simple_calc.btn5
import kotlinx.android.synthetic.main.activity_simple_calc.btn6
import kotlinx.android.synthetic.main.activity_simple_calc.btn7
import kotlinx.android.synthetic.main.activity_simple_calc.btn8
import kotlinx.android.synthetic.main.activity_simple_calc.btn9
import kotlinx.android.synthetic.main.activity_simple_calc.btnBack
import kotlinx.android.synthetic.main.activity_simple_calc.btnC
import kotlinx.android.synthetic.main.activity_simple_calc.btnChngSign
import kotlinx.android.synthetic.main.activity_simple_calc.btnDiv
import kotlinx.android.synthetic.main.activity_simple_calc.btnDot
import kotlinx.android.synthetic.main.activity_simple_calc.btnEquals
import kotlinx.android.synthetic.main.activity_simple_calc.btnMinus
import kotlinx.android.synthetic.main.activity_simple_calc.btnMult
import kotlinx.android.synthetic.main.activity_simple_calc.btnPlus
import kotlinx.android.synthetic.main.activity_simple_calc.resultsTV
import net.objecthunter.exp4j.ExpressionBuilder
import java.lang.StringBuilder
import java.text.DecimalFormat

class AdvancedCalcActivity : AppCompatActivity() {
    private var backClicked = 0
    private var advFunClicked = false

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
        setContentView(R.layout.activity_advenced_calc)
        btn0.setOnClickListener {
            resultsTV.text = addToInputText("0")
        }

        btn1.setOnClickListener {
            resultsTV.text = addToInputText("1")
        }

        btn2.setOnClickListener {
            val length = resultsTV.length()
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

        btnSin.setOnClickListener {
            val lastNumber = getLastNumber()
            if (lastNumber.isNotEmpty()) {
                resultsTV.text = resultsTV.text.subSequence(0, resultsTV.length() - lastNumber.length)
                resultsTV.text = "${resultsTV.text}sin($lastNumber)"
            } else {
                Toast.makeText(this, "Invalid operation", Toast.LENGTH_SHORT).show()
            }
        }

        btnCos.setOnClickListener {
            val lastNumber = getLastNumber()
            if (lastNumber.isNotEmpty()) {
                resultsTV.text = resultsTV.text.subSequence(0, resultsTV.length() - lastNumber.length)
                resultsTV.text = "${resultsTV.text}cos($lastNumber)"
            } else {
                Toast.makeText(this, "Invalid operation", Toast.LENGTH_SHORT).show()
            }
        }

        btnTan.setOnClickListener {
            val lastNumber = getLastNumber()
            if (lastNumber.isNotEmpty()) {
                resultsTV.text = resultsTV.text.subSequence(0, resultsTV.length() - lastNumber.length)
                resultsTV.text = "${resultsTV.text}tan($lastNumber)"
            } else {
                Toast.makeText(this, "Invalid operation", Toast.LENGTH_SHORT).show()
            }
        }

        btnTan.setOnClickListener {
            val lastNumber = getLastNumber()
            if (lastNumber.isNotEmpty()) {
                resultsTV.text = resultsTV.text.subSequence(0, resultsTV.length() - lastNumber.length)
                resultsTV.text = "${resultsTV.text}tan($lastNumber)"
            } else {
                Toast.makeText(this, "Invalid operation", Toast.LENGTH_SHORT).show()
            }
        }

        btnLog.setOnClickListener {
            val lastNumber = getLastNumber()
            if (lastNumber.isNotEmpty()) {
                resultsTV.text = resultsTV.text.subSequence(0, resultsTV.length() - lastNumber.length)
                resultsTV.text = "${resultsTV.text}log($lastNumber)"
            } else {
                Toast.makeText(this, "Invalid operation", Toast.LENGTH_SHORT).show()
            }
        }

        btnSqrt.setOnClickListener {
            val lastNumber = getLastNumber()
            if (lastNumber.isNotEmpty()) {
                resultsTV.text = resultsTV.text.subSequence(0, resultsTV.length() - lastNumber.length)
                resultsTV.text = "${resultsTV.text}sqrt($lastNumber)"
            } else {
                Toast.makeText(this, "Invalid operation", Toast.LENGTH_SHORT).show()
            }
        }

        btnX2.setOnClickListener {
            val lastNumber = getLastNumber()
            if (lastNumber.isNotEmpty()) {
                resultsTV.text = resultsTV.text.subSequence(0, resultsTV.length() - lastNumber.length)
                resultsTV.text = "${resultsTV.text}($lastNumber)^2"
            } else {
                Toast.makeText(this, "Invalid operation", Toast.LENGTH_SHORT).show()
            }
        }

        btnXy.setOnClickListener {
            val lastNumber = getLastNumber()
            if (lastNumber.isNotEmpty()) {
                resultsTV.text = resultsTV.text.subSequence(0, resultsTV.length() - lastNumber.length)
                resultsTV.text = "${resultsTV.text}($lastNumber)^"
            } else {
                Toast.makeText(this, "Invalid operation", Toast.LENGTH_SHORT).show()
            }
        }

        btnLN.setOnClickListener {
            val lastNumber = getLastNumber()
            if (lastNumber.isNotEmpty()) {
                resultsTV.text = resultsTV.text.subSequence(0, resultsTV.length() - lastNumber.length)
                resultsTV.text = "${resultsTV.text}loge($lastNumber)"
            } else {
                Toast.makeText(this, "Invalid operation", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getLastNumber(): String {
        var length = resultsTV.length()
        if (length > 0 && resultsTV.text.toString()[length-1].isDigit()) {
            var resultString = resultsTV.text.toString()[length - 1].toString()

            length--
            while (length != 0 && resultsTV.text.toString()[length - 1].isDigit()) {
                resultString = StringBuffer(resultString).insert(0, resultsTV.text.toString()[length - 1])
                    .toString()
                length--
            }

            return resultString
        }
        return ""
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
            if (resultsTV.text.toString()[length - 1].isDigit()
                || resultsTV.text.toString()[length - 1] == ')'
                || resultsTV.text.toString()[length - 1] == '^')
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