package com.example.findmyphoto

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import com.example.findmyphoto.databinding.ActivityMainBinding
import java.lang.Thread.sleep

class MainActivity : AppCompatActivity() {

    lateinit var binding : ActivityMainBinding
    lateinit var returns_img : Array<FloatArray>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imgList = arrayListOf(
            R.drawable.img_1,
            R.drawable.img_2,
            R.drawable.img_3,
            R.drawable.img_4,
            R.drawable.img_5,
            R.drawable.img_6,
            R.drawable.img_7,
            R.drawable.img_8,
            R.drawable.img_9,
            R.drawable.img_10,
            R.drawable.img_11,
            R.drawable.img_12
        )

        binding.searchEt.setOnEditorActionListener { v, actionId, event ->
            var handled = false
            if (actionId == EditorInfo.IME_ACTION_SEARCH){
                searchImage(v.text.toString(), imgList)
            }
            handled
        }

        binding.show.visibility = View.GONE
        binding.show.setOnClickListener {
            it.visibility = View.GONE
        }

        val visionRunner = VisionTransformerRunner()
        val bitmapList = arrayListOf<Bitmap>()
        val bmpFactoryOption = BitmapFactory.Options()
        bmpFactoryOption.inScaled = false
        for (imgID in imgList) {
            val bitmap = BitmapFactory.decodeResource(this.resources, imgID, bmpFactoryOption)
            bitmapList.add(bitmap)
        }

        returns_img = visionRunner.runSession(bitmapList)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun searchImage(query : String, imgList : ArrayList<Int>){
        val textRunner = TextTransformerRunner()
        val returns = textRunner.runSession(arrayListOf(query))

        val calc = SimilarityCalculator(returns, returns_img)
        val answer_idx = calc.run()
        binding.show.setImageResource(imgList[answer_idx])
        binding.show.visibility = View.VISIBLE
    }
}