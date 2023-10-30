package com.example.project1

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ResultsActivity : AppCompatActivity() {
    private lateinit var backButton: Button
    private lateinit var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)

        // read in pair-value pair
        val searchTerm = intent.getStringExtra("searchTerm").toString()
        val sourceName = intent.getStringExtra("sourceName").toString()
        val skip = intent.getStringExtra("skip").toString()
        val id = intent.getStringExtra("id").toString()
        // set actionbar to correct title
        val actionBar = supportActionBar
        actionBar!!.title = "$sourceName: '$searchTerm'"

        backButton = findViewById(R.id.backButtonResults)
        backButton.setOnClickListener {
            val intent = Intent(this@ResultsActivity, SourcesActivity::class.java)
            intent.putExtra("searchTerm", searchTerm)
            startActivity(intent)
        }

        recyclerView = findViewById(R.id.resultRecycler)
        val sourceManager = SourceManager()
        val apiKey = getString(R.string.news_api_key)
        var sources = listOf<SourcesData>()
        Log.d("RA", sourceName.toString());
        CoroutineScope(Dispatchers.IO).launch{
            if (skip.isNullOrEmpty()){
                sources = sourceManager.retrieveArticles(searchTerm, apiKey)
            } else {
                sources = sourceManager.retrieveArticlesWithSource(searchTerm, id, apiKey)
            }
            withContext(Dispatchers.Main){
                val adapter = ArticleAdapter(sources)
                recyclerView.adapter = adapter
                adapter.setOnItemClickListener(object: ArticleAdapter.onItemClickListener{
                    override fun onItemClick(position: Int) {
                        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(sources[position].articleURL))
                        startActivity(browserIntent)
                    }
                })
                recyclerView.layoutManager = LinearLayoutManager(this@ResultsActivity)
            }
        }
    }
}