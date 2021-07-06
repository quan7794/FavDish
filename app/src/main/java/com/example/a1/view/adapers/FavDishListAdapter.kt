package com.example.a1.view.adapers

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.a1.R
import com.example.a1.databinding.ItemDishBinding
import com.example.a1.model.entities.FavDish
import com.example.a1.utils.Constants
import com.example.a1.view.activities.AddUpdateDishActivity
import com.example.a1.view.fragments.AllDishesFragment
import com.example.a1.view.fragments.FavoriteDishesFragment
import timber.log.Timber

class FavDishListAdapter(private val fragment: Fragment) :
    RecyclerView.Adapter<FavDishListAdapter.DishViewHolder>() {

    private var dishes = listOf<FavDish>()

    class DishViewHolder(view: ItemDishBinding) : RecyclerView.ViewHolder(view.root) {
        val dishImage = view.ivDishImage
        val dishTitle = view.tvDishTitle
        val dishOptionMenu = view.btnDishOption
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
        holder.itemView.setOnClickListener {
            if (fragment is AllDishesFragment) {
                fragment.goToFavDishDetail(dish)
            } else if (fragment is FavoriteDishesFragment) {
                fragment.goToFavDishDetail(dish)
            }
        }
        if (fragment is AllDishesFragment) {
            holder.dishOptionMenu.visibility = View.VISIBLE
            holder.dishOptionMenu.setOnClickListener {
                Timber.e("Clicked on ${holder.dishTitle.text}")
                val popUp = PopupMenu(fragment.context, holder.dishOptionMenu)
                popUp.menuInflater.inflate(R.menu.menu_dish_options, popUp.menu)

                popUp.setOnMenuItemClickListener {
                    when (it.itemId){
                        R.id.action_edit_dish ->{
                            Timber.e("Clicked on action_edit_dish")
                            val intent = Intent(fragment.context, AddUpdateDishActivity::class.java)
                            intent.putExtra(Constants.DISH_UPDATE, dish)
                            fragment.requireActivity().startActivity(intent)
                        }
                        R.id.action_delete_dish -> {
                            Timber.e("Clicked on action_delete_dish")
                        }
                        R.id.action_like_dish -> {
                            Timber.e("Clicked on action_like_dish")
                        }
                    }
                    true
                }
                popUp.show()
            }
        } else {
            holder.dishOptionMenu.visibility = View.GONE
        }

    }

    override fun getItemCount(): Int {
        return dishes.size
    }

    fun updateDishes(list: List<FavDish>) {
        dishes = list
        notifyDataSetChanged()
    }
}