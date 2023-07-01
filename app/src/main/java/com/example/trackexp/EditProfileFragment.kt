package com.example.trackexp

import android.content.ContentResolver
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.datastore.core.DataStore
import androidx.fragment.app.DialogFragment
import com.example.trackexp.databinding.FragmentEditProfileBinding
import com.example.trackexp.databinding.FragmentEditTransactionBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.prefs.Preferences


class EditProfileFragment : Fragment() {

    private lateinit var binding: FragmentEditProfileBinding
    private val firebaseAuth =FirebaseAuth.getInstance()
    private var userImgUri = Uri.parse("android.resource://com.example.trackexp/${R.drawable.img_user}")
    private var defaultUserImgUri = Uri.parse("android.resource://com.example.trackexp/${R.drawable.img_user}")


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_edit_profile,container,false)





        firebaseAuth.currentUser?.let { user->

            if(user.displayName!=null){
                binding.etUserName.setText(user.displayName.toString())
                binding.tvGreetUser.text ="Hello, ${user.displayName}"
            }

            if(user.photoUrl != null){
                //binding.ivUserImage.setImageURI(user.photoUrl)
                //val uri : Uri = user.photoUrl!!
                //binding.ivUserImage.setImageURI(uri)
                Log.d("FIREBASEUSERPHOTOURL" ,user.photoUrl.toString())
                //binding.ivUserImage.setImageURI(user.photoUrl)
            }
        }
        //content://media/picker/0/com.android.providers.media.photopicker/media/1000027112
        //content://media/picker/0/com.android.providers.media.photopicker/media/1000027113


        val pickMedia =registerForActivityResult(ActivityResultContracts.PickVisualMedia()){
            if(it==null){
                Toast.makeText(requireContext(),"Could not load image...",Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(requireContext(),"Image loaded successfully...",Toast.LENGTH_SHORT).show()
                userImgUri =it
                Log.d("PHOTOPICKERPHOTOURI",it.toString())
                binding.ivUserImage.setImageURI(it)
            }
        }
        binding.ivUserImage.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
        binding.btnUpdateUserProfile.setOnClickListener {
            updateUserProfile()
            Log.d("FIREBASEUSERPROFILEAFTERUPDATE",firebaseAuth.currentUser?.photoUrl.toString())
        }

        return binding.root
    }


    fun updateUserProfile(){
        val userName =  binding.etUserName.text.toString()
        val profileUpdate = UserProfileChangeRequest.Builder()
            .setDisplayName(userName)
            .setPhotoUri(userImgUri)
            .build()

        val loadingDialog = LoginLoadingDialog(requireContext(),binding.root as ViewGroup)
        loadingDialog?.startLoginLoadingDialog()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                firebaseAuth.currentUser?.updateProfile(profileUpdate)?.await()
                withContext(Dispatchers.Main){
                    loadingDialog.stopLoginLoadingDialog()
                    binding.tvGreetUser.text = "Hello, $userName"
                    Toast.makeText(requireContext(),"Profile updated...",Toast.LENGTH_SHORT).show()
                }
            }catch (e:Exception){
                withContext(Dispatchers.Main){
                    loadingDialog.stopLoginLoadingDialog()
                    binding.tvGreetUser.text = "Hello, $userName"
                    Toast.makeText(requireContext(),"Profile updated...",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}