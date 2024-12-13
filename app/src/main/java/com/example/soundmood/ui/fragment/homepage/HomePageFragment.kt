package com.example.soundmood.ui.fragment.homepage

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.soundmood.R
import com.example.soundmood.databinding.FragmentHomepagefragmentBinding
import com.example.soundmood.ui.ViewModelFactory
import com.example.soundmood.ui.captureimagepage.CaptureImagePage
import com.example.soundmood.ui.fragment.profilepage.ProfilePageFragment


class HomePageFragment : Fragment() {


    private var _binding : FragmentHomepagefragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel:HomePageViewModel by viewModels {
        ViewModelFactory(requireContext().applicationContext)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomepagefragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter  = HomePageAdapter(mutableListOf())
        val gridLayoutManager = GridLayoutManager(requireContext(),3)

        binding.rvRecommendation.apply {
            layoutManager = gridLayoutManager
            this.adapter = adapter
        }

        viewModel.recommendationAlbum.observe(viewLifecycleOwner){playlists->
            adapter.updateData(playlists)
        }

        viewModel.isLoading.observe(viewLifecycleOwner){isLoading->
            if(isLoading){
                binding.progressBar.visibility = View.VISIBLE
            }else{
                binding.progressBar.visibility = View.GONE
            }
        }

        observeViewModel()

        binding.imageviewProfile.setOnClickListener {
            val userId = viewModel.userId.value ?: ""
            val userName = viewModel.userName.value ?: ""
            val userImage = viewModel.userImageProfile.value ?: ""

            val bundle = Bundle().apply {
                putString("user_id", userId)
                putString("user_name", userName)
                putString("user_image", userImage.toString())
            }
            findNavController().navigate(R.id.profilePageFragment,bundle)

        }
    }

    fun observeViewModel(){
        viewModel.userName.observe(viewLifecycleOwner){userName->
            binding.textviewUsername.text = userName
        }

        viewModel.userImageProfile.observe(viewLifecycleOwner){ imageUrl->
            Glide.with(requireContext())
                .load(imageUrl)
                .placeholder(R.drawable.startingpage)
                .circleCrop()
                .into(binding.imageviewProfile)
        }

        viewModel.userName.observe(viewLifecycleOwner){userName->
            if(!userName.isNullOrEmpty()){
                viewModel.getSeveralAlbum()
            }
        }

        val intent=Intent(requireContext(),CaptureImagePage::class.java)
        viewModel.userId.observe(viewLifecycleOwner){userId->
            intent.putExtra("user_id",userId)
        }


        binding.btnGenerateplaylist.setOnClickListener{
            startActivity(intent)
        }

        binding.ivMood1.setImageDrawable(
            ContextCompat.getDrawable(requireContext(),R.drawable.happy)
        )
        binding.ivMood2.setImageDrawable(
            ContextCompat.getDrawable(requireContext(),R.drawable.sad)
        )
        binding.ivMood3.setImageDrawable(
            ContextCompat.getDrawable(requireContext(),R.drawable.neutral)
        )

        binding.tvMood1.text = getString(R.string.happy)
        binding.tvMood2.text = getString(R.string.sad)
        binding.tvMood3.text = getString(R.string.neutral)
    }
}