package com.example.base_project.Utils.Views

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.text.InputType
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.FrameLayout
import androidx.core.view.isVisible
import com.example.base_project.R
import com.example.base_project.Utils.Extensions.isCnpj
import com.example.base_project.Utils.Extensions.isCpf
import com.example.base_project.Utils.Extensions.isEmail
import com.example.base_project.Utils.Mask
import com.example.base_project.databinding.CustomEditTextBinding
import java.text.SimpleDateFormat

private const val TYPE_CUSTOM = 0
private const val TYPE_NAME = 1
private const val TYPE_MAIL = 2
private const val TYPE_DATE = 3
private const val TYPE_PHONE = 4
private const val TYPE_CEP = 5
private const val TYPE_CREDIT_CARD = 6
private const val TYPE_PASSWORD = 7
private const val TYPE_MATCHING = 8
private const val TYPE_CPF = 9
private const val TYPE_CNPJ = 10
private const val TYPE_CPF_OR_CNPJ = 11
private const val TYPE_CREDIT_CARD_DATE = 12
private const val TYPE_NUMBER = 13
class CustomEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : FrameLayout(context, attrs, defStyle) {

    private var binding = CustomEditTextBinding.inflate(LayoutInflater.from(context), this, true)

    interface EditTextListener {
        fun onError()
        fun onValid()
        fun onFocusChange(isFocused: Boolean?)
    }

    private lateinit var mEditText: EditText

    var text: String
        get() = mEditText.text.toString()
        set(text) {
            mEditText.setText(text)
        }

    private var isErrorEnabled = false
    val isFieldValid: Boolean
        get() = !isErrorEnabled

    private var inputType: Int = 0
    private var editTextListener: EditTextListener? = null
    private var fieldNeedsValidation: Boolean = false
    private var regexErrorText: String? = null
    private var minLength: Int = 0
    private var matchingReference: EditText? = null
    private var mask: String? = null
    private var isRegexValid = false
    private var regex: String? = null
    private var erroExibir = "Campo inválido"
    private var isValid = true


    init{
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.customEditText, 0, 0)

            binding.txtNomeCampo.text = typedArray.getString(R.styleable.customEditText_fieldName)
            binding.input.hint = typedArray.getString(R.styleable.customEditText_placeholder)

            fieldNeedsValidation = typedArray.getBoolean(R.styleable.customEditText_validation, true)
            regexErrorText =
                if (typedArray.getString(R.styleable.customEditText_regexErrorText) == null) "" else typedArray.getString(
                    R.styleable.customEditText_regexErrorText
                )
            inputType = typedArray.getInt(R.styleable.customEditText_inputTextType, TYPE_CUSTOM)
            minLength = typedArray.getInt(R.styleable.customEditText_minLength, 0)
            mask =
                if (typedArray.getString(R.styleable.customEditText_customMask) == null) null else typedArray.getString(
                    R.styleable.customEditText_customMask
                )
            regex =
                if (typedArray.getString(R.styleable.customEditText_pattern) == null) "" else typedArray.getString(R.styleable.customEditText_pattern)
            typedArray.recycle()

