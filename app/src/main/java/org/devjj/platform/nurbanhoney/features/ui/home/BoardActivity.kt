package org.devjj.platform.nurbanhoney.features.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.view.GravityCompat
import androidx.core.view.children
import androidx.core.view.get
import dagger.hilt.android.AndroidEntryPoint
import org.devjj.platform.nurbanhoney.R
import org.devjj.platform.nurbanhoney.core.extension.observe
import org.devjj.platform.nurbanhoney.core.navigation.Navigator
import org.devjj.platform.nurbanhoney.core.platform.BaseFragment
import org.devjj.platform.nurbanhoney.core.platform.BaseNavigationActivity
import org.devjj.platform.nurbanhoney.databinding.ActivityNavigationBinding
import org.devjj.platform.nurbanhoney.features.Board
import org.devjj.platform.nurbanhoney.features.ui.home.boards.BoardFragment
import org.devjj.platform.nurbanhoney.features.ui.home.boards.subboards.BoardNoticeFragment
import org.devjj.platform.nurbanhoney.features.ui.home.boards.subboards.BoardPopularFragment
import org.devjj.platform.nurbanhoney.features.ui.home.profile.ProfileFragment
import org.devjj.platform.nurbanhoney.features.ui.home.ranking.RankingFragment
import javax.inject.Inject


@AndroidEntryPoint
class BoardActivity : BaseNavigationActivity() {
    override fun fragment() = BoardPopularFragment()
    private val gravity = GravityCompat.START

    @Inject
    lateinit var navigator: Navigator

    private val viewModel by viewModels<BoardAViewModel>()

    private lateinit var binding: ActivityNavigationBinding

    companion object {
        fun callingIntent(context: Context) =
            Intent(context, BoardActivity::class.java)
    }

    private fun renderBoards(boards: List<Board>?) {
        var menu = binding.navigationSideMenu.menu
        var boardList = boards?.map { it.name } ?: listOf()
        boardList.forEachIndexed { i, e ->
            menu.add(R.id.menu_group_boards, i, i, e)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNavigationBinding.inflate(layoutInflater)

        val view = binding.root
        setContentView(view)
       // addFragment(savedInstanceState)
        navigate(BoardPopularFragment())
        with(viewModel) {
            observe(boards, ::renderBoards)
        }

        viewModel.getBoards()

        binding.navigationNavigator.setOnNavigationItemReselectedListener {
            Log.d("navi_check__", it.itemId.toString())
        }

        binding.navigationNavigator.selectedItemId
        binding.navigationNavigator.setOnNavigationItemSelectedListener {

            Log.d("navi_check__", it.itemId.toString())
            when (it.itemId) {
                R.id.menu_popular_board -> {
                    var bundle = Bundle()
                    bundle.putParcelable(
                        R.string.BoardInfo.toString(),
                        Board(-1, "인기글", "popular")
                    )
                    var frag = BoardPopularFragment()
                    frag.arguments = bundle
                    navigate(frag)
                }
                R.id.menu_ranking -> navigate(RankingFragment())
                R.id.menu_profile -> navigate(ProfileFragment())
                else -> navigate(RankingFragment())
            }
            true
        }



        binding.navigationSideMenu.setNavigationItemSelectedListener {
            if (it.groupId == R.id.menu_group_boards) {
                Log.d("navi_check__bottom", it.toString())
                Log.d("navi_check__", it.groupId.toString())
                Log.d("navi_check__", it.itemId.toString())
                var bundle = Bundle()
                bundle.putParcelable(
                    R.string.BoardInfo.toString(),
                    viewModel.boards.value?.get(it.itemId)
                )
                var frag = BoardFragment()
                frag.arguments = bundle
                navigate(frag)
            } else {
                Log.d("navi_check__top", it.itemId.toString())
                when (it.itemId) {
                    R.id.menu_notice_board -> {
                        var bundle = Bundle()
                        bundle.putParcelable(
                            R.string.BoardInfo.toString(),
                            Board(-1, "공지사항", "notice")
                        )
                        var frag = BoardNoticeFragment()
                        frag.arguments = bundle
                        navigate(frag)
                    }
                    R.id.menu_popular_board -> {
                        var bundle = Bundle()
                        bundle.putParcelable(
                            R.string.BoardInfo.toString(),
                            Board(-1, "인기글", "popular")
                        )
                        var frag = BoardPopularFragment()
                        frag.arguments = bundle
                        navigate(frag)
                    }
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

        supportFragmentManager.addOnBackStackChangedListener {

            Log.d(
                "onBackPressed_check",
                "listened backStackChanged stack size = ${supportFragmentManager.backStackEntryCount}"
            )
            for(i in 0 until supportFragmentManager.backStackEntryCount) {
                Log.d(
                    "onBackPressed_check",
                    "listened backStackChanged $i = ${supportFragmentManager.getBackStackEntryAt(i).name}"
                )
            }
            if (supportFragmentManager.backStackEntryCount > 0) {
                supportFragmentManager.getBackStackEntryAt(supportFragmentManager.backStackEntryCount - 1)
                    .apply {
                        Log.d("fragment_stack_check", this.name.toString())
                        when (this.name.toString()) {
                            BoardPopularFragment::class.java.simpleName -> {
                                //binding.navigationNavigator.selectedItemId = R.id.menu_popular_board
                                binding.navigationNavigator.menu[0].isChecked = true
                            }
                            RankingFragment::class.java.simpleName -> {
                               // binding.navigationNavigator.selectedItemId = R.id.menu_ranking
                                binding.navigationNavigator.menu[1].isChecked = true
                            }
                            ProfileFragment::class.java.simpleName -> {
                                //binding.navigationNavigator.selectedItemId = R.id.menu_profile
                                binding.navigationNavigator.menu[2].isChecked = true
                            }
                        }
                    }
            }
        }
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

    fun setActionBarTitle(title: String) {
        supportActionBar?.title = title
    }

    private fun navigate(fragment: BaseFragment) =
        navigator.transFragment(
            supportFragmentManager,
            fragment,
            binding.navigationFragmentContainer
        ).let {
            Log.d("navigate","navigated to ${fragment::class.java.simpleName}")
        }

}