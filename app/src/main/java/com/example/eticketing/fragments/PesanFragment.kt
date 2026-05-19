package com.example.eticketing.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.eticketing.activities.ChatListActivity
import com.example.eticketing.adapters.NotifikasiAdapter
import com.example.eticketing.data.AppDatabase
import com.example.eticketing.databinding.FragmentPesanBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PesanFragment : Fragment() {

    private var _binding: FragmentPesanBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPesanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val prefs = requireContext().getSharedPreferences("session", Context.MODE_PRIVATE)
        val userId = prefs.getLong("userId", -1L)
        val db = AppDatabase.getDatabase(requireContext())

        val notifAdapter = NotifikasiAdapter()
        _binding?.rvNotifikasi?.layoutManager =
            androidx.recyclerview.widget.LinearLayoutManager(requireContext())
        _binding?.rvNotifikasi?.adapter = notifAdapter

        viewLifecycleOwner.lifecycleScope.launch {
            db.messageDao().getNotifications(userId).collect { notifs ->
                if (_binding == null) return@collect
                notifAdapter.submitList(notifs)
                _binding?.tvEmptyNotif?.visibility =
                    if (notifs.isEmpty()) View.VISIBLE else View.GONE

                withContext(Dispatchers.IO) {
                    db.messageDao().markAllNotificationsRead(userId)
                }
            }
        }

        _binding?.btnKeChat?.setOnClickListener {
            startActivity(Intent(requireContext(), ChatListActivity::class.java))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}