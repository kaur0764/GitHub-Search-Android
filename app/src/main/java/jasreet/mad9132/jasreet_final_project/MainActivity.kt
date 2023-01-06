package jasreet.mad9132.jasreet_final_project

/*
* Created by Jasreet Kaur on November 24, 2022
*/

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import jasreet.mad9132.jasreet_final_project.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    // region Properties

    private lateinit var binding: ActivityMainBinding

    private val minPage = 1
    private val maxPage = 100
    private val startPage = 30

    private val baseUrl = "https://api.github.com/search/"

    // endregion

    // region Methods

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val internetConnection = InternetConnection(this)

        if (!internetConnection.isConnected) {
            AlertDialog.Builder(this)
                .setTitle(R.string.message_title)
                .setMessage(R.string.message_text)
                .setIcon(R.drawable.ic_baseline_network_check_24)
                .setNegativeButton(R.string.quit) { _, _ ->
                    finish()
                }
                .setCancelable((false))
                .show()
        } else {
            binding.searchButton.setOnClickListener {
                fetchJSONData()
            }

            binding.perPageNumberPicker.minValue = minPage
            binding.perPageNumberPicker.maxValue = maxPage

            // region Load the three Search Options using SharedPreferences
            val localStorage = LocalStorage()

            if (localStorage.contains(getString(R.string.repos_key))) {
                binding.minReposEditText.setText(
                    localStorage.getValueInt(getString(R.string.repos_key)).toString()
                )
            } else {
                binding.minReposEditText.setText("0")
            }

            if (localStorage.contains(getString(R.string.followers_key))) {
                binding.minFollowersEditText.setText(
                    localStorage.getValueInt(getString(R.string.followers_key)).toString()
                )
            } else {
                binding.minFollowersEditText.setText("0")
            }

            if (localStorage.contains(getString(R.string.page_size_key))) {
                binding.perPageNumberPicker.value =
                    localStorage.getValueInt(getString(R.string.page_size_key))
            } else {
                binding.perPageNumberPicker.value = startPage
            }

            // endregion

            // region Add listener for text change in searchUser control
            // When text in searchUser control changes hide no results message and enable search button
            binding.searchUser.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }

                override fun afterTextChanged(s: Editable?) {
                    binding.noResultsMessage.text = ""
                    binding.searchButton.isEnabled = true
                }

            })

            // endregion

            // region  keyboard - support return key
            binding.searchUser.setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                    if (binding.searchButton.isEnabled) {
                        binding.searchButton.callOnClick()
                    }
                    return@OnKeyListener true
                }
                false

            })

            // endregion

        }
    }

    // onStop is where we should save application data
    override fun onStop() {
        super.onStop()
        val localStorage = LocalStorage()

        // Save the three Search Options into SharedPreferences
        localStorage.save(
            (this.getString(R.string.repos_key)),
            binding.minReposEditText.text.toString().toInt()
        )
        localStorage.save(
            (this.getString(R.string.followers_key)),
            binding.minFollowersEditText.text.toString().toInt()
        )
        localStorage.save(
            (this.getString(R.string.page_size_key)),
            binding.perPageNumberPicker.value
        )
    }

    // region fetchJSONData

    private fun fetchJSONData() {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create()) // Run the data through the Gson converter, so that it becomes pure JSON
            .build()

        val restApi = retrofit.create(RestApi::class.java)

        // Check if input controls are empty and if empty then set them to zero
        if (TextUtils.isEmpty(binding.minFollowersEditText.text)) {
            binding.minFollowersEditText.setText("0")
        }
        if (TextUtils.isEmpty(binding.minReposEditText.text)) {
            binding.minReposEditText.setText("0")
        }

        val minNumberOfFollowers = binding.minFollowersEditText.text.toString().toInt()
        val minNumberOfRepos = binding.minReposEditText.text.toString().toInt()

        val searchString =
            "${binding.searchUser.text} repos:>=$minNumberOfRepos followers:>=$minNumberOfFollowers"

        val call = restApi.getUserData(searchString, binding.perPageNumberPicker.value)

        binding.progressBar.visibility = View.VISIBLE

        // enqueue makes an asynchronous connection and waits for a call back
        call.enqueue(object : Callback<ResponseDataClass> {
            override fun onResponse(
                call: Call<ResponseDataClass>,
                response: Response<ResponseDataClass>
            ) {
                val responseBody = response.body()

                val users = responseBody?.items
                val numberOfUsers = users?.size ?: 0

                if (numberOfUsers > 0) {
                    //Navigate to the results activity and send the data if we got back users data
                    val intent = Intent(TheApp.context, ResultsActivity::class.java)
                    intent.putParcelableArrayListExtra(getString(R.string.user_data_key), users)
                    startActivity(intent)
                } else {
                    //Show no results message and disable search button when number of users is zero
                    binding.searchButton.isEnabled = false
                    binding.noResultsMessage.text =
                        getString(R.string.no_results, binding.searchUser.text)
                }

                binding.progressBar.visibility = View.GONE
            }

            // Show toast message when fetch fails
            override fun onFailure(call: Call<ResponseDataClass>, t: Throwable) {
                toast(t.message.toString())
                binding.progressBar.visibility = View.GONE
            }

        })

    }

    // endregion

    // region options menu

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val inflater = MenuInflater(this)
        inflater.inflate(R.menu.main_menu, menu) // Inflate the menu

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.menu_about -> {
                val intent = Intent(this, AboutActivity::class.java) // create an intent
                startActivity(intent) // Navigate to the about activity
                return true
            }

        }

        return super.onOptionsItemSelected(item)
    }

    // endregion

    // region Keyboard - Hide the soft keyboard when no input control has

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {

        currentFocus?.let {
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
        }

        return super.dispatchTouchEvent(ev)
    }

    // endregion

    private fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    // endregion

}