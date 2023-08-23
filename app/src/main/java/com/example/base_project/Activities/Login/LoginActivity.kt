package com.example.base_project.Activities.Login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.lifecycle.Observer
import com.example.base_project.Activities.Home.createHomeIntent
import com.example.base_project.Utils.BaseActivity
import com.example.base_project.Utils.Constants
import com.example.base_project.databinding.ActivityLoginBinding

class LoginActivity : BaseActivity() {
    private lateinit var binding: ActivityLoginBinding
    private var model = LoginViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        configureEditTexts()
        setListeners()
        setObservers()
    }

    override fun onBackPressed() {
        val a = Intent(Intent.ACTION_MAIN)
        a.addCategory(Intent.CATEGORY_HOME)
        a.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(a)
    }

    private fun configureEditTexts(){
        binding.txtUsername.setTypeButton(EditorInfo.IME_ACTION_NEXT)
//        binding.txtUsername.setButtonAction(EditorInfo.IME_ACTION_NEXT) {
//            binding.txtSenha.requestFocus();
//        }

        binding.txtSenha.setTypeButton(EditorInfo.IME_ACTION_GO)
        binding.txtSenha.setButtonAction(EditorInfo.IME_ACTION_GO){
            login()
        }
    }
    private fun setListeners(){
        binding.btnLogin.setOnClickListener {
            login()
        }
        binding.btnGet.setOnClickListener {
            startActivity(createHomeIntent(false))
        }
    }

    private fun setObservers(){
        model.loading.observe(this, Observer {
            binding.btnLogin.setLoading(it)
        })

        model.errorMessage.observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })

        model.success.observe(this, Observer {
            startActivity(createHomeIntent(true))
        })
    }

    private fun allFieldsValid(): Boolean {
        binding.txtUsername.validate()
        binding.txtSenha.validate()
        return binding.txtUsername.isFieldValid && binding.txtSenha.isFieldValid
    }

    private fun login(){
        if (allFieldsValid()) model.login(binding.txtUsername.text,  binding.txtSenha.text)
    }
}


fun Context.createLoginIntent() =
    Intent(this, LoginActivity::class.java)