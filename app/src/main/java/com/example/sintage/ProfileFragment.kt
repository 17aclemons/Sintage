package com.example.sintage

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.firebase.FirebaseError
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneId.from
import java.time.ZoneId.systemDefault
import java.util.*
import kotlin.random.Random

// TODO: Rename parameter arguments, choose names that match

class ProfileFragment : Fragment() {
    lateinit var achievement2 : RandAchievement
    lateinit var achievement3 : RandAchievement

    // ↓ here!!!
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        var testButton = view.findViewById<Button>(R.id.testyboi)
        var userDisplay = view.findViewById<TextView>(R.id.userDisplay)
        var ach1text = view.findViewById<TextView>(R.id.achivement1text)
        var ach2text = view.findViewById<TextView>(R.id.achievement2text)
        var ach3text = view.findViewById<TextView>(R.id.achievement3text)

        testButton.setOnClickListener{
            ach2text.setText("${achievement2.goal}\nXP: ${achievement2.xp}")
            ach3text.setText("${achievement3.goal}\nXP: ${achievement3.xp}")
        }


        return view
    }


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        var todaysRand : List<Int>
        var db = FirebaseFirestore.getInstance()

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

    class CountAchievement(){
        var db = FirebaseFirestore.getInstance()
        var goal : String = "Scan your first wine!"
        var countGoal = 1
        var count = 0
        var xp = 50
        var achieved = false

        constructor(old: CountAchievement) : this() {
            this.count = old.count
            this.xp = 50
            this.achieved = false

            if(old.countGoal == 1) this.countGoal = 5
            else this.countGoal = old.countGoal + 5

            goal = "Scan ${countGoal} wines!"
        }
    }

    fun getDateSeed(): Int {
        var year = Calendar.getInstance().get(Calendar.YEAR)
        var month = Calendar.getInstance().get(Calendar.MONTH)
        var day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

        return (year * 100) + (month * 10) + (day * 1)
    }
}