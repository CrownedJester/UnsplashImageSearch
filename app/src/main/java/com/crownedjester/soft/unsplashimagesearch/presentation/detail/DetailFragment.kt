package com.crownedjester.soft.unsplashimagesearch.presentation.detail

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.crownedjester.soft.unsplashimagesearch.R
import com.crownedjester.soft.unsplashimagesearch.data.remote.dto.PhotoDto
import com.crownedjester.soft.unsplashimagesearch.databinding.FragmentDetailBinding
import java.io.File

class DetailFragment : Fragment(R.layout.fragment_detail) {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding

    private val args by navArgs<DetailFragmentArgs>()
    private lateinit var photo: PhotoDto

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentDetailBinding.bind(view)
        photo = args.photo

        binding?.apply {

            Glide.with(this@DetailFragment)
                .load(photo.urls.full)
                .error(R.drawable.ic_error)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.isVisible = false
                        textViewStatus.isVisible = false
                        textViewErrorMsg.isVisible = true
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.isVisible = false
                        textViewStatus.isVisible = false
                        textViewDescription.isVisible = true
                        textViewUserName.isVisible = true
                        return false
                    }

                })
                .into(imageView)

            textViewDescription.text = photo.description
            textViewUserName.apply {
                text = "Photo made by ${photo.user.name} on Unsplash"
                setOnClickListener {
                    goToPhotoSource(photo.user.attributionUrl)
                }
                paint.isUnderlineText = true
            }
        }
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.menu_top_bar_detail, menu)

        val downloadAction = menu.findItem(R.id.action_download)
        downloadAction.icon.setTint(Color.WHITE)

        downloadAction.setOnMenuItemClickListener {
            downloadPhoto(photo.links.download)
            true
        }

    }

    private fun goToPhotoSource(uriString: String) {
        val uri = Uri.parse(uriString)
        val intent = Intent(Intent.ACTION_VIEW, uri)

        requireContext().startActivity(intent)
    }

    private fun downloadPhoto(uriString: String) {
        try {
            val filename = System.currentTimeMillis().toString()
            val uri = Uri.parse(uriString)
            val downloadManager =
                requireContext().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val request = DownloadManager.Request(uri)
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
                .setAllowedOverRoaming(false)
                .setTitle(filename)
                .setMimeType("image/jpeg")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_PICTURES,
                    File.separator + filename + ".jpg"
                )
            downloadManager.enqueue(request)
            Toast.makeText(requireContext(), "Image download started... ", Toast.LENGTH_SHORT)
                .show()
        } catch (e: Exception) {
            Toast.makeText(
                requireContext(),
                "Image download failed...\n${e.localizedMessage} ",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}