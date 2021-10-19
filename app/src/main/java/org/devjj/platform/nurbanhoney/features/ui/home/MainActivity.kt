package org.devjj.platform.nurbanhoney.features.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import org.devjj.platform.nurbanhoney.core.platform.BaseActivity
import org.devjj.platform.nurbanhoney.R
import org.devjj.platform.nurbanhoney.core.extension.inTransaction
import org.devjj.platform.nurbanhoney.core.navigation.Navigator
import org.devjj.platform.nurbanhoney.core.platform.BaseFragment
import org.devjj.platform.nurbanhoney.databinding.ActivityMainBinding
import javax.inject.Inject

class MainActivity : BaseActivity() {
    override fun fragment() = HomeFragment()

    @Inject
    lateinit var navigator: Navigator

    private lateinit var binding : ActivityMainBinding

    companion object{
        fun callingIntent(context: Context) =
            Intent(context, MainActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.Tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when(tab!!.position){
                    0 -> transFragment(HomeFragment())
                    1-> transFragment(NurbanHoneyFragment())
                    2-> transFragment(FreeBoardFragment())
                    else -> transFragment(HomeFragment())
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                when(tab!!.position){
                    0 -> transFragment(HomeFragment())
                    1-> transFragment(NurbanHoneyFragment())
                    2-> transFragment(FreeBoardFragment())
                    else -> transFragment(HomeFragment())
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }
        })
    }

    fun transFragment(frag : BaseFragment){
        supportFragmentManager.beginTransaction()
            .replace(binding.FragmentContainer.id, frag)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
    }



}