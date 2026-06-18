package com.agartha.didik.data.repository

import com.agartha.didik.data.local.dao.UlasanDao
import com.agartha.didik.data.local.entity.*
import com.agartha.didik.ui.review.ReviewModel

class ReviewRepository(private val ulasanDao: UlasanDao) {

    suspend fun getAllReviewsWithDetails(): List<ReviewModel> {
        val ulasanLengkap = ulasanDao.getUlasanLengkap()
        
        return ulasanLengkap.map { item ->
            ReviewModel(
                reviewId = item.ulasan.id,
                userId = item.ulasan.userId,
                companyId = item.ulasan.perusahaanId,
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
                pros = item.ulasan.poinKelebihan,
                cons = item.ulasan.poinKekurangan,
                location = item.lokasi
            )
        }
    }

    // Mendapatkan daftar perusahaan
    suspend fun getCompaniesList(): List<PerusahaanEntity> {
        return ulasanDao.getAllCompanies()
    }

    // Mendapatkan profil detail satu perusahaan
    suspend fun getCompanyDetail(companyId: Int): PerusahaanEntity? {
        return ulasanDao.getCompanyById(companyId)
    }

    // Mendapatkan riwayat ulasan untuk perusahaan tertentu
    suspend fun getReviewsForCompany(companyId: Int): List<UlasanEntity> {
        return ulasanDao.getUlasanByCompanyId(companyId)
    }

    // Pencarian & Filter
    suspend fun searchAndFilterCompanies(keyword: String, kategoriId: Int?): List<PerusahaanEntity> {
        return ulasanDao.querySearchFilter(keyword, kategoriId)
    }

    // Menambahkan Ulasan Baru dengan Validasi
    suspend fun createNewReview(ulasan: UlasanEntity) {
        validateReview(ulasan)
        ulasanDao.insertReview(ulasan)
    }

    // Update Ulasan
    suspend fun updateExistingReview(ulasan: UlasanEntity) {
        validateReview(ulasan)
        ulasanDao.updateUlasan(ulasan)
    }

    // Hapus Ulasan
    suspend fun deleteExistingReview(ulasan: UlasanEntity) {
        ulasanDao.deleteUlasan(ulasan)
    }

    // Riwayat ulasan user
    suspend fun getUserReviews(userId: Int): List<UlasanEntity> {
        return ulasanDao.getUlasanByUserId(userId)
    }

    private fun validateReview(ulasan: UlasanEntity) {
        require(ulasan.teksUlasan.isNotBlank()) { "Teks ulasan tidak boleh kosong" }
        require(ulasan.namaPosisi.isNotBlank()) { "Posisi tidak boleh kosong" }
        // Rating 0 diperbolehkan jika user memang tidak ingin memberi bintang, 
        // atau kita bisa default ke 1 di level UI
    }
}
