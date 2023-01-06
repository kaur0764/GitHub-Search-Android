package jasreet.mad9132.jasreet_final_project

/*
* Created by Jasreet Kaur on November 24, 2022
*/

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import jasreet.mad9132.jasreet_final_project.databinding.ActivityWebViewBinding

class WebViewActivity : AppCompatActivity() {

    // region Properties

    private lateinit var binding: ActivityWebViewBinding

    // endregion

    // region Methods

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val url = intent.getStringExtra(getString(R.string.url_key))

        binding.webViewGitHub.settings.javaScriptEnabled = true
        binding.webViewGitHub.settings.loadWithOverviewMode = true
        binding.webViewGitHub.settings.useWideViewPort = true


        url?.let {
            binding.webViewGitHub.loadUrl(url) //The WebView loads the users url passed from the DetailsActivity
        }

    }

    // endregion

}