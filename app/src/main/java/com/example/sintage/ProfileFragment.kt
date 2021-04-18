package com.example.sintage

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

class ProfileFragment : Fragment() {
    // TODO: Rename and change types of parameter
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    // ↓ here!!!
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        var todaysRand : List<Int>
        var db = FirebaseFirestore.getInstance()


        //list of achievement IDs
        var achievementsIDList = arrayListOf<String>()
//        var randAchievementDoc1 : String = ""
//        var randAchievementDoc2 : String = ""

        db.collection("achievements").get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    for (document in task.result!!){
                        var temp = document.id.toString()
                        achievementsIDList.add(temp)
                    }
                    todaysRand = getRandomList(Random(getDateSeed()), achievementsIDList.size)
//                    randAchievementDoc1 = achievementsIDList[todaysRand[0]]
//                    randAchievementDoc2 = achievementsIDList[todaysRand[1]]
                }
            }


        //var achievement1 = CountAchievement()//TODO: how to pull how many total scanned?

        //achievement 2

//        var achievement2 : RandAchievement
//        val docRef = db.collection("achievements").document(randAchievementDoc1)
//        docRef.get()
//                .addOnSuccessListener { document ->
//                    if (document != null){
//                        achievement2 = RandAchievement(document.id.toString(), document.data!!["goal"].toString(),
//                                document.data!!["key"].toString(), document.data!!["xp"].toString().toInt())
//                    }
//                }
    }

    fun getRandomList(random: Random, size: Int): List<Int> = List(2) {random.nextInt(0, size)}


    class RandAchievement(docID: String, docGoal:String, docKey:String, docXP:Int){
        var id = docID
        var goal = docGoal
        var key = docKey
        var xp = docXP
        var achieved = false
    }

//    class CountAchievement(){
//        var db = FirebaseFirestore.getInstance()
//        var goal : String = "Scan your first wine!"
//        var countGoal = 1
//        var count = 0
//        var xp = 50
//        var achieved = false
//
//        constructor(old: CountAchievement) : this() {
//            this.count = old.count
//            this.xp = 50
//            this.achieved = false
//
//            if(old.countGoal == 1) this.countGoal = 5
//            else this.countGoal = old.countGoal + 5
//
//            goal = "Scan ${countGoal} wines!"
//        }
//    }
//
    fun getDateSeed(): Int {
        var year = Calendar.getInstance().get(Calendar.YEAR)
        var month = Calendar.getInstance().get(Calendar.MONTH)
        var day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

        return (year * 100) + (month * 10) + (day * 1)
    }
}