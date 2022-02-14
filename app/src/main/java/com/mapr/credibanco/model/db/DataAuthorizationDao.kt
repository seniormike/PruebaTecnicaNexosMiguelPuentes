package com.mapr.credibanco.model.db

import androidx.room.*

@Dao
interface DataAuthorizationDao {
    @Query("SELECT * FROM DataAuthorization")
    fun getAll(): List<DataAuthorization>

    @Query("SELECT * FROM DataAuthorization WHERE receiptId LIKE :receiptId")
    fun findByReceiptId(receiptId: String): DataAuthorization

    @Query("SELECT * FROM DataAuthorization WHERE rrn LIKE :rrn")
    fun findByRrn(rrn: String): DataAuthorization

    @Insert
    fun insertOne(vararg value: DataAuthorization)

    @Delete
    fun delete(value: DataAuthorization)

    @Query("DELETE FROM DataAuthorization")
    fun deleteAll()

    @Update
    fun update(vararg value: DataAuthorization)
}