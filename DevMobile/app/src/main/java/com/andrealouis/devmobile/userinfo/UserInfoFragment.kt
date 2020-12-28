package com.andrealouis.devmobile.userinfo

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import coil.load
import com.andrealouis.devmobile.BuildConfig
import com.andrealouis.devmobile.R
import com.andrealouis.devmobile.authentication.SHARED_PREF_TOKEN_KEY
import com.andrealouis.devmobile.network.Api
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File


class UserInfoFragment : Fragment() {
    companion object {
        const val EDIT_USER_INFO_REQUEST_CODE = 600
        const val USER_INFO_KEY = "newUserInfo"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val takePictureButton = view?.findViewById<Button>(R.id.take_picture_button)
        takePictureButton.setOnClickListener {
            askCameraPermissionAndOpenCamera()
        }
        val pickPictureButton = view?.findViewById<Button>(R.id.upload_image_button)
        pickPictureButton.setOnClickListener {
            // use
            pickInGallery.launch("image/*")
        }
        val validateButton = view?.findViewById<Button>(R.id.userInfo_valid_button)
        validateButton.setOnClickListener {
            val lastName = view?.findViewById<EditText>(R.id.editTextUserInfoLastName).text.toString()
            val firstName = view?.findViewById<EditText>(R.id.editTextUserInfoFirstName).text.toString()
            val mail = view?.findViewById<EditText>(R.id.editTextUserInfoMail).text.toString()

            val newUserInfo = UserInfo(mail,firstName,lastName)
            findNavController().currentBackStackEntry?.savedStateHandle?.set(USER_INFO_KEY, newUserInfo)
            findNavController().navigate(R.id.action_userInfoFragment_to_taskListFragment)

            /*intent.putExtra(UserInfoActivity.USER_INFO_KEY, newUserInfo)
            setResult(AppCompatActivity.RESULT_OK, intent)
            finish()*/
        }
        val logoutButton = view?.findViewById<ImageButton>(R.id.log_out_imageButton)
        logoutButton.setOnClickListener {
            PreferenceManager.getDefaultSharedPreferences(context).edit().remove(SHARED_PREF_TOKEN_KEY).apply()
            findNavController().navigate(R.id.action_userInfoFragment_to_authenticationFragment)
        }



        val userInfoPrevious = findNavController().previousBackStackEntry?.savedStateHandle?.get(USER_INFO_KEY) as? UserInfo
        //val userInfoPrevious = intent.getSerializableExtra(UserInfoActivity.USER_INFO_KEY) as? UserInfo
        if (userInfoPrevious != null){
            view?.findViewById<EditText>(R.id.editTextUserInfoLastName).setText(userInfoPrevious.lastName)
            view?.findViewById<EditText>(R.id.editTextUserInfoFirstName).setText(userInfoPrevious.firstName)
            view?.findViewById<EditText>(R.id.editTextUserInfoMail).setText(userInfoPrevious.email)
        }

        val imageAvatar = view?.findViewById<ImageView>(R.id.image_view)
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
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED -> openCamera()
            shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> showExplanationDialog()
            else -> requestCameraPermission()
        }
    }

    private fun showExplanationDialog() {
        AlertDialog.Builder(activity).apply {
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
            requireContext(),
            BuildConfig.APPLICATION_ID +".fileprovider",
            File.createTempFile("avatar", ".jpeg", activity?.externalCacheDir!!)

        )
    }

    // register
    private val takePicture = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) handleImage(photoUri)
        else Toast.makeText(requireContext(), "Erreur ! 😢", Toast.LENGTH_LONG).show()
    }

    // use
    private fun openCamera() = takePicture.launch(photoUri)


    // convert
    private fun convert(uri: Uri) =
        MultipartBody.Part.createFormData(
            name = "avatar",
            filename = "temp.jpeg",
            body = activity?.contentResolver?.openInputStream(uri)!!.readBytes().toRequestBody()
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