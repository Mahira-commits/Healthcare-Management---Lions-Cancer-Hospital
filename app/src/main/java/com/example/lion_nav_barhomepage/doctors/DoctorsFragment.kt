package com.example.lion_nav_barhomepage.doctors

import android.app.ProgressDialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.example.lion_nav_barhomepage.R
import com.example.lion_nav_barhomepage.databinding.FragmentDoctorsBinding
import com.google.firebase.database.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


/**
 * A simple [Fragment] subclass.
 * Use the [DoctorsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
lateinit var DoctorList : ArrayList<data>
class DoctorsFragment : Fragment() ,DoctorsAdapter.Click ,DoctorsAdapter.replace{
    private var _binding: FragmentDoctorsBinding? = null
    private lateinit var dbref : DatabaseReference
    var slots : MutableList<MutableList<Int>> = mutableListOf(mutableListOf(0,0,0), mutableListOf(0,0,0), mutableListOf(0,0,0), mutableListOf(0,0,0),mutableListOf(0,0,0), mutableListOf(0,0,0), mutableListOf(0,0,0))
    var avl: MutableList<Int> = mutableListOf(0)
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var search: EditText
    lateinit var myview: View
    private val viewModel: DoctorsViewModel by activityViewModels()
    override fun go_to_main(R_id: Int) {
    viewModel.setposition(R_id)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


//      arguments?.let {
//          param1 = it.getString(ARG_PARAM1)
//
//      }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentDoctorsBinding.inflate(inflater, container, false)
        val view = binding.root
        return view


    }

     fun isConnected(context: Context): Boolean {
        Log.e("network--->","isconnect called")
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            val network = connectivityManager.activeNetwork ?: return false

            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true

                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true

                else -> false
            }
        } else {
            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val progressDialog = ProgressDialog(context)
        progressDialog.setMessage("Fetching data....")
        progressDialog.setCancelable(false)
        progressDialog.show()
        val fakedata=data(1254646,"name", "spec",
            "about",
            "not done yet",
            "exp",
            avl as ArrayList<Int>,
            slots,
            "lan",
            "200"
        )
        DoctorList = arrayListOf<data>()
        dbref = FirebaseDatabase.getInstance().getReference("Doctors")
        if (!isConnected(requireContext())) {
            Toast.makeText(requireContext(), "No Internet ", Toast.LENGTH_SHORT).show();
            Log.e("network--->","if-block")
            progressDialog.dismiss()
        }
        dbref.addValueEventListener( object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if(snapshot.exists()){
                    for (Doctorsnap in snapshot.children){
                        val doctor= Doctorsnap.getValue(data::class.java)
                        DoctorList.add(doctor!!)

                    }
                    DoctorList.remove(fakedata)
                    recyclerView.adapter = DoctorsAdapter(this@DoctorsFragment, DoctorList, this@DoctorsFragment,this@DoctorsFragment)
                    progressDialog.dismiss()
                }

            }


            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        recyclerView = binding.RecyclerView
        search = binding.sampleEditText

        myview = view
        viewModel.set_data()
        (activity as AppCompatActivity).supportActionBar?.title="Doctors"




        search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                filter(p0.toString())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })


    }

    fun filter(text: String) {
        val filtered: ArrayList<data> = ArrayList()

        val courseAry: ArrayList<data> = DoctorList

        for (eachCourse in courseAry) {
            if (eachCourse.name!!.toLowerCase()
                    .contains(text.toLowerCase()) || eachCourse.spec!!.toLowerCase()
                    .contains(text.toLowerCase())
            ) {
                filtered.add(eachCourse)
            }
            else if( eachCourse.spec!!.toLowerCase()
                    .contains(text.toLowerCase()) || eachCourse.spec!!.toLowerCase()
                    .contains(text.toLowerCase())){
                filtered.add(eachCourse)
            }
        }

        //calling a method of the adapter class and passing the filtered list
//        DoctorsAdapter(this,filtered)
        recyclerView.adapter = DoctorsAdapter(this, filtered, this,this)
    }

    override fun replaceFragment(fragment: Fragment) {
        val fragmentManager = parentFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.framelayout, fragment)
        fragmentTransaction.commit()

    }


}


