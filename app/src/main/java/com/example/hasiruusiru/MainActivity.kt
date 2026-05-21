package com.example.hasiruusiru

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore

    private lateinit var treeList: ArrayList<String>

    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        // FIREBASE

        db = FirebaseFirestore.getInstance()

        // UI IDS

        val etSpecies =
            findViewById<EditText>(R.id.etSpecies)

        val etGirth =
            findViewById<EditText>(R.id.etGirth)

        val btnAddTree =
            findViewById<Button>(R.id.btnAddTree)

        val btnEmptyPit =
            findViewById<Button>(R.id.btnEmptyPit)

        val btnOpenMap =
            findViewById<Button>(R.id.btnOpenMap)

        val tvSuggestion =
            findViewById<TextView>(R.id.tvSuggestion)

        val listView =
            findViewById<ListView>(R.id.listViewTrees)

        // TREE LIST

        treeList = ArrayList()

        adapter = ArrayAdapter(

            this,

            android.R.layout.simple_list_item_1,

            treeList
        )

        listView.adapter = adapter

        // LOAD TREES

        loadTrees()

        // ADD TREE BUTTON

        btnAddTree.setOnClickListener {

            val species =
                etSpecies.text.toString().trim()

            val girth =
                etGirth.text.toString()
                    .toIntOrNull() ?: 0

            if (species.isEmpty()) {

                Toast.makeText(
                    this,
                    "Please Enter Tree Name",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            // TREE HEALTH

            val health = when {

                girth > 8 ->
                    "Good 🌳"

                girth >= 5 ->
                    "Average 🌱"

                else ->
                    "Poor 🍂"
            }

            // OXYGEN CALCULATION

            val oxygen =
                calculateOxygen(species, girth)

            // AI SUGGESTION

            val suggestion =
                getTreeSuggestion(species)

            tvSuggestion.text =
                "🤖 AI Suggestion:\n$suggestion"

            // FIREBASE DATA

            val tree = hashMapOf(

                "name" to species,

                "girth" to girth,

                "oxygenScore" to oxygen,

                "health" to health
            )

            // SAVE TREE

            db.collection("trees")
                .add(tree)

                .addOnSuccessListener {

                    Toast.makeText(
                        this,
                        "🌳 Tree Added Successfully",
                        Toast.LENGTH_SHORT
                    ).show()

                    etSpecies.text.clear()

                    etGirth.text.clear()

                    loadTrees()
                }

                .addOnFailureListener {

                    Toast.makeText(
                        this,
                        "❌ Failed To Add Tree",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }

        // EMPTY PIT BUTTON

        btnEmptyPit.setOnClickListener {

            val pit = hashMapOf(

                "status" to "empty"
            )

            db.collection("pits")
                .add(pit)

                .addOnSuccessListener {

                    Toast.makeText(
                        this,
                        "❌ Empty Pit Marked",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }

        // OPEN MAP BUTTON

        btnOpenMap.setOnClickListener {

            startActivity(
                Intent(this, MapsActivity::class.java)
            )
        }
    }

    // LOAD TREES FUNCTION

    private fun loadTrees() {

        db.collection("trees")
            .get()

            .addOnSuccessListener { result ->

                // CLEAR OLD LIST

                treeList.clear()

                var totalTrees = 0

                var totalOxygen = 0

                // LOOP THROUGH DATA

                for (document in result) {

                    val name =
                        document.getString("name")
                            ?: "Unknown"

                    val oxygen =
                        document.getLong("oxygenScore")
                            ?.toInt() ?: 0

                    val health =
                        document.getString("health")
                            ?: "Unknown"

                    // ADD TO LIST

                    treeList.add(

                        "🌳 Tree Name : $name\n\n" +

                                "🌱 Oxygen Score : $oxygen\n\n" +

                                "💚 Health Status : $health"
                    )

                    totalTrees++

                    totalOxygen += oxygen
                }

                // REFRESH LISTVIEW

                adapter.notifyDataSetChanged()

                // POPUP

                Toast.makeText(
                    this,
                    "Total Trees Loaded : $totalTrees",
                    Toast.LENGTH_SHORT
                ).show()

                // DASHBOARD

                findViewById<TextView>(
                    R.id.tvTotalTrees
                ).text =
                    "🌳 Total Trees : $totalTrees"

                findViewById<TextView>(
                    R.id.tvTotalOxygen
                ).text =
                    "🌱 Total Oxygen : $totalOxygen"

                // GREEN GAP DETECTION

                if (totalTrees < 5) {

                    findViewById<TextView>(
                        R.id.tvGreenGap
                    ).text =
                        "⚠️ Green Gap Detected - Plant More Trees"

                } else {

                    findViewById<TextView>(
                        R.id.tvGreenGap
                    ).text =
                        "✅ Good Green Coverage Area"
                }
            }

            .addOnFailureListener {

                Toast.makeText(
                    this,
                    "❌ Failed To Load Trees",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    // OXYGEN FORMULA

    private fun calculateOxygen(
        species: String,
        girth: Int
    ): Int {

        val factor = when (species.lowercase()) {

            "neem" -> 5

            "peepal" -> 7

            "honge" -> 4

            "mango" -> 3

            "coconut" -> 2

            else -> 1
        }

        return girth * factor
    }

    // AI TREE SUGGESTION

    private fun getTreeSuggestion(
        species: String
    ): String {

        return when (species.lowercase()) {

            "neem" ->
                "Neem is a medicinal native tree with high oxygen production."

            "peepal" ->
                "Peepal tree provides very high oxygen and shade."

            "honge" ->
                "Honge tree improves soil fertility."

            "mango" ->
                "Mango tree gives fruits and environmental benefits."

            "coconut" ->
                "Coconut tree is useful in tropical climate."

            else ->
                "Try planting native trees like Neem or Honge."
        }
    }
}