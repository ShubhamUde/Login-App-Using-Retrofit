package com.shubham.retrofitexample

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import com.shubham.retrofitexample.ModelResponse.RegisterResponse

import com.shubham.retrofitexample.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.loginText.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.btnRegister.setOnClickListener {
            registerUser()
        }
    }

    private fun registerUser() {
        val userName = binding.rgName.text.toString()
        val userEmail = binding.rgEmail.text.toString()
        val userPassword = binding.rgPassword.text.toString()

        if(userName.isEmpty()){
            binding.rgName.requestFocus()
            binding.rgName.error = "Please enter your name"
            return
        }

        if(userEmail.isEmpty()) {
            binding.rgEmail.requestFocus()
            binding.rgEmail.error = "Please enter your email"
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
            binding.rgEmail.requestFocus()
            binding.rgEmail.error = "Please enter a valid email address"
            return
        }

        if(userPassword.isEmpty()) {
            binding.rgPassword.requestFocus()
            binding.rgPassword.error = "Please enter your password"
            return
        }

        if (userPassword.length < 6) {
            binding.rgPassword.requestFocus()
            binding.rgPassword.error = "Password must be at least 8 characters long"
            return
        }

        RetrofitClient.getInstance().api.register(userName, userEmail, userPassword).enqueue(object : Callback<RegisterResponse?> {
            override fun onResponse(call: Call<RegisterResponse?>, response: Response<RegisterResponse?>) {

                if (response.isSuccessful) {
                    val registerResponse: RegisterResponse = response.body()!!
                    runOnUiThread {
                        val intent = Intent(this@MainActivity, LoginActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                        Toast.makeText(this@MainActivity, registerResponse.message, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    runOnUiThread {
                        Toast.makeText(this@MainActivity, errorBody ?: "Unknown error", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<RegisterResponse?>, t: Throwable) {
                runOnUiThread {
                    Toast.makeText(this@MainActivity, t.message ?: "Unknown error", Toast.LENGTH_SHORT).show()
                }
            }
        })

    }
}