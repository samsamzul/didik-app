package com.agartha.didik.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.agartha.didik.data.local.dao.UserDao
import com.agartha.didik.data.local.dao.UlasanDao
import com.agartha.didik.data.local.entity.KategoriEntity
import com.agartha.didik.data.local.entity.PerusahaanEntity
import com.agartha.didik.data.local.entity.UlasanEntity
import com.agartha.didik.data.local.entity.UserEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [UserEntity::class, KategoriEntity::class, PerusahaanEntity::class, UlasanEntity::class],
    version = 1,
    exportSchema = false
)
abstract class DidikDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun ulasanDao(): UlasanDao

    companion object {
        @Volatile
        private var INSTANCE: DidikDatabase? = null

        fun getDatabase(context: Context): DidikDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DidikDatabase::class.java,
                    "didik_database"
                )
                    .addCallback(DatabaseCallback(context))
                    .fallbackToDestructiveMigration(true)
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(private val context: Context) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        seedDatabase(database.ulasanDao())
                    }
                }
            }

            private suspend fun seedDatabase(ulasanDao: UlasanDao) {
                val categories = listOf(
                    KategoriEntity(id = 1, namaKategori = "Design"),
                    KategoriEntity(id = 2, namaKategori = "Tech"),
                    KategoriEntity(id = 3, namaKategori = "Finance"),
                    KategoriEntity(id = 4, namaKategori = "Healthcare")
                )
                val users = listOf(
                    UserEntity(id = 1, namaLengkap = "Nadilah Nur", email = "nadilah@mail.com", password = "123"),
                    UserEntity(id = 2, namaLengkap = "Farhan Alif", email = "farhan@mail.com", password = "123"),
                    UserEntity(id = 3, namaLengkap = "Budi Santoso", email = "budi@mail.com", password = "123"),
                    UserEntity(id = 4, namaLengkap = "Sarah Wijaya", email = "sarah@mail.com", password = "123"),
                    UserEntity(id = 5, namaLengkap = "Andi Hakim", email = "andi@mail.com", password = "123")
                )
                val companies = listOf(
                    PerusahaanEntity(id = 1, kategoriId = 1, namaPerusahaan = "Google Indonesia", lokasi = "Jakarta", deskripsi = "Perusahaan teknologi global terkemuka."),
                    PerusahaanEntity(id = 2, kategoriId = 2, namaPerusahaan = "Tokopedia", lokasi = "Jakarta", deskripsi = "Platform e-commerce terbesar di Indonesia."),
                    PerusahaanEntity(id = 3, kategoriId = 3, namaPerusahaan = "Bank BCA", lokasi = "Jakarta", deskripsi = "Bank swasta terbesar di Indonesia."),
                    PerusahaanEntity(id = 4, kategoriId = 4, namaPerusahaan = "Kimia Farma", lokasi = "Bandung", deskripsi = "Perusahaan industri farmasi pertama di Indonesia."),
                    PerusahaanEntity(id = 5, kategoriId = 2, namaPerusahaan = "Gojek", lokasi = "Jakarta", deskripsi = "Super-app penyedia layanan on-demand.")
                )
                val reviews = listOf(
                    UlasanEntity(
                        userId = 1, perusahaanId = 1, namaPosisi = "UI/UX Designer Intern",
                        deskripsiKerja = "Membuat wireframe dan UI design system Skilink.",
                        teksUlasan = "Mentornya sangat suportif dan lingkungan kerjanya sangat inklusif.",
                        ratingWorkload = 5, ratingMentorship = 5, ratingCulture = 4,
                        ilmuDidapat = "Figma, Design System, User Research", poinKelebihan = "Networking luas",
                        poinKekurangan = "Target harian cukup ketat", isAnonim = false
                    ),
                    UlasanEntity(
                        userId = 2, perusahaanId = 2, namaPosisi = "Software Engineer Intern",
                        deskripsiKerja = "Maintenance fitur database dan optimasi query.",
                        teksUlasan = "Tech stack modern dan banyak belajar best practice coding.",
                        ratingWorkload = 4, ratingMentorship = 5, ratingCulture = 5,
                        ilmuDidapat = "Go, Redis, PostgreSQL", poinKelebihan = "Free lunch & snack",
                        poinKekurangan = "Persaingan cukup kompetitif", isAnonim = false
                    ),
                    UlasanEntity(
                        userId = 3, perusahaanId = 3, namaPosisi = "Data Analyst Intern",
                        deskripsiKerja = "Melakukan pembersihan data dan membuat dashboard laporan bulanan.",
                        teksUlasan = "Sangat rapi dalam dokumentasi dan belajar banyak soal data perbankan.",
                        ratingWorkload = 4, ratingMentorship = 4, ratingCulture = 5,
                        ilmuDidapat = "SQL, Tableau, Excel", poinKelebihan = "Sistem kerja terstruktur",
                        poinKekurangan = "Proses birokrasi agak lama", isAnonim = false
                    ),
                    UlasanEntity(
                        userId = 4, perusahaanId = 4, namaPosisi = "Quality Control Intern",
                        deskripsiKerja = "Memastikan kualitas produk obat sesuai standar laboratorium.",
                        teksUlasan = "Pengalaman berharga belajar standar keamanan industri farmasi.",
                        ratingWorkload = 3, ratingMentorship = 4, ratingCulture = 4,
                        ilmuDidapat = "ISO Standard, Lab Testing", poinKelebihan = "Fasilitas lengkap",
                        poinKekurangan = "Lokasi pabrik cukup jauh", isAnonim = false
                    ),
                    UlasanEntity(
                        userId = 5, perusahaanId = 5, namaPosisi = "Backend Developer Intern",
                        deskripsiKerja = "Mengembangkan API untuk layanan transportasi logistik.",
                        teksUlasan = "Kecepatan kerja sangat tinggi, cocok buat yang suka tantangan.",
                        ratingWorkload = 5, ratingMentorship = 4, ratingCulture = 5,
                        ilmuDidapat = "Microservices, Kafka, Java", poinKelebihan = "Budaya kerja santai tapi produktif",
                        poinKekurangan = "Work-life balance menantang", isAnonim = true
                    )
                )

                ulasanDao.insertKategori(categories)
                ulasanDao.insertUsers(users)
                ulasanDao.insertPerusahaan(companies)
                reviews.forEach { ulasanDao.insertReview(it) }
            }
        }
    }
}
