package com.example.myapplication.ui
import MakananFragment
import com.example.myapplication.ui.adapter.ViewPagerAdapter
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.myapplication.ui.frament.PerawatanFragment
import com.example.myapplication.databinding.MainMainBinding
import com.example.myapplication.response.CareDetails
import com.example.myapplication.response.Makanan
import com.google.android.material.tabs.TabLayoutMediator

class DetailViewPagerActivity : AppCompatActivity() {

    private lateinit var binding: MainMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ambil data dari Intent
        val careDetails = intent.getParcelableExtra<CareDetails>("CARE_DETAILS")
        val foodDetails = intent.getParcelableExtra<Makanan>("FOOD_DETAILS")


        val imageUriString = intent.getStringExtra("CAT_IMAGE_URI")

        if (!imageUriString.isNullOrEmpty()) {
            // Konversi String menjadi Uri
            val imageUri = Uri.parse(imageUriString)

            // Memuat gambar dari URI ke ImageView
            binding.ivCat.setImageURI(imageUri)
        }



        // Set up ViewPager dan TabLayout
        val viewPagerAdapter = ViewPagerAdapter(this)
        binding.viewPager.adapter = viewPagerAdapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Perawatan"
                1 -> tab.text = "Makanan"
            }
        }.attach()

        // Mengirimkan data ke masing-masing fragment
        val perawatanFragment = PerawatanFragment().apply {
            arguments = Bundle().apply {
                putParcelable("CARE_DETAILS", careDetails)
            }
        }
        val makananFragment = MakananFragment().apply {
            arguments = Bundle().apply {
                putParcelable("FOOD_DETAILS", foodDetails)
            }
        }

        // Set fragment untuk ViewPager
        binding.viewPager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount() = 2
            override fun createFragment(position: Int): Fragment {
                return when (position) {
                    0 -> perawatanFragment
                    1 -> makananFragment
                    else -> perawatanFragment
                }
            }
        }
    }
}
