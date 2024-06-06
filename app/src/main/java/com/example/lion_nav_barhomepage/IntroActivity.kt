package com.example.lion_nav_barhomepage

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.lion_nav_barhomepage.databinding.ActivityIntroBinding
import com.example.lion_nav_barhomepage.patientdashboard.patientdata

class IntroActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)
        val register: Button = findViewById(R.id.button2)
        val guest : Button = findViewById(R.id.button)
        register.setOnClickListener{
            val intent = Intent(this,
                LoginActivity::class.java)
            startActivity(intent)
        }
        guest.setOnClickListener{
            patient_main_data= patientdata()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }
}