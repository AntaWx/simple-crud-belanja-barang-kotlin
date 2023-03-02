package com.surya_yasa_antariksa.crude_comerse

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.view.isNotEmpty
import com.jakewharton.threetenabp.AndroidThreeTen
import com.surya_yasa_antariksa.crude_comerse.database.UserDatabase
import com.surya_yasa_antariksa.crude_comerse.database.entity.UserEntity
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var namaPembeli : EditText
    private lateinit var mySpinner: Spinner
    private lateinit var jumlahBarang: EditText
    private lateinit var hargaBarang: TextView
    private lateinit var buatPesanan: Button
    private lateinit var database: UserDatabase
    private lateinit var riwayatButton: Button
    private lateinit var productImage: ImageView
    private lateinit var datePicker: Button


    var selectedPrice = 0
    var drawableResId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AndroidThreeTen.init(this)

        namaPembeli = findViewById(R.id.edit_pembeli)
        mySpinner = findViewById(R.id.mySpinner)
        jumlahBarang = findViewById(R.id.edit_jumlah)
        hargaBarang = findViewById(R.id.price_view)
        buatPesanan = findViewById(R.id.buat_pesanan)
        database = UserDatabase.getInstance(applicationContext)
        riwayatButton = findViewById(R.id.riwayat_pesanan)
        datePicker = findViewById(R.id.date_picker_button)

        val currentDate = Date()
        val dateFormat = SimpleDateFormat("HH:hh dd MMM yyyy", Locale.getDefault())
        val formattedDate = dateFormat.format(currentDate)

        val items = resources.getStringArray(R.array.mySpinnerItems)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, items)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        productImage = findViewById(R.id.product_image)
        mySpinner.visibility = View.VISIBLE

        mySpinner.adapter = adapter

        mySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedPrice = when (position) {
                    1 -> 7000
                    2 -> 9000
                    3 -> 1000
                    else -> 0
                }
                val jumlah = jumlahBarang.text.toString().toIntOrNull() ?: 1
                val total = selectedPrice * jumlah
                val formatter = DecimalFormat("Rp#,###")
                val formattedPrice = formatter.format(total)
                val priceText = getString(R.string.price_text, formattedPrice)
                hargaBarang.text = priceText

                // Set the appropriate image resource based on the selected item
                drawableResId = when (position) {
                    1 -> R.drawable.ini_hai
                    2 -> R.drawable.canang_sari_gede
                    3 -> R.drawable.canang_vertival
                    else -> R.drawable.ini_hai
                }
                productImage.setImageResource(drawableResId)

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        var selectedDate: Date? = null // variabel untuk menyimpan tanggal yang dipilih

        val pilihTanggal = datePicker.setOnClickListener {
            val calendar = Calendar.getInstance()
            if (selectedDate != null) { // jika ada tanggal yang dipilih sebelumnya, set kalendar ke tanggal tersebut
                calendar.time = selectedDate
            }
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                { _, tahun, tanggal, dayOfMonth ->
                    val selectedCalendar = Calendar.getInstance()
                    selectedCalendar.set(Calendar.YEAR, tahun)
                    selectedCalendar.set(Calendar.MONTH, tanggal)
                    selectedCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    selectedDate = selectedCalendar.time // simpan tanggal yang dipilih dalam variabel
                    val formatTanggal = dateFormat.format(selectedDate!!)
                    datePicker.text = formatTanggal
                    datePicker.setTextColor(ContextCompat.getColor(this, R.color.black))
                },
                year,
                month,
                day
            )

            datePickerDialog.show()
        }

        riwayatButton.setOnClickListener {
            startActivity(Intent(this, RiwayatPesananActivity::class.java))
            finish()
        }

        jumlahBarang.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val jumlah = s.toString().toIntOrNull() ?: 1
                val total = selectedPrice * jumlah
                val formatter = DecimalFormat("Rp#,###")
                val formattedPrice = formatter.format(total)
                val priceText = getString(R.string.price_text, formattedPrice)
                hargaBarang.text = priceText
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        buatPesanan.setOnClickListener {
            val jumlah = jumlahBarang.text.toString().toIntOrNull() ?: 1
            val total = selectedPrice * jumlah
            val pesanan = mySpinner.selectedItem

            when{
                namaPembeli.text.toString().isEmpty() ->
                    Toast.makeText(this, "Nama pembeli harus diisi", Toast.LENGTH_SHORT).show()
            }

            if (pesanan.toString() !in listOf("Canang Sari", "Canang Ceper", "Canang Sari Gede")) {
            Toast.makeText(this, "Mohon pilih canang terlebih dahulu", Toast.LENGTH_SHORT).show()
            return@setOnClickListener
            }

            database.userDao().insertAll(
                UserEntity(
                    null,
                    namaPembeli.text.toString(),
                    total,
                    pesanan.toString()   ,
                    jumlah,
                    formattedDate,
                    selectedDate.toString(),
                    drawableResId
                )
            )
            startActivity(Intent(this, RiwayatPesananActivity::class.java))
        }

    }
}
