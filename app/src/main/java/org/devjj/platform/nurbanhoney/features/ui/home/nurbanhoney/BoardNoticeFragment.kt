package org.devjj.platform.nurbanhoney.features.ui.home.nurbanhoney

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.devjj.platform.nurbanhoney.core.extension.invisible

class BoardNoticeFragment : BoardFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.boardWriteFab.invisible()
    }
}