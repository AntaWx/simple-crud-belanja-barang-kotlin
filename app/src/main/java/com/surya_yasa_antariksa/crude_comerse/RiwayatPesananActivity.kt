package com.surya_yasa_antariksa.crude_comerse

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.surya_yasa_antariksa.crude_comerse.adapter.UserAdapter
import com.surya_yasa_antariksa.crude_comerse.database.UserDatabase
import com.surya_yasa_antariksa.crude_comerse.database.entity.UserEntity
import java.text.SimpleDateFormat
import java.util.*

class RiwayatPesananActivity : AppCompatActivity(), UserAdapter.OnItemClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var fab: FloatingActionButton
    private var list = mutableListOf<UserEntity>()
    private lateinit var adapter: UserAdapter
    private lateinit var database: UserDatabase
    private val dateFormat = SimpleDateFormat("HH:mm, dd MMMM yyyy", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_riwayat_pesanan)

        recyclerView = findViewById(R.id.recycler_view)
        fab = findViewById(R.id.fab)
        database = UserDatabase.getInstance(applicationContext)

        adapter = UserAdapter(list, this)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(applicationContext, VERTICAL, false)

        fab.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        getData()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun getData() {
        list.clear()
        list.addAll(database.userDao().getAll())
        adapter.notifyDataSetChanged()
    }

    override fun onUpdateClick(position: Int) {
        val user = list[position]
        val intent = Intent(this, DetailPesananActivity::class.java)
        intent.putExtra("userUid", user.uid.toString())
        intent.putExtra("nama_pembeli", user.namaPembeli)
        intent.putExtra("nama_barang", user.namaPesanan)
        intent.putExtra("jumlah_barang", user.jumlahPesanan.toString())
        intent.putExtra("harga_barang", user.hargaBarang.toString())
        intent.putExtra("tanggal_dipesan", user.tanggalDibuat)
        intent.putExtra("tanggal_update", user.tanggalUpdate)
        startActivity(intent)
    }


}
