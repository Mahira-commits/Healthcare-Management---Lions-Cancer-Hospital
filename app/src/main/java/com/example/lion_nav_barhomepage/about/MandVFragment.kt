package com.example.lion_nav_barhomepage.about

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.lion_nav_barhomepage.R
import com.example.lion_nav_barhomepage.databinding.FragmentLoginTabBinding
import com.example.lion_nav_barhomepage.databinding.FragmentMandVBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MandVFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MandVFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var _binding: FragmentMandVBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMandVBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val v=0.0f
        binding.mission.setTranslationY(300F)
        binding.missiondes.setTranslationY(300F)
        binding.vision.setTranslationY(300F)
        binding.visiondes.setTranslationY(300F)
        binding.mission.setAlpha(v)
        binding.missiondes.setAlpha(v)
        binding.visiondes.setAlpha(v)
        binding.vision.setAlpha(v)

        binding.mission.animate().translationY(0F).alpha(1F).setDuration(1000).setStartDelay(400).start()
        binding.missiondes.animate().translationY(0F).alpha(1F).setDuration(1000).setStartDelay(400).start()
        binding.vision.animate().translationY(0F).alpha(1F).setDuration(1000).setStartDelay(400).start()
        binding.visiondes.animate().translationY(0F).alpha(1F).setDuration(1000).setStartDelay(400).start()

        super.onViewCreated(view, savedInstanceState)
    }
}