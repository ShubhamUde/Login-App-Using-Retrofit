package com.shubham.retrofitexample.Fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.Toolbar
import com.shubham.retrofitexample.LoginActivity
import com.shubham.retrofitexample.ModelResponse.LoginResponse
import com.shubham.retrofitexample.ModelResponse.UpdateResponse
import com.shubham.retrofitexample.R
import com.shubham.retrofitexample.RetrofitClient
import com.shubham.retrofitexample.SharedPrefManager
import com.shubham.retrofitexample.databinding.ActivityHomeBinding.inflate
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ProfileFragment : Fragment() {

    private lateinit var etUserName: EditText
    private lateinit var etUserEmail: EditText
    private lateinit var currentPass: EditText
    private lateinit var newPass: EditText
    private lateinit var btnUpdateAccount: AppCompatButton
    private lateinit var btnUpdatePassword: AppCompatButton
    lateinit var sharedPrefManager: SharedPrefManager
    private var userId: Int? = null
    private var userEmailId: String? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        etUserName = view.findViewById(R.id.username)
        etUserEmail = view.findViewById(R.id.userEmail)
        btnUpdateAccount = view.findViewById(R.id.btnUpdateAccount)

        currentPass = view.findViewById(R.id.currentPassword)
        newPass = view.findViewById(R.id.newPassword)
        btnUpdatePassword = view.findViewById(R.id.btnUpdatePassword)

        sharedPrefManager = SharedPrefManager(requireActivity())
        userId = sharedPrefManager.getUser()?.id

        userEmailId = sharedPrefManager.getUser()?.email

        btnUpdateAccount.setOnClickListener {
            updateUserAccount()
        }

        btnUpdatePassword.setOnClickListener {
            updateUserPassword()
        }

        return view
    }

    private fun updateUserPassword() {
        val userCurrentPassword = currentPass.text.toString()
        val userNewPassword = newPass.text.toString()

        if (userCurrentPassword.isEmpty()){
            currentPass.requestFocus()
            currentPass.error = "Please enter your current password"
            return
        }

        if (userCurrentPassword.length < 6){
            currentPass.requestFocus()
            currentPass.error = "Password must be at least 6 characters long"
            return
        }

        if (userNewPassword.isEmpty()){
            newPass.requestFocus()
            newPass.error = "Please enter your new password"
            return
        }

        if (userNewPassword.length < 6){
            newPass.requestFocus()
            newPass.error = "Password must be 6 digit"
            return
        }

        userEmailId?.let {
            RetrofitClient.getInstance().api.updateUserPassword(it, userCurrentPassword, userCurrentPassword).enqueue(object : Callback<UpdateResponse?> {
                override fun onResponse(
                    call: Call<UpdateResponse?>,
                    response: Response<UpdateResponse?>,
                ) {
                    val passwordResponse: UpdateResponse? = response.body()

                    if (response.isSuccessful){
                        if(passwordResponse?.error == "200"){
                            Toast.makeText(requireActivity(), passwordResponse.message, Toast.LENGTH_SHORT).show()
                        }
                        else{
                            Toast.makeText(requireActivity(), passwordResponse?.message, Toast.LENGTH_SHORT).show()
                        }
                    }else{
                        Toast.makeText(requireActivity(), "Failed", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<UpdateResponse?>, t: Throwable) {
                    Toast.makeText(requireActivity(), t.message, Toast.LENGTH_SHORT).show()
                }
            })
        }

    }

    private fun updateUserAccount() {
        val username = etUserName.text.toString()
        val email = etUserEmail.text.toString()

        if(email.isEmpty()) {
            etUserEmail.requestFocus()
            etUserEmail.error = "Please enter your email"
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etUserEmail.requestFocus()
            etUserEmail.error = "Please enter a valid email address"
            return
        }

        if (username.isEmpty()) {
            etUserName.requestFocus()
            etUserName.error = "Please enter your username"
            return
        }

        userId?.let {
            RetrofitClient.getInstance().api.updateUserAccount(it, username, email).enqueue(object : Callback<LoginResponse?> {
                override fun onResponse(
                    call: Call<LoginResponse?>,
                    response: Response<LoginResponse?>,
                ) {

                    val updateResponse = response.body()!!
                    if (response.isSuccessful && updateResponse != null){

                        if(updateResponse.error == "200"){
                            updateResponse.user?.let {
                                sharedPrefManager.saveUser(it)
                            }
                            Toast.makeText(requireActivity(), updateResponse.message, Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(requireActivity(), updateResponse.message, Toast.LENGTH_SHORT).show()
                        }
                    }else{
                        Toast.makeText(requireActivity(), "Failed", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse?>, t: Throwable) {
                    Toast.makeText(requireActivity(), t.message, Toast.LENGTH_SHORT).show()
                }
            })
        }

    }
}