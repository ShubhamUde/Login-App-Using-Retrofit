package com.shubham.retrofitexample

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.shubham.retrofitexample.ModelResponse.LoginResponse
import com.shubham.retrofitexample.databinding.ActivityLoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding
    private var sharedPrefManager: SharedPrefManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.registerText.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogin.setOnClickListener {
            loginUser()
        }

        sharedPrefManager = SharedPrefManager(this)

    }

    private fun loginUser() {
        val userEmail = binding.logEmail.text.toString()
        val userPassword = binding.logPassword.text.toString()

        if(userEmail.isEmpty()) {
            binding.logEmail.requestFocus()
            binding.logEmail.error = "Please enter your email"
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
            binding.logEmail.requestFocus()
            binding.logEmail.error = "Please enter a valid email address"
            return
        }

        if(userPassword.isEmpty()) {
            binding.logPassword.requestFocus()
            binding.logPassword.error = "Please enter your password"
            return
        }

        if (userPassword.length < 6) {
            binding.logPassword.requestFocus()
            binding.logPassword.error = "Password must be at least 8 characters long"
            return
        }


        RetrofitClient.getInstance().api.login(userEmail, userPassword).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {

                val loginResponse: LoginResponse? = response.body()

                if (response.isSuccessful) {

                    if (loginResponse != null && loginResponse.error == "200") {

                        if (loginResponse.user != null) {

                            sharedPrefManager!!.saveUser(loginResponse.user!!)
                            val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                            Toast.makeText(this@LoginActivity, loginResponse.message, Toast.LENGTH_SHORT).show()

                        } else {
                            Toast.makeText(this@LoginActivity, loginResponse.message ?: "Login failed", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        // Handle unsuccessful login
                        Toast.makeText(this@LoginActivity, loginResponse?.message ?: "Wrong credentials!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this@LoginActivity, "Login failed: ${response.message()}", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<LoginResponse?>, t: Throwable) {

                runOnUiThread {
                    Toast.makeText(this@LoginActivity, t.message ?: "Unknown error", Toast.LENGTH_SHORT).show()
                }
            }
        })

    }

    override fun onStart() {
        super.onStart()

        if (sharedPrefManager!!.isLogged()){
            val intent = Intent(this@LoginActivity, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}