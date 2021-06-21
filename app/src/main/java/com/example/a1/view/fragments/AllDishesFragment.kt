package com.example.a1.view.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.a1.MainApplication
import com.example.a1.R
import com.example.a1.databinding.FragmentAllDishesBinding
import com.example.a1.view.activities.AddUpdateDishActivity
import com.example.a1.view.adapers.FavDishListAdapter
import com.example.a1.viewmodel.FavDishViewModel
import com.example.a1.viewmodel.FavDishViewModelFactory
import com.example.a1.viewmodel.AllDishesViewModel

class AllDishesFragment : Fragment() {

    private lateinit var allDishesViewModel: AllDishesViewModel
    private var _binding: FragmentAllDishesBinding? = null
    private val mFavDishViewModel: FavDishViewModel by viewModels {
        FavDishViewModelFactory((requireActivity().application as MainApplication).repository)
    }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        allDishesViewModel = ViewModelProvider(this).get(AllDishesViewModel::class.java)
        _binding = FragmentAllDishesBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvDishesList.layoutManager = GridLayoutManager(requireActivity(), 2)
        val favDishAdapter = FavDishListAdapter(this)
        binding.rvDishesList.adapter = favDishAdapter

        mFavDishViewModel.allDishesList.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                binding.rvDishesList.visibility = View.VISIBLE
                binding.tvNoItem.visibility = View.GONE
                favDishAdapter.updateDishes(it)
            } else {
                binding.rvDishesList.visibility = View.GONE
                binding.tvNoItem.visibility = View.VISIBLE
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_all_dishes, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_add_dish -> {
                startActivity(Intent(requireActivity(), AddUpdateDishActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}