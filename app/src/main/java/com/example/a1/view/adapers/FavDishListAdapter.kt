package com.example.a1.view.adapers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.a1.databinding.ItemDishBinding
import com.example.a1.model.entities.FavDish

class FavDishListAdapter(private val fragment: Fragment) :
    RecyclerView.Adapter<FavDishListAdapter.DishViewHolder>() {

    private var dishes = listOf<FavDish>()

    class DishViewHolder(view: ItemDishBinding) : RecyclerView.ViewHolder(view.root) {
        val dishImage = view.ivDishImage
        val dishTitle = view.tvDishTitle
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DishViewHolder {
        val binding = ItemDishBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DishViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DishViewHolder, position: Int) {
        val dish = dishes[position]
        Glide.with(fragment)
            .load(dish.imagePath)
            .into(holder.dishImage)
        holder.dishTitle.text = dish.title
    }

    override fun getItemCount(): Int {
        return dishes.size
    }

    fun updateDishes(list: List<FavDish>) {
        dishes = list
        notifyDataSetChanged()
    }
}