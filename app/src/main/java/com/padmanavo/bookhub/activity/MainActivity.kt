package com.padmanavo.bookhub.activity

import android.app.Activity
import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar

import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.padmanavo.bookhub.R
import com.padmanavo.bookhub.fragment.AboutAppFragment
import com.padmanavo.bookhub.fragment.DashboardFragment
import com.padmanavo.bookhub.fragment.FavouritesFragment
import com.padmanavo.bookhub.fragment.ProfilesFragment

class MainActivity : AppCompatActivity()
{
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var coordinatorLayout: CoordinatorLayout
    private lateinit var toolbar: Toolbar
    private lateinit var frameLayout: FrameLayout
    private lateinit var navigationView: NavigationView
    private lateinit var onBackPressedCallback: OnBackPressedCallback

    private var previousMenuItem:MenuItem? = null


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout = findViewById(R.id.drawerLayout)
        coordinatorLayout = findViewById(R.id.coordinatorLayout)
        toolbar = findViewById(R.id.toolbar)
        frameLayout = findViewById(R.id.frameLayout)
        navigationView = findViewById(R.id.navigationView)

        setUpToolbar()

        openDashboard(DashboardFragment(), "Dashboard")

        val actionBarDrawerToggle = ActionBarDrawerToggle(this@MainActivity, drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer
        )

        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        navigationView.setNavigationItemSelectedListener {
            if(previousMenuItem!=null)
                previousMenuItem?.isChecked = false
            it.isCheckable = true
            it.isChecked = true
            previousMenuItem = it

            when(it.itemId)
            {
                R.id.dashboard ->{
                    openDashboard(DashboardFragment(), "Dashboard")
                    drawerLayout.closeDrawer(navigationView)
                    true}

                R.id.favourites ->{
                    openDashboard(FavouritesFragment(), "Favourites")
                    drawerLayout.closeDrawer(navigationView)
                    true}

                R.id.profile ->{
                    openDashboard(ProfilesFragment(), "Profile")
                    drawerLayout.closeDrawer(navigationView)
                    true}

                R.id.aboutApp ->{
                    openDashboard(AboutAppFragment(), "aboutApp")
                    drawerLayout.closeDrawer(navigationView)
                    true}

                else->false
            }
        }

        onBackPressedCallback = object:OnBackPressedCallback(true)
        {
            var backPressedTime: Long = 0
            override fun handleOnBackPressed()
            {
                if(supportFragmentManager.backStackEntryCount>0)
                    supportFragmentManager.popBackStack()
                else
                {
                    when(supportFragmentManager.findFragmentById(R.id.frameLayout))
                    {
                        !is DashboardFragment -> openDashboard(DashboardFragment(), "Dashboard")
                        else -> {
                            // Show a dialog asking the user if they want to exit the app
                            AlertDialog.Builder(this@MainActivity)
                                .setTitle("Exit?")
                                .setMessage("Are you sure you want to exit?")
                                .setPositiveButton("Yes") { _, _ -> ActivityCompat.finishAffinity(this@MainActivity) }
                                .setNegativeButton("No", null)
                                .show()
                            // Check if the back button was pressed twice within 2 seconds
//                            if (backPressedTime + 2000 > System.currentTimeMillis())
//                            {
//                                Toast.makeText(this@MainActivity, "inside if", Toast.LENGTH_SHORT).show()
//                                finish()
//                            }
//                            else
//                            {
//                                Toast.makeText(this@MainActivity, "Press back again to exit", Toast.LENGTH_SHORT).show()
//                                backPressedTime = System.currentTimeMillis()
//                                Log.d("MainActivity", "backPressedTime: $backPressedTime")
//                            }
                        }
                    }
                }
            }
        }
        //onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    private fun openDashboard(frag:Fragment, text:String)
    {
        supportFragmentManager.beginTransaction().replace(R.id.frameLayout, frag).addToBackStack(text).commit()
        supportActionBar?.title = text
        navigationView.setCheckedItem(R.id.dashboard)
    }

    private fun setUpToolbar()
    {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Toolbar Title"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        val id = item.itemId
        if(id==android.R.id.home)
            drawerLayout.openDrawer(GravityCompat.START)
        return super.onOptionsItemSelected(item)
    }
}