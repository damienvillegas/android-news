package com.example.project1

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class ArticleAdapter (private val sources: List<SourcesData>): RecyclerView.Adapter<ArticleAdapter.ViewHolder>() {

    private lateinit var mListener: onItemClickListener
    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }

    class ViewHolder (rootLayout: View, listener: onItemClickListener): RecyclerView.ViewHolder(rootLayout){
//        val sourceName: TextView = rootLayout.findViewById(R.id.sourceName)
//        val sourceDescription: TextView = rootLayout.findViewById(R.id.sourceDescription)

        val articleTitle: TextView = rootLayout.findViewById(R.id.articleTitle)
        val articleContent: TextView = rootLayout.findViewById(R.id.articleContent)
        val articleSource: TextView = rootLayout.findViewById(R.id.articleSource)
        val articleImage: ImageView = rootLayout.findViewById(R.id.articleIcon)
        val cardView: CardView = rootLayout.findViewById(R.id.cardView)


        init {
            rootLayout.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.d("RV", "Inside onCreateViewHolder")
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val rootLayout: View = layoutInflater.inflate(R.layout.articlelayout, parent, false)
        val viewHolder = ViewHolder(rootLayout, mListener)
        return viewHolder
    }

    override fun getItemCount(): Int {
        Log.d("RV", "Inside getItemCount()")
        return sources.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val  currentSource = sources[position]
        holder.articleTitle.text = currentSource.articleTitle
        holder.articleContent.text = currentSource.articleContent
        holder.articleSource.text = currentSource.sourceName
        if (currentSource.articleImage.isNotEmpty()){
            Picasso.get().setIndicatorsEnabled(true)
            Picasso.get().load(currentSource.articleImage).into(holder.articleImage)
        }

        val url = currentSource.articleURL
        val articleListeningContext = holder.cardView.context
        holder.cardView.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            articleListeningContext.startActivity(intent)
        }

        Log.d("RV", "inside onBindViewHolder at position $position")
    }
}
