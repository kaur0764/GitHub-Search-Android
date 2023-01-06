package jasreet.mad9132.jasreet_final_project

/*
* Created by Jasreet Kaur on November 24, 2022
*/

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import jasreet.mad9132.jasreet_final_project.databinding.ActivityAboutBinding

class AboutActivity : AppCompatActivity() {

    // region Properties

    private lateinit var binding: ActivityAboutBinding

    // endregion

    // region Methods

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    //endregion

}