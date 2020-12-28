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

class LoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val loginButton = view?.findViewById<Button>(R.id.log_in_login_button)
        loginButton.setOnClickListener {
            val emailText = view?.findViewById<TextView>(R.id.email_login_editText)
            val passwordText = view?.findViewById<TextView>(R.id.password_login_editText)
            if ((emailText?.text?.toString() != "") && (passwordText?.text?.toString() != "")){
                lifecycleScope.launch {
                    //var form = LoginForm(emailText?.text!!.toString(), passwordText?.text!!.toString())
                    val connectionToken = Api.INSTANCE.USER_WEB_SERVICE.login(LoginForm(emailText?.text!!.toString(), passwordText?.text!!.toString()))
                    if (connectionToken.isSuccessful){
                        /*PreferenceManager.getDefaultSharedPreferences(context).edit {
                            putString(SHARED_PREF_TOKEN_KEY, connectionToken?.body().token)
                        }*/
                        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(SHARED_PREF_TOKEN_KEY, connectionToken?.body()!!.token).apply()
                        val intent = Intent(activity, MainActivity::class.java)
                        startActivity(intent)
                    }
                    else{
                        Toast.makeText(context, "Mauvais identifiants", Toast.LENGTH_LONG).show()
                    }
                }
            }
            else{
                Toast.makeText(context, "Identifiants incomplets", Toast.LENGTH_LONG).show()
            }
        }
    }
}