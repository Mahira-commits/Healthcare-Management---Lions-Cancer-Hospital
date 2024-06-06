package com.example.lion_nav_barhomepage

import android.app.*
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.lion_nav_barhomepage.Vaccines.bookvaccine
import com.example.lion_nav_barhomepage.databinding.FragmentVaccineRequestBinding
import com.example.lion_nav_barhomepage.doctors.DoctorsFragment
import com.google.firebase.firestore.FirebaseFirestore

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [VaccineRequestFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
lateinit  var requestlist : ArrayList<bookvaccine>
class VaccineRequestFragment : Fragment(){
//    lateinit var notificationChannel: NotificationChannel
//    lateinit var notificationManager: NotificationManager
//    lateinit var builder: Notification.Builder
//    private val channelId = "12345"
//    private val description = "Test Notification"
    private lateinit var recyclerView: RecyclerView
    private lateinit var db: FirebaseFirestore
    private var _binding: FragmentVaccineRequestBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        db = FirebaseFirestore.getInstance()
        _binding = FragmentVaccineRequestBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = binding.RecyclerView
        (activity as AppCompatActivity).supportActionBar?.title = "Appointments"
        super.onViewCreated(view, savedInstanceState)
        val progressDialog = ProgressDialog(context)
        progressDialog.setMessage("Fetching data....")
        progressDialog.setCancelable(false)
        progressDialog.show()
        requestlist = arrayListOf<bookvaccine>()
        if (!DoctorsFragment().isConnected(requireContext())) {
            Toast.makeText(requireContext(), "No Internet ", Toast.LENGTH_SHORT).show();
            Log.e("network--->","if-block")
            progressDialog.dismiss()
        }
        db.collection("VaccineRequests")
            .whereEqualTo("patient_emial", patient_main_data.email.toString())
            .get()
            .addOnSuccessListener { datas ->

                for (i in datas) {
                    Log.e("list", "${i}")
                    val obj = bookvaccine(
                        i.data["vaccine_id"].toString(),
                        i.data["name"].toString(),
                        i.data["distributor"].toString(),
                        i.data["patient_emial"].toString(),
                        i.data["dose"].toString(),
                        i.data["other"].toString(),
                        i.data["status"].toString()
                    )
                    requestlist.add(obj)
                }
                recyclerView.adapter =
                    VaccineRequestsAdapter(this@VaccineRequestFragment, requestlist)
                progressDialog.dismiss()

            }
            .addOnFailureListener { exception ->
                Log.e("---->", "Error getting documents: ")
            }
        binding.search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                filter(p0.toString())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
//        val currentTime = LocalTime.now()
//        val currentDate = LocalDate.now()
//
//        val filename = patient_main_data.id.toString()+"-"+currentDate+"-"+currentTime
//        val docRef = db.collection("VaccineRequests").whereEqualTo(p)
//        docRef.addSnapshotListener { snapshot, e ->
//            if (e != null) {
//                Log.w(TAG, "Listen failed.", e)
//                return@addSnapshotListener
//            }
//
//            if (snapshot != null && snapshot.exists()) {
//                notification(requireView())
//                Log.d(TAG, "Current data: ${snapshot.data}")
//            } else {
//                Log.d(TAG, "Current data: null")
//            }
//        }
//
//    }
//    fun notification(view: View){
//
//        val intent = Intent(this.context,SplashActivity::class.java)
//        val pendingIntent = PendingIntent.getActivity(this.context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            notificationChannel = NotificationChannel(channelId, description, NotificationManager .IMPORTANCE_HIGH)
////                notificationChannel.lightColor = Color.BLUE notificationChannel.enableVibration(true)
//            notificationManager.createNotificationChannel(notificationChannel)
//            builder = Notification.Builder(this.context, channelId).setContentTitle("NOTIFICATION USING " +
//                    "KOTLIN").setContentText("Test Notification").setSmallIcon(R.drawable .ic_about_us_svgrepo_com).setLargeIcon(
//                BitmapFactory.decodeResource(this.resources, R.drawable
//                    .ic_launcher_background)).setContentIntent(pendingIntent)
//        }
//        notificationManager.notify(12345, builder.build())
//    }
    }
    fun filter(text: String) {
        val filtered: ArrayList<bookvaccine> = ArrayList()

        val courseAry: ArrayList<bookvaccine> = requestlist

        for (eachCourse in courseAry) {
            if (eachCourse.dose!!.toLowerCase()
                    .contains(text.toLowerCase()) || eachCourse.distributor!!.toLowerCase()
                    .contains(text.toLowerCase())
            ) {
                filtered.add(eachCourse)
            }
            else if( eachCourse.name!!.toLowerCase()
                    .contains(text.toLowerCase()) || eachCourse.distributor!!.toLowerCase()
                    .contains(text.toLowerCase())){
                filtered.add(eachCourse)
            }
            else if( eachCourse.status!!.toLowerCase()
                    .contains(text.toLowerCase()) || eachCourse.distributor!!.toLowerCase()
                    .contains(text.toLowerCase())){
                filtered.add(eachCourse)
            }
            else if( eachCourse.other!!.toLowerCase()
                    .contains(text.toLowerCase()) || eachCourse.distributor!!.toLowerCase()
                    .contains(text.toLowerCase())){
                filtered.add(eachCourse)
            }
            else if( eachCourse.vaccine_id!!.toLowerCase()
                    .contains(text.toLowerCase()) || eachCourse.distributor!!.toLowerCase()
                    .contains(text.toLowerCase())){
                filtered.add(eachCourse)
            }
        }

        //calling a method of the adapter class and passing the filtered list
//        DoctorsAdapter(this,filtered)
        recyclerView.adapter = VaccineRequestsAdapter(this@VaccineRequestFragment, filtered)
    }





}