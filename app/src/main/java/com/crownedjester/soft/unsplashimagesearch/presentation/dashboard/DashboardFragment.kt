package com.crownedjester.soft.unsplashimagesearch.presentation.dashboard

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.crownedjester.soft.unsplashimagesearch.R
import com.crownedjester.soft.unsplashimagesearch.data.remote.dto.PhotoDto
import com.crownedjester.soft.unsplashimagesearch.databinding.FragmentDashboardBinding
import com.crownedjester.soft.unsplashimagesearch.presentation.dashboard.adapter.UnsplashLoadStateAdapter
import com.crownedjester.soft.unsplashimagesearch.presentation.dashboard.adapter.UnsplashPhotoAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardFragment : Fragment(R.layout.fragment_dashboard),
    UnsplashPhotoAdapter.OnItemClickListener {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private val unsplashViewModel: UnsplashViewModel by viewModels()
    private val unsplashPhotoAdapter = UnsplashPhotoAdapter(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentDashboardBinding.bind(view)

        binding.apply {
            recyclerView.apply {
                setHasFixedSize(true)
                adapter = unsplashPhotoAdapter.withLoadStateHeaderAndFooter(
                    header = UnsplashLoadStateAdapter { unsplashPhotoAdapter.retry() },
                    footer = UnsplashLoadStateAdapter { unsplashPhotoAdapter.retry() }
                )
            }
            buttonRetry.setOnClickListener { unsplashPhotoAdapter.retry() }
        }

        unsplashViewModel.photos.observe(viewLifecycleOwner) { data ->
            unsplashPhotoAdapter.submitData(viewLifecycleOwner.lifecycle, data)
        }

        unsplashPhotoAdapter.addLoadStateListener { loadState ->
            binding.apply {
                progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                recyclerView.isVisible = loadState.source.refresh is LoadState.NotLoading
                buttonRetry.isVisible = loadState.source.refresh is LoadState.Error
                textViewError.isVisible = loadState.source.refresh is LoadState.Error

                if (loadState.source.refresh is LoadState.NotLoading &&
                    loadState.append.endOfPaginationReached && unsplashPhotoAdapter.itemCount < 1
                ) {
                    recyclerView.isVisible = false
                    textViewEmpty.isVisible = true
                } else {
                    textViewEmpty.isVisible = false
                }

            }
        }
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.menu_top_bar_dashboard, menu)

        val searchItem = menu.findItem(R.id.search_bar)
        val searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    binding.recyclerView.scrollToPosition(0)
                    unsplashViewModel.searchPhotos(query)
                    searchView.clearFocus()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })
    }

    override fun onItemClick(photo: PhotoDto) {
        val action = DashboardFragmentDirections.actionDashboardFragmentToDetailFragment(photo)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}