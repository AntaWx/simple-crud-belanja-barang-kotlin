package com.surya_yasa_antariksa.crude_comerse

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import com.surya_yasa_antariksa.crude_comerse.adapter.UserAdapter
import com.surya_yasa_antariksa.crude_comerse.database.UserDatabase
import com.surya_yasa_antariksa.crude_comerse.database.entity.UserEntity
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class EditPesananActivity : AppCompatActivity() {

    private lateinit var updateNamaPembeli : EditText
    private lateinit var updateMySpinner: Spinner
    private lateinit var updateJumlahBarangView: EditText
    private var list = mutableListOf<UserEntity>()
    private lateinit var updateHargaBarangView: TextView
    private lateinit var savePesanan: Button
    private lateinit var database: UserDatabase
    private lateinit var updateProductImage: ImageView
    private lateinit var updateDatePicker: Button

    var selectedPrice = 0
    var updateImage = 0
    val currentDate = Date()
    val dateFormat = SimpleDateFormat("HH:hh dd MMM yyyy", Locale.getDefault())
    val dateCompleks =  SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.getDefault())
    val formattedDate = dateFormat.format(currentDate)
    var updateSelectedDate : Date? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_pesanan)

        updateNamaPembeli = findViewById(R.id.update_pembeli)
        updateMySpinner = findViewById(R.id.update_mySpinner)
        updateJumlahBarangView = findViewById(R.id.update_jumlah)
        updateHargaBarangView = findViewById(R.id.update_price_view)
        savePesanan = findViewById(R.id.save_pesanan)
        updateProductImage = findViewById(R.id.update_product_image)
        updateDatePicker = findViewById(R.id.update_date_picker_button)

        database = UserDatabase.getInstance(applicationContext)

        val items = resources.getStringArray(R.array.mySpinnerItems)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val id = intent.getStringExtra("userUid")?.toIntOrNull() ?: -1
        val NamaPembeli = intent.getStringExtra("nama_pembeli").toString()
        val JumlahBarang = intent.getStringExtra("jumlah_barang")
        val namaBarang = intent.getStringExtra("nama_barang")
        val indexsOfNamaBarang  = items.indexOf(namaBarang)
        val tanggalBarangString = intent.getStringExtra("tanggal_update")

        updateMySpinner.visibility = View.VISIBLE

        updateMySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedPrice = when (position) {
                    1 -> 7000
                    2 -> 9000
                    3 -> 1000
                    else -> 0
                }
                val updateJumlah = updateJumlahBarangView.text.toString().toIntOrNull() ?: 1
                val updateTotal = selectedPrice * updateJumlah
                val formatter = DecimalFormat("Rp#,###")
                val formattedPrice = formatter.format(updateTotal)
                val priceText = getString(R.string.price_text, formattedPrice)
                updateHargaBarangView.text = priceText

                //set image when item selected
                updateImage = when (position) {
                    1 -> R.drawable.ini_hai
                    2 -> R.drawable.canang_sari_gede
                    3 -> R.drawable.canang_vertival
                    else -> R.drawable.ini_hai
                }

                updateProductImage.setImageResource(updateImage)

                val actionBar = supportActionBar
                when{
                    actionBar!= null -> {
                        actionBar.setDisplayHomeAsUpEnabled(true)
                        actionBar.title = "Update Pesanan"
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        //set text
        updateNamaPembeli.setText(NamaPembeli)
        updateJumlahBarangView.setText(JumlahBarang)
        updateHargaBarangView.text = selectedPrice.toString()
        updateMySpinner.setSelection(indexsOfNamaBarang)

        var selectedDate: Date? = null // variabel untuk menyimpan tanggal yang dipilih

        val update = updateDatePicker.setOnClickListener {
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
                    updateDatePicker.text = formatTanggal
                    updateDatePicker.setTextColor(ContextCompat.getColor(this, R.color.black))
                },
                year,
                month,
                day
            )

            datePickerDialog.show()
        }

        val newNama = updateNamaPembeli.text.toString()
        val newJumlah = updateJumlahBarangView.text.toString().toIntOrNull() ?: 1
        val newHarga = selectedPrice * newJumlah
        val pesanan = updateMySpinner.selectedItem.toString()

        savePesanan.setOnClickListener {
            val user = UserEntity(
                id,
                newNama,
                newHarga,
                pesanan,
                newJumlah,
                formattedDate,
                selectedDate.toString(),
                updateImage
            )

            // call the updateUser method of the UserDao to update the record with the specified ID
            database.userDao().update(user)

            Toast.makeText(this, "Data berhasil diupdate", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home ->{
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}