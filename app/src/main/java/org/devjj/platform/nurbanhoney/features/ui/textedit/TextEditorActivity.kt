package org.devjj.platform.nurbanhoney.features.ui.textedit

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import jp.wasabeef.richeditor.RichEditor
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import okhttp3.Dispatcher
import org.devjj.platform.nurbanhoney.R
import org.devjj.platform.nurbanhoney.databinding.ActivityTexteditorBinding
import javax.inject.Inject

@AndroidEntryPoint
class TextEditorActivity : AppCompatActivity() {
    private lateinit var mEditor: RichEditor

    private lateinit var binding : ActivityTexteditorBinding

    @Inject
    lateinit var boardRepository: BoardRepository
    @Inject
    lateinit var prefs : SharedPreferences

    companion object{
        fun callingIntent(context: Context) =
            Intent(context, TextEditorActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTexteditorBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        binding.titleEdtv.requestFocus()
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)


        mEditor = findViewById<View>(R.id.editor) as RichEditor
        //mEditor.setEditorHeight(maxNumPictureInPictureActions)

        mEditor.setEditorFontSize(15)
        mEditor.setEditorFontColor(Color.BLACK)
        //mEditor.setEditorBackgroundColor(Color.BLUE);
        //mEditor.setBackgroundColor(Color.BLUE);
        //mEditor.setBackgroundResource(R.drawable.bg);
       // mEditor.setPadding(10, 10, 10, 10)
        //mEditor.setBackground("https://raw.githubusercontent.com/wasabeef/art/master/chip.jpg");
        mEditor.setPlaceholder("Insert text here...")
        //mEditor.setInputEnabled(false);

        findViewById<View>(R.id.action_undo).setOnClickListener { mEditor.undo() }
        findViewById<View>(R.id.action_redo).setOnClickListener { mEditor.redo() }
        findViewById<View>(R.id.action_bold).setOnClickListener { mEditor.setBold() }
        findViewById<View>(R.id.action_italic).setOnClickListener { mEditor.setItalic() }
        findViewById<View>(R.id.action_subscript).setOnClickListener { mEditor.setSubscript() }
        findViewById<View>(R.id.action_superscript).setOnClickListener { mEditor.setSuperscript() }
        findViewById<View>(R.id.action_strikethrough).setOnClickListener { mEditor.setStrikeThrough() }
        findViewById<View>(R.id.action_underline).setOnClickListener { mEditor.setUnderline() }
        findViewById<View>(R.id.action_heading1).setOnClickListener { mEditor.setHeading(1) }
        findViewById<View>(R.id.action_heading2).setOnClickListener { mEditor.setHeading(2) }
        findViewById<View>(R.id.action_heading3).setOnClickListener { mEditor.setHeading(3) }
        findViewById<View>(R.id.action_heading4).setOnClickListener { mEditor.setHeading(4) }
        findViewById<View>(R.id.action_heading5).setOnClickListener { mEditor.setHeading(5) }
        findViewById<View>(R.id.action_heading6).setOnClickListener { mEditor.setHeading(6) }
        findViewById<View>(R.id.action_txt_color).setOnClickListener(object : View.OnClickListener {
            private var isChanged = false
            override fun onClick(v: View?) {
                mEditor.setTextColor(if (isChanged) Color.BLACK else Color.RED)
                isChanged = !isChanged
            }
        })
        findViewById<View>(R.id.action_bg_color).setOnClickListener(object : View.OnClickListener {
            private var isChanged = false
            override fun onClick(v: View?) {
                mEditor.setTextBackgroundColor(if (isChanged) Color.TRANSPARENT else Color.YELLOW)
                isChanged = !isChanged
            }
        })
        findViewById<View>(R.id.action_indent).setOnClickListener { mEditor.setIndent() }
        findViewById<View>(R.id.action_outdent).setOnClickListener { mEditor.setOutdent() }
        findViewById<View>(R.id.action_align_left).setOnClickListener { mEditor.setAlignLeft() }
        findViewById<View>(R.id.action_align_center).setOnClickListener { mEditor.setAlignCenter() }
        findViewById<View>(R.id.action_align_right).setOnClickListener { mEditor.setAlignRight() }
        findViewById<View>(R.id.action_blockquote).setOnClickListener { mEditor.setBlockquote() }
        findViewById<View>(R.id.action_insert_bullets).setOnClickListener { mEditor.setBullets() }
        findViewById<View>(R.id.action_insert_numbers).setOnClickListener { mEditor.setNumbers() }
        findViewById<View>(R.id.action_insert_image).setOnClickListener {
            mEditor.insertImage(
                "https://raw.githubusercontent.com/wasabeef/art/master/chip.jpg",
                "dachshund", 320
            )
        }
        findViewById<View>(R.id.action_insert_youtube).setOnClickListener {
            mEditor.insertYoutubeVideo(
                "https://www.youtube.com/embed/pS5peqApgUA"
            )
        }
        findViewById<View>(R.id.action_insert_audio).setOnClickListener { mEditor.insertAudio("https://file-examples-com.github.io/uploads/2017/11/file_example_MP3_5MG.mp3") }
        findViewById<View>(R.id.action_insert_video).setOnClickListener {
            mEditor.insertVideo(
                "https://test-videos.co.uk/vids/bigbuckbunny/mp4/h264/1080/Big_Buck_Bunny_1080_10s_10MB.mp4",
                360
            )
        }
        findViewById<View>(R.id.action_insert_link).setOnClickListener {
            mEditor.insertLink(
                "https://github.com/wasabeef",
                "wasabeef"
            )
        }
        findViewById<View>(R.id.action_insert_checkbox).setOnClickListener { mEditor.insertTodo() }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(
            R.menu.texteditor_menu,menu
        )
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.writing_done -> {
                Log.d("editor_check__", "${prefs.getString("NurbanToken", "").toString()}  ,  ${binding.titleEdtv.text.toString()} , ${mEditor.html.toString()}")

                prefs.getString("NurbanToken","")
                CoroutineScope(Dispatchers.IO).async {
                    val temp = boardRepository.uploadWriting(prefs.getString("NurbanToken", "").toString(), binding.titleEdtv.text.toString() ,mEditor.html.toString() )
                    Log.d("editor_check__", temp.toString())

                }
                finish()
                return super.onOptionsItemSelected(item)
            }
            else -> return super.onOptionsItemSelected(item)
        }

    }
}

