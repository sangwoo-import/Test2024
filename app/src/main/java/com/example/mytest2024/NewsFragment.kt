package com.example.mytest2024

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.example.mytest2024.databinding.NewsFragmentBinding

class NewsFragment : Fragment() {


    private lateinit var binding: NewsFragmentBinding


    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = NewsFragmentBinding.inflate(inflater)



        binding.myWebView.webViewClient = WebViewClient()
        binding.myWebView.settings.javaScriptEnabled = true
        binding.myWebView.loadUrl("https://m.news.naver.com/")



        return binding.root // root는 contraint 전체를 뜻한다
    }

    fun canGoBack(): Boolean {
        return binding.myWebView.canGoBack()
    }

    fun goBack() {
        binding.myWebView.goBack()
    }

}
