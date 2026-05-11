package com.stackunderflow.contacto

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ContactDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "contacts.db"
        const val DATABASE_VERSION = 1
        const val TABLE_CONTACTS = "contacts"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_PHONE = "phone"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = """
            CREATE TABLE $TABLE_CONTACTS(
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT,
                $COLUMN_PHONE TEXT
            )
        """.trimIndent()
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(
        db: SQLiteDatabase?, p1: Int, p2: Int
    ) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_CONTACTS")
        onCreate(db)
    }

    fun addContact(contact: Contact): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, contact.name)
            put(COLUMN_PHONE, contact.phone)
        }
        val success = db.insert(TABLE_CONTACTS, null, values)
        db.close()
        return success
    }

    fun getAllContacts(): List<Contact> {
        val contactList = mutableListOf<Contact>()
        val db = this.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM  $TABLE_CONTACTS", null)
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
                val phone = cursor.getString((cursor.getColumnIndexOrThrow(COLUMN_PHONE)))
                contactList.add(Contact(id, name, phone))
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return contactList
    }

    fun updateContact(contact: Contact): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, contact.name)
            put(COLUMN_PHONE, contact.phone)
        }
        val success =
            db.update(TABLE_CONTACTS, values, "$COLUMN_ID=?", arrayOf(contact.id.toString()))
        db.close()
        return success
    }

    fun deleteContact(contactId: Int): Int {
        val db = this.writableDatabase
        val success = db.delete(TABLE_CONTACTS, "$COLUMN_ID=?", arrayOf(contactId.toString()))
        db.close()
        return success
    }
}

