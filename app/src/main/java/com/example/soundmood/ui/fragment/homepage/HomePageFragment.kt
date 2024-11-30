package com.example.soundmood.ui.fragment.homepage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.soundmood.databinding.FragmentHomepagefragmentBinding


class HomePageFragment : Fragment() {


    private var _binding : FragmentHomepagefragmentBinding? = null
    private val binding get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomepagefragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogout.setOnClickListener {
            logout()
        }

    }

    private fun logout() {
        // Hapus token dari SharedPreferences
        val sharedPreferences = requireContext().getSharedPreferences("spotify_prefs", android.content.Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear() // Hapus semua data di SharedPreferences
        editor.apply()

        // Arahkan pengguna kembali ke LoginActivity
        val intent = android.content.Intent(requireContext(), com.example.soundmood.ui.loginpage.LoginActivity::class.java)
        intent.flags = android.content.Intent.FLAG_ACTIVITY_NEW_TASK or android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)

        // (Opsional) Hentikan aktivitas host jika diperlukan
        requireActivity().finish()
    }


}