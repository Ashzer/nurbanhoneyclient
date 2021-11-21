package org.devjj.platform.nurbanhoney.features.ui.textedit

import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.fragment.app.viewModels
import com.theartofdev.edmodo.cropper.CropImage
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.richeditor.RichEditor
import kotlinx.android.synthetic.main.frament_text_editor.*
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
import java.io.File
import java.net.URL
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class TextEditorFragment : BaseFragment() {
    override fun layoutId() = R.layout.frament_text_editor

    private var _binding: FramentTextEditorBinding? = null
    private val binding get() = _binding!!
    private lateinit var nurbanToken: String
    private lateinit var uuid: UUID
    private lateinit var mEditor: RichEditor

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

        nurbanToken = prefs.getString(R.string.prefs_nurban_token_key.toString(), "").toString()
        uuid = UUID.randomUUID()

        binding.titleEt.requestFocus()
        val imm = requireActivity().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)

        mEditor = binding.editor
        mEditor.setEditorFontSize(15)
        mEditor.setEditorFontColor(Color.BLACK)
        mEditor.setPlaceholder("내용을 입력해주세요.")
        mEditor.setTextEditorListeners(binding)

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
                val temp = textEditorViewModel.uploadArticle(
                    prefs.getString(R.string.prefs_nurban_token_key.toString(), "").toString(),
                    binding.titleEt.text.toString(),
                    uuid.toString(),
                    binding.lossCutEt.text.toString().toLong(),
                    thumbnailUrl,
                    mEditor.html.toString()
                )

                Log.d("check___" , temp.toString())
                requireActivity().supportFragmentManager.popBackStack()
                requireActivity().finish()
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
}
