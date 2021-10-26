package org.devjj.platform.nurbanhoney.features.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.hilt.android.AndroidEntryPoint
import org.devjj.platform.nurbanhoney.R
import org.devjj.platform.nurbanhoney.core.navigation.Navigator
import org.devjj.platform.nurbanhoney.core.platform.BaseFragment
import org.devjj.platform.nurbanhoney.databinding.FragmentNurbanboardBinding
import javax.inject.Inject

@AndroidEntryPoint
class NurbanHoneyFragment : BaseFragment() {
    override fun layoutId() = R.layout.fragment_nurbanboard

    @Inject
    lateinit var navigator : Navigator

    private var _binding : FragmentNurbanboardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNurbanboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.WriteNurban.setOnClickListener {
            navigator.showTextEditor(requireActivity())
        }
    }
}