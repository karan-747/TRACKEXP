package com.example.trackexp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import com.example.trackexp.databinding.FragmentLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.lang.Exception


class LoginFragment : Fragment(R.layout.fragment_login) {
    private lateinit var binding:FragmentLoginBinding


    private val REQUEST_CODE_SIGN_IN = 0
    private lateinit var loginFragmentViewModel: LoginSignUpVM
    private lateinit var firebaseAuth: FirebaseAuth
    private  var firebaseUser: FirebaseUser? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_login,container,false)

        loginFragmentViewModel = ViewModelProvider(this)[LoginSignUpVM::class.java]



         firebaseAuth = FirebaseAuth.getInstance()
        firebaseUser = firebaseAuth.currentUser




//        if(firebaseUser !=null){
//            if(controller.currentDestination?.id == R.id.loginFragment){
//                Toast.makeText(requireContext(),"yes",Toast.LENGTH_SHORT).show()
//            }
//        }

        binding.btnLogin1.setOnClickListener {
            loginTheUser()
        }


        binding.tvSignUp.setOnClickListener {
            binding.root.findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
        }
        binding.cvLogInWithGoogle.setOnClickListener {
            signInWithGoogleAccount()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }


    private fun NavController.safeNavigate(direction :NavDirections){
        Toast.makeText(requireContext(),"User was Logged In a",Toast.LENGTH_SHORT).show()
        currentDestination?.getAction(direction.actionId)?.run {
            //navigate(direction)
            Toast.makeText(requireContext(),"User was Logged In b ",Toast.LENGTH_SHORT).show()
            //navigate(direction)
        }
        Toast.makeText(requireContext(),"User was Logged In c",Toast.LENGTH_SHORT).show()
    }






    private fun signInWithGoogleAccount() {

        val options =GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
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
                googleAuthForFireBase(it)
            }
        }
    }

    private fun googleAuthForFireBase(account: GoogleSignInAccount) {

        val loadingDialog = LoginLoadingDialog(requireContext(),binding.root as ViewGroup)
        loadingDialog.startLoginLoadingDialog()
        CoroutineScope(Dispatchers.IO).launch {

            val result = loginFragmentViewModel.signInWithGoogle(account)
            if(result.first){
                withContext(Dispatchers.Main){
                    loadingDialog.stopLoginLoadingDialog()
                    Toast.makeText(requireContext(),result.second,Toast.LENGTH_SHORT).show()
                    binding.root.findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                }
            }else{
                withContext(Dispatchers.Main){
                    loadingDialog.stopLoginLoadingDialog()
                    Toast.makeText(requireContext(),result.second,Toast.LENGTH_SHORT).show()

                }

            }
        }
    }


    private fun validCredentials(): Boolean {
        val loginEmail=binding.etLoginEmail.text.toString()
        val loginPassword=binding.etLoginPassword.text.toString()
        return loginPassword.isNotEmpty() && loginEmail.isNotEmpty()
    }



    private fun loginTheUser(){
        if(validCredentials()){
            val loginEmail=binding.etLoginEmail.text.toString()
            val loginPassword=binding.etLoginPassword.text.toString()

            val loadingDialog = LoginLoadingDialog(requireContext(),binding.root as ViewGroup)
            loadingDialog.startLoginLoadingDialog()

            CoroutineScope(Dispatchers.IO).launch {
                val result = loginFragmentViewModel.loginUser(loginEmail,loginPassword)
                if(result.first){
                    withContext(Dispatchers.Main){
                        loadingDialog.stopLoginLoadingDialog()
                        Toast.makeText(requireContext(),result.second,Toast.LENGTH_SHORT).show()

                        binding.root.findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                    }
                }
                else{
                    withContext(Dispatchers.Main){
                        loadingDialog.stopLoginLoadingDialog()
                        Toast.makeText(requireContext(),result.second,Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }else{
            Toast.makeText(requireContext(),"Enter credentials..",Toast.LENGTH_SHORT).show()
        }
    }










}