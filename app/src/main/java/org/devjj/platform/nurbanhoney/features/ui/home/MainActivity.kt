package org.devjj.platform.nurbanhoney.features.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import org.devjj.platform.nurbanhoney.core.navigation.Navigator
import org.devjj.platform.nurbanhoney.core.platform.BaseFragment
import org.devjj.platform.nurbanhoney.core.platform.BaseTabLayoutActivity
import org.devjj.platform.nurbanhoney.databinding.ActivityTabLayoutBinding
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : BaseTabLayoutActivity() {
    override fun fragment() = HomeFragment()

    @Inject
    lateinit var navigator: Navigator

    private lateinit var binding: ActivityTabLayoutBinding

    companion object {
        fun callingIntent(context: Context) =
            Intent(context, MainActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTabLayoutBinding.inflate(layoutInflater)

        val view = binding.root
        setContentView(view)
        addFragment(savedInstanceState)
        tapSelectedListener(binding.Tabs)

    }

    private fun tapSelectedListener(tabLayout: TabLayout) =
        tabLayout.addOnTabSelectedListener(
            object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    selectTab(tab!!.position)
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                    selectTab(tab!!.position)
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                }

                fun selectTab(position: Int) =
                    when (position) {
                        0 -> navigate(HomeFragment())
                        1 -> navigate(NurbanHoneyFragment())
                        2 -> navigate(FreeBoardFragment())
                        else -> navigate(HomeFragment())
                    }

                fun navigate(fragment: BaseFragment) =
                    navigator.transFragment(
                        supportFragmentManager,
                        fragment,
                        binding.TabLayoutFragmentContainer
                    )
            })
}