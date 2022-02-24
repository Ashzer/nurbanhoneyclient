package org.devjj.platform.nurbanhoney.features.ui.home.boards.subboards

import android.os.Bundle
import android.view.View
import org.devjj.platform.nurbanhoney.core.extension.invisible
import org.devjj.platform.nurbanhoney.features.ui.home.boards.BoardFragment

class BoardNoticeFragment : BoardFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.boardWriteFab.invisible()
    }
}