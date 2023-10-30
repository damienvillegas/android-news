package com.example.project1

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TopHeadlinesActivity : AppCompatActivity() {
    private lateinit var backButton: Button
    private lateinit var prevButton: Button
    private lateinit var nextButton: Button
    private lateinit var topHeadlineSpinner: Spinner
    private lateinit var recyclerView: RecyclerView
    private lateinit var pageText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_top_headlines)

        var pageNumber = 1

        // set actionbar to correct title
        val actionBar = supportActionBar
        actionBar!!.title = "Current Top Headlines"

        pageText = findViewById(R.id.pageText)

        val sharedPrefs = getSharedPreferences("savedStuff", MODE_PRIVATE)
        val sharedCategory = sharedPrefs.getString("TOP_CATEGORY","").toString()
        val sharedPos = sharedPrefs.getInt("TOP_POS", 0)
        Log.d("SHARED", "$sharedCategory")

        prevButton = findViewById(R.id.prevButton)
        prevButton.setOnClickListener {
            if (pageNumber != 1) pageNumber--
            val currentCategory = topHeadlineSpinner.selectedItem.toString()
            recyclerView = findViewById(R.id.topHeadlineRecycler)
            val sourceManager = SourceManager()
            val apiKey = getString(R.string.news_api_key)
            var topHeadlines = listOf<SourcesData>()
            CoroutineScope(Dispatchers.IO).launch {
                topHeadlines = sourceManager.retrieveTopHeadlines(currentCategory, pageNumber, apiKey)
                withContext(Dispatchers.Main) {
                    val adapter = ArticleAdapter(topHeadlines)
                    recyclerView.adapter = adapter
                    adapter.setOnItemClickListener(object : ArticleAdapter.onItemClickListener {
                        override fun onItemClick(position: Int) {
                            val browserIntent = Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse(topHeadlines[position].articleURL)
                            )
                            startActivity(browserIntent)
                        }
                    })
                    recyclerView.layoutManager = LinearLayoutManager(this@TopHeadlinesActivity)
                }
            }
            pageText.text = "$pageNumber/5"
        }

        nextButton = findViewById(R.id.nextButton)
        nextButton.setOnClickListener {
            if (pageNumber != 5) pageNumber++
            val currentCategory = topHeadlineSpinner.selectedItem.toString()
            recyclerView = findViewById(R.id.topHeadlineRecycler)
            val sourceManager = SourceManager()
            val apiKey = getString(R.string.news_api_key)
            var topHeadlines = listOf<SourcesData>()
            CoroutineScope(Dispatchers.IO).launch {
                topHeadlines = sourceManager.retrieveTopHeadlines(currentCategory, pageNumber, apiKey)
                withContext(Dispatchers.Main) {
                    val adapter = ArticleAdapter(topHeadlines)
                    recyclerView.adapter = adapter
                    adapter.setOnItemClickListener(object : ArticleAdapter.onItemClickListener {
                        override fun onItemClick(position: Int) {
                            val browserIntent = Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse(topHeadlines[position].articleURL)
                            )
                            startActivity(browserIntent)
                        }
                    })
                    recyclerView.layoutManager = LinearLayoutManager(this@TopHeadlinesActivity)
                }
            }
            pageText.text = "$pageNumber/5"
        }

        backButton = findViewById(R.id.backButtonHeadlines)
        backButton.setOnClickListener {
            val intent = Intent(this@TopHeadlinesActivity, MainActivity::class.java)
            startActivity(intent)
        }

        // spinner
        val categories = resources.getStringArray(R.array.categories)
        topHeadlineSpinner = findViewById(R.id.topHeadlineSpinner)
        val sourceSpinnerAdapter = ArrayAdapter(
            this, android.R.layout.simple_spinner_dropdown_item,
            categories
        )


        topHeadlineSpinner.adapter = sourceSpinnerAdapter
        topHeadlineSpinner.setSelection(sharedPos)
        topHeadlineSpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                pageNumber = 1
                pageText.text = "$pageNumber/5"
                recyclerView = findViewById(R.id.topHeadlineRecycler)
                val sourceManager = SourceManager()
                val apiKey = getString(R.string.news_api_key)
                var topHeadlines = listOf<SourcesData>()
                val category = categories[position]
                sharedPrefs.edit().putString("TOP_CATEGORY", category).apply()
                sharedPrefs.edit().putInt("TOP_POS", position).apply()
                CoroutineScope(Dispatchers.IO).launch {
                    topHeadlines = sourceManager.retrieveTopHeadlines(category, pageNumber, apiKey)
                    withContext(Dispatchers.Main) {
                        val adapter = ArticleAdapter(topHeadlines)
                        recyclerView.adapter = adapter
                        adapter.setOnItemClickListener(object : ArticleAdapter.onItemClickListener {
                            override fun onItemClick(position: Int) {
                                val browserIntent = Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse(topHeadlines[position].articleURL)
                                )
                                startActivity(browserIntent)
                            }
                        })
                        recyclerView.layoutManager = LinearLayoutManager(this@TopHeadlinesActivity)
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
//                 set up recyclerview
                recyclerView = findViewById(R.id.topHeadlineRecycler)
                val sourceManager = SourceManager()
                val apiKey = getString(R.string.news_api_key)
                var topHeadlines = listOf<SourcesData>()
                CoroutineScope(Dispatchers.IO).launch {
                    topHeadlines = sourceManager.retrieveTopHeadlines(sharedCategory, pageNumber, apiKey)
                    withContext(Dispatchers.Main) {
                        val adapter = ArticleAdapter(topHeadlines)
                        recyclerView.adapter = adapter
                        adapter.setOnItemClickListener(object : ArticleAdapter.onItemClickListener {
                            override fun onItemClick(position: Int) {
                                val browserIntent = Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse(topHeadlines[position].articleURL)
                                )
                                startActivity(browserIntent)
                            }
                        })
                        recyclerView.layoutManager = LinearLayoutManager(this@TopHeadlinesActivity)
                    }
                }
            }
        }
    }
}