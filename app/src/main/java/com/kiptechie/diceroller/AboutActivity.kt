package com.kiptechie.diceroller

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_about.*
import org.jetbrains.anko.browse
import org.jetbrains.anko.email
import org.jetbrains.anko.share

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        supportActionBar?.hide()

        setClickFunctions()
    }

    private fun setClickFunctions() {
        email_ll.setOnClickListener(View.OnClickListener {
            email("theecodepoet@gmail.com")
        })

        git_hub_card.setOnClickListener(View.OnClickListener {
            browse("https://github.com/kiptechie")
        })

        phone_ll.setOnClickListener(View.OnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            val number = "+254711928250"
            intent.data = Uri.parse("tel:$number")
            startActivity(intent)
        })

        share_my_app.setOnClickListener(View.OnClickListener {
            share("Hey check out more of my apps on Play Store https://play.google.com/store/apps/developer?id=Kiptechie")
        })
    }
}
