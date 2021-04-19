package com.example.sintage

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import kotlin.collections.HashMap
import kotlin.random.Random

// TODO: Rename parameter arguments, choose names that match

class ProfileFragment : Fragment() {
    lateinit var achievement1 : CountAchievement
    lateinit var achievement2 : RandAchievement
    lateinit var achievement3 : RandAchievement
    lateinit var auth: FirebaseAuth
    lateinit var userProg : UserProgress

    // ↓ here!!!
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        auth = FirebaseAuth.getInstance()
        var startButton = view.findViewById<Button>(R.id.viewAchievements)
        var logOutButton = view.findViewById<Button>(R.id.logOut)
        var levelDisplay = view.findViewById<TextView>(R.id.levelDisplay)
        var ach1text = view.findViewById<TextView>(R.id.achievement1text)
        var ach2text = view.findViewById<TextView>(R.id.achievement2text)
        var ach3text = view.findViewById<TextView>(R.id.achievement3text)
        var ach1image = view.findViewById<ImageView>(R.id.ach1image)
        var ach2image = view.findViewById<ImageView>(R.id.ach2image)
        var ach3image = view.findViewById<ImageView>(R.id.ach3image)
        var ach1icon = view.findViewById<ImageView>(R.id.wineIcon1)
        var ach2icon = view.findViewById<ImageView>(R.id.wineIcon2)
        var ach3icon = view.findViewById<ImageView>(R.id.wineIcon3)
        var progBar = view.findViewById<ProgressBar>(R.id.achProgressBar)

        levelDisplay.setText("Achievements")

        ach1image.setVisibility(View.INVISIBLE)
        ach2image.setVisibility(View.INVISIBLE)
        ach3image.setVisibility(View.INVISIBLE)
        ach1icon.setVisibility(View.INVISIBLE)
        ach2icon.setVisibility(View.INVISIBLE)
        ach3icon.setVisibility(View.INVISIBLE)

        logOutButton.setOnClickListener {
            auth.signOut()
            startActivity(Intent(container?.context, SigninActivity::class.java))
            //TODO: why is text not black??
//            var alert = AlertDialog.Builder(container?.context, R.style.MyThemeOverlay_MaterialComponents_MaterialAlertDialog)
//            alert.setTitle("Logging out")
//            alert.setMessage("You are about to log out. Are you sure?")
//            alert.setPositiveButton("Yes", { dialogInterface: DialogInterface, i: Int ->
//                auth.signOut()
//            startActivity(Intent(container?.context, SigninActivity::class.java))
//
//            })
//            alert.setNegativeButton("No", { dialogInterface: DialogInterface, i: Int -> })
//            alert.show()
        }

        startButton.setOnClickListener {
            progBar.progress = userProg.currentXP
            levelDisplay.setText("Level: ${userProg.currentLevel.toString()}")
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
            logOutButton.setVisibility(View.INVISIBLE)
        }

        return view
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        auth = FirebaseAuth.getInstance()
        super.onCreate(savedInstanceState)
        var db = FirebaseFirestore.getInstance()

        if(auth.currentUser != null){
            db.collection("users_xp").get().addOnCompleteListener { task ->
                if(task.isSuccessful){
                    for (document in task.result!!){
                        if(auth.currentUser?.uid.toString().equals(document.data["UID"].toString())){
                            userProg = UserProgress(document.data["UID"].toString(), document.data["uXP"].toString().toInt())
                        }
                    }
                }
            }
        }


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
                        userScannedCount += document.data!!["count"].toString().toInt()
                    }
                    achievement1 = CountAchievement(userScannedCount, userProg)
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

    class CountAchievement(old: Int, userProg: UserProgress){
        var goal : String = ""
        var nextGoal : Int = 1
        var scanned : Int = 0
        var xp : Int = 50
        var achieved : Boolean = false

        init {
            xp = 50
            achieved = false
            scanned = old
            nextGoal = old

            if(nextGoal == 0){
                nextGoal = 1
                goal = "Scan your first wine!"
            }else{
                if(nextGoal == 1){
                    nextGoal = 5
                    userProg.add(xp)
                }
                while (nextGoal%5 != 0){
                    nextGoal++
                }
                if(scanned == nextGoal){
                    nextGoal = nextGoal+5
                    userProg.add(xp)
                }

                goal = "Scan ${nextGoal} wines. Current: ${scanned}"
            }
                //TODO:NIKKI
        }
    }

    class UserProgress(o_uid: String, cXP: Int){

        var uid : String = ""
        var db = FirebaseFirestore.getInstance()
        var totalXP : Int = 1
        var currentXP : Int = 1
        var currentLevel : Int = 1

        init{
            uid = o_uid

            totalXP = cXP
            currentXP = cXP
            currentLevel = 1

            while(currentXP > 200){
                currentLevel++
                currentXP = currentXP - 200
            }
        }

        fun add(xp: Int){
            totalXP += xp

            var userXP : MutableMap<String, Any?> = HashMap()
            userXP["UID"] = uid
            userXP["uXP"] = totalXP
            db.collection("users").document(uid).set(userXP)

            currentXP = currentXP + xp
            if(currentXP > 200){
                currentLevel++
                currentXP = currentXP - 200
            }
        }

        fun calculateXPfromRandAchievements(scanned: Int): Int{
            var countAchievementsEarned = scanned%5 + 1
            var xpFromCountAchievements = countAchievementsEarned * 50

            var xPfromRand = currentXP - xpFromCountAchievements
            return xPfromRand
        }

    }



    fun getDateSeed(): Int {
        var year = Calendar.getInstance().get(Calendar.YEAR)
        var month = Calendar.getInstance().get(Calendar.MONTH)
        var day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

        return (year * 100) + (month * 10) + (day * 1)
    }
}