package com.example.sintage

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputType
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.graphics.drawable.toBitmap
import com.example.sintage.ml.LiteModelOnDeviceVisionClassifierPopularWineV11
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.tensorflow.lite.support.image.TensorImage

class UploadActivity : AppCompatActivity() {
    lateinit var imgView: ImageView
    lateinit var button: Button
    lateinit var btm: Bitmap
    lateinit var auth : FirebaseAuth
    val pickImage = 100
    var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload)
        auth = FirebaseAuth.getInstance()
        var db = FirebaseFirestore.getInstance()

        var back = findViewById<Button>(R.id.backHome)
        imgView = findViewById<ImageView>(R.id.imageView)
        button = findViewById<Button>(R.id.upload)
        var pred = findViewById<Button>(R.id.model)

        var test = findViewById<TextView>(R.id.test)

        back.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }

        button.setOnClickListener{
            var gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)

        }
        pred.setOnClickListener{
            //make a bitmat from a image view
            //db.collection("${auth.currentUser?.uid.toString()}_saved").addit
            btm = imgView.getDrawable().toBitmap(224,224)
            var labels = useModel(this, btm)
            var wine  = labels.get(0)
            var correct = true
            var alert = AlertDialog.Builder(this, R.style.MyThemeOverlay_MaterialComponents_MaterialAlertDialog)
            alert.setTitle("Predicted Wine")
            alert.setMessage("We predicted this to be ${wine}. Is this correct?")
            alert.setPositiveButton("Yes", { dialogInterface: DialogInterface, i: Int ->}) //do nothing
            alert.setNegativeButton("No", { dialogInterface: DialogInterface, i: Int ->
                correct = false
                dialogInterface.dismiss()
            })
            alert.setOnDismissListener {
                var editText = EditText(this)
                editText.setText(wine)
                if (!correct){
                    var correctAlert = AlertDialog.Builder(this, R.style.MyThemeOverlay_MaterialComponents_MaterialAlertDialog)
                    correctAlert.setTitle("Edit label")
                    correctAlert.setMessage("Correct your shit")
                    correctAlert.setView(editText)
                    correctAlert.setPositiveButton("Submit", { dialogInterface: DialogInterface, i: Int ->
                        wine = editText.text.toString()

                        //add wine to db here!
                        test.text = wine
                        dialogInterface.dismiss()

                    }) //do nothing
                    correctAlert.setOnDismissListener {
                        var favAlert = AlertDialog.Builder(this, R.style.MyThemeOverlay_MaterialComponents_MaterialAlertDialog)
                        favAlert.setTitle("Favorite?")
                        favAlert.setMessage("Would you like to add ${wine} to your favorites?")
                        favAlert.setPositiveButton("Yes", { dialogInterface: DialogInterface, i: Int ->

                            var oldCount = 0
                            db.collection("${auth.currentUser?.uid.toString()}_scanned").get()
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        for (document in task.result!!){
                                            if (document.data["name"].toString().equals(wine)){
                                                oldCount = document.data["count"].toString().toInt()
                                            }
                                        }
                                    }
                                }


                            var wineItem : MutableMap<String, Any?> = HashMap()
                            wineItem["name"] = wine
                            wineItem["count"] = oldCount+1
                            if(auth.currentUser != null){
                                db.collection("${auth.currentUser?.uid.toString()}_saved").add(wineItem)
                                    .addOnSuccessListener {
                                        Toast.makeText(this, "Wine added to favorites!", Toast.LENGTH_SHORT).show()
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
                                    }

                                db.collection("${auth.currentUser?.uid.toString()}_scanned").add(wineItem)
                                    .addOnSuccessListener {
                                        Toast.makeText(this, "Wine added to Collection", Toast.LENGTH_SHORT).show()
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
                                    }
                            }

                        })
                        favAlert.setNegativeButton("No", { dialogInterface: DialogInterface, i: Int ->

                            var oldCount = 0
                            db.collection("${auth.currentUser?.uid.toString()}_scanned").get()
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        for (document in task.result!!){
                                            if (document.data["name"].toString().equals(wine)){
                                                oldCount = document.data["count"].toString().toInt()
                                            }
                                        }
                                    }
                                }


                            var wineItem : MutableMap<String, Any?> = HashMap()
                            wineItem["name"] = wine
                            wineItem["count"] = oldCount+1
                            db.collection("${auth.currentUser?.uid.toString()}_scanned").add(wineItem)
                                .addOnSuccessListener {
                                    Toast.makeText(this, "Wine added to Collection", Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener {
                                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
                                }
                            correct = false
                            dialogInterface.dismiss()
                        })
                        favAlert.show()
                    }
                    correctAlert.show()
                }else{
                    var favAlert = AlertDialog.Builder(this, R.style.MyThemeOverlay_MaterialComponents_MaterialAlertDialog)
                    favAlert.setTitle("Favorite?")
                    favAlert.setMessage("Would you like to add ${wine} to your favorites?")
                    favAlert.setPositiveButton("Yes", { dialogInterface: DialogInterface, i: Int ->

                        var oldCount = 0
                        db.collection("${auth.currentUser?.uid.toString()}_scanned").get()
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    for (document in task.result!!){
                                        if (document.data["name"].toString().equals(wine)){
                                            oldCount = document.data["count"].toString().toInt()
                                        }
                                    }
                                }
                            }


                        var wineItem : MutableMap<String, Any?> = HashMap()
                        wineItem["name"] = wine
                        wineItem["count"] = oldCount+1
                        if(auth.currentUser != null){
                            db.collection("${auth.currentUser?.uid.toString()}_saved").add(wineItem)
                                .addOnSuccessListener {
                                    Toast.makeText(this, "Wine added to favorites!", Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener {
                                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
                                }

                            db.collection("${auth.currentUser?.uid.toString()}_scanned").add(wineItem)
                                .addOnSuccessListener {
                                    Toast.makeText(this, "Wine added to Collection", Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener {
                                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
                                }
                        }

                    })
                    favAlert.setNegativeButton("No", { dialogInterface: DialogInterface, i: Int ->

                        var oldCount = 0
                        db.collection("${auth.currentUser?.uid.toString()}_scanned").get()
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    for (document in task.result!!){
                                        if (document.data["name"].toString().equals(wine)){
                                            oldCount = document.data["count"].toString().toInt()
                                        }
                                    }
                                }
                            }


                        var wineItem : MutableMap<String, Any?> = HashMap()
                        wineItem["name"] = wine
                        wineItem["count"] = oldCount+1
                        db.collection("${auth.currentUser?.uid.toString()}_scanned").add(wineItem)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Wine added to Collection", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener {
                                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
                            }
                        correct = false
                        dialogInterface.dismiss()
                    })
                    favAlert.show()
                }

            }
            alert.show()
        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== RESULT_OK && requestCode == pickImage){
            imageUri = data?.data
            imgView.setImageURI(imageUri)
        }
    }


    fun useModel(context: Context, bitmap: Bitmap): List<String>{
        val model = LiteModelOnDeviceVisionClassifierPopularWineV11.newInstance(context)

        // Creates inputs for reference.
        val image = TensorImage.fromBitmap(bitmap)

        // Runs model inference and gets result.
        val outputs = model.process(image)
        val probability = outputs.probabilityAsCategoryList

        var sorted = probability.sortedWith(compareBy({it.score}))

        // Releases model resources if no longer used.
        var size = sorted.size
        var labels = arrayListOf<String>(sorted.get(0).label,sorted.get(0).label,sorted.get(0).label)
        model.close()
        return labels
    }
}