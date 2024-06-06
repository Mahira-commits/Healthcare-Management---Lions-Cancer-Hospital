package com.example.lion_nav_barhomepage.doctors

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import coil.load
import com.example.lion_nav_barhomepage.Appointment.AppointmentFragment
import com.example.lion_nav_barhomepage.R
import com.example.lion_nav_barhomepage.databinding.DoctorsProfileBinding
import com.example.lion_nav_barhomepage.databinding.FragmentDoctorsBinding



class DoctorsProfileFragment : Fragment() {
    private var _binding: DoctorsProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DoctorsViewModel by activityViewModels()
    private lateinit var docname: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //arguments?.let {
         //   param1 = it.getString(ARG_PARAM1)
          //  param2 = it.getString(ARG_PARAM2)
       // }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DoctorsProfileBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val doc : data? = viewModel.get_data()

        val img_url= doc?.img_url.toString()
        binding.docimg
            .load(img_url.toUri()){
            placeholder(R.drawable.loading_animation)
            error(R.drawable.ic_broken_image)
        }
        docname = binding.name
        docname.text=doc?.name.toString()
        binding.docId.text= doc?.id.toString()
        binding.spec.text=doc?.spec.toString()

        binding.docExp.text=doc?.exp.toString()
        binding.docEdu.text=doc?.about.toString()
        if (doc != null) {
            viewModel.setavl(doc.avl!!)
        }
        val avl : String = viewModel.text
        binding.avl.text=avl
        binding.lang.text=doc?.lang.toString()
        binding.fee.text=doc?.fee.toString()
        (activity as AppCompatActivity).supportActionBar?.title="Doctors Profile"
        binding.Back.setOnClickListener {
            replaceFragment(DoctorsFragment())
        }
        binding.book.setOnClickListener {
            replaceFragment(AppointmentFragment())
        }

    }
 //override fun OnBackPressedCallback(){
    //  replaceFragment(DoctorsFragment())

 //}
    fun replaceFragment(fragment: Fragment) {
        val fragmentManager = parentFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.framelayout, fragment)
        fragmentTransaction.commit()

    }



}