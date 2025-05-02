package com.fake.scrapingby

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ExpenseAdapter(
    private val context: Context,
    private var expenseList: List<Expenses>
) : RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder>() {

    inner class ExpenseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val categoryText: TextView = view.findViewById(R.id.textCategory)
        val descriptionText: TextView = view.findViewById(R.id.textDescription)
        val amountText: TextView = view.findViewById(R.id.textAmount)
        val imageView: ImageView = view.findViewById(R.id.imageThumb)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_expense, parent, false)
        return ExpenseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val expense = expenseList[position]
        holder.categoryText.text = expense.expenseTitle
        holder.descriptionText.text = expense.description
        holder.amountText.text = "R%.2f".format(expense.expenseAmount)

        if (!expense.expenseImage.isNullOrEmpty()) {
            try {
                holder.imageView.setImageURI(Uri.parse(expense.expenseImage))
            } catch (e: Exception) {
                holder.imageView.setImageResource(android.R.drawable.ic_menu_report_image)
            }
        } else {
            holder.imageView.setImageResource(android.R.drawable.ic_menu_report_image)
        }
    }

    override fun getItemCount(): Int = expenseList.size

    fun updateList(newList: List<Expenses>) {
        expenseList = newList
        notifyDataSetChanged()
    }
}
