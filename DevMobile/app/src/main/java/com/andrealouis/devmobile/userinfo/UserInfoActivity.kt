package com.andrealouis.devmobile.userinfo

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import coil.load
import com.andrealouis.devmobile.BuildConfig
import com.andrealouis.devmobile.R
import com.andrealouis.devmobile.network.Api
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class UserInfoActivity : AppCompatActivity() {
    companion object {
        const val EDIT_USER_INFO_REQUEST_CODE = 600
        const val USER_INFO_KEY = "newUserInfo"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)

        val takePictureButton = findViewById<Button>(R.id.take_picture_button)
        takePictureButton.setOnClickListener {
            askCameraPermissionAndOpenCamera()
        }
        val pickPictureButton = findViewById<Button>(R.id.upload_image_button)
        pickPictureButton.setOnClickListener {
            // use
            pickInGallery.launch("image/*")
        }
        val validateButton = findViewById<Button>(R.id.userInfo_valid_button)
        validateButton.setOnClickListener {
            val lastName = findViewById<EditText>(R.id.editTextUserInfoLastName).text.toString()
            val firstName = findViewById<EditText>(R.id.editTextUserInfoFirstName).text.toString()
            val mail = findViewById<EditText>(R.id.editTextUserInfoMail).text.toString()



            val newUserInfo = UserInfo(mail,firstName,lastName)

            intent.putExtra(USER_INFO_KEY, newUserInfo)
            setResult(RESULT_OK, intent)
            finish()
        }


        val userInfoPrevious = intent.getSerializableExtra(USER_INFO_KEY) as? UserInfo
        if (userInfoPrevious != null){
            findViewById<EditText>(R.id.editTextUserInfoLastName).setText(userInfoPrevious.lastName)
            findViewById<EditText>(R.id.editTextUserInfoFirstName).setText(userInfoPrevious.firstName)
            findViewById<EditText>(R.id.editTextUserInfoMail).setText(userInfoPrevious.email)
        }

        val imageAvatar = findViewById<ImageView>(R.id.image_view)
        lifecycleScope.launch {
            val userInfo = Api.INSTANCE.USER_WEB_SERVICE.getInfo().body()!!
            imageAvatar.load(userInfo.avatar)
        }
    }

    private val requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
                if (isGranted) openCamera()
                else showExplanationDialog()
            }

    private fun requestCameraPermission() =
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)

    private fun askCameraPermissionAndOpenCamera() {
        when {
            ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED -> openCamera()
            shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> showExplanationDialog()
            else -> requestCameraPermission()
        }
    }

    private fun showExplanationDialog() {
        AlertDialog.Builder(this).apply {
            setMessage("On a besoin de la caméra sivouplé ! 🥺")
            setPositiveButton("Bon, ok") { _, _ ->
                requestCameraPermission()
            }
            setCancelable(true)
            show()
        }
    }

    // create a temp file and get a uri for it
    private val photoUri by lazy {
        FileProvider.getUriForFile(
                this,
                BuildConfig.APPLICATION_ID +".fileprovider",
                File.createTempFile("avatar", ".jpeg", externalCacheDir)

        )
    }

    // register
    private val takePicture = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) handleImage(photoUri)
        else Toast.makeText(this, "Erreur ! 😢", Toast.LENGTH_LONG).show()
    }

    // use
    private fun openCamera() = takePicture.launch(photoUri)

    /*
    // register
    private val takePicture = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        val tmpFile = File.createTempFile("avatar", "jpeg")
        tmpFile.outputStream().use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
        }
        handleImage(tmpFile.toUri())
    }

    // use
    private fun openCamera() = takePicture.launch()
    */

    // convert
    private fun convert(uri: Uri) =
            MultipartBody.Part.createFormData(
                    name = "avatar",
                    filename = "temp.jpeg",
                    body = contentResolver.openInputStream(uri)!!.readBytes().toRequestBody()
            )


    private fun handleImage(uri: Uri){
        lifecycleScope.launch {
            Api.INSTANCE.USER_WEB_SERVICE.updateAvatar(convert(uri))
        }
    }

    // register
    private val pickInGallery =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            handleImage(uri)
        }
}