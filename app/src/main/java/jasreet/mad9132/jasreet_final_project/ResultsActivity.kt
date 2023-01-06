package jasreet.mad9132.jasreet_final_project

/*
* Created by Jasreet Kaur on November 24, 2022
*/

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import jasreet.mad9132.jasreet_final_project.databinding.ActivityResultsBinding

class ResultsActivity : AppCompatActivity() {

    // region Properties

    private lateinit var binding: ActivityResultsBinding

    // endregion

    // region Methods

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResultsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data: ArrayList<Users>?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            data = intent.getParcelableArrayListExtra(
                getString(R.string.user_data_key),
                Users::class.java
            )
        } else {
            @Suppress("DEPRECATION")
            data = intent.getParcelableArrayListExtra(getString(R.string.user_data_key))
        }

        supportActionBar?.title = "${data?.size} Results" // Change the action bar title

        // Define how data will be displayed in recycler view
        binding.recyclerViewMain.layoutManager = LinearLayoutManager(this)
        // Set up recycler view adapter
        binding.recyclerViewMain.adapter = data?.let { CustomViewHolderClass.MainAdapter(it) }
    }

    // endregion

}