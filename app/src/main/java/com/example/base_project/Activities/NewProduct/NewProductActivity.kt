package com.example.base_project.Activities.NewProduct

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.example.base_project.Activities.NewProduct.Model.ProductRequest
import com.example.base_project.Utils.BaseActivity
import com.example.base_project.Utils.Constants
import com.example.base_project.databinding.ActivityNewProductBinding


class NewProductActivity : BaseActivity() {
    private val produtoId by lazy {
        (intent.getSerializableExtra(Constants.EXTRA_MODEL) as Int?)
    }
    private var novoProduto = true
    private lateinit var binding: ActivityNewProductBinding
    private var model = NewProductViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewProductBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setupView()
        verifyProduct()
        setListeners()
        setObservers()
    }

    private fun setupView(){
        binding.navBar.backButton.setOnClickListener {
            onBackPressed()
        }
    }

    private fun verifyProduct(){
        if (produtoId != null) {
            novoProduto = false
            binding.viewTitulo.setTitle("Atualizar Produto")
            binding.viewTitulo.setSubtitle("Informe todos os dados do produto para ser atualizado!")
            binding.btnSend.setButtonText("Atualizar")
            model.getProduct(produtoId!!)
        }
    }

    private fun setListeners(){
        binding.btnSend.setOnClickListener {
            if (allFieldsValid()){
                with (binding){
                    val produto = ProductRequest(txtNome.text, txtDesc.text.toString(), txtOrigem.text)
                    if(novoProduto) model.createProduct(produto)
                    else model.updateProduct(produtoId!!, produto)
                }
            }
        }
    }

    private fun setObservers(){
        model.loading.observe(this, Observer {
            binding.loading.isVisible = it
        })
        model.loadingRequest.observe(this, Observer {
            binding.btnSend.setLoading(it)
        })

        model.errorMessage.observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })

        model.product.observe(this, Observer {
            val returnIntent = Intent()
            returnIntent.putExtra("origin", "new")
            setResult(RESULT_OK, returnIntent)
            finish()
        })

        model.productDetail.observe(this, Observer {
            with(binding){
                txtNome.text = it.name.toString()
                txtOrigem.text = it.origin.toString()
                txtDesc.setText(it.description.toString())

            }
        })
    }

    private fun allFieldsValid(): Boolean {
        with(binding){
            txtNome.validate()
            txtOrigem.validate()
            return txtNome.isFieldValid && txtOrigem.isFieldValid
        }
    }
}


fun Context.createNewProductIntent(id: Int?) =
    Intent(this, NewProductActivity::class.java).apply {
        putExtra(Constants.EXTRA_MODEL, id)
    }