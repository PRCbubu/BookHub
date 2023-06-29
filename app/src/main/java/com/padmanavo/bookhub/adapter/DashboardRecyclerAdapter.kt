package com.padmanavo.bookhub.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

import androidx.recyclerview.widget.RecyclerView
import com.padmanavo.bookhub.R
import com.padmanavo.bookhub.model.Manga
import com.squareup.picasso.Picasso

class DashboardRecyclerAdapter(private val context: Context, private val manga: ArrayList<Manga>) : RecyclerView.Adapter<DashboardRecyclerAdapter.DashboardViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardRecyclerAdapter.DashboardViewHolder
    {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_dashboard_single_row, parent, false)
        return DashboardViewHolder(view)
    }
    override fun onBindViewHolder(holder: DashboardRecyclerAdapter.DashboardViewHolder, position: Int)
    {
        val manga = manga[position]
        Picasso.get().load(manga.bookImage).error(R.drawable.book).into(holder.imgPreview)
        holder.txtRecyclerRowItem.text = manga.bookName
        holder.txtRecyclerRowItemAuthorName.text = manga.bookAuthor
        holder.txtRecyclerRowItemPrice.text = manga.bookPrice
        holder.txtRecyclerRowItemRating.text = manga.bookRatings

        holder.llContent.setOnClickListener{
            Toast.makeText(context, "Clicked on ${holder.txtRecyclerRowItem.text}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int
    {
        return manga.size
    }

    class DashboardViewHolder(view: View): RecyclerView.ViewHolder(view)
    {
        val imgPreview: ImageView = view.findViewById(R.id.imgPreview)
        val txtRecyclerRowItem: TextView = view.findViewById(R.id.txtRecyclerRowItem)
        val txtRecyclerRowItemRating: TextView = view.findViewById(R.id.txtRecyclerRowItemRating)
        val txtRecyclerRowItemAuthorName: TextView = view.findViewById(R.id.txtRecyclerRowItemAuthorName)
        val txtRecyclerRowItemPrice: TextView = view.findViewById(R.id.txtRecyclerRowItemPrice)
        val llContent: LinearLayout = view.findViewById(R.id.llcontent)
    }


}