package com.example.countryapp.data.sqlite

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.countryapp.data.api.DataModel


    // Table contents are grouped together in an anonymous object.

const val TABLE_NAME = "countries"
const val COLUMN_NAME_ID = "id"
const val COLUMN_NAME_ID_YEAR = "id_year"
const val COLUMN_NAME_STATE = "estado"
const val COLUMN_NAME_YEAR = "ano"
const val COLUMN_NAME_POPULATION = "populacao"
const val COLUMN_NAME_SLUG = "slug"


private const val SQL_CREATE_ENTRIES =
    "CREATE TABLE $TABLE_NAME (" +
            "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "$COLUMN_NAME_ID TEXT," +
            "$COLUMN_NAME_ID_YEAR TEXT," +
            "$COLUMN_NAME_STATE TEXT," +
            "$COLUMN_NAME_YEAR TEXT," +
            "$COLUMN_NAME_POPULATION TEXT," +
            "$COLUMN_NAME_SLUG TEXT);"

private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS $TABLE_NAME"

class SQLite (context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    fun addCountry(country: DataModel): Boolean{
        // Gets the data repository in write mode
        val db = this.writableDatabase
        // Create a new map of values, where column names are the keys
        val values = ContentValues().apply {
            put(COLUMN_NAME_ID, country.IDState)
            put(COLUMN_NAME_STATE, country.State)
            put(COLUMN_NAME_ID_YEAR, country.IDYear)
            put(COLUMN_NAME_YEAR, country.Year)
            put(COLUMN_NAME_POPULATION, country.Population)
            put(COLUMN_NAME_SLUG, country.SlugState)
        }
        var newRowId = db.insert(TABLE_NAME, null, values)
        if(newRowId == (-1).toLong()) {
            return false
        }
        return true
    }

    fun showAll() :MutableList<String>{
        var db: SQLiteDatabase = writableDatabase //Open the connection

        //Creating the string vector
        var completeList: MutableList<String> = ArrayList()

        //Using cursor to store select
        var c: Cursor = db.rawQuery("SELECT * FROM $TABLE_NAME;", arrayOf<String>())
        if(c.moveToFirst())
        {
            do {
                //Put the String in the ListView
                var content = c.getString(3) + " " +c.getString(5)
                //Add in the list
                completeList.add(content)
            } while (c.moveToNext())
        }
        db.close()
        //Return
        return completeList
    }

    fun deleteSelectedCountry(countryName: String) {
        val db = writableDatabase //Open the connection
        //SQL Statement
        db.delete(TABLE_NAME, "$COLUMN_NAME_STATE=?", arrayOf(countryName))
        db.close()
    }

    fun updateCountryData(country: DataModel) {
        val db = writableDatabase
        val contentValues = ContentValues()

        contentValues.put(COLUMN_NAME_ID, country.IDState)
        contentValues.put(COLUMN_NAME_ID_YEAR, country.IDYear)
        contentValues.put(COLUMN_NAME_STATE, country.State)
        contentValues.put(COLUMN_NAME_POPULATION, country.Population)
        contentValues.put(COLUMN_NAME_YEAR, country.Year)
        contentValues.put(COLUMN_NAME_SLUG, country.SlugState)


        db.update(TABLE_NAME, contentValues, "$COLUMN_NAME_ID = ?", arrayOf(country.IDState))
    }

    fun showSelectedCountry(country: DataModel, state: String) : DataModel {
        val db = readableDatabase //Open the connection


        val c = db.rawQuery("SELECT * FROM $TABLE_NAME WHERE $COLUMN_NAME_STATE = ?;", arrayOf(state))

        if (c.moveToFirst()) {
            do {
                country.IDState = c.getString(1)
                country.IDYear = c.getString(2)
                country.State = c.getString(3)
                country.Year = c.getString(4)
                country.Population = c.getString(5)
                country.SlugState = c.getString(6)
            } while (c.moveToNext())
        }
        db.close()
        //Return
        return country
    }

    companion object {
        // If you change the database schema, you must increment the database version.
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "FeedReader.db"
    }
}

