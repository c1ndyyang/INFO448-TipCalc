package edu.uw.ischool.c1ndyy.tipcalc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    var tipPercent: Double = 0.10
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tipButton = findViewById<Button>(R.id.tipButton)
        val amountInput = findViewById<EditText>(R.id.amountInput)
        val radioGroup = findViewById<RadioGroup>(R.id.radioGroup)

        amountInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                // enable the button when there is text in the EditText
                val input = s.toString()
                val strInput = input.replace("$", "")
                var isValid = isValidCurrencyAmount(strInput)

                amountInput.removeTextChangedListener(this)
                if (strInput != null) {
                    if (isValid && strInput.contains(".")) {
                        val decimalPlaces = strInput.split(".")[1]
                        if (decimalPlaces.length > 2 || decimalPlaces.length == 0) {
                            isValid = false
                            amountInput.setSelection(strInput.length) // Place the cursor at the end
                            return
                        }
                    }

                    tipButton.isEnabled = isValid

                    if (isValid == false) {
                        amountInput.error = "Please enter correct currency value"
                    } else {
                        amountInput.error = null
                        val formattedInput = "$" + strInput
                        amountInput.setText(formattedInput)
                        amountInput.setSelection(formattedInput.length)
                    }
                }
                amountInput.addTextChangedListener(this)
            }
        })

        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            // update the selected tipPct state
            tipPercent = when (checkedId) {
                R.id.tenRadio -> 0.10
                R.id.fifteenRadio -> 0.15
                R.id.eighteenRadio -> 0.18
                R.id.twentyRadio -> 0.20
                else -> 0.10
            }
        }

        tipButton.setOnClickListener (View.OnClickListener {
            val userInput = amountInput.text.toString().replace("$", "")
            val total = userInput.toDouble()
            val tipFinal = tipPercent * total
            val tipFinalStr = String.format("$%.2f", tipFinal)
            val toast = Toast.makeText(this, tipFinalStr, Toast.LENGTH_LONG)
            toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 60)
            toast.show()
        })
    }

    private fun isValidCurrencyAmount(input: String): Boolean {
        // Regular expression to match valid currency amounts
        val currencyPattern = """^\d+(\.\d{1,2})?$""".toRegex()
        return currencyPattern.matches(input)
    }
}