package com.example.pwd_app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.pwd_app.model.ImageData
import com.example.pwd_app.model.LoginCredentials
import com.example.pwd_app.model.RegisteredSchools
import com.example.pwd_app.model.WorkOrderTimelineModel
import com.example.pwd_app.model.SchoolBuildings
import com.example.pwd_app.model.WorkOrders

@Database(
    entities = [
        LoginCredentials::class,
        RegisteredSchools::class,
        SchoolBuildings::class,
        WorkOrders::class,
        ImageData::class,
        WorkOrderTimelineModel::class
    ],
    version = 2
)
abstract class DatabaseHelper : RoomDatabase() {

    abstract fun Dao(): Dao
    //abstract fun delete(imageData: ImageData)

    companion object {
        private var INSTANCE: DatabaseHelper? = null

        fun getDatabase(context: Context): DatabaseHelper {
            if (INSTANCE == null) {
                synchronized(DatabaseHelper::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        DatabaseHelper::class.java,
                        "DB"
                    ).build()
                }
            }

            return INSTANCE!!
        }
    }
}
