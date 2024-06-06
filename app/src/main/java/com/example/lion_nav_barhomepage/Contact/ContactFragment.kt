package com.example.lion_nav_barhomepage.Contact

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.lion_nav_barhomepage.R
import com.example.lion_nav_barhomepage.databinding.ActivityZoomClassBinding
import com.example.lion_nav_barhomepage.databinding.FragmentContactBinding


class ContactFragment : Fragment() {
    private var _binding: FragmentContactBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentContactBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as AppCompatActivity).supportActionBar?.title="Contact Us "
        super.onViewCreated(view, savedInstanceState)
        binding.phone1.setOnClickListener{
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:9849162699")
            startActivity(intent)
        }
        binding.phone2.setOnClickListener{
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:9700508408")
            startActivity(intent)
        }
        binding.phone3.setOnClickListener{
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:08912503228")
            startActivity(intent)
        }
        binding.mail.setOnClickListener{
            val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:lionscancerhospital@gmail.com")
            }
            startActivity(Intent.createChooser(emailIntent, "Send feedback"))
        }
        binding.web.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://lionscancerhospital.org")
            startActivity(intent)
        }
        binding.share.setOnClickListener {
            val intent= Intent()
            intent.action=Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_TEXT,"Download that lions cancer hospital app on playstore")
            intent.type="text/plain"
            startActivity(Intent.createChooser(intent,"Share To:"))
        }
        binding.location.setOnClickListener{
            val uri = Uri.parse("google.navigation:q=Lions+Cancer+Hospital,+Seethammadhara+Road, +Balayya+Sastri+Layout, +Seethammadhara, +Visakhapatnam, +Andhra+Pradesh+530013+Visakhapatnam")
            val intent = Intent(Intent.ACTION_VIEW,uri)
            intent.setPackage("com.google.android.apps.maps")
            startActivity(intent)
        }


    }



}