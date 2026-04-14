package com.example.eticketing

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eticketing.data.AppDatabase
import com.example.eticketing.databinding.FragmentTicketsBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class TicketsFragment : Fragment() {
    private var _binding: FragmentTicketsBinding? = null
    private val binding get() = _binding!!
    private lateinit var db: AppDatabase
    private lateinit var adapter: TicketAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTicketsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db = AppDatabase.getDatabase(requireContext())

        val sharedPref = requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val userId = sharedPref.getLong("user_id", -1)

        adapter = TicketAdapter(emptyList())
        binding.rvTickets.layoutManager = LinearLayoutManager(requireContext())
        binding.rvTickets.adapter = adapter

        if (userId != -1L) {
            lifecycleScope.launch {
                db.ticketDao().getTicketsByUserId(userId).collectLatest { ticketList ->
                    val ticketsWithNames = ticketList.map { ticket ->
                        val dest = db.destinationDao().getDestinationById(ticket.destinationId)
                        ticket to (dest?.name ?: "Unknown")
                    }
                    adapter.updateData(ticketsWithNames)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
