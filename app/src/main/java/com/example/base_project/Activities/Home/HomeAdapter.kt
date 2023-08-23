package com.example.base_project.Activities.Home

import android.R
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.base_project.Activities.Home.Models.ProductResponse
import com.example.base_project.Utils.Views.loadFromurl
import com.example.base_project.databinding.HomeItemBinding


@SuppressLint("NotifyDataSetChanged", "SetTextI18n")
class HomeAdapter(
    private var list: List<ProductResponse> = arrayListOf(),
    var setOnClickListener: (ProductResponse?) -> Unit? = { },
    var setOnLongClickListener: ((ProductResponse, View) -> Unit)? = null,
) : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(HomeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    fun update(data: List<ProductResponse>?) {
        list = data ?: arrayListOf()
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: HomeItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ProductResponse) {
            with(binding) {
                item.avatar?.let { img.loadFromurl(it) }
                txtNome.text = item.name
                txtMatricula.text = item.origin
                root.setOnClickListener {
                    setOnClickListener(item)
                }
                root.setOnLongClickListener {
                    setOnLongClickListener?.invoke(item, it)
                    return@setOnLongClickListener true
                }

            }
        }
    }

}