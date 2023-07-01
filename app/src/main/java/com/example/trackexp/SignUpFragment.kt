package com.example.trackexp

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.trackexp.databinding.FragmentSignUpBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class SignUpFragment : Fragment(R.layout.fragment_sign_up) {

    private lateinit var binding:FragmentSignUpBinding
    private lateinit var signUpVM: LoginSignUpVM
    private val REQUEST_CODE_SIGN_IN = 0
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_sign_up,container,false)
        signUpVM = ViewModelProvider(this)[LoginSignUpVM::class.java]
        firebaseAuth=FirebaseAuth.getInstance()
        binding.btnSignUp.setOnClickListener {
            signUpNewUser()
        }
        binding.cvSignUpWithGoogle.setOnClickListener {
            //it.findNavController().navigate(R.id.action_signUpFragment_to_homeFragment)
            signInWithGoogleAccount()

        }
        return binding.root
    }




    private fun signInWithGoogleAccount() {

        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(resources.getString(R.string.web_client_id))
            .requestEmail().build()

        val signInClient = GoogleSignIn.getClient( requireContext() ,options)
        signInClient.signInIntent.also {

            startActivityForResult(it,REQUEST_CODE_SIGN_IN)
        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_CODE_SIGN_IN){
            val account = GoogleSignIn.getSignedInAccountFromIntent(data).result
            account?.let {
               CoroutineScope(Dispatchers.IO).launch {
                   val result =signUpVM.signUpWithGoogle(it)
                   if(result.first){
                       withContext(Dispatchers.Main){
                           Toast.makeText(requireContext(),result.second ,Toast.LENGTH_SHORT).show()
                           binding.root.findNavController().navigate(R.id.action_signUpFragment_to_homeFragment)
                       }
                   }else{
                       withContext(Dispatchers.Main){
                           Toast.makeText(requireContext(),result.second ,Toast.LENGTH_SHORT).show()
                       }
                   }
               }

            }
        }
    }



    private fun signUpNewUser(){
        if(isValidCredentials()){
            val signUpMail=binding.etSignUpEmail.text.toString()
            val signUpPassword=binding.etSignUpPassword.text.toString()
            val loadingDialog = LoginLoadingDialog(requireContext(),binding.root as ViewGroup)
            loadingDialog.startLoginLoadingDialog()

                CoroutineScope(Dispatchers.IO).launch {
                    val result =signUpVM.signUpUser(signUpMail,signUpPassword)
                    if(result.first){
                        withContext(Dispatchers.Main){
                            loadingDialog.stopLoginLoadingDialog()
                            Toast.makeText(requireContext(),result.second ,Toast.LENGTH_SHORT).show()
                            binding.root.findNavController().navigate(R.id.action_signUpFragment_to_homeFragment)
                        }
                    }else{
                        withContext(Dispatchers.Main){
                            loadingDialog.stopLoginLoadingDialog()
                            Toast.makeText(requireContext(),result.second ,Toast.LENGTH_SHORT).show()
                        }
                    }
                }

        }

    }

    private fun checkStatus() {
        if(firebaseAuth.currentUser!=null){
            binding.root.findNavController().navigate(R.id.action_signUpFragment_to_homeFragment)
        }
    }

    private fun isValidCredentials(): Boolean {
        val signUpMail=binding.etSignUpEmail.text.toString()
        val signUpPassword=binding.etSignUpPassword.text.toString()
        val signUpPasswordConfirmed=binding.etSignUpPasswordConfirmed.text.toString()
        if(signUpMail.isNotEmpty() && signUpPassword.isNotEmpty() && signUpPasswordConfirmed.isNotEmpty() && signUpPassword == signUpPasswordConfirmed){
            return true
        }
        if(signUpMail.isNotEmpty() && signUpPassword.isNotEmpty() && signUpPasswordConfirmed.isNotEmpty() && signUpPassword != signUpPasswordConfirmed){
            Toast.makeText(requireContext(),"The passwords do not match..",Toast.LENGTH_SHORT).show()
            binding.etSignUpPassword.setText("")
            binding.etSignUpPasswordConfirmed.setText("")
            return false
        }
        else{
            Toast.makeText(requireContext(),"Enter credentials" ,Toast.LENGTH_SHORT).show()
            return false
        }
    }

}