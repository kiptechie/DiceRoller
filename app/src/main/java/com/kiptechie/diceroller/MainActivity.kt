package com.kiptechie.diceroller

import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.kiptechie.diceroller.api.ApiInterface
import com.kiptechie.diceroller.api.ApiUtils
import com.kiptechie.diceroller.api.response.Categories
import com.kiptechie.diceroller.connectivity_util.ConnectivityReceiver
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.browse
import retrofit2.Call
import retrofit2.Response
import java.util.*


class MainActivity : AppCompatActivity(), ConnectivityReceiver.ConnectivityReceiverListener {

    private var snackBar: Snackbar? = null
    private var connected: Boolean = false
    private var myApi: ApiInterface? = null

    lateinit var diceImage: ImageView

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        myApi = ApiUtils.getAPIService()

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
            val intent = Intent(applicationContext, AboutActivity::class.java)
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

        myApi?.randomJokes?.enqueue(object: retrofit2.Callback<Categories> {
            override fun onFailure(call: Call<Categories>, t: Throwable) {
                val error = t.toString()
                snackBar = Snackbar.make(findViewById(R.id.home_ll), error, Snackbar.LENGTH_LONG) //Assume "home_ll" as the root layout of every activity.
                snackBar?.duration = BaseTransientBottomBar.LENGTH_LONG
                snackBar?.show()
            }

            override fun onResponse(call: Call<Categories>, response: Response<Categories>) {

                val joke = response.body()?.value.toString()
                val bad = response.body()?.categories
                runOnUiThread {
                    progressBar.visibility = View.GONE
                    factTv.visibility = View.VISIBLE
                    factTv.text = joke
                    // to do add the 18+ icon
//                    try {
//                        if ( bad?.get(0).toString() == "explicit") {
//                            explicit_logo.visibility = View.VISIBLE
//                        } else {
//                            explicit_logo.visibility = View.GONE
//                        }
//                    } catch (t: Throwable) {
//                        t.printStackTrace()
//                    }
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
    @SuppressLint("SetTextI18n")
    private fun showNetworkMessage(isConnected: Boolean) {
        if (!isConnected) {
            roll_button.isEnabled = false
            connected = false
            snackBar = Snackbar.make(findViewById(R.id.home_ll), "You are offline", Snackbar.LENGTH_LONG) //Assume "home_ll" as the root layout of every activity.
            snackBar?.duration = BaseTransientBottomBar.LENGTH_INDEFINITE
            snackBar?.show()
            factTv.text = "You're Offline"
        } else {
            roll_button.isEnabled = true
            connected = true
            snackBar?.dismiss()
            factTv.text = "You're online! let's roll"
        }
    }
}
