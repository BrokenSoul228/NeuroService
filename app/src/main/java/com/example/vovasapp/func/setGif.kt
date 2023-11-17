package com.example.vovasapp.func

import android.content.Context
import android.media.Image
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.gif.GifDrawable

fun setGif (context : Context, drawable: Int, view: ImageView){
    Glide.with(context).load(drawable).into(view)
}