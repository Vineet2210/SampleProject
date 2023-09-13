package com.vineet.shelfapp.ui.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.vineet.shelfapp.R
import com.vineet.shelfapp.data.models.Country
import com.vineet.shelfapp.data.models.User
import com.vineet.shelfapp.databinding.FragmentSignUpBinding
import com.vineet.shelfapp.ui.viewmodel.AuthVIewModel
import com.vineet.shelfapp.utils.AppConstants
import com.vineet.shelfapp.utils.AppUtils
import com.vineet.shelfapp.utils.ShelfPreference
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException
import java.nio.charset.Charset

@AndroidEntryPoint
class SignUpFragment : Fragment(), AdapterView.OnItemSelectedListener {
    private lateinit var binding: FragmentSignUpBinding
    private val authViewModel by viewModels<AuthVIewModel>()


    private  var country:Country?= null
    private val countryList=ArrayList<String>()

    /**
     * lifecycle methods
     */

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val jsonString = getJSONObject()
        country = Gson().fromJson(
            jsonString,
            Country::class.java
        )

        country?.data?.entries?.forEach {country->
            countryList.add(country.value.country)
        }
        setSpinner(countryList)
        listeners()
        backPressed {
            findNavController().popBackStack()
        }
    }

    private fun validations(): Boolean {
        if (binding.etName.text.isNullOrEmpty()&&binding.etPassword.text.isNullOrEmpty()){
            Toast.makeText(requireActivity(), "Please Enter the Details", Toast.LENGTH_SHORT).show()
            return false
        }
        else {
            return if (binding.etName.text?.length!! <= 3){
                Toast.makeText(requireActivity(), "Please Enter a valid name", Toast.LENGTH_SHORT)
                    .show()
                false
            } else if(binding.etPassword.text?.isEmpty() == true || !AppUtils.isPasswordValid(binding.etPassword.text.toString())) {
                Toast.makeText(
                    requireActivity(),
                    "Please enter a valid password, min 8 characters with atleast one number, special characters, one lowercase letter, and one uppercase letter is mandatory.",
                    Toast.LENGTH_SHORT
                ).show()
                false
            } else {
                true
            }
        }
    }

    private fun listeners() {
        binding.btnSignUp.setOnClickListener {
            if (validations()) {
                authViewModel.insertUser(
                    User(
                        System.currentTimeMillis(),
                        binding.etName.text?.trim().toString(),
                        authViewModel.countryMld.value,
                        binding.etPassword.text?.trim().toString()
                    )
                )
                ShelfPreference.savePreference(
                    AppConstants.PreferenceConstants.GET_TOKEN, binding.etName.text.toString() +
                            binding.etPassword.text.toString()
                )
                findNavController().navigate(R.id.bookListFragment)
            }
        }
        binding.tvLogin.setOnClickListener {
            findNavController().navigate(R.id.loginFragment)
        }
    }

    private fun setSpinner(countryList: ArrayList<String>) {
        binding.countrySpinner.onItemSelectedListener = this
        val arrayAdapter: ArrayAdapter<*> =
            ArrayAdapter(requireContext(), androidx.transition.R.layout.support_simple_spinner_dropdown_item, countryList)
        arrayAdapter.setDropDownViewResource(androidx.transition.R.layout.support_simple_spinner_dropdown_item)
        binding.countrySpinner.adapter = arrayAdapter
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        binding.countrySpinner.setSelection(p2)
        authViewModel.countryMld.value=countryList[p2]
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

    /**
     * json object
     */
    private fun getJSONObject(): String? {
        val json: String?
        val charset: Charset = Charsets.UTF_8
        try {
            val jsonFile = activity?.assets?.open("country.json")
            val size = jsonFile?.available()
            val bufferStorage = ByteArray(size!!)
            jsonFile.read(bufferStorage)
            jsonFile.close()
            json = String(bufferStorage, charset)
        } catch (e: IOException) {
            print(e)
            return null
        }
        return json
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