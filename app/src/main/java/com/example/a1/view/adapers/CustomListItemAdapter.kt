package com.example.a1.view.adapers

import android.app.Activity
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a1.databinding.DialogCustomItemBinding
import com.example.a1.utils.Constants
import com.example.a1.utils.SelectedItem
import com.example.a1.view.activities.AddUpdateDishActivity
import com.example.a1.view.activities.MainActivity
import timber.log.Timber

class CustomListItemAdapter(
    private val activity: Activity, private val listItems: List<String>, private val listType: String, private val callback: SelectedItem?
) : RecyclerView.Adapter<CustomListItemAdapter.ViewHolder>() {

    class ViewHolder(view: DialogCustomItemBinding) : RecyclerView.ViewHolder(view.root) {
        val tvText = view.itemContent
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DialogCustomItemBinding.inflate(activity.layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listItems[position]
        holder.tvText.text = item
        holder.tvText.setOnClickListener {
            Timber.e("Clicked on: $item")
            when (activity) {
                is AddUpdateDishActivity -> {
                    activity.onSelected(item, listType)
                }
                is MainActivity -> {
                    callback?.onSelected(item, Constants.DISH_TYPE)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return listItems.size
    }

}