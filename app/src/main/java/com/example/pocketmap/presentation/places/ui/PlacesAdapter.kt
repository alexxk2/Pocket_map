package com.example.pocketmap.presentation.places.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pocketmap.R
import com.example.pocketmap.databinding.PlaceItemBinding
import com.example.pocketmap.domain.models.Place

class PlacesAdapter(
    private val context: Context,
    private val actionListener: PlacesClickListener
) : ListAdapter<Place, PlacesAdapter.PlacesViewHolder>(DiffCallBack), View.OnClickListener {


    class PlacesViewHolder(val binding: PlaceItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlacesViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val binding = PlaceItemBinding.inflate(inflater, parent, false)

        binding.root.setOnClickListener(this)
        binding.placeNameTextView.setOnClickListener(this)
        return PlacesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlacesViewHolder, position: Int) {
        val item = getItem(position)

        with(holder.binding) {


            placeNameTextView.text = context.getString(R.string.place_name_text, item.id.toString())
            placeLocationTextView.text =
                context.getString(
                    R.string.place_location_text,
                    String.format("%.4f", item.lat),
                    String.format("%.4f", item.lon)
                )

            root.tag = item
            placeNameTextView.tag = item
        }
    }

    override fun onClick(v: View?) {
        val place = v?.tag as Place
        when (v.id) {
            R.id.place_name_text_view -> actionListener.onPlaceClick(place)
            else -> actionListener.onPlaceClick(place)
        }
    }

    interface PlacesClickListener {
        fun onPlaceClick(place: Place)
    }

    companion object {
        val DiffCallBack = object : DiffUtil.ItemCallback<Place>() {

            override fun areItemsTheSame(oldItem: Place, newItem: Place): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: Place, newItem: Place): Boolean {
                return oldItem == newItem
            }
        }
    }

}