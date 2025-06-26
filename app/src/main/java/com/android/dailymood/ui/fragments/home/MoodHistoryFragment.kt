package com.android.dailymood.ui.fragments.home

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.dailymood.data.local.dao.AppDatabase
import com.android.dailymood.databinding.FragmentMoodHistoryBinding
import com.android.dailymood.ui.adapters.MoodHistoryAdapter
import com.android.dailymood.utils.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MoodHistoryFragment : Fragment() {

    private var _binding: FragmentMoodHistoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: MoodHistoryAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMoodHistoryBinding.inflate(inflater, container, false)

        val userId = SessionManager.getLoggedInUser(requireContext())
        val db = AppDatabase.getDatabase(requireContext())

        binding.rvMoodHistory.layoutManager = LinearLayoutManager(requireContext())

        lifecycleScope.launch(Dispatchers.IO) {
            val history = userId?.let { db.moodDao().getMoodHistory(it) } ?: emptyList()

            withContext(Dispatchers.Main) {
                adapter = MoodHistoryAdapter(history)
                binding.rvMoodHistory.adapter = adapter
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
