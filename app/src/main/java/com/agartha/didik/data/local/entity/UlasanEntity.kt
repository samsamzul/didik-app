package com.agartha.didik.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "ulasan",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = PerusahaanEntity::class,
            parentColumns = ["id"],
            childColumns = ["perusahaan_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        androidx.room.Index(value = ["user_id"]),
        androidx.room.Index(value = ["perusahaan_id"])
    ]
)
data class UlasanEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "user_id")
    val userId: Int,

    @ColumnInfo(name = "perusahaan_id")
    val perusahaanId: Int,

    @ColumnInfo(name = "nama_posisi")
    val namaPosisi: String,

    @ColumnInfo(name = "deskripsi_kerja")
    val deskripsiKerja: String,

    @ColumnInfo(name = "rating_workload")
    val ratingWorkload: Int,

    @ColumnInfo(name = "rating_mentorship")
    val ratingMentorship: Int,

    @ColumnInfo(name = "rating_culture")
    val ratingCulture: Int,

    @ColumnInfo(name = "teks_ulasan")
    val teksUlasan: String,

    @ColumnInfo(name = "ilmu_didapat")
    val ilmuDidapat: String,

    @ColumnInfo(name = "poin_kelebihan")
    val poinKelebihan: String,

    @ColumnInfo(name = "poin_kekurangan")
    val poinKekurangan: String,

    @ColumnInfo(name = "is_anonim")
    val isAnonim: Boolean,

    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
)