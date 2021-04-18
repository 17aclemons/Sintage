package com.example.sintage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CollectionFragment : Fragment() {
lateinit var auth: FirebaseAuth
lateinit var favoriteWineList : WineList
lateinit var collectionWineList : WineList
lateinit var listAdapter : ArrayAdapter<String>


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        //val view = inflater.inflate(R.layout.fragment_collection, container, false)
        val view = LayoutInflater.from(container?.context).inflate(R.layout.fragment_collection, container, false)
        var favoritesBtn = view.findViewById<Button>(R.id.favoritesButton) //saved
        var collectionBtn = view.findViewById<Button>(R.id.collectionButton) //all scanned
        var list = view.findViewById<ListView>(R.id.listViewer)



        favoritesBtn.setOnClickListener {
            listAdapter = ArrayAdapter(container!!.getContext(), android.R.layout.simple_list_item_1, favoriteWineList.list)
            list.adapter = listAdapter

        }

        collectionBtn.setOnClickListener {
            listAdapter = ArrayAdapter(container!!.getContext(), android.R.layout.simple_list_item_1, favoriteWineList.list)
            list.adapter = listAdapter
        }

        return view
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        collectionWineList = WineList()
        favoriteWineList = WineList()

        if (auth.currentUser != null) {
            db.collection("${auth.currentUser?.uid.toString()}_saved").get()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            for (document in task.result!!) {
                                favoriteWineList.add(document.data["name"].toString())
                            }
                        }

                    }

            db.collection("${auth.currentUser?.uid.toString()}_scanned").get()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            for (document in task.result!!) {
                                collectionWineList.add(document.data["name"].toString())
                            }
                        }

                    }
        }
    }

    //TODO: Implement count
    class WineList(){
        var list = arrayListOf<String>()
//        var count = arrayListOf<Int>()
        fun add(wine: String){
            list.add(wine)
        }

        fun get(index: Int): String{
            return list.get(index)
        }
    }
}