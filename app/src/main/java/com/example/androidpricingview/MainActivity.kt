package com.example.androidpricingview

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.androidpricingview.databinding.ActivityMainBinding

class MainActivity : Activity() {

    val pageView = arrayOf("0", "10K", "50K", "100K", "500K", "1M")
    val priceView = arrayOf("0", "$8.00", "$12.00", "$16.00", "$24.00", "$36.00")
    private val discount: Double = 25.0
    private lateinit var binderObject: ActivityMainBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binderObject = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binderObject.apply {
            pageText.text = pageView[1] + " " + getString(R.string.page_views)
            priceText.text = getSpannableString(priceView[1])
            seekBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    pageText.text = pageView[progress] + " " + getString(R.string.page_views)
                    priceText.text = getSpannableString(priceView[progress])
                    //here we can write some code to do something when progress is changed
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {
                    //here we can write some code to do something whenever the user touche the seekbar
                }

                override fun onStopTrackingTouch(seekBar: SeekBar) {
                    // show some message after user stopped scrolling the seekbar
                }
            })
        }
    }

    private fun getSpannableString(s: String): SpannableString {
        val ssS = SpannableString("$s/ month")
        ssS.setSpan(
            RelativeSizeSpan(3f),
            0,
            ssS.indexOf("/"),
            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        ssS.setSpan(
            StyleSpan(Typeface.BOLD),
            0,
            ssS.indexOf("/"),
            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return ssS
    }

    // Button of Start my Trail Click 
    fun onButtonClick(view: View) {
        val cost: Double? = getTotalPrice()
        // added toast for testing , please remove on last
        Toast.makeText(this, "Your Total Payable cost is $$cost", Toast.LENGTH_LONG).show()
    }

    private fun getTotalPrice(): Double? {

        return if (binderObject.checkbox.isChecked) {
            getDiscountPrice(getPrice())
        } else {
            getPrice()
        }
    }

    private fun getDiscountPrice(price: Double?): Double? {
        return when {
            price != null -> {
                (price - ((price / 100) * discount)) * 12
            }
            else -> {
                null
            }
        }
    }

    private fun getPrice(): Double? {
        val text = binderObject.priceText.text.removePrefix("$")
        val value =
            text.replaceRange(text.indexOf("/"), text.length, "").toString().toDoubleOrNull()
        if (value != null) {
            return value.toDouble()
        }
        return null
    }
}

