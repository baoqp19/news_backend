package com.example.news_backend.ui.menu.update

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.news_backend.R
import com.example.news_backend.databinding.FragmentUpdateUserBinding
import com.example.news_backend.utils.Resource
import com.example.news_backend.utils.viewBinding

class UpdateUserFragment : Fragment(R.layout.fragment_update_user) {
    private val binding by viewBinding(FragmentUpdateUserBinding::bind)
    private val viewModel: UpdateUserViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        observeViewModel()
    }

    private fun initUI() {
        binding.btnUpdateUser.setOnClickListener {
            val name = binding.txtName.text.toString()
            val email = binding.txtEmail.text.toString()
            val password = binding.txtPassword.text.toString()
            viewModel.updateUserInfo(name, email, password)
        }
    }

    private fun observeViewModel() {
        viewModel.updateUser.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Success -> {
                    Toast.makeText(requireContext(), R.string.update_info_successfully.toString() + result.message, Toast.LENGTH_SHORT).show()
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }
                is Resource.Error -> {
                    Toast.makeText(requireContext(), R.string.update_info_failed.toString(), Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading -> {

                }
            }
        }
    }

}