package org.devjj.platform.nurbanhoney.features.ui.home.profile

import org.devjj.platform.nurbanhoney.core.extension.empty

data class SignOutResponse(val result : String){
    companion object{
        val empty = SignOutResponse(String.empty())
    }
}