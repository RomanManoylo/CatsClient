package com.example.user.cats.main

import android.content.Context
import android.widget.TextView
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.user.cats.R
import com.example.user.cats.entity.Cat
import android.graphics.BitmapFactory
import android.widget.ImageButton
import java.nio.file.Files.exists
import com.example.user.cats.R.drawable.cat
import java.io.File
import com.example.user.cats.R.drawable.cat


class CatsAdapter(var cats: List<Cat>, val cachePath: String, val listener: CatItemListener) : RecyclerView.Adapter<CatsAdapter.CatViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.view_cat, parent, false)
        return CatViewHolder(v)
    }

    override fun getItemCount() = cats.size

    override fun onBindViewHolder(holder: CatViewHolder, position: Int) {
        holder.catName.text = cats[position].name
        holder.catId.text = cats[position].id.toString()
        holder.catInfo.text = cats[position].info
        if (cats[position].getImage() != null) {
            val image = File(cachePath, cats[position].getImage())
            if (image.exists())
                holder.catPhoto.setImageBitmap(BitmapFactory.decodeFile(image.absolutePath))
        }
        holder.deleteCat.setOnClickListener(View.OnClickListener {
            listener.deleteCat(cats[position])
        })
        holder.catItem.setOnLongClickListener(View.OnLongClickListener {
            listener.updateCat(cats[position])
            true
        })
    }


    class CatViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var catName: TextView = itemView.findViewById(R.id.cat_name)
        internal var catId: TextView = itemView.findViewById(R.id.cat_id)
        internal var deleteCat: ImageButton = itemView.findViewById(R.id.delete)
        internal var catInfo: TextView = itemView.findViewById(R.id.cat_info)
        internal var catPhoto: ImageView = itemView.findViewById(R.id.cat_photo) as ImageView
        internal var catItem: CardView = itemView.findViewById(R.id.cat_item_cv)

    }

    interface CatItemListener{
        fun deleteCat(cat: Cat)
        fun updateCat(cat: Cat)

    }
}