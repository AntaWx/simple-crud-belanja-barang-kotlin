package com.surya_yasa_antariksa.crude_comerse

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
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
    private lateinit var searchButton: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_riwayat_pesanan)

        recyclerView = findViewById(R.id.recycler_view)
        fab = findViewById(R.id.fab)
        searchButton = findViewById(R.id.cari_data)
        database = UserDatabase.getInstance(applicationContext)

        adapter = UserAdapter(list, this)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(applicationContext, VERTICAL, false)

        fab.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        searchButton.setOnClickListener {
            val search = searchButton.text.toString().trim()
            searchData(search)
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

    override fun onViewClick(position: Int) {
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

    override fun onUpdateClick(position: Int) {
        val user = list[position]
        val intent = Intent(this, EditPesananActivity::class.java)
        intent.putExtra("userUid", user.uid.toString())
        intent.putExtra("nama_pembeli", user.namaPembeli)
        intent.putExtra("nama_barang", user.namaPesanan)
        intent.putExtra("jumlah_barang", user.jumlahPesanan.toString())
        intent.putExtra("harga_barang", user.hargaBarang.toString())
        intent.putExtra("tanggal_dipesan", user.tanggalDibuat)
        intent.putExtra("tanggal_update", user.tanggalUpdate)
        startActivity(intent)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun searchData(data: String){
        val findCustomer = database.userDao().getAll().filter { it.namaPembeli!!.contains(data, true) }
        val findBarang = database.userDao().getAll().filter { it.namaPesanan!!.contains(data, true) }

        list.clear()
        list.addAll(findCustomer)
        list.addAll(findBarang)
        if(findCustomer.isEmpty() && findBarang.isEmpty()){
            Toast.makeText(this, "Data tidak ditemukan", Toast.LENGTH_SHORT).show()
        }
        adapter.notifyDataSetChanged()

        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(searchButton.windowToken, 0)
        // menghilangkan fokus dari searchEditText
        searchButton.clearFocus()
    }

    fun convertDate(date: String){
        val date = database.userDao().getAll().filter { it.tanggalUpdate!!.contains(date, true) }

    }
}
