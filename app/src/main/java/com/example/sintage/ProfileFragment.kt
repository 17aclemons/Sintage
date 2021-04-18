package com.example.sintage

import android.media.Image
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import com.google.firebase.FirebaseError
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneId.from
import java.time.ZoneId.systemDefault
import java.util.*
import kotlin.random.Random

// TODO: Rename parameter arguments, choose names that match

class ProfileFragment : Fragment() {
    lateinit var achievement1 : CountAchievement
    lateinit var achievement2 : RandAchievement
    lateinit var achievement3 : RandAchievement
    lateinit var auth: FirebaseAuth

    // ↓ here!!!
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        var startButton = view.findViewById<Button>(R.id.testyboi)
        var userDisplay = view.findViewById<TextView>(R.id.userDisplay)
        var ach1text = view.findViewById<TextView>(R.id.achievement1text)
        var ach2text = view.findViewById<TextView>(R.id.achievement2text)
        var ach3text = view.findViewById<TextView>(R.id.achievement3text)
        var ach1image = view.findViewById<ImageView>(R.id.ach1image)
        var ach2image = view.findViewById<ImageView>(R.id.ach2image)
        var ach3image = view.findViewById<ImageView>(R.id.ach3image)
        var ach1icon = view.findViewById<ImageView>(R.id.wineIcon1)
        var ach2icon = view.findViewById<ImageView>(R.id.wineIcon2)
        var ach3icon = view.findViewById<ImageView>(R.id.wineIcon3)


        ach1image.setVisibility(View.INVISIBLE)
        ach2image.setVisibility(View.INVISIBLE)
        ach3image.setVisibility(View.INVISIBLE)
        ach1icon.setVisibility(View.INVISIBLE)
        ach2icon.setVisibility(View.INVISIBLE)
        ach3icon.setVisibility(View.INVISIBLE)

        startButton.setOnClickListener {
            ach1image.setVisibility(View.VISIBLE)
            ach2image.setVisibility(View.VISIBLE)
            ach3image.setVisibility(View.VISIBLE)

            ach1icon.setVisibility(View.VISIBLE)
            ach2icon.setVisibility(View.VISIBLE)
            ach3icon.setVisibility(View.VISIBLE)


            ach1text.setText("${achievement1.goal}\nXP:${achievement1.xp}")
            ach2text.setText("${achievement2.goal}\nXP:${achievement2.xp}")
            ach3text.setText("${achievement3.goal}\nXP:${achievement3.xp}")

            startButton.setVisibility(View.INVISIBLE)

        }

        return view
    }


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        var db = FirebaseFirestore.getInstance()
        var todaysRand : List<Int>

        //list of achievement IDs
        var achievementsIDList = arrayListOf<String>()
        var randAchievementDoc2 : String = ""
        var randAchievementDoc3 : String = ""

        // ↓ Andrew's main function here!!!
        db.collection("achievements").get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    for (document in task.result!!){
                        var temp = document.id.toString()
                        achievementsIDList.add(temp)
                    }
                    todaysRand = getRandomList(Random(getDateSeed()), achievementsIDList.size)
                    randAchievementDoc2 = achievementsIDList[todaysRand[0]]
                    randAchievementDoc3 = achievementsIDList[todaysRand[1]]

                    for (document in task.result!!){
                        if(document.id.toString().equals(randAchievementDoc2)){
                            achievement2 = RandAchievement(document.id.toString(), document.data!!["goal"].toString(),
                                document.data!!["key"].toString(), document.data!!["xp"].toString().toInt())
                        }else if(document.id.toString().equals(randAchievementDoc3)){
                            achievement3 = RandAchievement(document.id.toString(), document.data!!["goal"].toString(),
                                document.data!!["key"].toString(), document.data!!["xp"].toString().toInt())
                        }
                    }
                }
            }
        //asdflkjj43j32lkfjswelkafd_scanned
        //asdflkjj43j32lkfjswelkafd_saved
        db.collection("${auth.currentUser?.uid.toString()}_scanned").get()
            .addOnCompleteListener { task ->
                var userScannedCount = 0
                if (task.isSuccessful){
                    for (document in task.result!!){
                        userScannedCount += document.data!!["scannedCount"].toString().toInt()
                    }
                    achievement1 = CountAchievement(userScannedCount)
                }
            }

        //var achievement1 = CountAchievement()//TODO: how to pull how many total scanned?
    }

    fun getRandomList(random: Random, size: Int): List<Int> = List(2) {random.nextInt(0, size)}

    class RandAchievement(docID: String, docGoal:String, docKey:String, docXP:Int){
        var id = docID
        var goal = docGoal
        var key = docKey
        var xp = docXP
        var achieved = false
    }

    class CountAchievement(old: Int){
        var goal : String = ""
        var nextGoal : Int = 1
        var scanned : Int = 0
        var xp : Int = 50
        var achieved : Boolean = false

        init {
            scanned = old
            nextGoal = old
            while (nextGoal%5 != 0){
                nextGoal++
            }
            if (nextGoal == 0) {
                nextGoal = 1
                goal = "Scan your first wine!"
            }else{
                goal = "Scan ${nextGoal} wines"
            }
            xp = 50
            achieved = false
        }
    }

    fun getDateSeed(): Int {
        var year = Calendar.getInstance().get(Calendar.YEAR)
        var month = Calendar.getInstance().get(Calendar.MONTH)
        var day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

        return (year * 100) + (month * 10) + (day * 1)
    }
}