package com.nikhil.buyerapp.Signup

import android.graphics.drawable.PictureDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nikhil.buyerapp.R
import com.caverock.androidsvg.SVG
import com.nikhil.buyerapp.databinding.ItemOnboardingPageBinding
class OnboardingAdapter : RecyclerView.Adapter<OnboardingAdapter.OnboardingViewHolder>() {

    // Sample onboarding data - replace with your actual data
    private val onboardingItems = listOf(
        OnboardingItem(
            R.drawable.search, // Replace with your drawable
            "Find the right person for your project",
            "Work with the largest network of independent professionals to get things done."
        ),
        OnboardingItem(
            R.drawable.language, // Replace with your drawable
            "Bridge the Communication Gap",
            "Seamless language support ensures you can easily connect and collaborate without barriers."
        ),
        OnboardingItem(
            R.drawable.money, // Replace with your drawable
            "Hassle-free INR Transactions",
            "Easily manage payments in INR — no need to worry about USD conversions or international transaction issues"
        )
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnboardingViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_onboarding_page, parent, false)
        return OnboardingViewHolder(view)
    }

    override fun onBindViewHolder(holder: OnboardingViewHolder, position: Int) {
        holder.bind(onboardingItems[position])
    }

    override fun getItemCount(): Int = onboardingItems.size

    class OnboardingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.ivOnboarding)
        private val titleText: TextView = itemView.findViewById(R.id.tvTitle)
        private val descriptionText: TextView = itemView.findViewById(R.id.tvDescription)

        fun bind(item: OnboardingItem) {
            imageView.setImageResource(item.imageRes)
            titleText.text = item.title
            descriptionText.text = item.description
        }
    }

    data class OnboardingItem(
        val imageRes: Int,
        val title: String,
        val description: String
    )
}

//class Adapter(private val images: List<Int>) :
//    RecyclerView.Adapter<Adapter.OnboardingViewHolder>() {
//
//    inner class OnboardingViewHolder(val binding: ItemOnboardingPageBinding) : RecyclerView.ViewHolder(binding.root)
//
//
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnboardingViewHolder {
//        val binding = ItemOnboardingPageBinding.inflate(
//            LayoutInflater.from(parent.context), parent, false)
//        return OnboardingViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: OnboardingViewHolder, position: Int) {
//
//        holder.binding.imageView.setImageResource(images[position])
//    }
//
//    override fun getItemCount() = images.size
//}