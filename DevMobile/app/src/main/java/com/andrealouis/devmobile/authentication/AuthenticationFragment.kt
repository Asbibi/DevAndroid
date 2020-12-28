package com.andrealouis.devmobile.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.andrealouis.devmobile.R
import com.andrealouis.devmobile.network.Api


class AuthenticationFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_authentication, container, false)
        return rootView
        //return super.onCreateView(inflater, container, savedInstanceState)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val loginButton = view?.findViewById<Button>(R.id.log_in_authentication_button)
        loginButton.setOnClickListener {
            findNavController().navigate(R.id.action_authenticationFragment_to_loginFragment)
        }
        val signupButton = view?.findViewById<Button>(R.id.sign_up_authentication_button)
        signupButton.setOnClickListener {
            findNavController().navigate(R.id.action_authenticationFragment_to_signupFragment)
        }

        if (Api.INSTANCE.getToken() != ""){
            findNavController().navigate(R.id.action_authenticationFragment_to_taskListFragment)
        }
    }
}