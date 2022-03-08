package org.devjj.platform.nurbanhoney.features.ui.home.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.view.*
import androidx.fragment.app.viewModels
import androidx.gridlayout.widget.GridLayout
import dagger.hilt.android.AndroidEntryPoint
import org.devjj.platform.nurbanhoney.R
import org.devjj.platform.nurbanhoney.core.controller.removeKeyboard
import org.devjj.platform.nurbanhoney.core.extension.*
import org.devjj.platform.nurbanhoney.core.navigation.Navigator
import org.devjj.platform.nurbanhoney.core.platform.BaseFragment
import org.devjj.platform.nurbanhoney.databinding.FragmentProfileBinding
import org.devjj.platform.nurbanhoney.features.ui.home.HomeActivity
import javax.inject.Inject
import kotlin.collections.forEach

@AndroidEntryPoint
class ProfileFragment : BaseFragment() {

    @Inject
    lateinit var navigator: Navigator
    @Inject
    lateinit var profileListener: ProfileListener

    private val viewModel by viewModels<ProfileViewModel>()

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
    private fun settingBadge(badge: String?) {
        val badgeUrl = badge ?: ""
        binding.ivBadge.loadFromUrl(badgeUrl, R.drawable.ic_action_no_badge)
    }

    // 닉네임 셋팅하는 메소드
    private fun settingNickname(nickname: String?) {
        val nicknameNotNull = nickname ?: ""
        binding.tvNickname.text = nicknameNotNull
    }

    // 설명 셋팅하는 메소드
    private fun settingDescription(description: String?) {
        val descriptionNotNull = description ?: ""
        binding.tvDescriptionContent.text = descriptionNotNull
    }

    // 포인트 셋팅하는 메소드
    private fun settingPoint(point: Int?) {
        val pointNotNull = point ?: 0
        binding.tvPoint.text = pointNotNull.toString()
    }

    // 보여주는 휘장 셋팅하는 메소드
    private fun settingInsigniaShow(insigniaShow: List<String>?) {
        insigniaShow?.forEach { url ->
            val imageView = createInsigniaFromUrl(url)
            addInsigniaShowImage(imageView)
        }
    }

    // 소유한 휘장 셋팅하는 메소드
    private fun settingInsigniaOwn(insigniaOwn: List<String>?) {
        insigniaOwn?.forEach { url ->
            val imageView = createInsigniaFromUrl(url)
            addInsigniaOwnImage(imageView)
            //  setInsigniaOnClickListener(imageView)
        }
    }

    private fun setInsigniaOnClickListener(imageView: ImageView) =
        imageView.setOnSingleClickListener {
            if (imageView.paddingRight == 5) {
                insigniaDeselected(imageView)
            } else {
                insigniaSelected(imageView)
            }
        }

    private fun insigniaSelected(imageView: ImageView) {
        imageView.setPadding(5)
        imageView.setMarginsGridlayout(10, 10, 10, 10)
    }

    private fun insigniaDeselected(imageView: ImageView) {
        imageView.setPadding(0)
        imageView.setMarginsGridlayout(15, 15, 15, 15)

    }

    private fun removeInsigniaOnClickListener(imageView: ImageView) {
        imageView.setOnSingleClickListener { }
    }

    // 내가 쓴 글 수 셋팅하는 메소
    private fun settingMyArticleCount(myArticleCount: Int?) {
        val myArticleCountNotNull = myArticleCount ?: 0
        binding.tvMyarticle.text = myArticleCountNotNull.toString()
    }

    // 내가 쓴 댓글 수 셋팅하는 메소
    private fun settingMyCommentCount(myCommentCount: Int?) {
        val myCommentCountNotNull = myCommentCount ?: 0
        binding.tvMycomment.text = myCommentCountNotNull.toString()
    }

    // 휘장 이미지 셋팅하는 메소드
    private fun addInsigniaOwnImage(imageView: ImageView) {
        binding.insigniaOwnGlo.addView(imageView)
    }

    private fun addInsigniaShowImage(imageView: ImageView) {
        binding.insigniaShowGlo.addView(imageView)
    }

    private fun createInsigniaFromUrl(url: String): ImageView {
        val imageView = ImageView(requireContext())
        with(imageView) {
            loadFromUrl(url, R.drawable.ic_action_no_badge)
            background = ContextCompat.getDrawable(requireContext(), R.drawable.edges_rectangle)
            insigniaDeselected(this)
        }
        return imageView
    }

