package com.example.lion_nav_barhomepage.doctors

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.lion_nav_barhomepage.Appointment.appointmentViewmodel
import com.example.lion_nav_barhomepage.R
import com.example.lion_nav_barhomepage.databinding.ConfirmAptDialogBinding
import com.example.lion_nav_barhomepage.databinding.FragmentAppointmentBinding

class ConfirmDialogFragment : DialogFragment() {
    private var _binding: ConfirmAptDialogBinding? = null
    private val binding get() = _binding!!
    private val viewModel: appointmentViewmodel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = ConfirmAptDialogBinding.inflate(inflater, container, false)
        val view = binding.root
        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val data : List<String> = viewModel.get_date()
        binding.docname.text=data[0]
        binding.date.text=data[1]
        binding.timeslot.text=data[2]
        binding.ok.setOnClickListener {
            dismiss()
        }
        super.onViewCreated(view, savedInstanceState)

    }
}