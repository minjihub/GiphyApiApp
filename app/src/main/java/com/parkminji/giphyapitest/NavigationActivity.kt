package com.parkminji.giphyapitest

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_navigation.*

class NavigationActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)

        supportFragmentManager.beginTransaction().add(R.id.fragment_view, GifListView()).commit()
        navigation_bar.setOnNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        when(item.itemId){
            R.id.search_gif -> {
                fragmentTransaction.replace(R.id.fragment_view, GifListView()).commitAllowingStateLoss()
                return true
            }
            R.id.favorite_list -> {
                fragmentTransaction.replace(R.id.fragment_view, FavoriteListView()).commitAllowingStateLoss()
                return true
            }
        }
        return false
    }
}