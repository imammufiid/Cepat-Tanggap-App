package com.example.cepattanggapapp.activities

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.cepattanggapapp.R
import com.example.cepattanggapapp.utils.Constants
import com.example.cepattanggapapp.viewmodels.UserState
import com.example.cepattanggapapp.viewmodels.UserViewModel
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class EditProfileActivity : AppCompatActivity() {
    var photoFile: File? = null
    private val REQUEST_TAKE_PHOTO = 1
    lateinit var currentPhotoPath: String
    private var profile_img: Bitmap? = null

    var output_file_name = String

    private lateinit var userViewModel: UserViewModel
    private lateinit var context: Context
    private val REQUEST_IMAGE_CAPTURE = 1
    private var imagePath = ""
    private lateinit var imageName: MultipartBody.Part

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        setSupportActionBar(main_toolbar)
        supportActionBar?.title = ""
        title_toolbar.text = "Edit Profile"

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = resources.getColor(R.color.colorWhite)

        // init view models
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        userViewModel.getState().observer(this, Observer {
            handleUIState(it)
        })

        checkPermission()

        et_nama_lengkap.setText(Constants.getNamaUser(this))
        et_username.setText(Constants.getEmail(this))
        et_alamat.setText(Constants.getAlamat(this))

        btn_save.setOnClickListener {
            val namaLengkap = et_nama_lengkap.text.toString()
            val username = et_username.text.toString()
            val alamat = et_alamat.text.toString()

            // photo
            val file = File(currentPhotoPath)
            // val file = createFile(profile_img!!)
            val reqFile: RequestBody = RequestBody.create(MediaType.parse("image/*"), file!!)
            val part = MultipartBody.Part.createFormData("newimage", file.name, reqFile)

            userViewModel.editProfile(Constants.getToken(this), namaLengkap, username, alamat)
        }

        profile_image.setOnClickListener {
            captureImage()
        }
    }

    private fun doUpload() {
        // photo
        val file = File(currentPhotoPath) // path
        // val file = createFile(profile_img!!) // bitmap
        val reqFile: RequestBody = RequestBody.create(MediaType.parse("image/*"), file)
        val part = MultipartBody.Part.createFormData("image", file.name, reqFile)
        userViewModel.uploadProfile(Constants.getToken(this), part)
    }

    /*
    * function for capture image from camera
    * */
    private fun captureImage() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a` camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                photoFile = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    Log.e("ERROR PHOTO", ex.message!!)
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "com.example.cepattanggapapp.fileprovider",
                        it
                    )
                    Log.d("PHOTOURI", photoURI.toString())
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // GlobalScope.launch {
            // upload
            // launch {
            // ---------------------------------------------------------
            val myBitmap = BitmapFactory.decodeFile(photoFile?.absolutePath)
            rotate(myBitmap)
            Log.d("myBitmap", myBitmap.toString())
            Log.d("PATH", photoFile?.absolutePath.toString())
            // }
            doUpload()
            //}


        }
    }

    /*
    * function for rotate bitmap
    * */
    private fun rotate(bitmap: Bitmap) {
        val ei = ExifInterface(currentPhotoPath)
        val orientation =
            ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)
        Log.d("EXIF value", ei.getAttribute(ExifInterface.TAG_ORIENTATION).toString());
        var rotateBitmap: Bitmap? = null

        val eiValue = ei.getAttribute(ExifInterface.TAG_ORIENTATION)?.toInt()
        when (eiValue) {
            6 -> rotateImg(bitmap, 90F)
            8 -> rotateImg(bitmap, 270F)
            3 -> rotateImg(bitmap, 180F)
            1 -> rotateImg(bitmap, 0F)
            0 -> rotateImg(bitmap, 90F)
        }
    }

    private fun rotateImg(bitmap: Bitmap, i: Float) {
        val matrix = Matrix()
        matrix.setRotate(i)
        val img = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        profile_img = img
        profile_image.setImageBitmap(img)
    }

    /*
    * function for create bitmap to file
    * */
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val root = Environment.getExternalStorageDirectory().toString()
        val myDir = File("$root/capture_photo")
        myDir.mkdirs()
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
            Log.d("currentPhotoPath", currentPhotoPath)
        }
    }

    /*
    * function to create file
    * */
    private fun createFile(bitmap: Bitmap): File? {
        val file = File(
            getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            , System.currentTimeMillis().toString() + "_image.jpg"
        )
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos)
        val bitmapData: ByteArray = bos.toByteArray()
        //write the bytes in file
        try {
            val fos = FileOutputStream(file)
            fos.write(bitmapData)
            fos.flush()
            fos.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return file
    }

    /*
    * function check permission for SDK >= 23 / LOLLIPOP
    * */
    private fun checkPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || checkSelfPermission(
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED || checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        android.Manifest.permission.CAMERA,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ),
                    1
                )
            }
        }
    }

    /*
    * function handle UI from view model
    * */
    private fun handleUIState(it: UserState?) {
        when (it) {
            is UserState.IsLoading -> isLoading(it.state)
            is UserState.SuccessEdit -> isSuccess(it.message)
            is UserState.Error -> showToast(it.err)
            is UserState.Validation -> {
                it.nama_lengkap?.let {
                    setNamaError(it)
                }
                it.email?.let {
                    setEmailError(it)
                }
                it.alamat?.let {
                    setAlamatError(it)
                }
            }
        }
    }

    private fun setAlamatError(it: String) {
        et_alamat.error = "This field Required!"
    }

    private fun setEmailError(it: String) {
        et_username.error = "This field Required!"
    }

    private fun setNamaError(it: String) {
        et_nama_lengkap.error = "This field Required!"
    }

    private fun showToast(it: String) {
        Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
    }

    private fun isSuccess(message: String?) {
        showToast(message!!)
    }

    private fun isLoading(state: Boolean) {
        btn_save.isEnabled = !state
    }

    //------------------------------------------------------------------------------


}