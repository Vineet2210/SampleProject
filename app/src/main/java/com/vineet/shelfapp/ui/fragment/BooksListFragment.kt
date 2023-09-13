package com.vineet.shelfapp.ui.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.vineet.shelfapp.R
import com.vineet.shelfapp.data.models.BookListResponse
import com.vineet.shelfapp.data.models.Favorites
import com.vineet.shelfapp.databinding.FragmentBooksListBinding
import com.vineet.shelfapp.ui.adapter.BooksAdapter
import com.vineet.shelfapp.ui.adapter.Sorting
import com.vineet.shelfapp.ui.adapter.SortingAdapter
import com.vineet.shelfapp.ui.viewmodel.BookViewModel
import com.vineet.shelfapp.utils.AppConstants
import com.vineet.shelfapp.utils.ShelfPreference
import com.vineet.shelfapp.utils.Status
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BooksListFragment : Fragment() {
    private val booksViewModel by viewModels<BookViewModel>()
    private lateinit var booksAdapter: BooksAdapter
    private lateinit var sortingAdapter: SortingAdapter
    private val bookList = ArrayList<BookListResponse.BookListResponseItem>()// master list
    private val sortedList = ArrayList<BookListResponse.BookListResponseItem>() // sorting operation list


    private lateinit var binding: FragmentBooksListBinding


    /**
     * lifecycle methods
     */

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBooksListBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * setting up the adapters for recycler views for sorting  and book and managing its respective clicks
     */
    private fun setAdapter() {
        booksViewModel.getBookList()
        booksAdapter = BooksAdapter()
        booksAdapter.onTapItemCallback { bookDetails ->
            Log.d("bookDetails====", bookDetails.toString())
            booksViewModel.favList.value?.forEach {
                if (it.id == bookDetails.id) {
                    bookDetails.isFavorite = true
                }
            }
            val bundle = Bundle()
            bundle.putParcelable(AppConstants.BundleConstants.BOOK_DETAILS, bookDetails)
            findNavController().navigate(R.id.bookDetailsFragment, bundle)
        }
        booksAdapter.onFavClickedCallback { bookDetails, isFav ->
            if (isFav) {
                bookDetails.id?.let { Favorites(it, isFav) }?.let { booksViewModel.insertFav(it) }
                bookDetails.id?.let { Favorites(it, isFav) }
                    ?.let { booksViewModel.favList.value?.add(it) }
            } else {
                bookDetails.id?.let { Favorites(it, !isFav) }?.let { booksViewModel.deleteFav(it) }
                bookDetails.id?.let { Favorites(it, isFav) }
                    ?.let { booksViewModel.favList.value?.add(it) }
            }
        }
        binding.rvCategories.apply {
            layoutManager = LinearLayoutManager(
                binding.rvCategories.context,
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter = booksAdapter
        }
        sortingAdapter = SortingAdapter {
            filterDataList(it)
        }
        binding.rvSorting.apply {
            layoutManager = LinearLayoutManager(
                binding.rvSorting.context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = sortingAdapter
        }
        sortingAdapter.setData(getFilterData())
    }

    /**
     * filtering list as per different selection made by the user
     */
    private fun filterDataList(sorting: Sorting) {
        sortedList.clear()
        sortedList.addAll(bookList)
        when (sorting.id) {
            AppConstants.SortingTypes.TITLE -> {
                sortedList.sortWith(compareBy(String.CASE_INSENSITIVE_ORDER) { it.title.toString() })
            }
            AppConstants.SortingTypes.HITS -> {
                sortedList.sortByDescending { it.hits }
            }
            else -> {
                booksViewModel.favList.value?.forEach { fav ->
                    sortedList.forEach { books ->
                        if (books.id == fav.id)
                            books.isFavorite = fav.isFavorite
                    }
                }
                sortedList.sortByDescending {
                    it.isFavorite == true
                }
            }
        }

        // managing the user sort preference whether ascending or descending
        if (ShelfPreference.getBoolPreference(AppConstants.PreferenceConstants.USER_SORT_PREFERENCE)) {
            booksViewModel.favList.value?.let {
                booksAdapter.setAdapterData(
                    sortedList.reversed() as ArrayList<BookListResponse.BookListResponseItem>,
                    it
                )
            }
        } else {
            booksViewModel.favList.value?.let { booksAdapter.setAdapterData(sortedList, it) }
        }
    }

    /**
     * setting up the static filter data to pass in the recycler view adapter
     */
    private fun getFilterData(): ArrayList<Sorting> {
        val filterList = ArrayList<Sorting>()
        filterList.add(Sorting(1, "Title"))
        filterList.add(Sorting(2, "Hits"))
        filterList.add(Sorting(3, "Fav"))
        return filterList
    }

    /**
     * observing the api calls and managing the data respectively
     */
    private fun setObserver() {
        booksViewModel.observeBookListResponse().observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    if (it.data?.isEmpty() == true) {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.error_occurred),
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        it.data?.let { it1 -> bookList.addAll(it1) }
                        booksViewModel.favList.value?.let { it1 ->
                            booksAdapter.setAdapterData(
                                bookList,
                                it1
                            )
                        }
                    }
                }
                Status.ERROR -> {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.error_occurred),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                Status.LOADING -> {

                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        booksViewModel.getFavBooks()
        if (ShelfPreference.getBoolPreference(AppConstants.PreferenceConstants.USER_SORT_PREFERENCE)) {
            binding.switchButton.isChecked = true
        }
        setAdapter()
        setObserver()
        clickListeners()
        backPressed {
            requireActivity().finishAffinity()
        }
    }

    /**
     * click listeners managed
     */
    private fun clickListeners() {
        binding.tvLogout.setOnClickListener {
            ShelfPreference.savePreference(AppConstants.PreferenceConstants.GET_TOKEN, null)
            findNavController().navigate(R.id.loginFragment)
        }
        binding.switchButton.setOnCheckedChangeListener { _, b ->
            if (b) {
                ShelfPreference.saveBoolPreference(
                    AppConstants.PreferenceConstants.USER_SORT_PREFERENCE,
                    b
                )
                booksViewModel.favList.value?.let {
                    booksAdapter.setAdapterData(
                        sortedList.reversed() as ArrayList<BookListResponse.BookListResponseItem>,
                        it
                    )
                }
            } else {
                ShelfPreference.saveBoolPreference(
                    AppConstants.PreferenceConstants.USER_SORT_PREFERENCE,
                    b
                )
                booksViewModel.favList.value?.let { booksAdapter.setAdapterData(sortedList, it) }
            }
        }
    }

    // to get the callback of device back button
    fun backPressed(backLogic: () -> Unit) {
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