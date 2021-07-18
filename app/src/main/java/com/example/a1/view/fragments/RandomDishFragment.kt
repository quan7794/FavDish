package com.example.a1.view.fragments

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.a1.MainApplication
import com.example.a1.databinding.FragmentRandomDishBinding
import com.example.a1.model.entities.ExtendedIngredient
import com.example.a1.model.entities.FavDish
import com.example.a1.model.entities.Ingredient
import com.example.a1.model.entities.Recipe
import com.example.a1.utils.Constants
import com.example.a1.utils.Status
import com.example.a1.utils.setFavoriteIcon
import com.example.a1.viewmodel.RandomDishViewModel
import com.example.a1.viewmodel.RandomDishViewModelFactory
import timber.log.Timber

class RandomDishFragment : Fragment() {

    private lateinit var randomDishViewModel: RandomDishViewModel
    private var _binding: FragmentRandomDishBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val factory = RandomDishViewModelFactory((requireActivity().application as MainApplication).repository)
        _binding = FragmentRandomDishBinding.inflate(inflater, container, false)
        randomDishViewModel = ViewModelProvider(this, factory).get(RandomDishViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.dishFavorite.setOnClickListener {
            if (binding.dishFavorite.isClickable) {
                binding.randomDish?.let {
                    Timber.e("Add Random dish to favorite list")
                    it.favoriteDish = true
                    randomDishViewModel.addRandomDishToFavorite(it)
                    setFavoriteIcon(binding.dishFavorite,true)
                    binding.dishFavorite.isClickable = false
                }
            }
        }

        randomDishViewModel.getRandomDishes().observe(viewLifecycleOwner) {
            when (it.status) {
                Status.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.scrollView2.visibility = View.GONE
                    binding.dishFavorite.isClickable = false
                }
                Status.ERROR -> {
                    binding.progressBar.visibility = View.GONE
                    binding.scrollView2.visibility = View.VISIBLE
                    binding.dishIngredient.text = it.message
                    binding.dishFavorite.isClickable = false
                    Toast.makeText(context, "Error when get random dish.", Toast.LENGTH_LONG).show()
                }
                Status.SUCCESS -> {
                    binding.progressBar.visibility = View.GONE
                    binding.scrollView2.visibility = View.VISIBLE
                    binding.dishFavorite.isClickable = true
                    it.data?.let { data -> updateDishData(data.recipes[0]) }
                }
            }
        }
    }

    private fun updateDishData(data: Recipe) {
        Toast.makeText(context, "randomDish - title: $data", Toast.LENGTH_LONG).show()
        Glide.with(this@RandomDishFragment).load(data.image).listener(imageListener()).into(binding.dishImage)
        data.apply {
            binding.randomDish = FavDish(
                imagePath = image,
                imageSource = Constants.DISH_IMAGE_SOURCE_REMOTE,
                title = title,
                type = dishTypes[0],
                category = "Other",
                ingredients = getIngredients(extendedIngredients),
                cookingTime = readyInMinutes.toString(),
                directionToCook = instructions,
                favoriteDish = false
            )
            binding.dishTitle.isSelected = true
        }
    }

    private fun getIngredients(ingredients: List<ExtendedIngredient>): String {
        var ingredient = ""
        for (item in ingredients) {
            ingredient += "${item.original}, "
        }
        return ingredient
    }

    private fun imageListener(): RequestListener<Drawable> {
        return object : RequestListener<Drawable> {
            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                Timber.e("Load image fail: $e")
                return true
            }

            override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                Palette.from(resource!!.toBitmap()).generate { palette ->
                    palette?.dominantSwatch?.rgb?.let {
                        binding.detailParentView.setBackgroundColor(it)
                    }
                }
                return false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}