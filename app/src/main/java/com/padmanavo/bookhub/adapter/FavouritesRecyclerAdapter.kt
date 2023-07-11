package com.padmanavo.bookhub.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.padmanavo.bookhub.R
import com.padmanavo.bookhub.database.BookEntities
import com.squareup.picasso.Picasso

class FavouritesRecyclerAdapter(val context: Context, private val bookList:List<BookEntities>): RecyclerView.Adapter<FavouritesRecyclerAdapter.FavouriteViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouritesRecyclerAdapter.FavouriteViewHolder
    {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.reycycler_favourite_single_row, parent, false)
        return FavouriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavouritesRecyclerAdapter.FavouriteViewHolder, position: Int)
    {
        val book = bookList[position]
        holder.txtBookName.text = book.bookName
        holder.txtBookAuthor.text = book.bookAuthor
        holder.txtBookPrice.text = book.bookPrice
        holder.txtBookRating.text = book.bookRatings
        Picasso.get().load(book.bookImage).error(R.drawable.book).into(holder.imgBookImage)
    }

    override fun getItemCount(): Int
    {
        return  bookList.size
    }

    class FavouriteViewHolder(view: View):RecyclerView.ViewHolder(view)
    {
        val txtBookName: TextView = view.findViewById(R.id.txtFavBookTitle)
        val txtBookAuthor: TextView = view.findViewById(R.id.txtFavBookAuthor)
        val txtBookPrice: TextView = view.findViewById(R.id.txtFavBookPrice)
        val txtBookRating: TextView = view.findViewById(R.id.txtFavBookRating)
        val imgBookImage: ImageView = view.findViewById(R.id.imgBookImage)
        val llContent: LinearLayout = view.findViewById(R.id.llFavContent)
    }

}