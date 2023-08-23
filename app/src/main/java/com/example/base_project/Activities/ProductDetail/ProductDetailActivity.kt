package com.example.base_project.Activities.ProductDetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.example.base_project.Utils.BaseActivity
import com.example.base_project.Utils.Constants
import com.example.base_project.Utils.Extensions.formatDate
import com.example.base_project.Utils.Views.loadFromurl
import com.example.base_project.databinding.ActivityProductDetailBinding

class ProductDetailActivity : BaseActivity() {
    private val produtoId by lazy {
        (intent.getSerializableExtra(Constants.EXTRA_MODEL) as Int)
    }
    private lateinit var binding: ActivityProductDetailBinding
    private var model = ProductDetailViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setupView()
        setListeners()
        setObservers()
        model.getProduct(produtoId)
    }

    private fun setupView(){
        binding.navBar.backButton.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setListeners(){
        binding.btnSend.setOnClickListener {
            val returnIntent = Intent()
            returnIntent.putExtra("origin", "detail")
            returnIntent.putExtra("id", produtoId)
            setResult(RESULT_OK, returnIntent)
            finish()
        }
    }

    private fun setObservers(){
        model.loading.observe(this, Observer {
            binding.loading.isVisible = it
        })

        model.errorMessage.observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })

        model.productDetail.observe(this, Observer {
            with(binding){
                txtNome.text = it.name.toString()
                txtOrigem.text = "Origem: ${it.origin.toString()}"
                txtid.text = "Id: ${it.id.toString()}"
                txtData.text = "Data: ${it.createdAt.toString().formatDate()}"
                txtDescricao.setText("Descrição: \n${it.description.toString()}")
                it.avatar?.let { img.loadFromurl(it) }
            }
        })
    }
}


fun Context.createProductDetailIntent(id: Int) =
    Intent(this, ProductDetailActivity::class.java).apply {
        putExtra(Constants.EXTRA_MODEL, id)
    }