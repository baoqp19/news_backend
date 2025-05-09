package com.example.news_backend.ui.webview

import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.example.news_backend.R
import com.example.news_backend.databinding.FragmentWebviewBinding
import com.example.news_backend.utils.viewBinding

class WebviewFragment : Fragment(R.layout.fragment_webview) {
    private val binding by viewBinding(FragmentWebviewBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        binding.back.setOnClickListener { requireActivity().onBackPressedDispatcher.onBackPressed() }
        val link = arguments?.getString("link") ?: arguments?.getString("linknews")
        link?.let {
            loadUrlToWebView(it)
            Log.d("link1", it)
        }
    }

    private fun loadUrlToWebView(link: String) {
        binding.WebConnect.apply {
            webViewClient = object : WebViewClient() {
                override fun onPageCommitVisible(view: WebView?, url: String?) {
                    super.onPageCommitVisible(view, url)
                    binding.progressBarWebView.visibility = View.GONE
                }
            }
            loadUrl(link)
        }
    }

}
