package com.lottery.lotteryallinone.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.lottery.lotteryallinone.model.SixNumber
import com.lottery.lotteryallinone.model.ThreeNumbers
import com.pick3andpick4pred.suvarnatechlabs.model.FourNumbers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


private const val DB_NAME = "lottery"
private const val TABLE_POWERBALL = "powerBall"
private const val TABLE_USERSELECTION = "userSelection"
private const val DB_VERSION = 1

private const val SRNO = "SNo"
private const val FIRST_NO = "N1"
private const val SECOND_NO = "N2"
private const val THIRD_NO = "N3"
private const val FOURTH_NO = "N4"
private const val FIFTH_NO = "N5"
private const val SIXTH_NO = "N6"
private const val SEVENTH_NO = "N7"

private const val NO_OF_COL = "No_Of_Col"
private const val MAX_NO_OF_FIRST_COL = "Max_No_Of_First_Col"
private const val MAX_NO_OF_LAST_COL = "Max_No_Of_Last_Col"

class MyDatabase (var context: Context) {
    private val SQLQUERY_POWERBALL = buildString {
        append("CREATE TABLE ")
        append(TABLE_POWERBALL)
        append("(")
        append(SRNO)
        append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
        append(FIRST_NO)
        append(" TEXT, ")
        append(SECOND_NO)
        append("  TEXT, ")
        append(THIRD_NO)
        append("  TEXT, ")
        append(FOURTH_NO)
        append("  TEXT, ")
        append(FIFTH_NO)
        append("  TEXT, ")
        append(SIXTH_NO)
        append("  TEXT, ")
        append(SEVENTH_NO)
        append(" TEXT)")
    }

    private val SQLQUERY_USER_SELECTION = buildString {
        append("CREATE TABLE ")
        append(TABLE_USERSELECTION)
        append("(")
        append(SRNO)
        append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
        append(NO_OF_COL)
        append(" TEXT, ")
        append(MAX_NO_OF_FIRST_COL)
        append(" TEXT, ")
        append(MAX_NO_OF_LAST_COL)
        append(" TEXT)")
    }

    private var dbHelper : DBHelper = DBHelper(context)
    private var sqliteDatabase : SQLiteDatabase = dbHelper.writableDatabase

    suspend fun insertDetails(N1: String, N2: String, N3: String, N4: String, N5: String, N6: String, N7: String) {
        withContext(Dispatchers.IO){
            val contentValues = ContentValues()
            contentValues.put(FIRST_NO, N1)
            contentValues.put(SECOND_NO, N2)
            contentValues.put(THIRD_NO, N3)
            contentValues.put(FOURTH_NO, N4)
            contentValues.put(FIFTH_NO, N5)
            contentValues.put(SIXTH_NO, N6)
            contentValues.put(SIXTH_NO, N6)
            contentValues.put(SEVENTH_NO, N7)

            sqliteDatabase.insert(TABLE_POWERBALL, null, contentValues)
        }
    }

    suspend fun insertIntoUserSelection(noOfCol: Int, maxNoOfFirstCol: Int, maxNoOfLastCol: Int){
        withContext(Dispatchers.IO){
            val contentValue = ContentValues()
            contentValue.put(NO_OF_COL, noOfCol)
            contentValue.put(MAX_NO_OF_FIRST_COL, maxNoOfFirstCol)
            contentValue.put(MAX_NO_OF_LAST_COL, maxNoOfLastCol)
            sqliteDatabase.insert(TABLE_USERSELECTION, null, contentValue)
        }
    }

    suspend fun getDetails() : List<SixNumber>{
        return withContext(Dispatchers.IO){
            val numberList = ArrayList<SixNumber>()
            val cursor : Cursor = sqliteDatabase.rawQuery("SELECT * FROM $TABLE_POWERBALL", null)
            if (cursor.count > 0){
                if (cursor.moveToFirst()){
                    do {
                        val n1 = cursor.getString(0)
                        val n2 = cursor.getString(1)
                        val n3 = cursor.getString(2)
                        val n4 = cursor.getString(3)
                        val n5 = cursor.getString(4)
                        val n6 = cursor.getString(5)
                        val row = SixNumber(n1, n2, n3, n4, n5, n6)
                        numberList.add(row)
                    }while (cursor.moveToNext())
                }
            }
            numberList
        }
    }

