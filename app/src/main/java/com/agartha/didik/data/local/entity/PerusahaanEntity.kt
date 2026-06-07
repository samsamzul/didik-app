package com.agartha.didik.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "perusahaan",
    foreignKeys = [
        ForeignKey(
            entity = KategoriEntity::class,
            parentColumns = ["id"],
            childColumns = ["kategori_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        androidx.room.Index(value = ["kategori_id"])
    ]
)
data class PerusahaanEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "kategori_id")
    val kategoriId: Int,

    @ColumnInfo(name = "nama_perusahaan")
    val namaPerusahaan: String,

    @ColumnInfo(name = "lokasi")
    val lokasi: String,

    @ColumnInfo(name = "deskripsi")
    val deskripsi: String
)