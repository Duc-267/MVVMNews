package com.example.mvvmnewsapp.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmnewsapp.R
import com.example.mvvmnewsapp.adapters.NewsAdapter
import com.example.mvvmnewsapp.databinding.FragmentBreakingNewsBinding
import com.example.mvvmnewsapp.ui.MainActivity
import com.example.mvvmnewsapp.util.Constants.Companion.QUERY_PAGE_SIZE
import com.example.mvvmnewsapp.util.Resources
import com.example.mvvmnewsapp.viewmodel.NewsViewModel


class BreakingNewsFragment: Fragment(R.layout.fragment_breaking_news) {
    private lateinit var viewModel:NewsViewModel
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var binding: FragmentBreakingNewsBinding
    private val TAG = "BreakingNewsFragment"
    private var isLoading = false
    private var isLastPage = false
    private var isScrolling = false

    val scrollListener = object : RecyclerView.OnScrollListener(){
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val totalVisibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLoadPage = !isLastPage && !isLoading
            val isAtLastItem = firstVisibleItemPosition + totalVisibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount > QUERY_PAGE_SIZE



        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBreakingNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        setUpRecycleView()

        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(R.id.action_breakingNewsFragment_to_articleFragment, bundle)
        }

        viewModel.breakingNews.observe(viewLifecycleOwner, {
            when(it){
                is Resources.Success -> {
                    hideSuccessBar()
                    it.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles)
                    }
                }
                is Resources.Error -> {
                    it.messages?.let{message ->
                        Log.d(TAG, "Breaking News Error: $message")
                    }
                }
                is Resources.Loading -> {
                    showSuccessBar()
                }
            }
        })
    }

    private fun setUpRecycleView(){
        newsAdapter = NewsAdapter()
        binding.rvBreakingNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun hideSuccessBar(){
        binding.paginationProgressBar.visibility = View.INVISIBLE
    }
    
    private fun showSuccessBar(){
        binding.paginationProgressBar.visibility = View.VISIBLE
    }
}