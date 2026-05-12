package com.example.hasiruusiru

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    lateinit var db: FirebaseFirestore
    lateinit var treeList: ArrayList<String>
    lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = FirebaseFirestore.getInstance()

        val etSpecies = findViewById<EditText>(R.id.etSpecies)
        val etGirth = findViewById<EditText>(R.id.etGirth)
        val btnAddTree = findViewById<Button>(R.id.btnAddTree)
        val btnEmptyPit = findViewById<Button>(R.id.btnEmptyPit)
        val listView = findViewById<ListView>(R.id.listViewTrees)

        val tvTotalTrees = findViewById<TextView>(R.id.tvTotalTrees)
        val tvTotalOxygen = findViewById<TextView>(R.id.tvTotalOxygen)
        val tvSuggestion = findViewById<TextView>(R.id.tvSuggestion)

        treeList = ArrayList()
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, treeList)
        listView.adapter = adapter

        loadTrees()

        // Add Tree
        btnAddTree.setOnClickListener {

            val name = etSpecies.text.toString().trim()
            val oxygen = etGirth.text.toString().toIntOrNull() ?: 0

            if (name.isEmpty()) {
                Toast.makeText(this, "Enter tree name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 🤖 AI Suggestion
            val suggestion = getTreeSuggestion(name)
            tvSuggestion.text = "🤖 AI Suggestion:\n$suggestion"

            val tree = hashMapOf(
                "name" to name,
                "oxygenScore" to oxygen,
                "location" to "Bangalore"
            )

            db.collection("trees")
                .add(tree)
                .addOnSuccessListener {
                    Toast.makeText(this, "✅ Tree Added Successfully", Toast.LENGTH_SHORT).show()
                    etSpecies.text.clear()
                    etGirth.text.clear()
                    loadTrees()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Error ❌", Toast.LENGTH_SHORT).show()
                }
        }

        // Empty Pit
        btnEmptyPit.setOnClickListener {
            db.collection("pits")
                .add(mapOf("status" to "empty"))
                .addOnSuccessListener {
                    Toast.makeText(this, "Empty pit saved ❌", Toast.LENGTH_SHORT).show()
                }
        }
    }

    // Load Data + Dashboard
    private fun loadTrees() {
        db.collection("trees")
            .get()
            .addOnSuccessListener { result ->

                treeList.clear()

                var totalTrees = 0
                var totalOxygen = 0

                for (document in result) {
                    val name = document.getString("name") ?: "Unknown"
                    val oxygen = document.getLong("oxygenScore")?.toInt() ?: 0

                    treeList.add("🌳 $name\nOxygen: $oxygen")

                    totalTrees++
                    totalOxygen += oxygen
                }

                adapter.notifyDataSetChanged()

                findViewById<TextView>(R.id.tvTotalTrees).text =
                    "🌳 Total Trees: $totalTrees"

                findViewById<TextView>(R.id.tvTotalOxygen).text =
                    "🌱 Total Oxygen: $totalOxygen"
            }
    }

    // 🤖 AI Logic
    private fun getTreeSuggestion(name: String): String {

        return when (name.lowercase()) {

            "neem" -> "🌳 High Oxygen Tree\nBenefits: Medicinal, Air Purifier"
            "peepal" -> "🌳 Very High Oxygen Tree\nBenefits: Releases oxygen 24hrs"
            "honge" -> "🌳 Medium Oxygen Tree\nBenefits: Improves soil fertility"
            "mango" -> "🌳 Fruit Tree\nBenefits: Provides food and shade"
            "coconut" -> "🌳 Tropical Tree\nBenefits: Multi-purpose tree"

            else -> "🌱 Unknown Tree\nSuggestion: Try native trees like Neem or Peepal"
        }
    }
}