    private fun View.setMarginsGridlayout(left: Int, top: Int, right: Int, bottom: Int) {
        var gl = GridLayout.LayoutParams()
        gl.setMargins(left, top, right, bottom)
        this.layoutParams = gl
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
        settingNickname(profile?.nickname)
        // 설명 셋팅하는 메소드
        settingDescription(profile?.description)
        // 포인트 셋팅하는 메소드
        settingPoint(profile?.point)
        // 보여주는 휘장 셋팅하는 메소드
        binding.insigniaShowGlo.removeAllViews()
        settingInsigniaShow(profile?.insigniaShow)
        // 소유한 휘장 셋팅하는 메소드
        binding.insigniaOwnGlo.removeAllViews()
        settingInsigniaOwn(profile?.insigniaOwn)
        // 내가 쓴 글 수 셋팅하는 메소
        settingMyArticleCount(profile?.myArticleCount)
        // 내가 쓴 댓글 수 셋팅하는 메소
        settingMyCommentCount(profile?.myCommentCount)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as HomeActivity).setActionBarTitle("프로필")
        //작성한 글 보기
        showMyArticles(binding.llMyarticle)
        //작성 댓글 보기
        showMyComment(binding.llMycomment)
        //프로필 수정 버튼
        startDescriptionModify(binding.btnModify)
        //프로필 수정 취소 버튼
        cancelDescriptionModify(binding.btnModifyCancel)
        //프로필 수정 완료 버튼
        completeDescriptionModify(binding.btnModifyComplete)
    }

    private fun showMyArticles(view: View) = view.setOnSingleClickListener {
        navigator.showProfileArticle(requireContext())
    }

    private fun showMyComment(view: View) = view.setOnSingleClickListener {
        navigator.showProfileComment(requireContext())
    }

    private fun startDescriptionModify(view: View) = view.setOnSingleClickListener {
        binding.btnModify.invisible()
        binding.btnModifyComplete.visible()
        binding.btnModifyCancel.visible()

        binding.tvNickname.invisible()
        binding.tvDescriptionContent.invisible()
        binding.etNickname.visible()
        binding.etDescriptionContent.visible()

        binding.etNickname.setText(binding.tvNickname.text)
        binding.etDescriptionContent.setText(binding.tvDescriptionContent.text)

        binding.insigniaOwnGlo.children.forEach { insigniaIv ->
            Log.d("listener_attached", "OwnGlo $insigniaIv")
            setInsigniaOnClickListener(insigniaIv as ImageView)
        }

        viewModel.profile.value?.insigniaShow?.forEach { show ->
            if(viewModel.insigniaOwn.value != null) {
                insigniaSelected(binding.insigniaOwnGlo[viewModel.insigniaOwn.value?.indexOf(show)!!]as ImageView)
            }
        }
    }

    private fun cancelDescriptionModify(view: View) = view.setOnSingleClickListener {
        binding.btnModifyComplete.invisible()
        binding.btnModifyCancel.invisible()
        binding.btnModify.visible()

        binding.tvNickname.visible()
        binding.tvDescriptionContent.visible()
        binding.etNickname.invisible()
        binding.etDescriptionContent.invisible()

        binding.insigniaOwnGlo.children.forEach { insigniaIv ->
            Log.d("listener_detached", "OwnGlo $insigniaIv")
            removeInsigniaOnClickListener(insigniaIv as ImageView)
        }

        removeKeyboard(requireActivity(), view)
    }

    private fun completeDescriptionModify(view: View) = view.setOnSingleClickListener {
        binding.btnModifyComplete.invisible()
        binding.btnModifyCancel.invisible()
        binding.btnModify.visible()
        binding.tvNickname.visible()
        binding.tvDescriptionContent.visible()
        binding.etNickname.invisible()
        binding.etDescriptionContent.invisible()

        binding.insigniaOwnGlo.children.forEach { insigniaIv ->
            Log.d("listener_detached", "OwnGlo $insigniaIv")
            removeInsigniaOnClickListener(insigniaIv as ImageView)
        }

        var insigniaShow: MutableList<String> = mutableListOf()
        binding.insigniaOwnGlo.forEachIndexed { i, _ ->
            Log.d("insignia_check__", i.toString())
            if ((binding.insigniaOwnGlo[i] as ImageView).paddingRight == 5) {
                insigniaShow.add(viewModel.insigniaOwn.value?.get(i) ?: "")
            }
        }

        viewModel.editProfile(
            binding.etNickname.text.toString(),
            binding.etDescriptionContent.text.toString(),
            insigniaShow
        )
        removeKeyboard(requireActivity(), view)
    }

}