package com.stackunderflow.contacto

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.stackunderflow.contacto.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private lateinit var dbHelper: ContactDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = ContactDatabaseHelper(this)

        val newContact = Contact(name="Juan", phone="123456789")
        val newContact2 = Contact(name="Pedro", phone="987654321")

        dbHelper.addContact(newContact)
        dbHelper.addContact(newContact2)

        val contacts = dbHelper.getAllContacts()

        val text = contacts.joinToString(separator = "\n") { contact ->
            "ID: ${contact.id}, Name: ${contact.name}. Phone: ${contact.phone}"
        }

        binding.textView.text = text
    }
}