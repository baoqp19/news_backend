package com.example.news_backend.ui.account.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.news_backend.R
import com.example.news_backend.activity.main.MainActivity
import com.example.news_backend.data.sharedpreferences.DataLocalManager
import com.example.news_backend.databinding.FragmentSignInBinding
import com.example.news_backend.ui.account.AccountViewModel
import com.example.news_backend.utils.LoadingScreen
import com.example.news_backend.utils.Resource
import com.example.news_backend.utils.viewBinding

class LoginFragment : Fragment(R.layout.fragment_sign_in) {
    private val TAG: String = "TOKEN_T"

    private val binding by viewBinding(FragmentSignInBinding::bind)
    private val viewModel: AccountViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClickListener()
    }

    private fun onClickListener() {
        binding.apply {
            txtNextApp.setOnClickListener {
                DataLocalManager.getInstance().setSaveUserInfo(0, "", "", "", "")
                startActivity(Intent(requireContext(), MainActivity::class.java))
                requireActivity().finish()
            }

            signInBtn.setOnClickListener {
                val username = binding.editEmailSignIN.text.toString()
                val password = binding.editPassSignIn.text.toString()
                if (username == "" || password == "") {
                    Toast.makeText(requireContext(), "Hãy Nhập Đầy Đủ Thông Tin", Toast.LENGTH_SHORT).show()
                } else {
                    viewModel.login(username, password)
                    observeViewModel()
                }
            }
        }
    }

    private fun observeViewModel() {
        viewModel.loginResult.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    // user login successfully and save shareFerference
                    DataLocalManager.getInstance().setFirstInstalled(true)

                    /**
                     * save info User Login
                     * **/
                    resource.data?.let {
                        DataLocalManager.getInstance().setSaveTokenKey(it.data.token)
                        DataLocalManager.getInstance().setSaveUserInfo(
                            it.data.id,
                            it.data.name,
                            it.data.username,
                            it.data.email,
                            "USER"
                        )
                        Log.d(TAG, it.data.token )
                        Log.d(TAG, it.data.name )
                        Log.d(TAG, it.data.username )
                        Log.d(TAG, it.data.email )

                    }

                    // start MainActivity
                    startActivity(Intent(
                        requireContext(),
                        MainActivity::class.java)
                    )
                    requireActivity().finish()
                }
                is Resource.Error -> {
                    val errorMessage = resource.message ?: "Unknown error occurred"
                    Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading -> {
//                    LoadingScreen.displayLoading(requireContext(), false)
                }
            }
        }
    }

}
