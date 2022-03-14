package org.devjj.platform.nurbanhoney.features.ui.home.profile

import org.devjj.platform.nurbanhoney.core.extension.empty

data class EditProfileResponse(val result : String){
    companion object{
        val empty = EditProfileResponse(String.empty())
    }
}