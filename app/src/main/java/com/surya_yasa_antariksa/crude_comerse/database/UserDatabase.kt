package com.surya_yasa_antariksa.crude_comerse.database

import android.content.Context
import androidx.room.*
import com.surya_yasa_antariksa.crude_comerse.converter.TimeConverter
import com.surya_yasa_antariksa.crude_comerse.database.dao.UserDao
import com.surya_yasa_antariksa.crude_comerse.database.entity.UserEntity


@Database(
    entities = [UserEntity::class],
    version = 6
)
@TypeConverters(TimeConverter::class)
abstract class UserDatabase: RoomDatabase() {
    abstract fun userDao() : UserDao

    companion object{
        private var instance: UserDatabase? = null

        fun getInstance(context: Context): UserDatabase{
            if(instance == null){
                instance = Room.databaseBuilder(context, UserDatabase::class.java, "user-database")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
            }
            return instance!!
        }
    }
}