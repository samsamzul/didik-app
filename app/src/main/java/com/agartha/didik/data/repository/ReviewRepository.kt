package com.agartha.didik.data.repository

import com.agartha.didik.data.local.dao.UlasanDao
import com.agartha.didik.data.local.entity.PerusahaanEntity
import com.agartha.didik.data.local.entity.UlasanEntity

class ReviewRepository(private val ulasanDao: UlasanDao) {

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