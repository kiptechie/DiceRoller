package com.example.diceroller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
// import com.google.android.gms.ads.AdRequest
// import com.google.android.gms.ads.AdView
// import com.google.android.gms.ads.MobileAds
import java.util.*

class MainActivity : AppCompatActivity() {

    // lateinit var mAdView : AdView
    lateinit var diceImage: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

       // MobileAds.initialize(this) {}

       // mAdView = findViewById(R.id.adView)
       // val adRequest = AdRequest.Builder().build()
       // mAdView.loadAd(adRequest)

        val rollButton: Button = findViewById(R.id.roll_button)
        rollButton.text = "Let's Roll"
        rollButton.setOnClickListener {
                rollDice()
            }
        diceImage = findViewById(R.id.dice_image)

    }

    private fun rollDice() {
        val diceImage: ImageView = findViewById(R.id.dice_image)

        val randomInt = Random().nextInt(6) + 1

        val drawableResource = when (randomInt) {
            1 -> R.drawable.dice_1
            2 -> R.drawable.dice_2
            3 -> R.drawable.dice_3
            4 -> R.drawable.dice_4
            5 -> R.drawable.dice_5
            else -> R.drawable.dice_6
        }
        diceImage.setImageResource(drawableResource)
    }
}
