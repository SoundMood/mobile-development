package com.example.soundmood.ui.fragment.historypage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.soundmood.R
import com.example.soundmood.databinding.FragmentHistoryPageBinding
import com.example.soundmood.ui.ViewModelFactory


class HistoryPageFragment : Fragment() {
    private var _binding : FragmentHistoryPageBinding?=null
    private val binding get() = _binding!!

    private val historyPageViewModel:HistoryPageViewModel by viewModels{
        ViewModelFactory(requireContext().applicationContext)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryPageBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

}