package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.github.ybq.android.spinkit.SpinKitView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class InputName : AppCompatActivity() {

    lateinit var userInputName:TextView
    lateinit var submitbtn:Button
    private lateinit var backtoMenu:ImageView
    lateinit var alertTxt: TextView
    lateinit var progress: SpinKitView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_name)


        submitbtn = findViewById(R.id.submitName)
        backtoMenu = findViewById(R.id.backToMenu)
        progress = findViewById(R.id.spin_kit)

        backtoMenu.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }


        submitbtn.setOnClickListener{

            userInputName = findViewById(R.id.myName)
            alertTxt = findViewById(R.id.alertText)
            var userName:String = userInputName.text.toString()
            if ((userName.length) > 0){

                try{
                    var firstName: String = userName.split(" ")[0]
                    var lastName: String = userName.split(" ")[1]
                    alertTxt.text =""
                    userInputName.text = ""
                    initilizedRetrofit(firstName, lastName)
                }catch (e:Exception){
                    //Toast.makeText(this@InputName, "Provide First and Last Name", Toast.LENGTH_LONG).show()
                    alertTxt.text = "Provide First and Last Name"

                }

            }else{
               // Toast.makeText(this@InputName, "Provide First and Last Name", Toast.LENGTH_LONG).show()
                alertTxt.text = "Provide First and Last Name"

            }

        }
    }


    fun initilizedRetrofit(firstName: String = "", lastName: String ="") {

        progress.visibility = View.VISIBLE
        val service = JokesApiService.RetrofitFactory.makeRetrofitService()
        CoroutineScope(Dispatchers.IO).launch {

            val request = service.getJokes(firstName, lastName)
            withContext(Dispatchers.Main) {
                try {
                    val response = request.await()

                    if (response.isSuccessful) {
                        val myRandomJoke:String =  response.body()?.value?.get(0)?.joke.toString()
                        initilizeDialog(myRandomJoke)
                        progress.visibility = View.GONE

                    } else {
                       // Toast.makeText(this@InputName, "Not connected!", Toast.LENGTH_SHORT).show()

                    }
                } catch (e: HttpException) {
                    Toast.makeText(this@InputName, "Not connected3!", Toast.LENGTH_SHORT).show()

                } catch (e: Throwable) {
                    Toast.makeText(this@InputName, "Not connected4!", Toast.LENGTH_SHORT).show()

                }
            }
        }
    }

    private fun initilizeDialog(myRandomJoke: String) {


        val mDialogView = LayoutInflater.from(this).inflate(R.layout.chuck_norris_diaolg, null)

        var myJoke: TextView = mDialogView.findViewById(R.id.my_joke)
        var dissmissButton: Button = mDialogView.findViewById(R.id.dismissBtn)
        myJoke.text = myRandomJoke

        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)

        //show dialog
        val  mAlertDialog = mBuilder.show()
        dissmissButton.setOnClickListener{
            mAlertDialog.dismiss()
        }
    }
}
