package com.example.project1
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SourceAdapter (private val sources: List<SourcesData>): RecyclerView.Adapter<SourceAdapter.ViewHolder>() {

    private lateinit var mListener: onItemClickListener
    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }

    class ViewHolder (rootLayout: View, listener: onItemClickListener): RecyclerView.ViewHolder(rootLayout){
        val sourceName: TextView = rootLayout.findViewById(R.id.sourceName)
        val sourceDescription: TextView = rootLayout.findViewById(R.id.sourceDescription)

//        val articleTitle: TextView = rootLayout.findViewById(R.id.articleTitle)
//        val articleContent: TextView = rootLayout.findViewById(R.id.articleContent)
//        val articleImage: TextView = rootLayout.findViewById(R.id.articleImage)


        init {
            rootLayout.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.d("RV", "Inside onCreateViewHolder")
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val rootLayout: View = layoutInflater.inflate(R.layout.cardviewlayout, parent, false)
        val viewHolder = ViewHolder(rootLayout, mListener)
        return viewHolder
    }

    override fun getItemCount(): Int {
        Log.d("RV", "Inside getItemCount()")
        return sources.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val  currentSource = sources[position]
        holder.sourceName.text = currentSource.sourceName
        holder.sourceDescription.text = currentSource.sourceDescription
//        holder.articleTitle.text = currentSource.articleTitle
//        holder.articleContent.text = currentSource.articleContent
//        holder.articleImage.image = currentSource.articleImage


        //  holder.icon.text

        Log.d("RV", "inside onBindViewHolder at position $position")
    }
}
