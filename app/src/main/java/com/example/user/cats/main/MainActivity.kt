package com.example.user.cats.main

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import com.example.user.cats.R
import com.example.user.cats.addeditcat.AddEditActivity
import com.example.user.cats.entity.Cat
import com.example.user.cats.login.LoginActivity
import com.example.user.cats.repository.CatsRepository
import com.example.user.cats.utils.LoginUtils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


const val ADD_EDIT_CAT = 1

class MainActivity : AppCompatActivity(), MainContract.View, CatsAdapter.CatItemListener {


    private lateinit var adapter: CatsAdapter
    var alertDialog: AlertDialog? = null
    private var imageFilePath: Uri? = null
    private var catsRepository: CatsRepository? = null
    private lateinit var loginUtils: LoginUtils
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        catsRepository = CatsRepository("", "")
        adapter = CatsAdapter(ArrayList(), baseContext.externalCacheDir.absolutePath, this)

        cats_rv.setHasFixedSize(true)
        cats_rv.layoutManager = LinearLayoutManager(baseContext)
        cats_rv.adapter = adapter
        fab.setOnClickListener { view ->
            val intent = Intent(this, AddEditActivity::class.java)
            startActivityForResult(intent, ADD_EDIT_CAT)
        }

        search_cat.setOnQueryTextListener(object : SearchView.OnQueryTextListener, android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                presenter.searchCat(query)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {


                return false
            }

        })
        search_cat.setOnCloseListener(object : android.widget.SearchView.OnCloseListener {

            override fun onClose(): Boolean {

                search_cat.onActionViewCollapsed()
                presenter.refresh()
                return false
            }
        })
        loginUtils = LoginUtils(application.getSharedPreferences("",Context.MODE_PRIVATE) ?: return)
        if (loginUtils.getLogin() != "" && loginUtils.getPassword() != "") {
            catsRepository = CatsRepository(loginUtils.getLogin(), loginUtils.getPassword())
            presenter.start()
        } else {
            startLoginActivity()
        }


    }

    override fun onResume() {
        super.onResume()
        if (loginUtils.getLogin() != "" && loginUtils.getPassword() != "") {
            presenter.refresh()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return when (item.itemId) {
            R.id.action_exit -> {
                this.finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun showCats(cats: List<Cat>?) {
        cats?.let {
            adapter.cats = it
            adapter.notifyDataSetChanged()
        }

    }

    override var presenter: MainContract.Presenter
        get() = MainPresenter(catsRepository!!, this, baseContext.externalCacheDir.absolutePath)
        set(value) {}


    override fun deleteCat(cat: Cat) {
        presenter.deleteCat(cat)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (data == null) {
            return
        }
        if (requestCode == ADD_EDIT_CAT)
            presenter.refresh()
    }

    override fun updateCat(cat: Cat) {
        val intent = Intent(this, AddEditActivity::class.java)
        intent.putExtra("cat", cat)
        startActivityForResult(intent, ADD_EDIT_CAT)
    }

    fun startLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}
