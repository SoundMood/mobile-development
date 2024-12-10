package com.example.soundmood.ui.fragment.homepage

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.soundmood.R
import com.example.soundmood.databinding.FragmentHomepagefragmentBinding
import com.example.soundmood.ui.ViewModelFactory
import com.example.soundmood.ui.captureimagepage.CaptureImagePage
import com.example.soundmood.ui.fragment.profilepage.ProfilePageFragment
import com.spotify.android.appremote.api.SpotifyAppRemote


class HomePageFragment : Fragment(R.layout.fragment_homepagefragment) {


    private var _binding : FragmentHomepagefragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel : HomePageViewModel

    private var spotifyAppRemote: SpotifyAppRemote? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomepagefragmentBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this,ViewModelFactory(requireContext())).get(HomePageViewModel::class.java)


        viewModel.userName.observe(viewLifecycleOwner){userName->
            binding.textviewUsername.text = userName
        }

        viewModel.userImageProfile.observe(viewLifecycleOwner){ imageUrl->
            Glide.with(requireContext())
                .load(imageUrl)
                .circleCrop()
                .into(binding.imageviewProfile)
        }

        binding.btnGenerateplaylist.setOnClickListener{
            startActivity(Intent(requireContext(),CaptureImagePage::class.java))
        }

        binding.imageviewProfile.setOnClickListener{
           findNavController().navigate(R.id.profilePageFragment)
        }

    }

}