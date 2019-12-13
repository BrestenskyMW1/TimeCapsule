package com.example.project

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.example.android.trackmysleepquality.database.MessageDatabase
import com.example.project.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity(){

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        val application = requireNotNull(this).application
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val dataSource= MessageDatabase.getInstance(application).msgDatabaseDao
        val viewModelFactory= MsgViewModelFactory(dataSource, application)
        val msgViewModel= ViewModelProviders.of(
            this, viewModelFactory).get(MsgViewModel::class.java)

        val subButton = binding.MessageSave
        val textEntry = binding.MessageEntry
        val deleteButton = binding.deleteButton

        //live data stuff
        binding.setLifecycleOwner(this)
        binding.msgViewModel = msgViewModel

        //create a new message
        subButton.setOnClickListener {
            Toast.makeText(this@MainActivity, textEntry.text, Toast.LENGTH_SHORT).show()
            msgViewModel.newMessage()
        }

        //delete all messages
        deleteButton.setOnClickListener{
            val dialogBuilder = AlertDialog.Builder(this)
            dialogBuilder.setMessage("Are you sure you want to delete all your saved messages?")
                .setCancelable(false)
                .setPositiveButton("Proceed", DialogInterface.OnClickListener {
                        dialog, id -> msgViewModel.onClearTracking()
                })
                .setNegativeButton("Cancel", DialogInterface.OnClickListener {
                        dialog, id -> dialog.cancel()
                })
            val alert = dialogBuilder.create()
            alert.setTitle("Delete Messages?")
            alert.show()
        }

        val botNav : BottomNavigationView = findViewById(R.id.navigation)
        botNav.setOnNavigationItemSelectedListener(object : BottomNavigationView.OnNavigationItemSelectedListener{
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                when (item.getItemId()) {
                    R.id.navigation_home -> {
                    }
                    R.id.navigation_map -> {
                        val a = Intent(this@MainActivity, MapsActivity::class.java)
                        startActivity(a)
                    }
                    R.id.navigation_game -> {
                        val b = Intent(this@MainActivity, MinigameActivity::class.java)
                        startActivity(b)
                    }
                }
                return false
            }
        })
        botNav.selectedItemId = R.id.navigation_home

    }

}
