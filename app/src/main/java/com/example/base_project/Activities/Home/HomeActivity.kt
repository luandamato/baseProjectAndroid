package com.example.base_project.Activities.Home

import android.app.Activity
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.NotificationCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.example.base_project.Activities.Home.Models.ProductResponse
import com.example.base_project.Activities.Login.createLoginIntent
import com.example.base_project.Activities.NewProduct.createNewProductIntent
import com.example.base_project.Activities.ProductDetail.createProductDetailIntent
import com.example.base_project.R
import com.example.base_project.Utils.BaseActivity
import com.example.base_project.Utils.Constants
import com.example.base_project.Utils.NotificationService
import com.example.base_project.Utils.PreferencesHelper
import com.example.base_project.Utils.Views.CustomDialog
import com.example.base_project.databinding.ActivityHomeBinding


class HomeActivity : BaseActivity(){
    private val INTENT_PRODUCT = 1
    private val logado by lazy {
        (intent.getSerializableExtra(Constants.EXTRA_MODEL) as Boolean)
    }
    private lateinit var binding: ActivityHomeBinding
    private var model = HomeViewModel()
    private val adapter by lazy { HomeAdapter() }
    private var itemSelecionado: ProductResponse? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        if (logado){
            val nome = PreferencesHelper.session?.firstName
            binding.appBarContainer.txtInfo.text = "Bem vindo de volta, $nome"
            registerForContextMenu(binding.appBarContainer.txtInfo)
        }
        else binding.appBarContainer.txtInfo.text = "Você não está logado"
        setListeners()
        setObservers()

        model.get()
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menu.setHeaderTitle("Choose your option")
        menuInflater.inflate(R.menu.home_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.getItemId()) {
            R.id.option_1 -> {
                Toast.makeText(this, "Option 1 selected", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.option_2 -> {
                Toast.makeText(this, "Option 2 selected", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    override fun onStop() {
        super.onStop()
        startService(Intent(this, NotificationService::class.java))
    }
    override fun onBackPressed() {
        val a = Intent(Intent.ACTION_MAIN)
        a.addCategory(Intent.CATEGORY_HOME)
        a.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(a)
    }


    private fun setupRecycler(guides: List<ProductResponse>?) {
        with(binding) {
            recyclerView.adapter = adapter
            adapter.update(guides)
            adapter.setOnClickListener = {
                if (it != null) {
                    it.id?.toInt()?.let { it1 ->
                        productDetail(it1)
                    }
                }
            }
            adapter.setOnLongClickListener = { item, view ->
                view.setBackgroundColor(getColor(R.color.red))
                CustomDialog(this@HomeActivity).setTitle("Deletar")
                    .setBody("Deseja realmente deletar o item ${item.name}, de ID ${item.id}?")
                    .setOkAction { deleteItem(item) }
                    .setCancelAction { view.setBackgroundColor(getColor(R.color.backgroud)) }
                    .show()
            }
        }
    }

    private fun deleteItem(item: ProductResponse){
        item.id?.let { model.delete(it.toInt()) }
        Toast.makeText(this, "Item ${item.name} excluido", Toast.LENGTH_SHORT).show()
    }

    private fun setListeners(){
        binding.appBarContainer.menu.setOnClickListener {
            CustomDialog(this).setTitle("Logout")
                .setBody("Deseja realmente fazer o logout?")
                .setOkAction { logout() }
                .show()
        }

        binding.btnAdd.setOnClickListener {
            editProduct(null)
        }

        binding.appBarContainer.alertMenu.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            val contentIntent =
                PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

            val b = NotificationCompat.Builder(this)

            b.setAutoCancel(false)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.l)
                .setTicker("Hearty365")
                .setContentTitle("Default notification")
                .setContentText("Lorem ipsum dolor sit amet, consectetur adipiscing elit.")
                .setDefaults(Notification.DEFAULT_LIGHTS or Notification.DEFAULT_SOUND)
                .setContentIntent(contentIntent)
                .setContentInfo("Info")
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(1, b.build())
        }
    }

    private fun setObservers(){
        model.loading.observe(this, Observer {
            binding.loading.isVisible = it
            binding.viewInfos.isVisible = !it
        })

        model.errorMessage.observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })

        model.products.observe(this, Observer {
            if (!it.isNullOrEmpty()) setupRecycler(it)
        })
    }

    private fun logout(){
        PreferencesHelper.session = null
        startActivity(createLoginIntent())
    }

    private fun editProduct(id: Int?){
        resultLauncher.launch( createNewProductIntent(id))
    }
    private fun productDetail(id: Int){
        resultLauncher.launch( createProductDetailIntent(id))
    }

    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            when (result.data?.getStringExtra("origin")){
                "new" -> {
                    if (result.resultCode == Activity.RESULT_OK) {
                        Toast.makeText(this, "Sucesso", Toast.LENGTH_SHORT).show()
                        model.get()
                    }
                    else if(result.resultCode == Activity.RESULT_CANCELED){
                        Toast.makeText(this, "Cancelado", Toast.LENGTH_SHORT).show()
                    }
                }
                "detail" -> {
                    if (result.resultCode == Activity.RESULT_OK) {
                        result.data?.getIntExtra("id", 0)?.let { editProduct(it) }
                    }
                }
            }

    }
}


fun Context.createHomeIntent(loggedIn: Boolean) =
    Intent(this, HomeActivity::class.java).apply {
        putExtra(Constants.EXTRA_MODEL, loggedIn)
    }