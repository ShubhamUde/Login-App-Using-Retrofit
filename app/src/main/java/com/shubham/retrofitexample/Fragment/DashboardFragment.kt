package com.shubham.retrofitexample.Fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.shubham.retrofitexample.LoginActivity
import com.shubham.retrofitexample.ModelResponse.DeleteResponse
import com.shubham.retrofitexample.R
import com.shubham.retrofitexample.RetrofitClient
import com.shubham.retrofitexample.SharedPrefManager
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardFragment : Fragment() {

    lateinit var etName: TextView
    lateinit var sharedPrefManager: SharedPrefManager

    @SuppressLint("MissingInflatedId", "SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        etName = view.findViewById(R.id.et_person_name)

        //add
        val toolbar: Toolbar = view.findViewById(R.id.toolbar)
        (activity as? AppCompatActivity)?.setSupportActionBar(toolbar)
        setHasOptionsMenu(true)

        sharedPrefManager = SharedPrefManager(requireActivity())

        val userName = "Hey! ${sharedPrefManager.getUser()?.username}"
        etName.setText(userName)

        return view
    }


    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.logout_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.logout -> {
                logoutUser()
                return true
            }
            R.id.deleteAccount -> {
                deleteAccount()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun deleteAccount() {

        sharedPrefManager.getUser()?.let {
            RetrofitClient.getInstance().api.deleteUser(it.id).enqueue(object : Callback<DeleteResponse?> {
                override fun onResponse(
                    call: Call<DeleteResponse?>,
                    response: Response<DeleteResponse?>,
                ) {

                    val deleteResponse: DeleteResponse? = response.body()

                    if (response.isSuccessful){

                        if(deleteResponse?.error == "200"){
                            logoutUser()
                            Toast.makeText(requireActivity(), deleteResponse.message, Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(requireActivity(), deleteResponse?.message, Toast.LENGTH_SHORT).show()
                        }
                    }else{
                        Toast.makeText(requireActivity(), "Failed", Toast.LENGTH_SHORT).show()
                    }

                }

                override fun onFailure(call: Call<DeleteResponse?>, t: Throwable) {
                    Toast.makeText(requireActivity(), t.message, Toast.LENGTH_SHORT).show()
                }
            })
        }

    }

    private fun logoutUser() {

        sharedPrefManager.logout()
        val intent = Intent(context, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        Toast.makeText(context,"Logout successful", Toast.LENGTH_SHORT).show()

    }
}