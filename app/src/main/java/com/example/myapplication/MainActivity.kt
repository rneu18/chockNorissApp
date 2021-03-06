package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.*
import android.R.attr.fragment
import android.content.Intent
import android.view.View
import com.github.ybq.android.spinkit.SpinKitView
import retrofit2.HttpException as Retrofit2HttpException


class MainActivity : AppCompatActivity() {

    lateinit var myJokes: Button
    lateinit var myText: Button
    lateinit var contText: Button
    lateinit var progress: SpinKitView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        myJokes = findViewById(R.id.r_joke)
        myText = findViewById(R.id.input_text)
        contText = findViewById(R.id.nEndingList)
        progress = findViewById(R.id.spin_kit)

        myJokes.setOnClickListener{
            initilizedRetrofit()
        }
        myText.setOnClickListener{
            inputText()
        }
        contText.setOnClickListener{
            getNeverEnText()
        }
    }

    private fun getNeverEnText() {
        val intent = Intent(this, JokesList::class.java)

        startActivity(intent)

    }

    fun initilizedRetrofit() {


        progress.visibility = View.VISIBLE
        val service = JokesApiService.RetrofitFactory.makeRetrofitService()
        CoroutineScope(Dispatchers.IO).launch {

            val request = service.getJokes2()
            withContext(Dispatchers.Main) {
                try {
                    val response = request.await()

                    if (response.isSuccessful) {

                    val myRandomJoke:String =  response.body()?.value?.get(0)?.joke.toString()
                        initilizeDialog(myRandomJoke)
                        progress.visibility = View.GONE

                    } else {
                      //  Toast.makeText(this@MainActivity, "Not connected!", Toast.LENGTH_SHORT).show()

                    }
                } catch (e: Retrofit2HttpException) {
                  //  Toast.makeText(this@MainActivity, "Not connected3!", Toast.LENGTH_SHORT).show()

                } catch (e: Throwable) {
                 //   Toast.makeText(this@MainActivity, "Not connected4!", Toast.LENGTH_SHORT).show()

                }
            }
        }
    }

    private fun initilizeDialog(myRandomJoke: String) {


        val mDialogView = LayoutInflater.from(this).inflate(R.layout.chuck_norris_diaolg, null)

        var myJoke:TextView = mDialogView.findViewById(R.id.my_joke)
        var dismissButton:Button = mDialogView.findViewById(R.id.dismissBtn)
        myJoke.text = myRandomJoke

        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)

        //show dialog
        val  mAlertDialog = mBuilder.show()
        dismissButton.setOnClickListener{
           mAlertDialog.dismiss()
        }
    }


    private fun inputText() {

        val intent = Intent(this, InputName::class.java)

        startActivity(intent)


    }


}
