package com.example.mvvmnewsapp.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mvvmnewsapp.R
import com.example.mvvmnewsapp.adapters.NewsAdapter
import com.example.mvvmnewsapp.data.ArticleDatabase
import com.example.mvvmnewsapp.databinding.FragmentBreakingNewsBinding
import com.example.mvvmnewsapp.repository.NewsRepository
import com.example.mvvmnewsapp.ui.MainActivity
import com.example.mvvmnewsapp.util.Resources
import com.example.mvvmnewsapp.viewmodel.NewsViewModel
import com.example.mvvmnewsapp.viewmodel.NewsViewModelProviderFactory


class BreakingNewsFragment: Fragment(R.layout.fragment_breaking_news) {
    private lateinit var viewModel:NewsViewModel
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var binding: FragmentBreakingNewsBinding
    private val TAG = "BreakingNewsFragment"

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