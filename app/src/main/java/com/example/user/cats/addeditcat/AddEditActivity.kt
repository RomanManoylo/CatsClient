package com.example.user.cats.addeditcat

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import com.example.user.cats.R
import com.example.user.cats.entity.Cat
import com.example.user.cats.login.AddEditContract
import com.example.user.cats.login.AddEditPresenter
import com.example.user.cats.repository.CatsRepository
import com.example.user.cats.utils.LoginUtils
import kotlinx.android.synthetic.main.activity_add_edit_cat.*
import java.io.File

const val PICK_PHOTO_FOR_CAT = 2

class AddEditActivity : AppCompatActivity(), AddEditContract.View {


    private var imageFilePath: Uri? = null
    private var cat:Cat? = null
    private lateinit var catsRepository: CatsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_cat)
        cancel.setOnClickListener(View.OnClickListener {
            this.finish()
        })
        add_edit.setOnClickListener(View.OnClickListener {
            if (cat!=null){
                cat?.name = name.text.toString()
                cat?.info = info.text.toString()
                presenter.addCat(cat!!,getPath(imageFilePath))

            }
            presenter.addCat(Cat(name.text.toString(),info.text.toString()),getPath(imageFilePath))

        })
        select_img.setOnClickListener(View.OnClickListener {
            selectImg()
        })
        val extras = intent.extras
        if (extras != null) {
            cat = intent.getSerializableExtra("cat") as Cat //Obtaining data
        }
        if (cat!=null) {
        initView()
        }
       var loginUtils = LoginUtils(getPreferences(Context.MODE_PRIVATE) ?: return)
        catsRepository = CatsRepository(loginUtils.getLogin(), loginUtils.getPassword())
    }

    fun selectImg() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_PHOTO_FOR_CAT)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, imageReturnedIntent: Intent?) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent)
        if (requestCode == PICK_PHOTO_FOR_CAT && resultCode == Activity.RESULT_OK) {
            imageFilePath = imageReturnedIntent?.data
            imageCat.setImageURI(imageFilePath)
        }

    }
    override var presenter: AddEditContract.Presenter
        get() = AddEditPresenter(catsRepository,this)
        set(value) {}

    override fun savingCompleated() {

        finish()
    }
    private fun getPath(uri: Uri?): String {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = getContentResolver().query(uri, projection, null, null, null)
        val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        return cursor.getString(column_index)
    }

    fun initView(){
        name.setText(cat?.name)
        info.setText(cat?.info)
        val image = File(baseContext.externalCacheDir, cat?.getImage())
        imageCat.setImageBitmap(BitmapFactory.decodeFile(image.absolutePath))
    }

}
