package com.android.dailymood.ui.fragments.home

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.android.dailymood.data.local.dao.AppDatabase
import com.android.dailymood.data.local.model.MoodEntry
import com.android.dailymood.databinding.FragmentRegisterMoodBinding
import com.android.dailymood.utils.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class RegisterMoodFragment : Fragment() {

    private var _binding: FragmentRegisterMoodBinding? = null
    private val binding get() = _binding!!
    private lateinit var db: AppDatabase
    private var userId: Int? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentRegisterMoodBinding.inflate(inflater, container, false)
        db = AppDatabase.getDatabase(requireContext())
        userId = SessionManager.getLoggedInUser(requireContext())

        binding.btnSave.setOnClickListener {
            val scale = binding.ratingBar.rating.toInt()
            val emoji = binding.etEmoji.text.toString()
            val description = binding.etDescription.text.toString()
            val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

            if (scale == 0 || emoji.isBlank()) {
                Toast.makeText(requireContext(), "Preencha a escala e o emoji!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            userId?.let { uid ->
                lifecycleScope.launch(Dispatchers.IO) {
                    val todayEntry = db.moodDao().getMoodByDate(uid, today)

                    if (todayEntry != null) {
                        launch(Dispatchers.Main) {
                            Toast.makeText(requireContext(), "Você já registrou seu humor hoje!", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        val newEntry = MoodEntry(
                            userId = uid,
                            date = today,
                            moodScale = scale,
                            emoji = emoji,
                            description = if (description.isBlank()) null else description
                        )
                        db.moodDao().insertMood(newEntry)

                        launch(Dispatchers.Main) {
                            Toast.makeText(requireContext(), "Humor registrado com sucesso!", Toast.LENGTH_SHORT).show()
                            binding.ratingBar.rating = 0f
                            binding.etEmoji.text.clear()
                            binding.etDescription.text.clear()
                        }
                    }
                }
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
