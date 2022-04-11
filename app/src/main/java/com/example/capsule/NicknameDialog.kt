package com.example.capsule

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.annotation.StringRes

class NicknameDialog(private val context: Context){
    private val builder : AlertDialog.Builder by lazy{
        AlertDialog.Builder(context).setView(view)
    }

    private val view: View by lazy{
        View.inflate(context, R.layout.nickname_dialog, null)
    }

    private val title : TextView by lazy{
        view.findViewById(R.id.title)
    }

    private val message : TextView by lazy{
        view.findViewById(R.id.message)
    }

    private val button : Button by lazy{
        view.findViewById(R.id.positiveButton)
    }

    private var dialog : AlertDialog? = null

    fun setTitle(@StringRes titleId: Int): NicknameDialog{
        title.text = context.getText(titleId)
        return this
    }

    fun setTitle(content: CharSequence): NicknameDialog {
        title.text = content
        return this
    }

    fun setMessage(@StringRes messageId: Int): NicknameDialog {
        message.text = context.getText(messageId)
        return this
    }

    fun setMessage(content: CharSequence): NicknameDialog {
        message.text = content
        return this
    }

    fun setPositiveButton(@StringRes textId: Int, listener: (view: View) -> (Unit)): NicknameDialog {
        button.apply {
            text = context.getText(textId)
            setOnClickListener(listener)
        }
        return this
    }

    fun setPositiveButton(text: CharSequence, listener: (view: View) -> (Unit)): NicknameDialog {
        button.apply {
            this.text = text
            setOnClickListener(listener)
        }
        return this
    }

    fun create(){
        dialog = builder.create()
    }

    fun show(){
        dialog = builder.create()
        dialog?.show()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
    }

    fun dismiss(){
        dialog?.dismiss()
    }
}