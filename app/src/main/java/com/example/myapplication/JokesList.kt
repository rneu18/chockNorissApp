package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.DataBindingUtil.setContentView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_jokes_list.*
import retrofit2.HttpException
import java.util.*
import android.widget.AbsListView
import kotlinx.coroutines.*


class JokesList : AppCompatActivity() {

     var myJokeList:MutableList<String> = mutableListOf()
    lateinit var myRecyclerView:RecyclerView
    lateinit var linearLayoutManager:LinearLayoutManager
    private lateinit var backToMenu: ImageView

    var isScrolling:Boolean = false
    var currentItems: Int = 0
    var scrollOutItems: Int = 0
    var totalItems: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jokes_list)

        backToMenu = findViewById(R.id.backToMenu)

        backToMenu.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        initilizedRetrofit()

        myRecyclerView = findViewById(R.id.recyclerView)
      //  recyclerView.setHasFixedSize(true)
        val linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = MyAdapter(myJokeList, this@JokesList)
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                currentItems = linearLayoutManager.childCount
                totalItems = linearLayoutManager.itemCount
                scrollOutItems = linearLayoutManager.findFirstVisibleItemPosition()

                if (isScrolling && currentItems + scrollOutItems === totalItems) {
                    isScrolling = false
                    initilizedRetrofit()
                }
            }
        })


    }

    fun initilizedRetrofit() {


        val service = JokesApiService.RetrofitFactory.makeRetrofitService()
        CoroutineScope(Dispatchers.IO).launch {

            val request = service.getJokes2(/*"", ""*/)
            withContext(Dispatchers.Main) {
                try {
                    val response = request.await()

                    if (response.isSuccessful) {
                        var total: Int? = response.body()?.value?.size?.minus(1)

                        if (total != null) for (x in 0..total){
                            val myRandomJoke:String =  response.body()?.value?.get(x)?.joke.toString()
                            myJokeList.add(myRandomJoke)
                           // println("11111111111111111111111111111 $myJokeList")
                        }
                      //  recyclerView.adapter = MyAdapter(myJokeList, this@JokesList)
                        (recyclerView.adapter as MyAdapter).notifyDataSetChanged()
                       // MyAdapter.notifyDataSetChanged()

                    } else {
                        //  Toast.makeText(this@MainActivity, "Not connected!", Toast.LENGTH_SHORT).show()

                    }
                } catch (e: HttpException) {
//                      Toast.makeText(JokesList@this, "Not connected3!", Toast.LENGTH_SHORT).show()

                } catch (e: Throwable) {
                    //   Toast.makeText(this@MainActivity, "Not connected4!", Toast.LENGTH_SHORT).show()

                }
            }
        }
    }



}
