package com.vineet.shelfapp.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.vineet.shelfapp.R
import com.vineet.shelfapp.data.models.BookListResponse
import com.vineet.shelfapp.data.models.Favorites
import com.vineet.shelfapp.databinding.IncludeBooksLayoutBinding
import com.vineet.shelfapp.utils.AppUtils
import kotlin.collections.ArrayList


class BooksAdapter :
    RecyclerView.Adapter<BooksAdapter.BookListViewHolder>() {
    private var onTapItem: (BookListResponse.BookListResponseItem) -> Unit={}
    private var onFavClicked: (BookListResponse.BookListResponseItem,Boolean) -> Unit={_,_->}
    private var booksList = ArrayList<BookListResponse.BookListResponseItem>()
    private var isFavorite=false
    private var map=HashMap<String,Boolean>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BookListViewHolder {
        return BookListViewHolder(
            IncludeBooksLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: BookListViewHolder, position: Int) {
        holder.bind(booksList[position])

    }


    override fun getItemCount(): Int {
        return booksList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setAdapterData(
        booksList: ArrayList<BookListResponse.BookListResponseItem>,
        favList: List<Favorites>
    ) {
        favList.forEach {
            if (map.contains(it.id))
            {
                it.isFavorite?.let { it1 -> map.replace(it.id, it1) }
            }
            else
            {
                it.isFavorite?.let { it1 -> map.put(it.id, it1) }
            }
        }
        this.booksList.clear()
        this.booksList.addAll(booksList)
        notifyDataSetChanged()
    }

    inner class BookListViewHolder(val binding: IncludeBooksLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(booksDetails: BookListResponse.BookListResponseItem) {
            AppUtils.setCircularImage(booksDetails.image,binding.ivBookImage)
            if (map[booksDetails.id] == true) {
                binding.ivFavorite.setImageResource(R.drawable.ic_fav_filled)
            } else {
                binding.ivFavorite.setImageResource(R.drawable.ic_fav)
            }
            binding.tvBookName.text=booksDetails.title
            binding.tvHits.text="Hits: ${booksDetails.hits}"
            binding.ivFavorite.setOnClickListener {
                if (map.contains(booksDetails.id))
                {
                    val value=map[booksDetails.id]
                    if (value != null) {
                        booksDetails.id?.let { it1 -> map.replace(it1,!value) }
                    }
                    if (value == true)
                    {
                        binding.ivFavorite.setImageResource(R.drawable.ic_fav)
                        onFavClicked(booksDetails,false)
                    }
                    else
                    {
                        binding.ivFavorite.setImageResource(R.drawable.ic_fav_filled)
                        onFavClicked(booksDetails,true)
                    }
                }
                else
                {
                    booksDetails.id?.let { it1 -> map.put(it1,true) }
                    binding.ivFavorite.setImageResource(R.drawable.ic_fav_filled)
                    onFavClicked(booksDetails,true)
                }
                booksDetails.isFavorite=isFavorite
            }
            binding.cvBook.setOnClickListener{
                onTapItem(booksDetails)
            }
        }
    }

    fun onTapItemCallback(listener:(BookListResponse.BookListResponseItem)->Unit){
        this.onTapItem=listener
    }
    fun onFavClickedCallback(listener:(BookListResponse.BookListResponseItem,Boolean)->Unit){
        this.onFavClicked=listener
    }
}