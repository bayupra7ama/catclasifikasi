
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.myapplication.databinding.FragmentMakananBinding
import com.example.myapplication.response.Makanan

class MakananFragment : Fragment() {

    private var _binding: FragmentMakananBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMakananBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Ambil data dari arguments
        val foodDetails = arguments?.getParcelable<Makanan>("FOOD_DETAILS")

        foodDetails?.let {
            binding.nutritionDescription.text = it.nutrisi
            Log.d("foto", "gambar link : ${it.imageLinks}")
            if (!it.imageLinks.isNullOrEmpty()) {
                Glide.with(this)
                    .load(it.imageLinks)
                    .into(binding.imageProduct)
            }
        }
    }

    override fun onResume() {
        super.onResume()


        val foodDetails = arguments?.getParcelable<Makanan>("FOOD_DETAILS")

        foodDetails?.let {
            binding.nutritionDescription.text = it.nutrisi
            Log.d("foto", "gambar link : ${it.imageLinks}")
            if (!it.imageLinks.isNullOrEmpty()) {
                Glide.with(this)
                    .load(it.imageLinks)
                    .into(binding.imageProduct)
            }
        }


    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Hindari memory leak
    }
}
