package com.example.myapplication.ui.frament

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.FragmentPerawatanBinding
import com.example.myapplication.response.CareDetails

class PerawatanFragment : Fragment() {
    private lateinit var binding: FragmentPerawatanBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPerawatanBinding.inflate(inflater, container, false)

        // Ambil data perawatan
        val careDetails = arguments?.getParcelable<CareDetails>("CARE_DETAILS")
        careDetails?.let {
            binding.tvDeskripsi.text = it.deskripsi

            // Perawatan Bulu dengan penomoran
            binding.tvPerawatanBulu.text = formatListWithNumbers(it.perawatanBulu)

            // Aktivitas dengan penomoran
            binding.tvAktivitasStimulasi.text = formatListWithNumbers(it.aktivitas)

            // Perawatan Gigi dengan penomoran
            binding.tvPerawatanGigi.text = formatListWithNumbers(it.perawatanGigi)

            // Perawatan Kuku dengan penomoran
            binding.tvPerawatanKuku.text = formatListWithNumbers(it.perawatanKuku)
        }

        return binding.root
    }
    private fun formatListWithNumbers(list: List<String?>?): String {
        if (list.isNullOrEmpty()) return "Data tidak tersedia"
        return list.mapIndexed { index, item ->
            "${index + 1}. ${item ?: "Informasi tidak tersedia"}"
        }.joinToString("\n")
    }

}