    suspend fun deleteUserSelectionTable(){
        withContext(Dispatchers.IO){
            suspend fun deleteInsertedDetailsTable(){
                withContext(Dispatchers.IO){
                    val cursor: Cursor = sqliteDatabase.rawQuery("SELECT * FROM $TABLE_POWERBALL", null)
                    if (cursor.count > 0) {
                        sqliteDatabase.delete(TABLE_USERSELECTION, null, null)
                    }
                }
            }
        }
    }

    suspend fun deleteInsertedDetailsTable(){
        withContext(Dispatchers.IO){
            val cursor: Cursor = sqliteDatabase.rawQuery("SELECT * FROM $TABLE_POWERBALL", null)
            if (cursor.count > 0) {
                sqliteDatabase.delete(TABLE_POWERBALL, null, null)
            }
        }
    }

    fun getTwoRow(): List<SixNumber>{
        val list = mutableListOf<SixNumber>()
        val cursor: Cursor = sqliteDatabase.rawQuery("SELECT * FROM powerBall ORDER BY RANDOM() LIMIT 2", null)
        if (cursor.count > 0){
            cursor.moveToFirst()
            do {
                list.add(SixNumber(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6)))
            }while (cursor.moveToNext())
        }
        return list
    }

    fun getFourtyRow(): List<SixNumber>{
        val list = mutableListOf<SixNumber>()
        val cursor: Cursor = sqliteDatabase.rawQuery("SELECT * FROM powerBall ORDER BY RANDOM() LIMIT 40", null)
        if (cursor.count > 0){
            cursor.moveToFirst()
            do {
                list.add(SixNumber(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6)))
            }while (cursor.moveToNext())
        }
        return list
    }

    fun getTwoRowForThreePredctor(): List<ThreeNumbers>{
        val list = mutableListOf<ThreeNumbers>()
        val cursor: Cursor = sqliteDatabase.rawQuery("SELECT * FROM powerBall ORDER BY RANDOM() LIMIT 2", null)
        if (cursor.count > 0){
            cursor.moveToFirst()
            do {
                list.add(ThreeNumbers(cursor.getString(1), cursor.getString(2), cursor.getString(3)))
            }while (cursor.moveToNext())
        }
        return list
    }

    fun getFourtyRowThreePredctor(): List<ThreeNumbers>{
        val list = mutableListOf<ThreeNumbers>()
        val cursor: Cursor = sqliteDatabase.rawQuery("SELECT * FROM powerBall ORDER BY RANDOM() LIMIT 40", null)
        if (cursor.count > 0){
            cursor.moveToFirst()
            do {
                list.add(ThreeNumbers(cursor.getString(1), cursor.getString(2), cursor.getString(3)))
            }while (cursor.moveToNext())
        }
        return list
    }

    fun getTwoRowForFourPredctor(): List<FourNumbers>{
        val list = mutableListOf<FourNumbers>()
        val cursor: Cursor = sqliteDatabase.rawQuery("SELECT * FROM powerBall ORDER BY RANDOM() LIMIT 2", null)
        if (cursor.count > 0){
            cursor.moveToFirst()
            do {
                list.add(FourNumbers(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4)))
            }while (cursor.moveToNext())
        }
        return list
    }

    fun getFourtyRowFourPredctor(): List<ThreeNumbers>{
        val list = mutableListOf<ThreeNumbers>()
        val cursor: Cursor = sqliteDatabase.rawQuery("SELECT * FROM powerBall ORDER BY RANDOM() LIMIT 40", null)
        if (cursor.count > 0){
            cursor.moveToFirst()
            do {
                list.add(ThreeNumbers(cursor.getString(1), cursor.getString(2), cursor.getString(3)))
            }while (cursor.moveToNext())
        }
        return list
    }

    inner class DBHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION){
        override fun onCreate(sqliteDb: SQLiteDatabase?) {
            sqliteDb?.execSQL(SQLQUERY_POWERBALL)
            sqliteDb?.execSQL(SQLQUERY_USER_SELECTION)
        }

        override fun onUpgrade(sqliteDb: SQLiteDatabase?, p1: Int, p2: Int) {
            TODO("Not yet implemented")
        }

    }
}