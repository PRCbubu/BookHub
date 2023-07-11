package com.padmanavo.bookhub

import ConnectionManager
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.padmanavo.bookhub.database.BookDatabase
import com.padmanavo.bookhub.database.BookEntities
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.lang.Exception

class DescriptionActivity : AppCompatActivity()
{
    lateinit var txtBookName: TextView
    lateinit var txtBookAuthor: TextView
    lateinit var txtBookPrice: TextView
    lateinit var txtBookRating: TextView
    lateinit var txtBookDesc: TextView
    lateinit var imgBookImage: ImageView
    lateinit var btnAddToFav: Button
    lateinit var progressBar: ProgressBar
    lateinit var progressLayout: RelativeLayout
    lateinit var toolbar: Toolbar

    var bookId: String? = "100"

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description)

        txtBookName =  findViewById(R.id.txtBookName)
        txtBookAuthor = findViewById(R.id.txtBookAuthor)
        txtBookPrice = findViewById(R.id.txtBookPrice)
        txtBookRating = findViewById(R.id.txtBookRating)
        txtBookDesc = findViewById(R.id.txtBookDesc)
        imgBookImage = findViewById(R.id.imgBookImage)
        btnAddToFav = findViewById(R.id.btnAddToFav)
        progressBar =  findViewById(R.id.progressBar)
        progressBar.visibility = View.VISIBLE
        progressLayout = findViewById(R.id.progressLayout)
        progressLayout.visibility = View.VISIBLE
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "book Details"

        if(intent != null)
            bookId = intent.getStringExtra("book_id")
        else
        {
            finish()
            Toast.makeText(this@DescriptionActivity, "Some unexpected error occurred if(intent != null)", Toast.LENGTH_SHORT).show()
        }

        if(bookId == "100")
        {
            finish()
            Toast.makeText(this@DescriptionActivity, "Some unexpected error occurred if(bookId == \"100\")", Toast.LENGTH_SHORT).show()
        }

        val queue = Volley.newRequestQueue(this@DescriptionActivity)
        val url = "http://13.235.250.119/v1/book/get_book/"

        val jsonParams = JSONObject()
        jsonParams.put("book_id",bookId)

        if(ConnectionManager().checkConnectivity(this@DescriptionActivity))
        {
            val jsonRequest = object: JsonObjectRequest(Request.Method.POST, url, jsonParams, Response.Listener {
                try
                {
                    val success = it.getBoolean("success")
                    if(success)
                    {
                        val bookJsonObject = it.getJSONObject("book_data")
                        progressLayout.visibility = View.GONE

                        val bookImageUri = bookJsonObject.getString("image")

                        Picasso.get().load(bookJsonObject.getString("image")).error(R.drawable.book).into(imgBookImage)
                        txtBookName.text = bookJsonObject.getString("name")
                        txtBookAuthor.text = bookJsonObject.getString("author")
                        txtBookPrice.text = bookJsonObject.getString("price")
                        txtBookRating.text = bookJsonObject.getString("rating")
                        txtBookDesc.text = bookJsonObject.getString("description")

                        val bookEntities = BookEntities(
                            bookId?.toInt() as Int,
                            txtBookName.text.toString(),
                            txtBookAuthor.text.toString(),
                            txtBookPrice.text.toString(),
                            txtBookRating.text.toString(),
                            txtBookDesc.text.toString(),
                            bookImageUri
                        )

                        val isFav = runBlocking{
                            DBAsyncTask(applicationContext, bookEntities, 1).execute()
                        }

                        if(isFav)
                        {
                            btnAddToFav.text = "Remove from Favourites"
                            val favColour = ContextCompat.getColor(applicationContext, R.color.favouriteColour)
                            btnAddToFav.setBackgroundColor(favColour)
                        }
                        else
                        {
                            btnAddToFav.text = "Add to Favourites"
                            btnAddToFav.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.blue))
                        }

                        btnAddToFav.setOnClickListener {
                            lifecycleScope.launch {
                                if (!DBAsyncTask(applicationContext, bookEntities, 1).execute())
                                {
                                    val result = DBAsyncTask(applicationContext, bookEntities, 2).execute()
                                    if (result)
                                    {
                                        Toast.makeText(this@DescriptionActivity, "Book added to favorites", Toast.LENGTH_SHORT).show()
                                        btnAddToFav.text = "Remove from Favorites"
                                        btnAddToFav.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.favouriteColour))
                                    }
                                    else
                                        Toast.makeText(this@DescriptionActivity, "Some error occurred if (result), if part", Toast.LENGTH_SHORT).show()
                                }
                                else
                                {
                                    val result = DBAsyncTask(applicationContext, bookEntities, 3).execute()

                                    if (result)
                                    {
                                        Toast.makeText(this@DescriptionActivity, "Book removed from the favorites", Toast.LENGTH_SHORT).show()
                                        btnAddToFav.text = "Add to Favorites"
                                        btnAddToFav.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.blue))
                                    }
                                    else
                                        Toast.makeText(this@DescriptionActivity, "Some error occurred if (result), else part", Toast.LENGTH_SHORT).show()

                                }
                            }
                        }
                    }
                    else
                        Toast.makeText(this@DescriptionActivity, "Some error occurred!!! if (result) json request", Toast.LENGTH_SHORT).show()
                }
                catch (e:Exception)
                {
                    Toast.makeText(this@DescriptionActivity, "Some error occurred!!! try catch block", Toast.LENGTH_SHORT).show()
                }
            }, Response.ErrorListener {
                Toast.makeText(this@DescriptionActivity, "Volley error occurred!!!", Toast.LENGTH_SHORT).show()
            })
            {
                override fun getHeaders(): MutableMap<String, String>
                {
                    val headers = HashMap<String, String>()
                    headers["Content-type"] = "application/json"
                    headers["token"] = "4e4f54072e09eb"
                    return headers
                }
            }
            queue.add(jsonRequest)
        }
        else
        {
            val dialog = AlertDialog.Builder(this@DescriptionActivity)
            dialog.setTitle("Failure")
            dialog.setMessage("Internet Connection is Not Found")

            dialog.setPositiveButton("Open Setttings"){text, listener->
                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                finish()
            }
            dialog.setNegativeButton("Cancel"){text, listener->
                ActivityCompat.finishAffinity(this@DescriptionActivity)
            }
            dialog.create().show()
        }
    }

    class DBAsyncTask(val context: Context, private val bookEntities: BookEntities, private val mode: Int) {
        /*
        Mode 1 -> Check DB if the book is favorite or not
        Mode 2 -> Save the book into DB as favorite
        Mode 3 -> Remove the favorite book
         */
        private val db = Room.databaseBuilder(context, BookDatabase::class.java, "books-db").build()

        suspend fun execute(): Boolean = withContext(Dispatchers.IO) {
            when (mode) {
                1 -> {
                    //Check if the book is favorite or not
                    val book: BookEntities? = db.bookDao().getBookById(bookEntities.book_id.toString())
                    db.close()
                    return@withContext book != null
                }

                2 -> {
                    // Save the book into DB as favorite
                    db.bookDao().insertBook(bookEntities)
                    db.close()
                    return@withContext true
                }
                3 -> {
                    // Remove the favorite book
                    db.bookDao().deleteBook(bookEntities)
                    db.close()
                    return@withContext true
                }
            }
            return@withContext false
        }
    }

}