package org.devjj.platform.nurbanhoney.features.ui.textedit

import android.content.Context.INPUT_METHOD_SERVICE
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.richeditor.RichEditor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import org.devjj.platform.nurbanhoney.R
import org.devjj.platform.nurbanhoney.core.extension.*
import org.devjj.platform.nurbanhoney.core.platform.BaseFragment
import org.devjj.platform.nurbanhoney.databinding.FramentTextEditorBinding
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class TextEditorFragment : BaseFragment() {
    override fun layoutId() = R.layout.frament_text_editor

    private var _binding: FramentTextEditorBinding? = null
    private val binding get() = _binding!!
    private lateinit var uuid: UUID
    private lateinit var mEditor: RichEditor

    @Inject
    lateinit var boardRepository: BoardRepository
    @Inject
    lateinit var prefs: SharedPreferences

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

        uuid = UUID.randomUUID()

        binding.titleEdtv.requestFocus()
        val imm = requireActivity().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)

        mEditor = binding.editor
        mEditor.setEditorFontSize(15)
        mEditor.setEditorFontColor(Color.BLACK)
        //mEditor.setEditorBackgroundColor(Color.BLUE);
        //mEditor.setBackgroundColor(Color.BLUE);
        //mEditor.setBackground("https://raw.githubusercontent.com/wasabeef/art/master/chip.jpg");
        mEditor.setPlaceholder("내용을 입력해주세요.")
        //mEditor.setInputEnabled(false);

        undoListener(binding.actionUndo, mEditor)
        redoListener(binding.actionRedo, mEditor)
        boldListener(binding.actionBold, mEditor)
        italicListener(binding.actionItalic, mEditor)
        underlineListener(binding.actionUnderline, mEditor)
        alignLeftListener(binding.actionAlignLeft, mEditor)
        alignCenterListener(binding.actionAlignCenter, mEditor)
        alignRightListener(binding.actionAlignRight, mEditor)

        binding.actionHeading1.setOnClickListener { mEditor.setHeading(1) }
        binding.actionHeading3.setOnClickListener { mEditor.setHeading(3) }
        binding.actionHeading5.setOnClickListener { mEditor.setHeading(5) }
        binding.actionInsertImage.setOnClickListener {

            mEditor.insertImage(
                "https://raw.githubusercontent.com/wasabeef/art/master/chip.jpg",
                "dachshund", 320
            )
        }
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
                        prefs.getString("NurbanToken", "").toString()
                    }  ,  ${binding.titleEdtv.text} , ${mEditor.html} , ${uuid.toString()}"
                )
                prefs.getString("NurbanToken", "")
                CoroutineScope(Dispatchers.IO).async {
                    val temp = boardRepository.uploadWriting(
                        prefs.getString("NurbanToken", "").toString(),
                        binding.titleEdtv.text.toString(),
                        mEditor.html.toString(),
                        uuid.toString()
                    )
                    Log.d("editor_check__", temp.toString())
                }
                requireActivity().finish()
            }
            else -> return super.onOptionsItemSelected(item)
        }
        return super.onOptionsItemSelected(item)
    }
}
