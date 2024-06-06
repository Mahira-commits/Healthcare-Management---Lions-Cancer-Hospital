package com.example.lion_nav_barhomepage.patientdashboard.diagnosis

import android.app.DownloadManager
import android.content.Context.DOWNLOAD_SERVICE
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
import androidx.core.net.toUri
import coil.load
import com.example.lion_nav_barhomepage.R
import com.example.lion_nav_barhomepage.databinding.ActivityZoomClassBinding
import com.example.lion_nav_barhomepage.databinding.FragmentPatientAppointmentsBinding
import java.io.File

class PresFragment : Fragment() {
    private var _binding: ActivityZoomClassBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = ActivityZoomClassBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = "Prescription"
        binding.largeImage.load(urlimg.toUri()) {
            placeholder(R.drawable.loading_animation)
            error(R.drawable.ic_broken_image)
        }
        binding.add.setOnClickListener {
            Log.e("button", "called")
            val url = urlimg.toString()
            val request = DownloadManager.Request(Uri.parse(url))
                .setTitle("Prescription_" + r_id.toString())
                .setDescription("Downloading")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setAllowedOverMetered(true)
                .setMimeType("image/jpeg")
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES,File.separator+r_id+".jpg")

            val dm = requireActivity().getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            dm.enqueue(request)
        }
    }
}
     