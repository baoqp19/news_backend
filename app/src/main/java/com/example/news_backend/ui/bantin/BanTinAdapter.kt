package com.example.news_backend.ui.bantin

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.news_backend.R
import com.example.news_backend.ui.webview.WebviewFragment
import com.example.news_backend.data.models.BanTin
import com.example.news_backend.data.sharedpreferences.DataLocalManager
import com.example.news_backend.databinding.ItemRowArticleBinding
import com.example.news_backend.ui.save.SaveBanTinViewModel
import com.example.news_backend.ui.save.ViewModelProviderFactory
import java.util.Locale

class BanTinAdapter(
    private val mContext: Context,
    private var mListTinTuc: List<BanTin>,
    private val viewModelProviderFactory: ViewModelProviderFactory
) : RecyclerView.Adapter<BanTinAdapter.ViewHolder>(), Filterable {

    private var mListTinTucFiltered: List<BanTin> = mListTinTuc.toList()

    private val viewModel: SaveBanTinViewModel by lazy {
        viewModelProviderFactory.provideViewModel()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRowArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tinTuc = mListTinTuc[position]
        holder.binding.tvArticleTitle.text = tinTuc.title
        holder.binding.tvArticlePublished.text = tinTuc.pubDate

        if (tinTuc.img.isEmpty())
            holder.binding.ivArticle.setImageResource(R.drawable.world)
        else {
            Glide.with(holder.binding.ivArticle.context)
                .load(tinTuc.img)
                .into(holder.binding.ivArticle)
        }

        holder.binding.itemTinTucConstrantlayout.setOnClickListener {
            // open web view
            openWebViewBanTin(tinTuc)

            // lưu thông tin về bản tin đã đọc (notification-service)
            saveBanTinDaDoc(tinTuc)
        }
    }

    private fun openWebViewBanTin(tinTuc: BanTin) {
        val fragment = WebviewFragment().apply {
            arguments = Bundle().apply {
                putString("link", tinTuc.link)
                Log.d("link", tinTuc.link)
            }
        }

        val fragmentManager = (mContext as AppCompatActivity).supportFragmentManager
        fragmentManager.beginTransaction()
            .replace(R.id.main_fragment, fragment)
            .addToBackStack("BanTinFragment")
            .commit()
    }

    private fun saveBanTinDaDoc(tinTuc: BanTin) {
        val userId = DataLocalManager.getInstance().getInfoUserId()
        viewModel.postNewsSave(tinTuc.title, tinTuc.link, tinTuc.img, tinTuc.pubDate, userId)
    }

    override fun getItemCount(): Int {
        return mListTinTuc.size
    }

    fun updateData(newList: List<BanTin>) {
        mListTinTuc = newList
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemRowArticleBinding) : RecyclerView.ViewHolder(binding.root)

    override fun getFilter(): Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val searchString = constraint?.toString()?.lowercase(Locale.getDefault()) ?: ""
            mListTinTucFiltered = if (searchString.isEmpty()) {
                mListTinTuc.toList()
            } else {
                mListTinTuc.filter {
                    it.title.lowercase(Locale.getDefault()).contains(searchString)
                }
            }
            return FilterResults().apply {
                values = mListTinTucFiltered // Gán kết quả đã lọc vào values
            }
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults) {
            mListTinTucFiltered = results.values as List<BanTin>
            notifyDataSetChanged()
        }
    }

}
