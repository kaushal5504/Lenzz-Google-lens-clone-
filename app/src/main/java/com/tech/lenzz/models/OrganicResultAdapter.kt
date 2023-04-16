package com.tech.lenzz.models

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.tech.lenzz.R
import com.tech.lenzz.imageLabeler.ImageLabelingActivity

class OrganicResultAdapter(private val activity: ImageLabelingActivity,private var searchRVModel :List<OrganicResult> = ArrayList()): RecyclerView.Adapter<OrganicResultAdapter.ViewHolder>()  {


    private var searchOrganicResult :List<OrganicResult> = ArrayList()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle : TextView = itemView.findViewById(R.id.tvTitle)
        val tvDescription : TextView =itemView.findViewById(R.id.tvDescription)
        val tvSnippet : TextView = itemView.findViewById(R.id.tvSnippet)
        val cardview : CardView = itemView.findViewById(R.id.cardview)


    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrganicResultAdapter.ViewHolder {
        val view:View = LayoutInflater.from(parent.context).inflate(R.layout.search_result_layout,parent,false)
        return OrganicResultAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrganicResultAdapter.ViewHolder, position: Int) {
        var currentSearch : OrganicResult = searchOrganicResult[position]

        searchRVModel.get(position)
        holder.tvTitle.text = currentSearch.title
        holder.tvDescription.text =currentSearch.snippet
        holder.tvSnippet.text =currentSearch.link

        holder.cardview.setOnClickListener {

            val intent = Intent(Intent.ACTION_VIEW)
            intent.setData(Uri.parse(currentSearch.link))
            activity.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
       return searchOrganicResult.size
    }

}