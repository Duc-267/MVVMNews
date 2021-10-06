package com.example.mvvmnewsapp.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mvvmnewsapp.R
import com.example.mvvmnewsapp.adapters.NewsAdapter
import com.example.mvvmnewsapp.databinding.FragmentSearchNewsBinding
import com.example.mvvmnewsapp.ui.MainActivity
import com.example.mvvmnewsapp.util.Constants.Companion.SEARCH_DELAY
import com.example.mvvmnewsapp.util.Resources
import com.example.mvvmnewsapp.viewmodel.NewsViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SavedNewsFragment: Fragment(R.layout.fragment_saved_news) {
    private lateinit var viewModel: NewsViewModel
    private lateinit var newsAdapter: NewsAdapter
    private val TAG = "SearchNewsFragment"
    private lateinit var binding: FragmentSearchNewsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        setUpRecycleView()

        var job: Job? = null
        binding.etSearch.addTextChangedListener {editable->
            job?.cancel()
            job = MainScope().launch {
                delay(SEARCH_DELAY)
                editable?.let {
                    if (editable.toString().isNotEmpty()) {
                        viewModel.getSearchNews(editable.toString())
                    }
                }
            }
        }

        viewModel.searchNews.observe(viewLifecycleOwner, {
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
        binding.rvSearchNews.apply {
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