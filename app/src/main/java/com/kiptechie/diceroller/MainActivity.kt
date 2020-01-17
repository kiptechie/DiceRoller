package com.kiptechie.diceroller

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.text.Html
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.kiptechie.diceroller.classes.ConnectivityReceiver
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import org.jetbrains.anko.browse
import org.json.JSONObject
import java.io.IOException
import java.util.*


class MainActivity : AppCompatActivity(), ConnectivityReceiver.ConnectivityReceiverListener {

    private var snackBar: Snackbar? = null
    private var connected: Boolean = false

    // we will use the ICNDB API for Chuck Norris Facts
    val URL = "https://api.icndb.com/jokes/random"
    var okHttpClient: OkHttpClient = OkHttpClient()

    lateinit var diceImage: ImageView

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rollButton: Button = findViewById(R.id.roll_button)
        rollButton.text = "Let's Roll"
        rollButton.setOnClickListener {
            if (connected) {
                loadRandomFact()
                rollDice()
                val rotate = AnimationUtils.loadAnimation(this, R.anim.rotate)
                diceImage.startAnimation(rotate)
            } else {
                rollDice()
                val rotate = AnimationUtils.loadAnimation(this, R.anim.rotate)
                diceImage.startAnimation(rotate)
                factTv.visibility = View.VISIBLE
                factTv.text = "You're Offline"
            }
            }
        diceImage = findViewById(R.id.dice_image)

        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        diceImage.startAnimation(fadeIn)

        registerReceiver(ConnectivityReceiver(), IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.three_dot_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here.
        val id = item.itemId

        if (id == R.id.about_me) {
            val intent = Intent(this, AboutActivity::class.java)
            startActivity(intent)
            return true
        }

        if (id == R.id.visit_my_site) {
            val url = "https://kiptechie.live/"
            browse(url)
            return true
        }

        return super.onOptionsItemSelected(item)

    }

    private fun rollDice() {
        val diceImage: ImageView = findViewById(R.id.dice_image)

        val drawableResource = when (Random().nextInt(6) + 1) {
            1 -> R.drawable.dice_1
            2 -> R.drawable.dice_2
            3 -> R.drawable.dice_3
            4 -> R.drawable.dice_4
            5 -> R.drawable.dice_5
            else -> R.drawable.dice_6
        }
        diceImage.setImageResource(drawableResource)
    }

    private fun loadRandomFact() {
        runOnUiThread {
            progressBar.visibility = View.VISIBLE
            factTv.visibility = View.GONE
        }

        val request: Request = Request.Builder().url(URL).build()
        okHttpClient.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call?, e: IOException?) {
                val error = e.toString()
                // build alert dialog
                val dialogBuilder = AlertDialog.Builder(this@MainActivity)
                // set message of alert dialog
                dialogBuilder.setMessage("Error: $error")
                    // if the dialog is cancelable
                    .setCancelable(false)
                    // positive button text and action
                    .setPositiveButton("Ok", DialogInterface.OnClickListener {
                            dialog, id -> finish()
                    })
                // create dialog box
                val alert = dialogBuilder.create()
                // show alert dialog
                alert.show()
            }

            override fun onResponse(call: Call?, response: Response?) {
                val json = response?.body()?.string()
                // we get the joke from the Web Service
                val txt = (JSONObject(json).getJSONObject("value").get("joke")).toString()

                // we update the UI from the UI Thread
                runOnUiThread {
                    progressBar.visibility = View.GONE
                    factTv.visibility = View.VISIBLE
                    // we use Html class to decode html entities
                    factTv.text = Html.fromHtml(txt)
                }
            }
        })

    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        showNetworkMessage(isConnected)
    }

    override fun onResume() {
        super.onResume()
        ConnectivityReceiver.connectivityReceiverListener = this
    }
    private fun showNetworkMessage(isConnected: Boolean) {
        if (!isConnected) {
            connected = false
            snackBar = Snackbar.make(findViewById(R.id.home_ll), "You are offline", Snackbar.LENGTH_LONG) //Assume "home_ll" as the root layout of every activity.
            snackBar?.duration = BaseTransientBottomBar.LENGTH_INDEFINITE
            snackBar?.show()
            factTv.text = "You're Offline"
        } else {
            connected = true
            snackBar?.dismiss()
            factTv.text = "You're online! let's roll"
        }
    }
}
