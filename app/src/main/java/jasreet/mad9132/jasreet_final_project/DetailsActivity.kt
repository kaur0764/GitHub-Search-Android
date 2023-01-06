package jasreet.mad9132.jasreet_final_project

/*
* Created by Jasreet Kaur on November 24, 2022
*/

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.widget.Toast
import com.google.gson.GsonBuilder
import com.squareup.picasso.Picasso
import okhttp3.*
import jasreet.mad9132.jasreet_final_project.databinding.ActivityDetailsBinding
import java.io.IOException

class DetailsActivity : AppCompatActivity() {

    // region Properties

    private lateinit var binding: ActivityDetailsBinding

    // endregion

    // region Methods

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = intent.getStringExtra(getString(R.string.details_title_key))

        // Get the keys from bundle object
        val url = intent.getStringExtra(getString(R.string.details_url_key))
        val htmlUrl = intent.getStringExtra(getString(R.string.details_html_url_key))

        val content = SpannableString(htmlUrl)
        content.setSpan(UnderlineSpan(), 0, htmlUrl?.length ?: 0, 0)
        binding.htmlURLTextView.text = content

        binding.htmlURLTextView.setOnClickListener {
            //Navigate to WebView activity when htmlURLTextView is clicked
            val intent = Intent(this, WebViewActivity::class.java)
            intent.putExtra(getString(R.string.url_key), htmlUrl)
            startActivity(intent)
        }

        url?.let {
            fetchJson(it)
        }
    }

    // region fetchJson

    private fun fetchJson(url: String) {

        // We are using okhttp client here, not Retrofit2
        val request = Request.Builder().url(url).build()

        val client = OkHttpClient()

        client.newCall(request).enqueue(object : Callback { // can't execute from main thread!
            override fun onFailure(call: Call, e: IOException) {
                // show a toast message on failure
                toast("Request Failed!")
            }

            override fun onResponse(call: Call, response: Response) {

                val body = response.body()?.string()

                val gson = GsonBuilder().create()
                val result = gson.fromJson(body, UserDetails::class.java)

                //  Display the user's details data
                runOnUiThread {
                    Picasso.get().load(result.avatar_url).into(binding.avatarImageView)

                    binding.nameTextView.text =
                        getString(R.string.user_name, result?.name ?: "unknown")
                    binding.locationTextView.text =
                        getString(R.string.user_location, result?.location ?: "unknown")
                    binding.companyTextView.text =
                        getString(R.string.user_company, result?.company ?: "unknown")
                    binding.followersTextView.text = getString(
                        R.string.user_followers,
                        result?.followers?.toString() ?: "unknown"
                    )
                    binding.pubicGistsTextView.text = getString(
                        R.string.user_public_gists,
                        result?.public_gists?.toString() ?: "unknown"
                    )
                    binding.pubicReposTextView.text = getString(
                        R.string.user_public_repos,
                        result?.public_repos?.toString() ?: "unknown"
                    )
                    binding.lastUpdateTextView.text = getString(
                        R.string.user_last_update,
                        result?.updated_at?.substring(0, 10) ?: "unknown"
                    )
                    binding.accountCreatedTextView.text = getString(
                        R.string.user_account_created,
                        result?.created_at?.substring(0, 10) ?: "unknown"
                    )
                }
            }
        })
    }

    // endregion

    // method to show toast message
    fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    // endregion

}