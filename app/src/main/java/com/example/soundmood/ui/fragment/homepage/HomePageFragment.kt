package com.example.soundmood.ui.fragment.homepage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.soundmood.databinding.FragmentHomepagefragmentBinding
import com.example.soundmood.ui.ViewModelFactory
import com.spotify.android.appremote.api.SpotifyAppRemote


class HomePageFragment : Fragment() {


    private var _binding : FragmentHomepagefragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel : HomePageViewModel
    private lateinit var adapter : RecyclerViewAdapter

    private var spotifyAppRemote: SpotifyAppRemote? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomepagefragmentBinding.inflate(inflater,container,false)

//        viewModel = ViewModelProvider(this, ViewModelFactory(requireContext())).get(HomePageViewModel::class.java)
//
//        adapter = RecyclerViewAdapter(emptyList())
//
//        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
//        binding.recyclerView.adapter = adapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        viewModel.recommendations.observe(viewLifecycleOwner){tracks->
//            adapter = RecyclerViewAdapter(tracks)
//            binding.recyclerView.adapter = adapter
//        }
//
//        viewModel.getRecommendations()


//        binding.btnLogout.setOnClickListener {
//            logout()
//        }

    }

//    private fun logout() {
//        // Hapus token dari SharedPreferences
//        val sharedPreferences = requireContext().getSharedPreferences("spotify_prefs", android.content.Context.MODE_PRIVATE)
//        val editor = sharedPreferences.edit()
//        editor.clear() // Hapus semua data di SharedPreferences
//        editor.apply()
//
//        // Arahkan pengguna kembali ke LoginActivity
//        val intent = android.content.Intent(requireContext(), com.example.soundmood.ui.loginpage.LoginActivity::class.java)
//        intent.flags = android.content.Intent.FLAG_ACTIVITY_NEW_TASK or android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
//        startActivity(intent)
//
//        // (Opsional) Hentikan aktivitas host jika diperlukan
//        requireActivity().finish()
//    }


}