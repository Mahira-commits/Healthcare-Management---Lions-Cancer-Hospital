package com.example.lion_nav_barhomepage.Appointment


import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView.OnDateChangeListener
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.lion_nav_barhomepage.LoginActivity
import com.example.lion_nav_barhomepage.R
import com.example.lion_nav_barhomepage.about.CustomDialogFragment
import com.example.lion_nav_barhomepage.appointmentid
import com.example.lion_nav_barhomepage.databinding.FragmentAppointmentBinding
import com.example.lion_nav_barhomepage.doctors.ConfirmDialogFragment
import com.example.lion_nav_barhomepage.doctors.DoctorsFragment
import com.example.lion_nav_barhomepage.doctors.DoctorsViewModel
import com.example.lion_nav_barhomepage.doctors.data
import com.example.lion_nav_barhomepage.patient_main_data
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AppointmentFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AppointmentFragment : Fragment() {
    private lateinit var db: FirebaseFirestore
    private var _binding: FragmentAppointmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DoctorsViewModel by activityViewModels()
    private val viewModel1: appointmentViewmodel by activityViewModels()
    lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAppointmentBinding.inflate(inflater, container, false)
        val view = binding.root
        db = FirebaseFirestore.getInstance()
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //---------------------------
        sharedPreferences = this.requireActivity().getSharedPreferences(
            "login",
            AppCompatActivity.MODE_PRIVATE
        )
        val appointmemt=db.collection("Appointment")
        val getid=db.collection("count")
        //---------------------------
        val doc: data? = viewModel.get_data()
        if (doc != null) {
            doc.avl?.remove(0)
            Log.e("avl ", "${doc.avl}")
        }
        Log.e("appointement", "$doc")
        var dayOfWeek: Int = 0
        var selecteddate: String = ""
        if (doc != null) {
            doc.avl?.let { viewModel.setavl(it) }
        }
        val avl: String = viewModel.text
        if (doc != null) {
            binding.showName.text = doc?.name.toString()
            binding.showAval.text = avl
        }
        val cal: Calendar = Calendar.getInstance()
        val simpleDateFormat = SimpleDateFormat("dd-MM-yyyy")
        val currdate = simpleDateFormat.format(cal.time)
        val dd = currdate.subSequence(0, 2).toString().toInt()
        val mm = currdate.subSequence(3, 5).toString().toInt()
        val yyyy = currdate.subSequence(6, 10).toString().toInt()
        val day = cal.get(Calendar.DAY_OF_WEEK)
//        Log.e("on","${dd},$mm,$yyyy}")

//        val cur_date : String = simpleDateFormat.format(calendar.getT)

        binding.calendar.setOnDateChangeListener(OnDateChangeListener { view, year, month, date ->
            val calendar: Calendar = Calendar.getInstance()
            calendar.set(year, month, date)
            dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
            selecteddate = "$date/${month + 1}/$year"
            if (doc != null && dayOfWeek != 0) {
                settimeslot(dayOfWeek, doc)
                check(date, month + 1, year, dd, mm, yyyy)
            }
        })
        if(dayOfWeek==0){
            dayOfWeek=day
            settimeslot(dayOfWeek,doc)
        }
        Log.e("Day of week","$dayOfWeek")
        var slot: String = ""
        binding.selectDoctor.setOnClickListener {
            replaceFragment(DoctorsFragment())

        }

        binding.time1.setOnClickListener {
            slot = "9:00 AM - 12:00 PM"
            if (doc != null) {
                settimeslot(dayOfWeek, doc)

                binding.time1.setBackgroundResource(R.drawable.selectbackground)
            }

        }
        binding.time2.setOnClickListener {
            slot = "6:00 PM - 9:00 PM"
            if (doc != null) {
                settimeslot(dayOfWeek, doc)
//                binding.time2.setBackgroundColor(Color.parseColor("#FF3C963F"))
                binding.time2.setBackgroundResource(R.drawable.selectbackground)
            }

        }
        binding.time3.setOnClickListener {
            slot = "2:00 PM - 5:00 PM"
            if (doc != null) {
                settimeslot(dayOfWeek, doc)
//                binding.time3.setBackgroundColor(Color.parseColor("#FF3C963F"))
                binding.time3.setBackgroundResource(R.drawable.selectbackground)
            }

        }

        binding.confirm.setOnClickListener {
            val check = binding.avlSlots.isVisible
            Log.e("Check:","$check")
            val checklogin = sharedPreferences.getBoolean("logged", false)
            if(checklogin==false)
            {
                val dialogBuilder = AlertDialog.Builder(requireContext())
                dialogBuilder.setMessage("Please Register")
                    .setCancelable(true)
                dialogBuilder.setPositiveButton("Cancel ") { dialog, id ->
                }
                dialogBuilder.setNegativeButton("Register") { dialog, id ->
                    val intent = Intent(this.context, LoginActivity::class.java)
                    startActivity(intent)

                }

                val alert = dialogBuilder.create()
                alert.show()
            }
            else {
                if (doc == null || selecteddate == "" || slot == "") {

                    val dialogBuilder = AlertDialog.Builder(requireActivity())
                    dialogBuilder.setMessage("select appropriate data")
                        .setCancelable(true)
                    val alert = dialogBuilder.create()
                    alert.show()


                } else if (check == true) {
                    val dialogBuilder = AlertDialog.Builder(requireActivity())
                    dialogBuilder.setMessage("select appropriate data")
                        .setCancelable(true)
                    val alert = dialogBuilder.create()
                    alert.show()
                } else {


                    viewModel1.set_data(doc.name!!, selecteddate, slot)
                    var dialog = ConfirmDialogFragment()
                    dialog.show(childFragmentManager, "custom")
                    //---------------

                    val aid = (doc.id.toString() + patient_main_data.id.toString() + appointmentid)
                    Log.e("id", " $aid")
                    val data = appointment(
                        aid,
                        doc.name.toString(),
                        doc.spec.toString(),
                        patient_main_data.email,
                        selecteddate,
                        slot,
                        "Pending"
                    )
                    appointmemt.document(aid).set(data)
                        .addOnSuccessListener {
                            val newid = appointmentid + 1
                            getid.document("appointment").update("id", newid)
                            Toast.makeText(this.context, "Appointment placed", Toast.LENGTH_LONG)
                                .show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this.context, "Appointment failed", Toast.LENGTH_LONG)
                                .show()

                        }
                    Log.e("data", "${data}")
                    //--------------
                }
            }
            Log.e("on","${viewModel1.get_date()}")


        }
        (activity as AppCompatActivity).supportActionBar?.title="Book Appointment"
        super.onViewCreated(view, savedInstanceState)
    }

    private fun check(date: Int, month: Int, year: Int, dd: Int, mm: Int, yyyy: Int) {
        if (date>dd && month>=mm && year>=yyyy){
            Log.e("on","1)True")


        }
        else if (date<dd && month>mm && year>=yyyy){
            Log.e("on","2)True")


        }
        else if (year>yyyy){
            Log.e("on","4)True")
        }
        else{
            binding.avlSlots.setText("Invalid Date")
            binding.avlSlots.isVisible=true
            binding.time1.isClickable=false
            binding.time1.isEnabled=false
            binding.time1.setBackgroundResource(R.drawable.avail)
            binding.time1.isClickable=false
            binding.time2.isEnabled=false
            binding.time2.setBackgroundResource(R.drawable.avail)
            binding.time1.isClickable=false
            binding.time3.isEnabled=false
            binding.time3.setBackgroundResource(R.drawable.avail)
            Log.e("on","3)False")

        }


    }
    fun invalid(){

    }

    private fun settimeslot(dayOfWeek: Int, doc: data?) {
        var week = doc?.avl
        Log.e("Checkavl","$week")
        if (week!=null) {
            val slots = doc?.timeslots!![dayOfWeek-1]
            Log.e("slots","$slots ,'---'$dayOfWeek")
            if (week.indexOf(dayOfWeek) != -1) {
                binding.avlSlots.isVisible=false
                if (slots[0] == 1){
                    binding.time1.isClickable=true
                    binding.time1.isEnabled=true
                    binding.time1.setBackgroundResource(R.drawable.avail)

                }
                else{
                    binding.time1.isClickable=false
                    binding.time1.isEnabled=false
                    binding.time1.setBackgroundResource(R.drawable.avail)
                }
                if (slots[1] == 1){
                    binding.time3.isClickable=true
                    binding.time3.isEnabled=true
                    binding.time3.setBackgroundResource(R.drawable.avail)
                }
                else{
                    binding.time3.isClickable=false
                    binding.time3.isEnabled=false
                    binding.time3.setBackgroundResource(R.drawable.avail)

                }
                if (slots[2] == 1){
                    binding.time2.isClickable=true
                    binding.time2.isEnabled=true
                    binding.time2.setBackgroundResource(R.drawable.avail)
                }
                else{
                    binding.time2.isClickable=false
                    binding.time2.isEnabled=false
                    binding.time2.setBackgroundResource(R.drawable.avail)
                }
            }
//            FFE44545
            else{

                binding.avlSlots.setText("Slots not available")
                binding.avlSlots.isVisible=true
                binding.time1.isClickable=false
                binding.time2.isClickable=false
                binding.time3.isClickable=false
                binding.time1.isEnabled=false
                binding.time2.isEnabled=false
                binding.time3.isEnabled=false
                binding.time2.setBackgroundResource(R.drawable.avail)
                binding.time1.setBackgroundResource(R.drawable.avail)
                binding.time3.setBackgroundResource(R.drawable.avail)

                Log.e("on","false")
            }
        }
        else{
            binding.time1.isClickable=false
            binding.time2.isClickable=false
            binding.time3.isClickable=false
            binding.time1.isEnabled=false
            binding.time2.isEnabled=false
            binding.time3.isEnabled=false
            binding.time2.setBackgroundResource(R.drawable.avail)
            binding.time1.setBackgroundResource(R.drawable.avail)
            binding.time3.setBackgroundResource(R.drawable.avail)
            Log.e("on","false")
        }

    }

    fun replaceFragment(fragment: Fragment) {
        val fragmentManager = parentFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.framelayout, fragment)
        fragmentTransaction.commit()

    }


}