package com.padmanavo.bookhub.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.padmanavo.bookhub.R
import com.padmanavo.bookhub.adapter.FavouritesRecyclerAdapter
import com.padmanavo.bookhub.database.BookDatabase
import com.padmanavo.bookhub.database.BookEntities
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavouritesFragment : Fragment()
{
    lateinit var recyclerFavourite: RecyclerView
    lateinit var progressLayout: RelativeLayout
    lateinit var progressBar: ProgressBar
    lateinit var layoutManager:RecyclerView.LayoutManager
    lateinit var recyclerAdapter: FavouritesRecyclerAdapter
    var dbBookList = listOf<BookEntities>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_favourite, container, false)

        recyclerFavourite = view.findViewById(R.id.recyclerFavourite)
        progressLayout = view.findViewById(R.id.progressLayout)
        progressBar = view.findViewById(R.id.progressBar)

        layoutManager = GridLayoutManager(activity as Context, 2)

        lifecycleScope.launch {
            dbBookList = RetrieveFavourites(activity as Context).execute()
            if(activity!=null)
            {
                progressLayout.visibility = View.GONE
                recyclerAdapter = FavouritesRecyclerAdapter(activity as Context, dbBookList)
                recyclerFavourite.adapter = recyclerAdapter
                recyclerFavourite.layoutManager = layoutManager
            }
        }
        return view
    }

    class RetrieveFavourites(val context: Context)
    {
//        private val db = Room.databaseBuilder(context, BookDatabase::class.java, "book-db").build()
        suspend fun execute(): List<BookEntities> = withContext(Dispatchers.IO)
        {
            val db = Room.databaseBuilder(context, BookDatabase::class.java, "book-db").build()
            return@withContext db.bookDao().getAllBooks()
        }
    }
}