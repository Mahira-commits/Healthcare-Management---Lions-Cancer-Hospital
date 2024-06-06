package com.example.lion_nav_barhomepage.patientdashboard

import android.app.Activity
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.ContactsContract
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import coil.load
import com.example.lion_nav_barhomepage.R
import com.example.lion_nav_barhomepage.databinding.FragmentEditProfileBinding
import com.example.lion_nav_barhomepage.patient_main_data
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*


class EditProfileFragment : Fragment() {
    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!
    var selectedPhotoUri : Uri?=null
    lateinit var sharedPreferences: SharedPreferences
    @RequiresApi(Build.VERSION_CODES.N)
    var formatDate = SimpleDateFormat("dd-MM-yyyy", Locale.US)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as AppCompatActivity).supportActionBar?.title="Edit Profile"
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences=this.requireActivity().getSharedPreferences("login",
            AppCompatActivity.MODE_PRIVATE
        )

        val db = FirebaseFirestore.getInstance()
        binding.Pemail.setText(patient_main_data.email)
        binding.Pname.setText(patient_main_data.name)
        binding.Pid.setText(patient_main_data.id)
        binding.Pphone.setText(patient_main_data.phone)
        binding.Pdob.setText(patient_main_data.DOB)
//        binding.Pgender.setText(patient_main_data.gender)
        when(patient_main_data.gender){
            "Male"-> binding.radiogroup.check(R.id.male)
            "Female"-> binding.radiogroup.check(R.id.female)
            "Other"-> binding.radiogroup.check(R.id.other)
        }
        binding.Pweight.setText(patient_main_data.weight)
        binding.Pheight.setText(patient_main_data.height)
        var img_url = patient_main_data.img_url
        if (img_url == ""){
            binding.Pimg.setImageResource(R.drawable.user_icon)
        }
        else {
            binding.Pimg
                .load(img_url?.toUri()) {
                    placeholder(R.drawable.loading_animation)
                    error(R.drawable.ic_broken_image)
                }
        }
        binding.Pimg.setOnClickListener {
            binding.Pimg.setImageDrawable(null)
            val intent = Intent(Intent.ACTION_PICK)
            intent.type="image/*"
            startActivityForResult(intent,0)

        }
        binding.Pdob.setOnClickListener {
            var dd : Int
            var mm : Int
            var yyyy : Int
            if (patient_main_data.DOB==""){
                val cal: Calendar = Calendar.getInstance()
                val simpleDateFormat = SimpleDateFormat("dd-MM-yyyy")
                val currdate = simpleDateFormat.format(cal.time)
                 dd = currdate.subSequence(0, 2).toString().toInt()
                 mm = currdate.subSequence(3, 5).toString().toInt()
                 yyyy = currdate.subSequence(6, 10).toString().toInt()
            }
            else
            {
                dd = patient_main_data.DOB?.subSequence(0, 2).toString().toInt()
                mm = patient_main_data.DOB?.subSequence(3, 5).toString().toInt()
                yyyy = patient_main_data.DOB?.subSequence(6, 10).toString().toInt()

            }
            val getDate: Calendar = Calendar.getInstance()
            var datepicker: DatePickerDialog
//            datepicker= DatePickerDialog(requireActivity(),null,2001,11,26)
            datepicker= DatePickerDialog( requireActivity(),android.R.style.ThemeOverlay_Material_Dialog,DatePickerDialog.OnDateSetListener{datePicker, i, i2, i3 ->
                val selectDate:Calendar= Calendar.getInstance()
                selectDate.set(Calendar.YEAR,i)
                selectDate.set(Calendar.MONTH,i2)
                selectDate.set(Calendar.DAY_OF_MONTH,i3)
                val date = formatDate.format(selectDate.time)
                binding.Pdob.text = date
            },
                yyyy,mm-1,dd)
            datepicker.show()



        }
        binding.radiogroup.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { radioGroup, i ->
            when (i) {
                R.id.male -> patient_main_data.gender = "Male"
                R.id.female -> patient_main_data.gender = "Female"
                R.id.other -> patient_main_data.gender = "Other"
            }
        })


        binding.Psubmit.setOnClickListener {

            val name = binding.Pname.text.toString()
            val dob = binding.Pdob.text.toString()
//            val gender = binding.Pgender.text.toString()
            val phone = binding.Pphone.text.toString()
            val  weight = binding.Pweight.text.toString()
            val height= binding.Pheight.text.toString()
            var flag=1
            if(name.isEmpty()){
                flag=2
                binding.Pname.error="Please enter name!"
                binding.Pname.requestFocus()
            }
            if(dob.isEmpty()){
                flag=2
                binding.Pdob.error="Please enter DOB!"
                binding.Pdob.requestFocus()
            }
//            if(gender.isEmpty()){
//                flag=2
//                binding.Pgender.error="Please enter gender!"
//                binding.Pgender.requestFocus()
//            }
            if(phone.isEmpty()){
                flag=2
                binding.Pphone.error="Please enter phone!"
                binding.Pphone.requestFocus()
            }
            if(weight.isEmpty()){
                flag=2
                binding.Pweight.error="Please enter weight!"
                binding.Pweight.requestFocus()
            }
            if(height.isEmpty()){
                flag=2
                binding.Pheight.error="Please enter height!"
                binding.Pheight.requestFocus()
            }
            if ( flag!=2){
                if (selectedPhotoUri != null){
                    val progressDialog = ProgressDialog(context)
                    progressDialog.setMessage("Uploading....")
                    progressDialog.setCancelable(false)
                    progressDialog.show()

                    val ref = FirebaseStorage.getInstance().getReference("/patient_img/${patient_main_data.email}")
                    val bitmap = MediaStore.Images.Media.getBitmap(getActivity()?.getContentResolver(), selectedPhotoUri)
                    val byteArrayOutputStream = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream)
                    val reducedImage: ByteArray = byteArrayOutputStream.toByteArray()

                    ref.putBytes(reducedImage)
                        .addOnSuccessListener {

                            ref.downloadUrl.addOnSuccessListener {
                                img_url = it.toString()
                                val data = patientdata(
                                  patient_main_data.id,
                                    name,
                                    patient_main_data.email,
                                    phone,
                                    img_url,
                                    patient_main_data.gender,
                                    dob,
                                    weight,
                                    height
                                )
                                db.collection("USERS").document(patient_main_data.email.toString()).set(data)
                                val toast = Toast.makeText(context," Sucessfully added", Toast.LENGTH_SHORT)
                                toast.show()
                                sharedPreferences.edit().putString("logged_email",data.email).apply()
                                sharedPreferences.edit().putString("logged_id",data.id).apply()
                                sharedPreferences.edit().putString("logged_name",data.name).apply()
                                sharedPreferences.edit().putString("logged_phone",data.phone).apply()
                                sharedPreferences.edit().putString("logged_img",data.img_url).apply()
                                sharedPreferences.edit().putString("logged_gender",data.gender).apply()
                                sharedPreferences.edit().putString("logged_dob",data.DOB).apply()
                                sharedPreferences.edit().putString("logged_height",data.height).apply()
                                sharedPreferences.edit().putString("logged_weight",data.weight).apply()
                                patient_main_data=data
//                                MainActivity().datachanged()
                                progressDialog.dismiss()
                                replaceFragment(PatientProfileFragment())

                            }

                        }
                        .addOnFailureListener{
                            val toast = Toast.makeText(context,"failed",Toast.LENGTH_SHORT)
                            toast.show()
                        }
                }
                else{
                    val progressDialog = ProgressDialog(context)
                    progressDialog.setMessage("Uploading....")
                    progressDialog.setCancelable(false)
                    progressDialog.show()
                    val data = patientdata(
                        patient_main_data.id,
                        name,
                        patient_main_data.email,
                        phone,
                        img_url,
                        patient_main_data.gender,
                        dob,
                        weight,
                        height
                    )
                    db.collection("USERS").document(patient_main_data.email.toString()).set(data)
                    val toast = Toast.makeText(context," Sucessfully added", Toast.LENGTH_SHORT)
                    toast.show()
                    sharedPreferences.edit().putString("logged_email",data.email).apply()
                    sharedPreferences.edit().putString("logged_id",data.id).apply()
                    sharedPreferences.edit().putString("logged_name",data.name).apply()
                    sharedPreferences.edit().putString("logged_phone",data.phone).apply()
                    sharedPreferences.edit().putString("logged_img",data.img_url).apply()
                    sharedPreferences.edit().putString("logged_gender",data.gender).apply()
                    sharedPreferences.edit().putString("logged_dob",data.DOB).apply()
                    sharedPreferences.edit().putString("logged_height",data.height).apply()
                    sharedPreferences.edit().putString("logged_weight",data.weight).apply()
                    patient_main_data=data
//                    MainActivity().datachanged()
                    progressDialog.dismiss()
                    replaceFragment(PatientProfileFragment())

                }

            }

        }
    }


    fun replaceFragment(fragment: Fragment) {
        val fragmentManager = parentFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.framelayout, fragment)
        fragmentTransaction.commit()

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==0 && resultCode== Activity.RESULT_OK && data!=null){
            selectedPhotoUri  =data.data
            val bitmap = MediaStore.Images.Media.getBitmap(getActivity()?.getContentResolver(),selectedPhotoUri)
            val bitmapDrawable= BitmapDrawable(bitmap)
            binding.Pimg.setBackgroundDrawable(bitmapDrawable)
        }
        else{
            if (patient_main_data.img_url == ""){
                binding.Pimg.setImageResource(R.drawable.user_icon)
            }
            else {
                binding.Pimg
                    .load(patient_main_data.img_url?.toUri()) {
                        placeholder(R.drawable.loading_animation)
                        error(R.drawable.ic_broken_image)
                    }
            }
        }
    }
}