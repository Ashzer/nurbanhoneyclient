package org.devjj.platform.nurbanhoney.features.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.core.view.GravityCompat
import dagger.hilt.android.AndroidEntryPoint
import org.devjj.platform.nurbanhoney.R
import org.devjj.platform.nurbanhoney.core.navigation.Navigator
import org.devjj.platform.nurbanhoney.core.platform.BaseFragment
import org.devjj.platform.nurbanhoney.core.platform.BaseTabLayoutActivity
import org.devjj.platform.nurbanhoney.databinding.ActivityNavigationBinding
import org.devjj.platform.nurbanhoney.features.ui.home.nurbanhoney.NurbanHoneyFragment
import org.devjj.platform.nurbanhoney.features.ui.home.profile.ProfileFragment
import org.devjj.platform.nurbanhoney.features.ui.home.ranking.RankingFragment
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : BaseTabLayoutActivity() {
    override fun fragment() = NurbanHoneyFragment()
    private val gravity = GravityCompat.START
    @Inject
    lateinit var navigator: Navigator
    private lateinit var binding: ActivityNavigationBinding

    companion object {
        fun callingIntent(context: Context) =
            Intent(context, MainActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNavigationBinding.inflate(layoutInflater)

        val view = binding.root
        setContentView(view)
        addFragment(savedInstanceState)

        binding.appNavigator.setOnNavigationItemSelectedListener {

            Log.d("navi_check__", it.itemId.toString())
            when (it.itemId) {
                R.id.menu_popular -> navigate(NurbanHoneyFragment())
                R.id.menu_ranking -> navigate(RankingFragment())
                R.id.menu_profile -> navigate(ProfileFragment())
                else -> navigate(RankingFragment())
            }
            true
        }

        binding.appNaviSidemenu.setNavigationItemSelectedListener {
            Log.d("navi_check__", it.itemId.toString())
            when (it.itemId) {
                R.id.menu_popular -> navigate(NurbanHoneyFragment())
                R.id.menu_ranking -> navigate(RankingFragment())
                R.id.menu_profile -> navigate(ProfileFragment())
                else -> navigate(RankingFragment())
            }
            binding.appNaviDrawer.closeDrawer(GravityCompat.START)
            false
        }

        setSupportActionBar(binding.appNaviToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_action_menu)
    }

    override fun onBackPressed() {
        if (binding.appNaviDrawer.isDrawerOpen(gravity)) {
            binding.appNaviDrawer.closeDrawer(gravity)
        } else {
            super.onBackPressed()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                val layoutDrawer = binding.appNaviDrawer

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

    private fun navigate(fragment: BaseFragment) =
        navigator.transFragment(
            supportFragmentManager,
            fragment,
            binding.NavigationFragmentContainer
        )

}