package org.devjj.platform.nurbanhoney.features.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.devjj.platform.nurbanhoney.R
import org.devjj.platform.nurbanhoney.core.platform.BaseFragment
import org.devjj.platform.nurbanhoney.databinding.FragmentFreeBoardBinding

class FreeBoardFragment : BaseFragment(){
    override fun layoutId() = R.layout.fragment_free_board

    private var _binding : FragmentFreeBoardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFreeBoardBinding.inflate(inflater, container, false)
        return binding.root
    }
}