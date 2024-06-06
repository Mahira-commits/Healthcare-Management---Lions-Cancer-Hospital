package com.example.lion_nav_barhomepage.Vaccines

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lion_nav_barhomepage.R
import com.example.lion_nav_barhomepage.databinding.FragmentNewVaccinesBinding
import com.example.lion_nav_barhomepage.doctors.DoctorsFragment
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [NewVaccinesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
var position: Int = -1
var vaccineList = ArrayList<newvaccines>()
var r_id=""
var url_img=""
class NewVaccinesFragment : Fragment(),NewVaccinesAdapter.Click {
    private lateinit var recyclerView: RecyclerView
    private lateinit var vaccineAdapter: NewVaccinesAdapter
    private var _binding: FragmentNewVaccinesBinding? = null
    private val binding get() = _binding!!
    var db = Firebase.firestore
    lateinit var myview: View


    private var vaccineList = ArrayList<newvaccines>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentNewVaccinesBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as AppCompatActivity).supportActionBar?.title="Vaccines"
        super.onViewCreated(view, savedInstanceState)

        recyclerView = binding.recyclerView
        recyclerView.layoutManager = GridLayoutManager(this.context, 2)



        val progressDialog = ProgressDialog(this.context)
        progressDialog.setMessage("Fetching data....")
        progressDialog.setCancelable(false)
        progressDialog.show()
        if (!DoctorsFragment().isConnected(requireContext())) {
            Toast.makeText(requireContext(), "No Internet ", Toast.LENGTH_SHORT).show();
            Log.e("network--->","if-block")
            progressDialog.dismiss()
        }
        db.collection("Newvaccine").get()
            .addOnSuccessListener { datas ->
                for (i in datas) {
                    Log.e("list", "${i}")
                    val obj = newvaccines(
                        i.data["image"].toString(),
                        i.data["text"].toString(),
                        i.data["distributor"].toString(),
                        i.data["dose"].toString(),
                        i.data["symptoms"].toString(),

                        )

                    vaccineList.add(obj)
                }
                vaccineAdapter = NewVaccinesAdapter(vaccineList, this@NewVaccinesFragment)
                recyclerView.adapter = vaccineAdapter
                progressDialog.dismiss()


            }
            .addOnFailureListener { exception ->
                Log.e("---->", "Error getting documents: ")
            }


    }



    override fun replaceFragment(fragment: Fragment,rid: String,url:String) {
        r_id=rid
        url_img=url
        val fragmentManager = parentFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.framelayout, fragment)
        fragmentTransaction.commit()

    }

}