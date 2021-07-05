package com.example.a1.view.fragments

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.a1.MainApplication
import com.example.a1.databinding.FragmentDetailDishBinding
import com.example.a1.model.entities.FavDish
import com.example.a1.viewmodel.DetailViewModel
import com.example.a1.viewmodel.FavDishViewModel
import com.example.a1.viewmodel.FavDishViewModelFactory
import timber.log.Timber

class DetailDishFragment : Fragment() {
    private lateinit var detailBinding: FragmentDetailDishBinding
    private val detailViewModel: DetailViewModel by viewModels()
    private val favDishViewModel: FavDishViewModel by viewModels{
        FavDishViewModelFactory((requireActivity().application as MainApplication).repository)
}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        detailBinding = FragmentDetailDishBinding.inflate(inflater, container, false)

        return detailBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: DetailDishFragmentArgs by navArgs()
        val dish = args.dishDetails
        detailViewModel.setDetailDish(dish)
        init(dish)
        detailBinding.dishFavorite.setOnClickListener{
            dish.favoriteDish = !dish.favoriteDish
            favDishViewModel.update(dish)
            detailViewModel.setDetailDish(dish)
        }

        detailViewModel.dish.observe(viewLifecycleOwner, {
            detailBinding.dish = it
        })
    }

    private fun init(dish: FavDish) {
//        detailBinding.dish = detailViewModel.dish.value

        Glide.with(this)
            .load(dish.imagePath)
            .listener(imageListener())
            .into(detailBinding.dishImage)


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
                        detailBinding.scrollView2.setBackgroundColor(it)
                    }
                }
                return false
            }
        }
    }

}

