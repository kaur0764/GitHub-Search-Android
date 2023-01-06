package jasreet.mad9132.jasreet_final_project

/*
* Created by Jasreet Kaur on November 24, 2022
*/

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import jasreet.mad9132.jasreet_final_project.databinding.DataRowBinding

class CustomViewHolderClass(
    private val view: View,
    var login: String = "",
    var user: Users? = null  // a single Users object from ResponseDataClass/Users classes
) : RecyclerView.ViewHolder(view) {

    // region Properties

    val binding = DataRowBinding.bind(view)

    // endregion

//    An Android RecyclerView control require an Adapter and a View Holder

    // region Nested Class

    //an Adapter handles the data to be displayed
    class MainAdapter(private val dataSource: ArrayList<Users>) :
        RecyclerView.Adapter<CustomViewHolderClass>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolderClass {
            // A LayoutInflater reads an XML file in which we describe how we want a UI layout to be.
            // It then creates actual View objects for UI from that XML.
            val layoutInflater = LayoutInflater.from(parent.context)
            val cellForRow = layoutInflater.inflate(R.layout.data_row, parent, false)
            return CustomViewHolderClass(cellForRow)
        }

        override fun onBindViewHolder(holder: CustomViewHolderClass, position: Int) {
            //View Holder is the layout/UI for the data
            holder.binding.loginTextView.text =
                TheApp.context.getString(R.string.user_login, dataSource[position].login)
            holder.binding.scoreTextView.text = TheApp.context.getString(
                R.string.user_score, (dataSource[position].score + 0.5).toInt().toString()
            )
            holder.binding.idTextView.text =
                TheApp.context.getString(R.string.user_id, dataSource[position].id.toString())

            Picasso.get().load(dataSource[position].avatar_url).into(holder.binding.imageView)

            holder.user = dataSource[position]

            holder.login = dataSource[position].login
        }

        //return the list size using Kotlin shorthand
        override fun getItemCount(): Int = dataSource.size
    }

    // endregion

    // region Methods

    init {
        view.setOnClickListener {
            //Add click listener to the CustomViewHolderClass
            val intent = Intent(view.context, DetailsActivity::class.java)

            intent.putExtra(view.context.getString(R.string.details_title_key), login)

            intent.putExtra(view.context.getString(R.string.details_url_key), user?.url)

            intent.putExtra(view.context.getString(R.string.details_html_url_key), user?.html_url)

            view.context.startActivity(intent)
        }
    }

    // endregion

}