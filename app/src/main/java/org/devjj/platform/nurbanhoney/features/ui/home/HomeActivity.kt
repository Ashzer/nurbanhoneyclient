package org.devjj.platform.nurbanhoney.features.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.view.GravityCompat
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
class HomeActivity : BaseNavigationActivity() {
    override fun fragment() = BoardPopularFragment()
    private val gravity = GravityCompat.END

    @Inject
    lateinit var navigator: Navigator

    private val viewModel by viewModels<HomeViewModel>()

    private lateinit var binding: ActivityNavigationBinding

    companion object {
        fun callingIntent(context: Context) =
            Intent(context, HomeActivity::class.java)
    }

    private fun renderBoards(boards: List<Board>?) {
        val menu = binding.navigationSideMenu.menu
        val boardList = boards?.map { it.name } ?: listOf()
        boardList.forEachIndexed { i, e ->
            menu.add(R.id.menu_group_boards, i, i, e)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNavigationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        launchPopularFragment()

        with(viewModel) {
            observe(boards, ::renderBoards)
        }
        viewModel.getBoards()

        setSupportActionBar(binding.navigationToolbar)

        setDrawerMenuSelectedListener()
        setNavigationMenuSelectedListener()
        synchronizeNavigationCheckedWhenFragmentChanged()
    }

    private fun setDrawerMenuSelectedListener() {
        binding.navigationSideMenu.setNavigationItemSelectedListener {
            if (it.groupId == R.id.menu_group_boards) {

                val bundle =
                    createBoardBundle(viewModel.boards.value?.get(it.itemId) ?: Board.empty)
                val frag = BoardFragment()
                frag.arguments = bundle
                navigate(frag)
            } else {
                when (it.itemId) {
                    R.id.menu_notice_board -> launchNoticeFragment()
                    R.id.menu_popular_board -> launchPopularFragment()
                    R.id.menu_ranking -> navigate(RankingFragment())
                    R.id.menu_profile -> navigate(ProfileFragment())
                }
            }
            binding.navigationDrawer.closeDrawer(GravityCompat.END)
            false
        }
    }

    private fun setNavigationMenuSelectedListener() {
        binding.navigationNavigator.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menu_popular_board -> launchPopularFragment()
                R.id.menu_ranking -> navigate(RankingFragment())
                R.id.menu_profile -> navigate(ProfileFragment())
                else -> launchPopularFragment()
            }
            true
        }
    }

    private fun synchronizeNavigationCheckedWhenFragmentChanged() {
        supportFragmentManager.addOnBackStackChangedListener {
            if (supportFragmentManager.backStackEntryCount > 0) {
                supportFragmentManager.getBackStackEntryAt(supportFragmentManager.backStackEntryCount - 1)
                    .apply {
                        when (this.name.toString()) {
                            BoardPopularFragment::class.java.simpleName -> {
                                binding.navigationNavigator.menu[0].isChecked = true
                            }
                            RankingFragment::class.java.simpleName -> {
                                binding.navigationNavigator.menu[1].isChecked = true
                            }
                            ProfileFragment::class.java.simpleName -> {
                                binding.navigationNavigator.menu[2].isChecked = true
                            }
                            else -> {
                                binding.navigationNavigator.menu[0].isChecked = true
                            }
                        }
                    }
            }
        }
    }

    private fun launchPopularFragment() {
        val bundle = createBoardBundle(-1, "인기글", "popular")
        val frag = BoardPopularFragment()
        frag.arguments = bundle
        navigate(frag)
    }

    private fun launchNoticeFragment() {
        val bundle = createBoardBundle(-1, "공지사항", "notice")
        val frag = BoardNoticeFragment()
        frag.arguments = bundle
        navigate(frag)
    }

    private fun createBoardBundle(id: Int, name: String, address: String) = Bundle().apply {
        this.putParcelable(
            R.string.BoardInfo.toString(),
            Board(id, name, address)
        )
    }

    private fun createBoardBundle(board: Board) = Bundle().apply {
        this.putParcelable(
            R.string.BoardInfo.toString(),
            board
        )
    }

    override fun onBackPressed() {
        if (binding.navigationDrawer.isDrawerOpen(gravity)) {
            binding.navigationDrawer.closeDrawer(gravity)
        } else {
            super.onBackPressed()
        }
    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.toolbar_drawer_menu, menu)
//        return super.onCreateOptionsMenu(menu)
//    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.toolbarNavigationBtn -> {
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
        supportActionBar?.title = ""
    }

    private fun navigate(fragment: BaseFragment) =
        navigator.transFragment(
            supportFragmentManager,
            fragment,
            binding.navigationFragmentContainer
        )
}