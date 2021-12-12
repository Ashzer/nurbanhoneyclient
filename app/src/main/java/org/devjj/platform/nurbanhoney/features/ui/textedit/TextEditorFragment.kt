package org.devjj.platform.nurbanhoney.features.ui.textedit

import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
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
import org.devjj.platform.nurbanhoney.core.extension.functionVisibility
import org.devjj.platform.nurbanhoney.core.extension.insertImageListener
import org.devjj.platform.nurbanhoney.core.extension.observe
import org.devjj.platform.nurbanhoney.core.extension.setTextEditorListeners
import org.devjj.platform.nurbanhoney.core.platform.BaseFragment
import org.devjj.platform.nurbanhoney.databinding.FramentTextEditorBinding
import org.devjj.platform.nurbanhoney.features.network.BoardService
import org.devjj.platform.nurbanhoney.features.network.repositories.texteditor.TextEditorRepository
import org.devjj.platform.nurbanhoney.features.ui.article.Article
import java.io.File
import java.net.URL
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class TextEditorFragment : BaseFragment() {
    override fun layoutId() = R.layout.frament_text_editor

    companion object {
        private const val PARAM_ARTICLE = "param_article"

        fun toModify(article : Article) = TextEditorFragment().apply {
            arguments = bundleOf(PARAM_ARTICLE to article)
        }
    }

    private var _binding: FramentTextEditorBinding? = null
    private val binding get() = _binding!!
    private lateinit var nurbanToken: String
    private lateinit var uuid: UUID
    private lateinit var mEditor: RichEditor
    private var isModify  :Boolean = false
    private lateinit var article : Article

    private val textEditorViewModel by viewModels<TextEditorViewModel>()

    @Inject
    lateinit var boardService: BoardService

    @Inject
    lateinit var textEditorRepository: TextEditorRepository

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
        with(textEditorViewModel) {
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
        _binding = FramentTextEditorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(arguments != null) isModify = true

        nurbanToken = prefs.getString(R.string.prefs_nurban_token_key.toString(), "").toString()


        binding.titleEt.requestFocus()
        val imm = requireActivity().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)

        mEditor = binding.editor
        mEditor.setEditorFontSize(15)
        mEditor.setEditorFontColor(Color.BLACK)
        mEditor.setPlaceholder("내용을 입력해주세요.")
        mEditor.setTextEditorListeners(binding)

        if(isModify){
            Log.d("modify_check__" , "modify")

            article = arguments?.get(PARAM_ARTICLE) as Article

            mEditor.html = article.content
            binding.titleEt.setText(article.title)
            uuid = UUID.fromString(article.uuid)
            binding.lossCutEt.setText(article.lossCut.toString())
        }else{
            Log.d("modify_check__" , "new")
            uuid = UUID.randomUUID()
        }


        binding.actionHeading1.setOnClickListener { mEditor.setEditorFontSize(20) }
        binding.actionHeading3.setOnClickListener { mEditor.setEditorFontSize(25) }
        binding.actionHeading5.setOnClickListener { mEditor.setEditorFontSize(30) }

        functionVisibility(binding.editor, binding.horizontalScrollView)

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
                textEditorViewModel.uploadImage(nurbanToken, uuidPart, imageFilePart)
            }
        }
        mEditor.insertImageListener(
            binding.actionInsertImage,
            requireActivity(),
            cropActivityResultLauncher
        )
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(
            R.menu.texteditor_menu, menu
        )
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.writing_done -> {

                Log.d(
                    "editor_check__",
                    "${
                        prefs.getString(R.string.prefs_nurban_token_key.toString(), "").toString()
                    }  ,  ${binding.titleEt.text} , ${mEditor.html} , $uuid ," +
                            " ${textEditorViewModel.searchThumbnail(mEditor.html.toString())}"
                )
                val thumbnailUrl = textEditorViewModel.searchThumbnail(mEditor.html.toString())
                Log.d("match_check__",thumbnailUrl)
                if(isModify){
                    textEditorViewModel.modifyArticle(
                        prefs.getString(R.string.prefs_nurban_token_key.toString(), "").toString(),
                        article.id,
                        thumbnailUrl,
                        binding.titleEt.text.toString(),
                        (binding.lossCutEt.text.toString()).toLong(),
                        mEditor.html.toString()
                    )
                }else {
                    textEditorViewModel.uploadArticle(
                        prefs.getString(R.string.prefs_nurban_token_key.toString(), "").toString(),
                        binding.titleEt.text.toString(),
                        uuid.toString(),
                        binding.lossCutEt.text.toString().toLong(),
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
        var uploadResponse = textEditorViewModel.articleResponse.value ?: "no_result"

        if (uploadResponse == "no_result" && !isModify) {
            textEditorViewModel.deleteImages(
                prefs.getString(
                    R.string.prefs_nurban_token_key.toString(),
                    ""
                ).toString(), uuid.toString()
            )
        }
        super.onDestroy()
    }
}
