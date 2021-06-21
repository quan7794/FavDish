package com.example.a1.view.adapers

import android.app.Activity
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.a1.databinding.DialogCustomItemBinding
import com.example.a1.view.activities.AddUpdateDishActivity
import timber.log.Timber

class CustomListItemAdapter(
    private val activity: Activity,
    private val listItems: List<String>,
    private val listType: String
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
            if (activity is AddUpdateDishActivity) {
                activity.selectedListItem(item, listType)
            }
        }
    }

    override fun getItemCount(): Int {
        return listItems.size
    }

}