package org.devjj.platform.nurbanhoney.features.ui.home.profile

import android.app.Activity
import org.devjj.platform.nurbanhoney.core.navigation.Navigator
import org.devjj.platform.nurbanhoney.databinding.FragmentProfileBinding
import javax.inject.Inject

class ProfileListener
@Inject constructor(){
    fun clickListener(binding: FragmentProfileBinding,
                      activity: Activity,
                      navigator: Navigator,
                      fragment: ProfileFragment
    ) {
        // 백키 눌렀을 때 이벤트 메소드
        binding.tvEdit.setOnClickListener {
            activity.finish()
        }

        //

    }
}