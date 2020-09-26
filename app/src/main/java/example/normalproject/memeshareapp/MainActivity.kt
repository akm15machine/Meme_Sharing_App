package example.normalproject.memeshareapp

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Switch
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var switch:Switch?=null
    private lateinit var saveData: SaveData
    var Url :String? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        saveData= SaveData(this)
        if(saveData.loadDarkModeState()==true){
            setTheme(R.style.DarkTheme)
        }
        else{
            setTheme(R.style.AppTheme)
        }
        super.onCreate(savedInstanceState)
                setContentView(R.layout.activity_main)
        switch = findViewById<View>(R.id.switch1) as Switch?
        if(saveData.loadDarkModeState()==true){
            switch!!.isChecked=true
        }
        switch!!.setOnCheckedChangeListener{_,isChecked->
            if(isChecked){
                saveData.setDarkModeState(true)
                restartApp()
            }
            else{
                saveData.setDarkModeState(false)
                restartApp()
            }
        }

   load()

    }

    private fun restartApp() {
        val i = Intent(applicationContext,MainActivity::class.java)
        startActivity(i)
        finish()
    }

    private fun load() {
        progressbar.visibility=View.VISIBLE
        val queue = Volley.newRequestQueue(this)
         Url = "https://meme-api.herokuapp.com/gimme"

// Request a string response from the provided URL.
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, Url, null ,Response.Listener { response ->
                // Display the first 500 characters of the response string.
            Url = response.getString( "url")
            Glide.with(this).load(Url).listener(object :
                RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    progressbar.visibility=View.GONE
                    return false
                }
                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    progressbar.visibility=View.GONE
                    return false
                }
            }).into(memeImage)
            },
            Response.ErrorListener { Toast.makeText(this,"Something went wrong",Toast.LENGTH_LONG).show() })
// Add the request to the RequestQueue.
        queue.add(jsonObjectRequest)
    }
    fun share(view: View) {
   val intent = Intent(Intent.ACTION_SEND)
        intent.type="text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, "Hey, Checkout this cool meme $Url")
        val chooser=Intent.createChooser(intent, "Share this meme using..")
        startActivity(chooser)
    }
    fun next(view: View) {
    load()
    }
}
