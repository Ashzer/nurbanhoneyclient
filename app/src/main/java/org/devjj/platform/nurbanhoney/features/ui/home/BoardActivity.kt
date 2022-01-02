package org.devjj.platform.nurbanhoney.features.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.view.GravityCompat
import dagger.hilt.android.AndroidEntryPoint
import org.devjj.platform.nurbanhoney.R
import org.devjj.platform.nurbanhoney.core.extension.observe
import org.devjj.platform.nurbanhoney.core.navigation.Navigator
import org.devjj.platform.nurbanhoney.core.platform.BaseFragment
import org.devjj.platform.nurbanhoney.core.platform.BaseTabLayoutActivity
import org.devjj.platform.nurbanhoney.databinding.ActivityNavigationBinding
import org.devjj.platform.nurbanhoney.features.ui.home.nurbanhoney.BoardFragment
import org.devjj.platform.nurbanhoney.features.ui.home.nurbanhoney.BoardViewModel
import org.devjj.platform.nurbanhoney.features.ui.home.profile.ProfileFragment
import org.devjj.platform.nurbanhoney.features.ui.home.ranking.RankingFragment
import org.devjj.platform.nurbanhoney.features.ui.splash.Board
import javax.inject.Inject


@AndroidEntryPoint
class BoardActivity : BaseTabLayoutActivity() {
    override fun fragment() = BoardFragment()
    private val gravity = GravityCompat.START

    @Inject
    lateinit var navigator: Navigator

    private val viewModel by viewModels<BoardViewModel>()

    private lateinit var binding: ActivityNavigationBinding

    companion object {
        fun callingIntent(context: Context) =
            Intent(context, BoardActivity::class.java)
    }

    private fun renderBoards(boards : List<Board>?){
        var menu = binding.navigationSideMenu.menu
        var boardList = boards?.map{it.name} ?: listOf()

        boardList.forEachIndexed { i, e ->
            menu.add(R.id.menu_group_boards, i, i, e)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNavigationBinding.inflate(layoutInflater)

        val view = binding.root
        setContentView(view)
        addFragment(savedInstanceState)

        with(viewModel){
            observe(boards, ::renderBoards)
        }

        viewModel.getBoards()


        binding.navigationNavigator.setOnNavigationItemSelectedListener {

            Log.d("navi_check__", it.itemId.toString())
            when (it.itemId) {
                R.id.menu_popular_board -> navigate(BoardFragment())
                R.id.menu_ranking -> navigate(RankingFragment())
                R.id.menu_profile -> navigate(ProfileFragment())
                else -> navigate(RankingFragment())
            }
            true
        }



        binding.navigationSideMenu.setNavigationItemSelectedListener {
            if (it.groupId == R.id.menu_group_boards) {
                Log.d("navi_check__bottom", it.toString())
                viewModel.setCurrentBoard(it.itemId)
                Log.d("navi_check__", it.groupId.toString())
                Log.d("navi_check__", it.itemId.toString())
                var bundle = Bundle()
                bundle.putInt(R.string.Board_ID.toString(),it.itemId)
                bundle.putString(R.string.Board_NAME.toString(),viewModel.boardName)
                bundle.putString(R.string.Board_ADDRESS.toString(),viewModel.board)
                var frag = BoardFragment()
                frag.arguments=bundle
                navigate(frag)
            } else {
                Log.d("navi_check__top", it.itemId.toString())
                when (it.itemId) {
                    R.id.menu_popular_board -> {}
                    R.id.menu_ranking -> navigate(RankingFragment())
                    R.id.menu_profile -> navigate(ProfileFragment())
                }
            }

            binding.navigationDrawer.closeDrawer(GravityCompat.START)
            false
        }

        setSupportActionBar(binding.navigationToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_action_menu)
    }

    override fun onBackPressed() {
        if (binding.navigationDrawer.isDrawerOpen(gravity)) {
            binding.navigationDrawer.closeDrawer(gravity)
        } else {
            super.onBackPressed()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                val layoutDrawer = binding.navigationDrawer

                if (layoutDrawer.isDrawerOpen(gravity)) {
                    layoutDrawer.closeDrawer(gravity)
                } else {
                    layoutDrawer.openDrawer(gravity)
                }
            }
            else -> {
            }
        }
        return false
    }

    public fun setActionBarTitle(title: String){
        supportActionBar?.title = title
    }

    private fun navigate(fragment: BaseFragment) =
        navigator.transFragment(
            supportFragmentManager,
            fragment,
            binding.navigationFragmentContainer
        )

}