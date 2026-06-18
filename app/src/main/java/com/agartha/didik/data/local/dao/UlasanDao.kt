package com.agartha.didik.data.local.dao

import androidx.room.*
import com.agartha.didik.data.local.entity.KategoriEntity
import com.agartha.didik.data.local.entity.PerusahaanEntity
import com.agartha.didik.data.local.entity.UlasanEntity
import com.agartha.didik.data.local.entity.UserEntity

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

    // Helper untuk mengambil data relasi lengkap tanpa mengubah schema
    @Query("""
        SELECT 
            u.*, 
            IFNULL(p.nama_perusahaan, 'Unknown') as nama_perusahaan, 
            IFNULL(p.lokasi, '-') as lokasi,
            IFNULL(k.nama_kategori, 'General') as kategori_nama, 
            IFNULL(us.nama_lengkap, 'Anonymous') as user_nama 
        FROM ulasan u
        LEFT JOIN perusahaan p ON u.perusahaan_id = p.id
        LEFT JOIN users us ON u.user_id = us.id
        LEFT JOIN kategori_perusahaan k ON p.kategori_id = k.id
    """)
    suspend fun getUlasanLengkap(): List<UlasanLengkap>

    // Helper untuk insert data awal (Dummy)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKategori(kategori: List<KategoriEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPerusahaan(perusahaan: List<PerusahaanEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<UserEntity>)
}

data class UlasanLengkap(
    @Embedded val ulasan: UlasanEntity,
    @ColumnInfo(name = "nama_perusahaan") val namaPerusahaan: String,
    @ColumnInfo(name = "lokasi") val lokasi: String,
    @ColumnInfo(name = "kategori_nama") val namaKategori: String,
    @ColumnInfo(name = "user_nama") val namaUser: String
)