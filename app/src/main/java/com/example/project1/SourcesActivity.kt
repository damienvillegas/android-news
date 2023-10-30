package com.example.project1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

class SourcesActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var backButton: Button
    private lateinit var sourceSkipButton: Button
    private lateinit var sourceSpinner: Spinner
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sources)

//        // set up recyclerview
//        recyclerView = findViewById(R.id.sourceRecyclerView)
//        val sources = getData()
//        val adapter = SourceAdapter(sources)
//        recyclerView.adapter = adapter
//        recyclerView.layoutManager = LinearLayoutManager(this)

        // read in pair-value pair
        val searchTerm = intent.getStringExtra("searchTerm").toString()
        // set actionbar to correct title
        val actionBar = supportActionBar
        actionBar!!.title = "Search for: '$searchTerm'"

        // back button
        backButton = findViewById(R.id.backButtonSource)
        backButton.setOnClickListener {
            val intent = Intent(this@SourcesActivity, MainActivity::class.java)
            startActivity(intent)
        }

        // skip button
        sourceSkipButton = findViewById(R.id.sourceSkipButton)
        sourceSkipButton.setOnClickListener {
            val intent = Intent(this@SourcesActivity, ResultsActivity::class.java)
            intent.putExtra("searchTerm",searchTerm)
            intent.putExtra("sourceName", "Results for")
            intent.putExtra("id", "")
            intent.putExtra("skip", null.toString())
            startActivity(intent)
        }

        // spinner
        val categories = resources.getStringArray(R.array.categories)
        sourceSpinner = findViewById(R.id.sourceSpinner)
        val sourceSpinnerAdapter = ArrayAdapter(
            this, android.R.layout.simple_spinner_dropdown_item,
            categories
        )
        sourceSpinner.adapter = sourceSpinnerAdapter
        sourceSpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                // set up recyclerview
                recyclerView = findViewById(R.id.sourceRecyclerView)
                val sourceManager = SourceManager()
                val currCategory = categories[position]
                val apiKey = getString(R.string.news_api_key)
                var sources = listOf<SourcesData>()

                // co-routine
                CoroutineScope(IO).launch{
                    sources = sourceManager.retrieveSources(currCategory, apiKey)

                    withContext(Main){
                        val adapter = SourceAdapter(sources)
                        recyclerView.adapter = adapter
                        adapter.setOnItemClickListener(object: SourceAdapter.onItemClickListener{
                            override fun onItemClick(position: Int) {
                                val intent = Intent(this@SourcesActivity, ResultsActivity::class.java)
                                intent.putExtra("searchTerm",searchTerm)
                                intent.putExtra("sourceName",sources[position].sourceName)
                                intent.putExtra("id", sources[position].id)
                                Log.d("OUR ID", sources[position].id)
                                intent.putExtra("skip", "skip")
                                startActivity(intent)
                            }

                        })
                        
                        recyclerView.layoutManager = LinearLayoutManager(this@SourcesActivity)
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                // nothing will ever be not selected
            }
        }
    }
}
