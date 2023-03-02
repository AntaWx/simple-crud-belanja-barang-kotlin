package com.surya_yasa_antariksa.crude_comerse

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.surya_yasa_antariksa.crude_comerse.database.UserDatabase
import java.text.NumberFormat
import java.util.*

class DetailPesananActivity : AppCompatActivity() {

    private lateinit var delete: Button
    private lateinit var save : Button
    private lateinit var database: UserDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_pesanan)

        database = UserDatabase.getInstance(applicationContext)

        delete = findViewById(R.id.delete_button)
        save = findViewById((R.id.save_button))

        val namaPembeli = intent.getStringExtra("nama_pembeli")
        val namaBarang = intent.getStringExtra("nama_barang")
        val jumlahBarang = intent.getStringExtra("jumlah_barang")?.toIntOrNull() ?: 0
        val hargaBarang = intent.getStringExtra("harga_barang")?.toIntOrNull() ?: 0
        val formatedHarga = formatHarga(hargaBarang.toString())
        val tanggalDipesan = intent.getStringExtra("tanggal_dipesan")
        val tanggalUpdate = intent.getStringExtra("tanggal_update")
        val userUid = intent.getStringExtra("userUid")?.toIntOrNull() ?: 0

        val detailNama = findViewById<TextView>(R.id.edit_pemesan)
        val detailNamaPesanan = findViewById<TextView>(R.id.edit_nama)
        val detailBarang = findViewById<TextView>(R.id.edt_jumlah)
        val detailHarga = findViewById<TextView>(R.id.edit_harga)
        val detailTanggal = findViewById<TextView>(R.id.edit_tanggal)
        val detailTanggalUpdate = findViewById<TextView>(R.id.edit_update)
        val uid = database.userDao().getUserById(userUid)

        detailNama.text = namaPembeli
        detailNamaPesanan.text = namaBarang
        detailBarang.text = jumlahBarang.toString()
        detailHarga.text = formatedHarga
        detailTanggal.text = tanggalDipesan
        detailTanggalUpdate.text = tanggalUpdate

        delete.setOnClickListener {
            val user = uid
            AlertDialog.Builder(this)
                .setTitle("Hapus Catatan")
                .setMessage("Apakah Anda yakin ingin menghapus riwayat pembelian ini ini?")
                .setPositiveButton("Ya") { _, _ ->
                    database.userDao().delete(user!!)
                    finish()
                }
                .setNegativeButton("Tidak", null)
                .create()
                .show()
        }

    }

    private fun formatHarga(harga: String): String {
        val formattedHarga = NumberFormat.getNumberInstance(Locale.getDefault()).parse(harga)?.toInt() ?: 0
        return "Rp$formattedHarga"
    }
}