            setInputType()
        }
    }

    //setTypeButton(EditorInfo.IME_ACTION_NEXT)
    fun setTypeButton(type: Int){
        mEditText.setImeOptions(type)
    }

    //setButtonAction(EditorInfo.IME_ACTION_NEXT) { action() }
    fun setButtonAction(type: Int, callback: () -> Unit){
        mEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == type) {
                callback.invoke()
                true
            }
            false
        }
    }

    fun disableInteraction() {
        mEditText.isFocusable = false
        mEditText.isClickable = true
        isClickable = true
        isFocusable = true
        mEditText.isEnabled = true
    }

    override fun setOnClickListener(l: OnClickListener?) {
        mEditText.setOnClickListener(l)
        disableInteraction()
        super.setOnClickListener(l)
    }

    private fun setInputType() {
        mEditText = binding.input
        setEditTextListener()
        if (inputType != TYPE_CUSTOM) {
            mEditText.inputType = when (inputType) {
                TYPE_PASSWORD -> InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                TYPE_MAIL -> InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
                TYPE_PHONE -> InputType.TYPE_CLASS_TEXT or InputType.TYPE_CLASS_PHONE
                TYPE_DATE, TYPE_CREDIT_CARD_DATE, TYPE_CEP, TYPE_CREDIT_CARD, TYPE_CPF, TYPE_CNPJ, TYPE_NUMBER, TYPE_CPF_OR_CNPJ -> InputType.TYPE_CLASS_NUMBER
                else -> InputType.TYPE_CLASS_TEXT
            }
        }
        mask = when (inputType) {
            TYPE_CUSTOM -> mask
            TYPE_DATE -> Mask.DATE
            TYPE_PHONE -> Mask.PHONE
            TYPE_CEP -> Mask.CEP
            TYPE_CREDIT_CARD -> Mask.CREDIT_CARD
            TYPE_CPF -> Mask.CPF
            TYPE_CNPJ -> Mask.CNPJ
            TYPE_CREDIT_CARD_DATE -> Mask.CREDIT_CARD_DATE
            TYPE_CPF_OR_CNPJ -> Mask.CPF_OR_CNPJ
            else -> null
        }
        mask?.let {
            mEditText.insertMask(it)
        }
    }

    private fun setEditTextListener() {
        mEditText.onFocusChangeListener = OnFocusChangeListener { _, focused ->
            editTextListener?.onFocusChange(focused)
            if (fieldNeedsValidation) {
                if (focused) {
                    binding.viewBg.setBackgroundResource(R.drawable.round_corners)
                    binding.error.isVisible = false
                    isValid = true
                    isErrorEnabled = false
                } else {
                    validate()
                }
            }
        }
    }

    fun setErrorListener(listener: EditTextListener) {
        editTextListener = listener
    }

    fun setRegex(regex: String, isRegexValid: Boolean) {
        this.regex = regex
        this.isRegexValid = isRegexValid
    }

    fun validate() {
        validateText(true)
    }

    private fun validateText(showError: Boolean) {
        if (fieldNeedsValidation) {
            //Type validations
            val isFieldValid = when (inputType) {
                TYPE_DATE -> isDateValid()
                TYPE_CREDIT_CARD_DATE -> isCardDateValid()
                TYPE_CNPJ -> isCnpjValid()
                TYPE_MAIL -> isMailValid()
                TYPE_MATCHING -> isMatchingValid()
                TYPE_CPF -> isCpfValid()
                TYPE_CEP -> isCepValid()
                TYPE_CPF_OR_CNPJ -> isCpfValid() || isCnpjValid()
                else -> true
            }

            binding.viewBg.setBackgroundResource(R.drawable.round_corners)

            //Emptiness validation
            if (showError) {
                if (!isFieldValid || !isNotEmpty()) {
                    setError()
                } else if (!isPatternValid()) {
                    setRegexErrorText()
                } else if (!isLengthValid()) {
                    if (mask.isNullOrBlank()) {
                        setErroVazio()
                    } else {
                        setInvalidErrorText()
                    }
                } else {
                    //if got here it's because field is valid
                    editTextListener?.onValid()
                    isErrorEnabled = false
                }
            }

        }
    }


    private fun isCepValid(): Boolean = mEditText.text.length == 9

    private fun isDateValid(): Boolean {
        val format = SimpleDateFormat("dd/MM/yyyy")
        return try {
            format.isLenient = false
            format.parse(mEditText.text.toString())
            true
        } catch (e: Exception) {
            false
        }
    }

    private fun isCardDateValid(): Boolean {
        val format = SimpleDateFormat("MM/yy")
        return try {
            format.isLenient = false
            format.parse(mEditText.text.toString())
            true
        } catch (e: Exception) {
            false
        }
    }

    //Returns true only if there' a regex and the Pattern matches
    private fun isPatternValid(): Boolean =
        regex.isNullOrEmpty() || mEditText.text.matches(regex!!.toRegex()) == isRegexValid

    private fun isLengthValid(): Boolean {
        if (!mask.isNullOrEmpty()) {
            if (mask == Mask.PHONE)
                return hasMinLengthOrMore(Mask.PHONE.length - 1) || hasMinLengthOrMore(
                    Mask.CEL_PHONE.length
                )
            if (mask == Mask.CPF_OR_CNPJ)
                return true
            minLength = mask!!.length
        }
        return hasMinLengthOrMore(minLength)
    }

    private fun isCpfValid(): Boolean{
        val retorno = mEditText.text.toString().isCpf()
        if (!retorno) erroExibir = "CPF inválido"
        isValid = retorno
        return retorno
    }

    private fun isCnpjValid(): Boolean{
        val retorno = mEditText.text.toString().isCnpj()
        if (!retorno) erroExibir = "CNPJ inválido"
        return retorno
    }

    private fun isNotEmpty(): Boolean{
        val retorno = mEditText.text.toString().isNotBlank()
        if (!retorno) erroExibir = "Este campo deve ser preenchido"
        return retorno
    }

    private fun isMailValid(): Boolean {
        val retorno = mEditText.text.toString().isEmail()
        if (!retorno) erroExibir = "EMAIL inválido"
        return retorno
    }

    private fun isMatchingValid(): Boolean = try {
        matchingReference?.text.toString() == mEditText.text.toString()
    } catch (e: NullPointerException) {
        true
    }

    private fun hasMinLengthOrMore(minLength: Int): Boolean {
        return minLength == 0 || mEditText.text.length >= minLength
    }

    private fun setError() {
        displayErrorBackground()
        editTextListener?.onError()
        binding.error.text = erroExibir
        binding.error.isVisible = true
    }

    private fun setRegexErrorText() {
        displayErrorBackground()
        editTextListener?.onError()
        binding.error.text = regexErrorText
        binding.error.isVisible = true
    }

    private fun setErroVazio() {
        displayErrorBackground()
        editTextListener?.onError()
        binding.error.text = "Este campo não pode ser vazio"
        binding.error.isVisible = true
    }

    private fun setInvalidErrorText() {
        displayErrorBackground()
        editTextListener?.onError()
        binding.error.text = "Campo inválido"
        binding.error.isVisible = true
    }


    private fun displayErrorBackground() {
        binding.viewBg.setBackgroundResource(R.drawable.round_corners_erro)
    }

    fun setMatchingReference(matchingReference: EditText) {
        this.matchingReference = matchingReference
    }

}