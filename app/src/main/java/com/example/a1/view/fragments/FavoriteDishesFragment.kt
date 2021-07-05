package com.example.a1.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.TimePicker
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a1.MainApplication
import com.example.a1.databinding.FragmentFavoriteDishesBinding
import com.example.a1.model.entities.FavDish
import com.example.a1.view.activities.MainActivity
import com.example.a1.view.adapers.FavDishListAdapter
import com.example.a1.viewmodel.FavoriteDishesViewModel
import com.example.a1.viewmodel.FavoriteDishesViewModelFactory
import timber.log.Timber

class FavoriteDishesFragment : Fragment() {

    private lateinit var favoriteDishesViewModel: FavoriteDishesViewModel
    private lateinit var binding: FragmentFavoriteDishesBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val repository = (requireActivity().application as MainApplication).repository
        favoriteDishesViewModel = ViewModelProvider(this, FavoriteDishesViewModelFactory(repository)).get(FavoriteDishesViewModel::class.java)
        binding = FragmentFavoriteDishesBinding.inflate(inflater, container, false)

        val favDishAdapter = FavDishListAdapter(this)
        binding.rvDishesList.apply {
            adapter = favDishAdapter
            layoutManager = GridLayoutManager(context, 1)
        }

        favoriteDishesViewModel.favoriteList.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                binding.rvDishesList.visibility = View.VISIBLE
                binding.tvNoItem.visibility = View.GONE
                favDishAdapter.updateDishes(it)
            } else {
                binding.rvDishesList.visibility = View.GONE
                binding.tvNoItem.visibility = View.VISIBLE
            }
        }
        return binding.root
    }

    fun goToFavDishDetail(favDish: FavDish) {
        findNavController().navigate( FavoriteDishesFragmentDirections.actionNavigationFavoriteDishesToNavigationDetailDish(favDish))
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
}