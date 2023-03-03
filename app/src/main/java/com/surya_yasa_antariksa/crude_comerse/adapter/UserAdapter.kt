package com.surya_yasa_antariksa.crude_comerse.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.surya_yasa_antariksa.crude_comerse.R
import com.surya_yasa_antariksa.crude_comerse.database.entity.UserEntity
import java.text.NumberFormat
import java.util.*

class UserAdapter(var list: List<UserEntity>, val listener: OnItemClickListener): RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        var namaPembeli : TextView
        var namaBarang : TextView
        var jumlahBarang: TextView
        var hargaBarang: TextView
        var buttonUpdate : ImageButton
        var tanggalDibuat : TextView
        var tanggalDiperbarui : TextView
        var gambarProduk : ImageView
        var buttonView : ImageButton

        init {
            namaPembeli = view.findViewById(R.id.nama_pembeli)
            namaBarang = view.findViewById(R.id.nama_barang)
            jumlahBarang = view.findViewById(R.id.jumlah_barang)
            hargaBarang = view.findViewById(R.id.harga_barang)
            buttonUpdate = view.findViewById(R.id.button_update)
            tanggalDibuat = view.findViewById(R.id.tanggal_dibuat)
            tanggalDiperbarui = view.findViewById(R.id.tanggal_diperbarui)
            gambarProduk = view.findViewById(R.id.gambar_produk)
            buttonView = view.findViewById(R.id.view_button)

            buttonUpdate.setOnClickListener {
                listener.onUpdateClick(adapterPosition)
            }

            buttonView.setOnClickListener {
                listener.onViewClick(adapterPosition)
            }
        }
    }

    interface OnItemClickListener {
        fun onUpdateClick(position: Int)
        fun onViewClick(position: Int)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_user_layout, parent, false)

        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.namaPembeli.text = list[position].namaPembeli.toString()
        holder.namaBarang.text = list[position].namaPesanan
        holder.jumlahBarang.text = list[position].jumlahPesanan.toString()
        holder.hargaBarang.text = formatHarga(list[position].hargaBarang.toString())
        holder.tanggalDibuat.text = list[position].tanggalDibuat.toString()
        holder.tanggalDiperbarui.text = list[position].tanggalUpdate.toString()

        val drawableResId = when (holder.namaBarang.text.toString()) {
            "Canang Sari" -> R.drawable.ini_hai
            "Canang Ceper" -> R.drawable.canang_vertival
            "Canang Sari Gede" -> R.drawable.canang_sari_gede
            else -> R.drawable.ini_hai
        }
        holder.gambarProduk.setImageResource(drawableResId)
    }


    override fun getItemCount(): Int {
        return list.size
    }

    private fun formatHarga(harga: String): String {
        val formattedHarga = NumberFormat.getNumberInstance(Locale.getDefault()).parse(harga)?.toInt() ?: 0
        return "Rp$formattedHarga"
    }
}