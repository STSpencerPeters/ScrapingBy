package com.fake.scrapingby



//Class that handles how the categories are going to be displayed.
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class CategoryAdapter(private val categories: List<Categories>) :
    RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryName: TextView = itemView.findViewById(R.id.categoryNameTextView)
        val categoryImage: ImageView = itemView.findViewById(R.id.categoryImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.category_item, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        holder.categoryName.text = category.categoryName

        // Load image using Glide
        /*
            Code Attribution:
            How to Use Glide Image Loader Library in Android Apps?, 2025.
            I used this reference to display the image in recycler view.
         */
        Glide.with(holder.itemView.context)
            .load(category.categoryImage) // Ensure this is a valid URI or path
            .placeholder(R.drawable.ic_image) // fallback image
            .into(holder.categoryImage)
    }

    override fun getItemCount() = categories.size
}
