 package com.padmanavo.bookhub.fragment

import ConnectionManager
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.padmanavo.bookhub.R
import com.padmanavo.bookhub.adapter.DashboardRecyclerAdapter
import com.padmanavo.bookhub.model.Manga
import org.json.JSONException

 class DashboardFragment : Fragment()
{
    private lateinit var recyclerDashboard: RecyclerView
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var recyclerAdapter: DashboardRecyclerAdapter
    lateinit var button:Button
    lateinit var manga: ArrayList<Manga>
    lateinit var progressLayout: RelativeLayout
    lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)
        // Inflate the layout for this fragment
        manga = ArrayList()
        recyclerDashboard = view.findViewById(R.id.recyclerDashboard)
        layoutManager = LinearLayoutManager(activity)
        recyclerAdapter = DashboardRecyclerAdapter(activity as Context, manga)
        val dialog = AlertDialog.Builder(activity as Context)
        progressLayout = view.findViewById(R.id.progressLayout)
        progressBar = view.findViewById(R.id.progressBar)
        progressLayout.visibility = View.VISIBLE


        recyclerDashboard.adapter=recyclerAdapter
        recyclerDashboard.layoutManager=layoutManager
        val queue = Volley.newRequestQueue(activity as Context)
        val url = "http://13.235.250.119/v1/book/fetch_books/"

        if(ConnectionManager().checkConnectivity(activity as Context))
        {
            val jsonObjectRequest = object :JsonObjectRequest(Request.Method.GET, url, null, Response.Listener{


                try
                {
                    progressLayout.visibility = View.GONE
                    val success = it.getBoolean("success")
                    if(success)
                    {
                        val data = it.getJSONArray("data")
                        for(i in 0 until data.length())
                        {
                            val bookJsonObject = data.getJSONObject(i)
                            val bookObject = Manga(
                                bookJsonObject.getString("book_id"),
                                bookJsonObject.getString("name"),
                                bookJsonObject.getString("author"),
                                bookJsonObject.getString("rating"),
                                bookJsonObject.getString("price"),
                                bookJsonObject.getString("image")
                            )
                            manga.add(bookObject)
                            recyclerAdapter = DashboardRecyclerAdapter(activity as Context, manga)
                            recyclerDashboard.adapter=recyclerAdapter
                            recyclerDashboard.layoutManager=layoutManager

                        }
                    }
                    else
                        Toast.makeText(activity as Context, "Some error occurred!!!", Toast.LENGTH_SHORT).show()
                }
                catch (e: JSONException)
                {
                    Toast.makeText(activity as Context, "Some unexpected error occurred", Toast.LENGTH_SHORT).show()
                }
            }, Response.ErrorListener {
                Toast.makeText(activity as Context, "Volley error occurred!!!", Toast.LENGTH_SHORT).show()
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
            queue.add(jsonObjectRequest)
        }
        else
        {
            dialog.setTitle("Failure")
            dialog.setMessage("Internet Connection is Not Found")

            dialog.setPositiveButton("Open Setttings"){text, listener->
                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                activity?.finish()
            }
            dialog.setNegativeButton("Cancel"){text, listener->
                ActivityCompat.finishAffinity(activity as Activity)
            }
            dialog.create().show()
        }
            return view
    }

 }