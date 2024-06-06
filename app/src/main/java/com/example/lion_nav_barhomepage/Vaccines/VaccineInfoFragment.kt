package com.example.lion_nav_barhomepage.Vaccines

import android.app.ProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import coil.load
import com.example.lion_nav_barhomepage.R
import com.example.lion_nav_barhomepage.about.teamlist
import com.example.lion_nav_barhomepage.databinding.FragmentVaccineInfoBinding

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [VaccineInfoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
val num = ""
class VaccineInfoFragment : Fragment(){
    private var _binding: FragmentVaccineInfoBinding? = null
    private val binding get() = _binding!!
    var img_url = ""
    var db = Firebase.firestore

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
        _binding = FragmentVaccineInfoBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val progressDialog = ProgressDialog(context)
        progressDialog.setMessage("Fetching data....")
        progressDialog.setCancelable(false)
        progressDialog.show()
        binding.vacimg.load(url_img.toUri()) {
            placeholder(R.drawable.loading_animation)
            error(R.drawable.ic_broken_image)
        }

        db.collection("Newvaccine").whereEqualTo("text",r_id).get()
            .addOnSuccessListener { datas ->
                for (i in datas) {
                    val obj = newvaccines(
                        i.data["image"].toString(),
                        i.data["text"].toString(),
                        i.data["distributor"].toString(),
                        i.data["dose"].toString(),
                        i.data["symptoms"].toString()
                    )
                    binding.name.setText(i.data["text"].toString())
                    binding.distributors.setText(i.data["distributor"].toString())
                    binding.nodose.setText(i.data["dose"].toString())
                    binding.sideeffects.setText(i.data["symptoms"].toString())

                    progressDialog.dismiss()


                }


            }

        (activity as AppCompatActivity).supportActionBar?.title="Vaccine Info"
        binding.Back.setOnClickListener {
            replaceFragment(NewVaccinesFragment())
        }
        binding.book.setOnClickListener {
            replaceFragment(BookVaccineFragment())
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