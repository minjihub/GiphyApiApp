package com.parkminji.giphyapitest.db

import android.content.Context
import androidx.room.RoomDatabase
import androidx.room.Database
import androidx.room.Room

@Database(entities = [GifEntity::class], version = 1)
abstract class GifsDB : RoomDatabase(){

    abstract fun gifsDao(): GifsDao

    companion object{
        private var INSTANCE: GifsDB? = null

        fun getInstance(context: Context): GifsDB?{
            synchronized(GifsDB::class){
                INSTANCE = Room.databaseBuilder(context,
                    GifsDB::class.java, "gifs.db")
                    .build()
            }
            return INSTANCE
        }

        fun destroyInstance(){
            INSTANCE = null
        }

    }
}