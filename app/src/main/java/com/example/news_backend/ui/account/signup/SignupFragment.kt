package com.example.news_backend.ui.account.signup

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.news_backend.R
import com.example.news_backend.data.sharedpreferences.DataLocalManager
import com.example.news_backend.databinding.FragmentSignUpBinding
import com.example.news_backend.ui.account.AccountViewModel
import com.example.news_backend.utils.LoadingScreen
import com.example.news_backend.utils.Resource
import com.example.news_backend.utils.viewBinding

class SignupFragment : Fragment(R.layout.fragment_sign_up) {
    private val binding by viewBinding(FragmentSignUpBinding::bind)
    private val viewModel: AccountViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
    }

    private fun initUI() {
        binding.signUpBtn.setOnClickListener {
            val name = binding.editNameSignUp.text.toString()
            val username = binding.editUsernameSignUp.text.toString()
            val email = binding.editEmailSignUp.text.toString()
            val password = binding.editPassSignUp.text.toString()
            if (name == "" || username == "" || email == "" || password == "") {
                Toast.makeText(requireContext(), "Hãy Nhập Đầy Đủ Thông Tin", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.signup(name, username, email, password)
                observeViewModel()
            }
        }
    }

    private fun observeViewModel() {
        viewModel.signupResult.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    resource.data?.let {
                        // Save in sharePreference
                        DataLocalManager.getInstance()
                            .setSaveUserInfo(it.data.id,
                                it.data.name,
                                it.data.username,
                                it.data.email,
                                it.data.role.first()
                            )
                    }
                    Toast.makeText(requireContext(), resource.message, Toast.LENGTH_SHORT).show()
                }
                is Resource.Error -> {
                    val errorMessage = resource.message ?: "Unknown error occurred"
                    Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading -> {
                    LoadingScreen.displayLoading(requireContext(), false)
                }
            }
        }
    }

}

