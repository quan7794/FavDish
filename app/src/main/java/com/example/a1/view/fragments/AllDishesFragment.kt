package com.example.a1.view.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.a1.MainApplication
import com.example.a1.R
import com.example.a1.databinding.FragmentAllDishesBinding
import com.example.a1.model.entities.FavDish
import com.example.a1.view.activities.AddUpdateDishActivity
import com.example.a1.view.activities.MainActivity
import com.example.a1.view.adapers.FavDishListAdapter
import com.example.a1.viewmodel.FavDishViewModel
import com.example.a1.viewmodel.FavDishViewModelFactory
import com.example.a1.viewmodel.AllDishesViewModel

class AllDishesFragment : Fragment() {

    private lateinit var allDishesViewModel: AllDishesViewModel
    private lateinit var binding: FragmentAllDishesBinding
    private val mFavDishViewModel: FavDishViewModel by viewModels {
        FavDishViewModelFactory((requireActivity().application as MainApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    fun goToFavDishDetail(favDish: FavDish) {
        findNavController().navigate( AllDishesFragmentDirections.actionNavigationAllDishesToNavigationDetailDish(favDish))
        if (requireActivity() is MainActivity) {
            (activity as MainActivity).hideBottomNav()
        }
    }

    override fun onResume() {
        super.onResume()
        if (requireActivity() is MainActivity) {
            (activity as MainActivity).showBottomNav()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        allDishesViewModel = ViewModelProvider(this).get(AllDishesViewModel::class.java)
        binding = FragmentAllDishesBinding.inflate(inflater, container, false)

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

    fun deleteDish(dish: FavDish) {
        mFavDishViewModel.delete(dish)
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_add_dishes, menu)
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
}