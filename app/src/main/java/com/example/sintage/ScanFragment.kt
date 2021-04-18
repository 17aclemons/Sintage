package com.example.sintage

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.sintage.ml.LiteModelOnDeviceVisionClassifierPopularWineV11
import org.tensorflow.lite.support.image.TensorImage
import java.util.jar.Manifest

class ScanFragment : Fragment() {

//    lateinit var imgView: ImageView
//    lateinit var button: Button
//    lateinit var btm: Bitmap
//    val pickImage = 100
//    var imageUri: Uri? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//
//    }
//
////    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
////        super.onActivityResult(requestCode, resultCode, data)
////        if(resultCode== RESULT_OK && requestCode == pickImage){
////            imageUri = data?.data
////            imgView.setImageURI(imageUri)
////        }
////    }
//
//
//    fun useModel(context: Context, bitmap: Bitmap): List<String>{
//        val model = LiteModelOnDeviceVisionClassifierPopularWineV11.newInstance(context)
//
//        // Creates inputs for reference.
//        val image = TensorImage.fromBitmap(bitmap)
//
//        // Runs model inference and gets result.
//        val outputs = model.process(image)
//        val probability = outputs.probabilityAsCategoryList
//
//        var sorted = probability.sortedWith(compareBy({it.score}))
//
//        // Releases model resources if no longer used.
//        var size = sorted.size
//        var labels = arrayListOf<String>(sorted.get(0).label,sorted.get(0).label,sorted.get(0).label)
//        model.close()
//        return labels
//    }
//
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//        val view =  inflater.inflate(R.layout.fragment_scan, container, false)
//        var upload = view.findViewById<Button>(R.id.button)
////        var man = Manifest.
////        upload.setOnClickListener {
////            if(ActivityCompat.checkSelfPermission(container!!.getContext(), Manifest.permission.READ_EXTERNAL_STORAGE))
////        }
//
//
//
////        view.findViewById<Button>(R.id.button).setOnClickListener {
////            var onClick: Unit {
////                if (ActivityCompat.checkSelfPermission(activity,
////                                Manifest.permission.READ_EXTERNAL_STORAGE) !== PackageManager.PERMISSION_GRANTED) {
////                    requestPermissions(arrayOf<String>(Manifest.permission.READ_EXTERNAL_STORAGE),
////                            2000)
////                } else {
////                    startGallary()
////                }
////            }
//        }
//
//
//
////        imgView = view.findViewById<ImageView>(R.id.imageView)
////        button = view.findViewById<Button>(R.id.button)
////        var pred = view.findViewById<Button>(R.id.model)
////        var tf1 = view.findViewById<TextView>(R.id.textView2)
////        var tf2 = view.findViewById<TextView>(R.id.textView3)
////        var tf3 = view.findViewById<TextView>(R.id.textView4)
////
////        button.setOnClickListener{
////            var gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
////            startActivityForResult(gallery, pickImage)
////
////        }
////        pred.setOnClickListener{
////            //make a bitmat from a image view
////            btm = imgView.getDrawable().toBitmap(224,224)
////            var labels = useModel(container!!.getContext(), btm)
////            tf1.text = labels.get(0)
////            tf2.text = labels.get(1)
////            tf3.text = labels.get(2)
////        }
////
////        return view
//
//    }
//
}