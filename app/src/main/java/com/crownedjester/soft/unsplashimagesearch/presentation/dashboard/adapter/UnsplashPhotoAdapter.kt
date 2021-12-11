package com.crownedjester.soft.unsplashimagesearch.presentation.dashboard.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.crownedjester.soft.unsplashimagesearch.R
import com.crownedjester.soft.unsplashimagesearch.data.remote.dto.PhotoDto
import com.crownedjester.soft.unsplashimagesearch.databinding.ItemDashboardBinding

class UnsplashPhotoAdapter :
    PagingDataAdapter<PhotoDto, UnsplashPhotoAdapter.PhotoViewHolder>(PHOTO_COMPARATOR) {

    class PhotoViewHolder(private val binding: ItemDashboardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(photo: PhotoDto) {
            binding.apply {
                Glide.with(itemView)
                    .load(photo.urls.regular)
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.ic_error)
                    .into(imageView)

                textViewUserName.text = photo.user.username
            }
        }
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val currentItem = getItem(position)

        currentItem?.let {
            holder.bind(currentItem)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val binding =
            ItemDashboardBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return PhotoViewHolder(binding)
    }

    companion object {
        private val PHOTO_COMPARATOR = object : DiffUtil.ItemCallback<PhotoDto>() {
            override fun areItemsTheSame(oldItem: PhotoDto, newItem: PhotoDto): Boolean =
                oldItem.id == newItem.id


            override fun areContentsTheSame(
                oldItem: PhotoDto,
                newItem: PhotoDto
            ): Boolean =
                oldItem == newItem

        }
    }
}

