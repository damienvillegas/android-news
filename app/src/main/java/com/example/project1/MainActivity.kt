package com.example.project1

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    private lateinit var searchText: TextView
    private lateinit var searchEditText: EditText
    private lateinit var searchButton: Button
    private lateinit var findNewsByLocationText: TextView
    private lateinit var viewMapButton: Button
    private lateinit var topHeadlinesText: TextView
    private lateinit var topHeadlinesButton: Button


    // allow search button to be seen
    private val textWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            Log.d("MainActivity", "inside beforeTextChange $p0")
            if (!searchEditText.text.isBlank()) {
                searchButton.setBackgroundColor(Color.BLACK)
                searchButton.isEnabled = true
            } else {
                searchButton.setBackgroundColor(Color.parseColor("#AFAEAE"))
                searchButton.isEnabled = false
            }
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            Log.d("MainActivity", "inside onTextChanged $p0")
            if (!searchEditText.text.isBlank()) {
                searchButton.setBackgroundColor(Color.BLACK)
                searchButton.isEnabled = true
            } else {
                searchButton.setBackgroundColor(Color.parseColor("#AFAEAE"))
                searchButton.isEnabled = false
            }
        }

        override fun afterTextChanged(p0: Editable?) {
            Log.d("MainActivity", "inside afterTextChanged $p0")
            // shared pref
            val sharedPrefs = getSharedPreferences("savedStuff", MODE_PRIVATE)
            val searchTerm: String = searchEditText.text.toString()

            sharedPrefs.edit().putString("SEARCH_TERM", searchTerm).apply()

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // set actionbar to correct title
        val actionBar = supportActionBar
        actionBar!!.title = "Android News"

        // get items
        searchText = findViewById(R.id.searchText)
        searchEditText = findViewById(R.id.searchEditText)
        searchButton = findViewById(R.id.searchButton)
        findNewsByLocationText = findViewById(R.id.findNewsByLocationText)
        viewMapButton = findViewById(R.id.viewMapButton)
        topHeadlinesText = findViewById(R.id.topHeadlinesText)
        topHeadlinesButton = findViewById(R.id.topHeadlinesButton)

        // set buttons black
        searchButton.setBackgroundColor(Color.BLACK)
        viewMapButton.setBackgroundColor(Color.BLACK)
        topHeadlinesButton.setBackgroundColor(Color.BLACK)


        // textwatcher for search edit text
        val sharedPrefs = getSharedPreferences("savedStuff", MODE_PRIVATE)
        searchEditText.addTextChangedListener(textWatcher)

        val sharedSearchTerm = sharedPrefs.getString("SEARCH_TERM","")
        searchEditText.setText(sharedSearchTerm)

        // intents
        searchButton.setOnClickListener {
            val intent = Intent(this@MainActivity, SourcesActivity::class.java)
            intent.putExtra("searchTerm",searchEditText.text.toString())
            startActivity(intent)
        }

        viewMapButton.setOnClickListener {
            val intent = Intent(this@MainActivity, MapsActivity::class.java)
            startActivity(intent)
        }

        topHeadlinesButton.setOnClickListener {
            val intent = Intent(this@MainActivity, TopHeadlinesActivity::class.java)
            startActivity(intent)
        }
    }

}