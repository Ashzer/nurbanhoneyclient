package org.devjj.platform.nurbanhoney.features.ui.textedit

import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.theartofdev.edmodo.cropper.CropImage
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.richeditor.RichEditor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.devjj.platform.nurbanhoney.R
import org.devjj.platform.nurbanhoney.core.extension.*
import org.devjj.platform.nurbanhoney.core.platform.BaseFragment
import org.devjj.platform.nurbanhoney.databinding.FragmentTextEditorNurbanBinding
import org.devjj.platform.nurbanhoney.features.ui.article.Article
import org.devjj.platform.nurbanhoney.features.ui.splash.Board
import java.io.File
import java.net.URL
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
open class TextEditorFragment : BaseFragment() {
    override fun layoutId() = R.layout.fragment_text_editor_nurban

    companion object {
        private const val PARAM_ARTICLE = "param_article"
        private const val PARAM_BOARD = "param_board"

        fun toModify(board: Board, article: Article) = TextEditorFragment().apply {
            arguments =
                bundleOf(PARAM_ARTICLE to article, PARAM_BOARD to board)
        }

        fun toWrite(board: Board) = TextEditorFragment().apply {
            arguments = bundleOf(PARAM_BOARD to board)
        }
    }

    private var _binding: FragmentTextEditorNurbanBinding? = null
    val binding get() = _binding!!
    private lateinit var nurbanToken: String
    lateinit var uuid: UUID
    lateinit var mEditor: RichEditor
    var isModify: Boolean = false
    lateinit var article: Article

    val viewModel by viewModels<TextEditorViewModel>()

    @Inject
    lateinit var prefs: SharedPreferences

    private val cropActivityResultContracts = object : ActivityResultContract<Any?, Uri?>() {
        override fun createIntent(context: Context, input: Any?): Intent {
            return CropImage.activity()
                .getIntent(context)
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
            return CropImage.getActivityResult(intent)?.uri
        }
    }

    private lateinit var cropActivityResultLauncher: ActivityResultLauncher<Any?>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(viewModel) {
            observe(imageURLs, ::renderImage)
            observe(articleResponse, ::uploadHandler)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        _binding = FragmentTextEditorNurbanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (this.arguments != null) {
            if (requireArguments().containsKey(PARAM_BOARD)) {
                viewModel.board = (arguments?.getParcelable(PARAM_BOARD) ?: Board.empty)
                Log.d("texteditor_check__", viewModel.board.toString())
            }

            if (requireArguments().containsKey(PARAM_ARTICLE)) {
                isModify = true
                (requireActivity() as TextEditorActivity).setActionBarTitle("${viewModel.board.name} - 글 수정")
            }else{
                (requireActivity() as TextEditorActivity).setActionBarTitle("${viewModel.board.name} - 글 작성")
            }
        }


        nurbanToken = prefs.getString(R.string.prefs_nurban_token_key.toString(), "").toString()


        binding.textEditorNurbanHeader.textEditorTitleEt.requestFocus()
        val imm = requireActivity().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
        binding.textEditorNurbanLossCutClo.invisible()
        mEditor = binding.textEditorNurbanBody.textEditorContentWv
        mEditor.setEditorFontSize(15)
        mEditor.setEditorFontColor(Color.BLACK)
        mEditor.setPlaceholder("내용을 입력해주세요.")
        mEditor.setTextEditorListeners(binding)

        if (isModify) {
            Log.d("modify_check__", "modify")

            article = arguments?.get(PARAM_ARTICLE) as Article

            mEditor.html = article.content
            binding.textEditorNurbanHeader.textEditorTitleEt.setText(article.title)
            uuid = UUID.fromString(article.uuid)
            binding.textEditorNurbanLossCutEt.setText(article.lossCut.toString())
        } else {
            Log.d("modify_check__", "new")
            uuid = UUID.randomUUID()
        }


        binding.textEditorNurbanBody.actionHeading1.setOnClickListener {
            mEditor.setEditorFontSize(
                20
            )
        }
        binding.textEditorNurbanBody.actionHeading3.setOnClickListener {
            mEditor.setEditorFontSize(
                25
            )
        }
        binding.textEditorNurbanBody.actionHeading5.setOnClickListener {
            mEditor.setEditorFontSize(
                30
            )
        }

        functionVisibility(
            binding.textEditorNurbanBody.textEditorContentWv,
            binding.textEditorNurbanBody.textEditorSv
        )

        cropActivityResultLauncher = registerForActivityResult(cropActivityResultContracts) {
            it?.let { uri ->

                val file = File(uri.path)

                val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                val imageFilePart =
                    MultipartBody.Part.createFormData("image", file.name, requestFile)
                val uuidPart = MultipartBody.Part.createFormData("uuid", uuid.toString())
                //val options = BitmapFactory.Options()
                //options.inSampleSize = 4
                // var src = BitmapFactory.decodeFile(uri.toString(),options)
                Log.d("uri_check__", "$uri  ,  $uuid")
                viewModel.uploadImage("nurban", nurbanToken, uuidPart, imageFilePart)
            }
        }
        mEditor.insertImageListener(
            binding.textEditorNurbanBody.actionInsertImage,
            requireActivity(),
            cropActivityResultLauncher
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.writing_done -> {

//                Log.d(
//                    "editor_check__",
//                    "${
//                        prefs.getString(R.string.prefs_nurban_token_key.toString(), "").toString()
//                    }  ,  ${binding.titleEt.text} , ${mEditor.html} , $uuid ," +
//                            " ${viewModel.searchThumbnail(mEditor.html.toString())}"
//                )
                val thumbnailUrl = viewModel.searchThumbnail(mEditor.html.toString())
                Log.d("match_check__", thumbnailUrl)
                if (isModify) {
                    viewModel.modifyArticle(
                        viewModel.board.address,
                        prefs.getString(R.string.prefs_nurban_token_key.toString(), "").toString(),
                        article.id,
                        thumbnailUrl,
                        binding.textEditorNurbanHeader.textEditorTitleEt.text.toString(),
                        mEditor.html.toString()
                    )
                } else {
                    viewModel.uploadArticle(
                        viewModel.board.address,
                        prefs.getString(R.string.prefs_nurban_token_key.toString(), "").toString(),
                        binding.textEditorNurbanHeader.textEditorTitleEt.text.toString(),
                        uuid.toString(),
                        thumbnailUrl,
                        mEditor.html.toString()
                    )
                }
            }
            else -> return super.onOptionsItemSelected(item)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun renderImage(imageURLs: List<URL>?) {
        mEditor.insertImage(
            imageURLs?.last().toString(),
            "image", 320
        )
    }

    private fun uploadHandler(result: String?) {
        requireActivity().supportFragmentManager.popBackStack()
        requireActivity().finish()
    }

    override fun onDestroy() {
        var uploadResponse = viewModel.articleResponse.value ?: "no_result"

        if (uploadResponse == "no_result" && !isModify) {
            viewModel.deleteImages(
                "nurban",
                prefs.getString(
                    R.string.prefs_nurban_token_key.toString(),
                    ""
                ).toString(), uuid.toString()
            )
        }
        super.onDestroy()
    }
}
