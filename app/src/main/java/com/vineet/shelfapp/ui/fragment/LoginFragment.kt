package com.vineet.shelfapp.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.vineet.shelfapp.R
import com.vineet.shelfapp.databinding.FragmentLoginBinding
import com.vineet.shelfapp.ui.viewmodel.AuthVIewModel
import com.vineet.shelfapp.utils.AppConstants
import com.vineet.shelfapp.utils.AppUtils
import com.vineet.shelfapp.utils.ShelfPreference
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private val authViewModel by viewModels<AuthVIewModel>()

    private lateinit var binding: FragmentLoginBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listeners()
        setObservers()
        backPressed {
            requireActivity().finishAffinity()
        }
    }

    private fun listeners() {
        binding.btnSignUp.setOnClickListener {
            if (validations()) {
                authViewModel.authenticateUser(
                    binding.etName.text?.trim().toString(),
                    binding.etPassword.text?.trim().toString()
                )
            }
        }
        binding.tvLogin.setOnClickListener {
            findNavController().navigate(R.id.signUpFragment)
        }
    }

    private fun setObservers() {
        authViewModel.user.observe(viewLifecycleOwner) {
            if (it) {
                ShelfPreference.savePreference(
                    AppConstants.PreferenceConstants.GET_TOKEN, binding.etName.text.toString() +
                            binding.etPassword.text.toString()
                )
                findNavController().navigate(R.id.bookListFragment)
            } else {
                Toast.makeText(requireActivity(), getString(R.string.error_occurred), Toast.LENGTH_SHORT).show()
            }
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