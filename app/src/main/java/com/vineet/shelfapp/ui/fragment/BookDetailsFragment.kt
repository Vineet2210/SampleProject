package com.vineet.shelfapp.ui.fragment

import android.annotation.SuppressLint
import android.os.Build
import android.os.Build.VERSION
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.vineet.shelfapp.R
import com.vineet.shelfapp.data.models.BookListResponse
import com.vineet.shelfapp.data.models.Favorites
import com.vineet.shelfapp.databinding.FragmentBookDetailsBinding
import com.vineet.shelfapp.ui.viewmodel.BookViewModel
import com.vineet.shelfapp.utils.AppConstants
import com.vineet.shelfapp.utils.AppUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookDetailsFragment : Fragment() {
    private lateinit var binding: FragmentBookDetailsBinding
    private val booksViewModel by viewModels<BookViewModel>()

    /**
     * lifecycle methods
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBookDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bookDetails = if (VERSION.SDK_INT>=Build.VERSION_CODES.TIRAMISU){
      requireArguments().getParcelable(AppConstants.BundleConstants.BOOK_DETAILS,BookListResponse.BookListResponseItem::class.java)
    }
        else
        {
             requireArguments().getParcelable(AppConstants.BundleConstants.BOOK_DETAILS)
        }
        booksViewModel.getFavBooks()
        if (bookDetails != null) {
            setData(bookDetails)
            listeners(bookDetails)
        }
        backPressed {
            findNavController().popBackStack()
        }
    }

    /**
     * managing the data on ui
     */
    @SuppressLint("SetTextI18n")
    private fun setData(bookDetails: BookListResponse.BookListResponseItem) {
        binding.tvBookName.text=getString(R.string.name)+" - "+bookDetails.title
        binding.tvAlias.text= getString(R.string.alias) + bookDetails.alias
        binding.tvHits.text = getString(R.string.hits)+ bookDetails.hits
        binding.tvLastUpdateUpon.text=getString(R.string.updated_on)+
            bookDetails.lastChapterDate?.let { AppUtils.convertLongToDate(it,"dd MMM, yyyy") }
        AppUtils.setRoundedImage(bookDetails.image,binding.ivBookImage.context,binding.ivBookImage,80f, 80f,0f,0f)
        if (bookDetails.isFavorite == true)
        {
            binding.ivFavorite.setImageResource(R.drawable.ic_fav_filled)
        }
        else
        {
            binding.ivFavorite.setImageResource(R.drawable.ic_fav)
        }

    }
    /**
     * managing the data on ui and click listeners
     */
    private fun listeners(bookDetails: BookListResponse.BookListResponseItem)
    {
        binding.ivFavorite.setOnClickListener {
            if (bookDetails.isFavorite==true){
                bookDetails.id?.let { it1 -> Favorites(it1,bookDetails.isFavorite) }
                    ?.let { it2 -> booksViewModel.deleteFav(it2) }
                binding.ivFavorite.setImageResource(R.drawable.ic_fav)
                bookDetails.isFavorite=false
            }
            else
            {
                bookDetails.id?.let { it1 -> Favorites(it1,true) }
                    ?.let { it2 -> booksViewModel.insertFav(it2) }
                binding.ivFavorite.setImageResource(R.drawable.ic_fav_filled)
                bookDetails.isFavorite=true
            }
        }
    }
    // to get the callback of device back button
    private fun backPressed(backLogic: () -> Unit) {
        activity?.onBackPressedDispatcher?.addCallback(
            requireActivity(),
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (isAdded)
                        backLogic()
                }
            })
    }
}