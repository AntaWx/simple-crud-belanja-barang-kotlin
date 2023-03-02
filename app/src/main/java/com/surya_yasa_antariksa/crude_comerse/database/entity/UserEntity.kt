package com.surya_yasa_antariksa.crude_comerse.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.util.Date

@Entity
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val uid: Int?,

    @ColumnInfo
    val namaPembeli : String?,

    @ColumnInfo
    val hargaBarang : Int?,

    @ColumnInfo
    val namaPesanan : String?,

    @ColumnInfo
    val jumlahPesanan : Int?,

    @ColumnInfo
    val tanggalDibuat : String?,

    @ColumnInfo
    val tanggalUpdate : String?,

    @ColumnInfo
    val imageAssets : Int?
)