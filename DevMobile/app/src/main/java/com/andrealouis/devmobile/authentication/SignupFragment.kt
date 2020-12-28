package com.andrealouis.devmobile.authentication

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import com.andrealouis.devmobile.MainActivity
import com.andrealouis.devmobile.R
import com.andrealouis.devmobile.network.Api
import kotlinx.coroutines.launch

class SignupFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val signupButton = view?.findViewById<Button>(R.id.sign_up_signup_button)
        signupButton.setOnClickListener {
            val firstnameText = view?.findViewById<TextView>(R.id.first_name_signup_editText)
            val lastnameText = view?.findViewById<TextView>(R.id.last_name_signup_editText)
            val emailText = view?.findViewById<TextView>(R.id.email_signup_editText)
            val passwordText = view?.findViewById<TextView>(R.id.password_signup_editText)
            val passwordConfirmationText = view?.findViewById<TextView>(R.id.password_confirmation_signup_editText)

            if ((emailText?.text?.toString() != "") && (firstnameText?.text?.toString() != "") && (lastnameText?.text?.toString() != "") && (passwordText?.text?.toString() != "") && (passwordText?.text?.toString() == passwordConfirmationText?.text?.toString())){
                lifecycleScope.launch {
                    val connectionToken = Api.INSTANCE.USER_WEB_SERVICE.signup(SignupForm(firstnameText?.text!!.toString(),
                            lastnameText?.text!!.toString(),
                            emailText?.text!!.toString(),
                            passwordText?.text!!.toString(),
                            passwordConfirmationText?.text!!.toString()))

                    if (connectionToken.isSuccessful){
                        /*PreferenceManager.getDefaultSharedPreferences(context).edit {
                            putString(SHARED_PREF_TOKEN_KEY, connectionToken?.body().token)
                        }*/
                        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(SHARED_PREF_TOKEN_KEY, connectionToken?.body()!!.token).apply()
                        val intent = Intent(activity, MainActivity::class.java)
                        startActivity(intent)
                    }
                    else{
                        Toast.makeText(context, "Inscription refusée", Toast.LENGTH_LONG).show()
                    }
                }
            }
            else{
                Toast.makeText(context, "Champs manquants ou mots de passe différents", Toast.LENGTH_LONG).show()
            }
        }
    }
}