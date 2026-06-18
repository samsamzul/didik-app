package com.agartha.didik.data.repository

import com.agartha.didik.data.local.dao.UlasanDao
import com.agartha.didik.data.local.entity.*
import com.agartha.didik.ui.review.ReviewModel

class ReviewRepository(private val ulasanDao: UlasanDao) {

    suspend fun getAllReviewsWithDetails(): List<ReviewModel> {
        val ulasanLengkap = ulasanDao.getUlasanLengkap()
        
        // Jika database masih kosong, kita isi dengan data dummy pertama kali
        if (ulasanLengkap.isEmpty()) {
            seedDummyData()
            return getAllReviewsWithDetails() // Rekursif sekali untuk ambil data yang baru di-insert
        }

        return ulasanLengkap.map { item ->
            ReviewModel(
                companyName = item.namaPerusahaan,
                position = item.ulasan.namaPosisi,
                category = item.namaKategori,
                jobDesc = item.ulasan.deskripsiKerja,
                reviewText = item.ulasan.teksUlasan,
                rating = (item.ulasan.ratingWorkload + item.ulasan.ratingMentorship + item.ulasan.ratingCulture) / 3.0f,
                reviewerName = item.namaUser,
                ratingWorkload = item.ulasan.ratingWorkload,
                ratingMentorship = item.ulasan.ratingMentorship,
                ratingCulture = item.ulasan.ratingCulture,
                location = item.lokasi
            )
        }
    }

    private suspend fun seedDummyData() {
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

    // Menyambungkan fungsi Menampilkan Daftar Industri (2.4.3)
    suspend fun getCompaniesList(): List<PerusahaanEntity> {
        return ulasanDao.getAllCompanies()
    }

    // Mendapatkan profil detail satu perusahaan
    suspend fun getCompanyDetail(companyId: Int): PerusahaanEntity? {
        return ulasanDao.getCompanyById(companyId)
    }

    // Mendapatkan riwayat ulasan dari para alumni magang di perusahaan tersebut
    suspend fun getReviewsForCompany(companyId: Int): List<UlasanEntity> {
        return ulasanDao.getUlasanByCompanyId(companyId)
    }

    // Menyambungkan fungsi Pencarian & Filter (2.4.4)
    suspend fun searchAndFilterCompanies(keyword: String, kategoriId: Int?): List<PerusahaanEntity> {
        return ulasanDao.querySearchFilter(keyword, kategoriId)
    }

    // Menyambungkan fungsi Menambahkan Ulasan Baru (2.4.5)
    suspend fun createNewReview(ulasan: UlasanEntity) {
        ulasanDao.insertReview(ulasan)
    }

    // Menyambungkan fungsi Edit Ulasan (2.4.6 - Jalur Update)
    suspend fun updateExistingReview(ulasan: UlasanEntity) {
        ulasanDao.updateUlasan(ulasan)
    }

    // Menyambungkan fungsi Hapus Ulasan (2.4.6 - Jalur Delete)
    suspend fun deleteExistingReview(ulasan: UlasanEntity) {
        ulasanDao.deleteUlasan(ulasan)
    }

    // Mengambil daftar ulasan pribadi milik user tertentu untuk halaman riwayat
    suspend fun getUserReviews(userId: Int): List<UlasanEntity> {
        return ulasanDao.getUlasanByUserId(userId)
    }
}