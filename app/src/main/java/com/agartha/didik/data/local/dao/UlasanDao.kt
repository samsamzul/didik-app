package com.agartha.didik.data.local.dao

import androidx.room.*
import com.agartha.didik.data.local.entity.PerusahaanEntity
import com.agartha.didik.data.local.entity.UlasanEntity

@Dao
interface UlasanDao {
    // 2.4.3 Melihat Daftar Ulasan & Profil Perusahaan
    @Query("SELECT * FROM perusahaan")
    suspend fun getAllCompanies(): List<PerusahaanEntity>

    @Query("SELECT * FROM perusahaan WHERE id = :companyId")
    suspend fun getCompanyById(companyId: Int): PerusahaanEntity?

    @Query("SELECT * FROM ulasan WHERE perusahaan_id = :companyId")
    suspend fun getUlasanByCompanyId(companyId: Int): List<UlasanEntity>

    // 2.4.4 Mencari dan Memfilter Ulasan Tempat Magang
    @Query("""
        SELECT * FROM perusahaan 
        WHERE (nama_perusahaan LIKE :query OR lokasi LIKE :query) 
        AND (:kategoriId IS NULL OR kategori_id = :kategoriId)
    """)
    suspend fun querySearchFilter(query: String, kategoriId: Int?): List<PerusahaanEntity>

    // 2.4.5 Menambahkan Ulasan Baru
    @Insert
    suspend fun insertReview(ulasan: UlasanEntity)

    // 2.4.6 Mengelola Ulasan Pribadi (Jalur Edit)
    @Update
    suspend fun updateUlasan(ulasan: UlasanEntity)

    // 2.4.6 Mengelola Ulasan Pribadi (Jalur Hapus)
    @Delete
    suspend fun deleteUlasan(ulasan: UlasanEntity)

    // Ambil daftar riwayat ulasan milik satu mahasiswa tertentu
    @Query("SELECT * FROM ulasan WHERE user_id = :userId")
    suspend fun getUlasanByUserId(userId: Int): List<UlasanEntity>
}