package com.surya_yasa_antariksa.crude_comerse.database.dao

import androidx.room.*
import com.surya_yasa_antariksa.crude_comerse.database.entity.UserEntity

@Dao
interface UserDao {

    @Query("SELECT * from userEntity")
    fun getAll(): List<UserEntity>

    @Insert
    fun insertAll(vararg usersEntity: UserEntity)

    @Update
    fun update(userEntity: UserEntity)

    @Delete
    fun delete(userEntity: UserEntity)

    @Query("SELECT * FROM userEntity WHERE uid = :userId")
    fun getUserById(userId: Int): UserEntity?


    @Query("SELECT * FROM userEntity WHERE namaPesanan LIKE :namaPesanan LIMIT 1")
    fun getUserByNamaPesanan(namaPesanan: String): UserEntity?

}