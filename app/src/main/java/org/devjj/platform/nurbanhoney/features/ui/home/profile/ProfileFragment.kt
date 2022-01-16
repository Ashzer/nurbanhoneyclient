package org.devjj.platform.nurbanhoney.features.ui.home.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.view.marginEnd
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import org.devjj.platform.nurbanhoney.R
import org.devjj.platform.nurbanhoney.core.extension.failure
import org.devjj.platform.nurbanhoney.core.extension.loadFromUrl
import org.devjj.platform.nurbanhoney.core.extension.observe
import org.devjj.platform.nurbanhoney.core.extension.setOnSingleClickListener
import org.devjj.platform.nurbanhoney.core.navigation.Navigator
import org.devjj.platform.nurbanhoney.core.platform.BaseFragment
import org.devjj.platform.nurbanhoney.databinding.FragmentProfileBinding
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : BaseFragment() {

    @Inject lateinit var navigator: Navigator
    @Inject lateinit var profileListener: ProfileListener

    override fun layoutId() = R.layout.fragment_profile

    private val viewModel by viewModels<ProfileViewModel>()

    private var _binding : FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        with(viewModel) {
            observe(profile, ::renderProfile)
            failure(failure, ::failureHandler)
        }

    }

    override fun onResume() {
        super.onResume()
        profileListener.clickListener(binding, requireActivity(), navigator, this)
        Log.d("test", "profile onResume")
        // 프로필 데이터를 받아오는 메소드
        viewModel.getProfile()
    }

    // 뱃지를 셋팅하는 메소드
    private fun settingBadge(badge: String?){
        val badgeUrl = badge ?: ""
        binding.ivBadge.loadFromUrl(badgeUrl, R.drawable.ic_action_no_badge)
    }

    // 닉네임 셋팅하는 메소드
    private fun settingNickanme(nickname: String?){
        val nicknameNotNull = nickname ?: ""
        binding.tvNickname.text = nicknameNotNull
    }

    // 설명 셋팅하는 메소드
    private fun settingDescription(description: String?){
        val descriptionNotNull = description ?: ""
        binding.tvDescriptionContent.text = descriptionNotNull
    }

    // 포인트 셋팅하는 메소드
    private fun settingPoint(point: Int?){
        val pointNotNull = point ?: 0
        binding.tvPoint.text = pointNotNull.toString()
    }

    // 보여주는 휘장 셋팅하는 메소드
    private fun settingInsigniaShow(insigniaShow: String?){
        Log.d("test", "insigniaShow : $insigniaShow")

        /*
        insigniaShow?.map {
            addInsigniaImage(binding.llInsigniaShowContent, )
        }
         */
    }

    // 소유한 휘장 셋팅하는 메소드
    private fun settingInsigniaOwn(insigniaOwn: String?){
        Log.d("test", "insigniaOwn : $insigniaOwn")
    }

    // 내가 쓴 글 수 셋팅하는 메소
    private fun settingMyArticleCount(myArticleCount: Int?){
        val myArticleCountNotNull = myArticleCount ?: 0
        binding.tvMyarticle.text = myArticleCountNotNull.toString()
    }

    // 내가 쓴 댓글 수 셋팅하는 메소
    private fun settingMyCommentCount(myCommentCount: Int?){
        val myCommentCountNotNull = myCommentCount ?: 0
        binding.tvMycomment.text = myCommentCountNotNull.toString()
    }

    // 휘장 이미지 셋팅하는 메소드
    private fun addInsigniaImage(ll: LinearLayout, url: String){
        val iv = ImageView(context)
        iv.loadFromUrl(url, R.drawable.ic_action_no_badge)
        //iv.maxWidth = 40
        //iv.maxHeight = 40
        ll.addView(iv)
    }

    // 프로필 데이터를 받아서 갱신하는 메소드
    private fun renderProfile(profile: Profile?) {
        Log.d("test", "profile id : ${profile?.id}")
        Log.d("test", "profile badge : ${profile?.badge}")
        Log.d("test", "profile description : ${profile?.description}")
        Log.d("test", "profile myArticleCount : ${profile?.myArticleCount}")
        Log.d("test", "profile myCommentCount : ${profile?.myCommentCount}")
        Log.d("test", "profile insigniaShow : ${profile?.insigniaShow}")
        Log.d("test", "profile insigniaOwn : ${profile?.insigniaOwn}")

        // 뱃지를 셋팅하는 메소드
        settingBadge(profile?.badge)
        // 닉네임 셋팅하는 메소드
        settingNickanme(profile?.nickname)
        // 설명 셋팅하는 메소드
        settingDescription(profile?.description)
        // 포인트 셋팅하는 메소드
        settingPoint(profile?.point)
        // 보여주는 휘장 셋팅하는 메소드
        settingInsigniaShow(profile?.insigniaShow)
        // 소유한 휘장 셋팅하는 메소드
        settingInsigniaOwn(profile?.insigniaOwn)
        // 내가 쓴 글 수 셋팅하는 메소
        settingMyArticleCount(profile?.myArticleCount)
        // 내가 쓴 댓글 수 셋팅하는 메소
        settingMyCommentCount(profile?.myCommentCount)

        binding.llMyarticle.setOnSingleClickListener {
            navigator.showProfileArticle(requireContext())
        }

        binding.llMycomment.setOnSingleClickListener {
            navigator.showProfileComment(requireContext())
        }
    }

}