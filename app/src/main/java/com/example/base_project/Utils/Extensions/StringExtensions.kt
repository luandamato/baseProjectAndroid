package com.example.base_project.Utils.Extensions

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.text.Html
import android.text.Spanned
import com.google.gson.Gson
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*


fun String.cleaned() = replace("[^A-z0-9]".toRegex(), "")

fun String?.isEmail(): Boolean {
    return this?.matches("[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}".toRegex()) == true
}

fun String.toDate(format: String): Date? {
    return try {
        SimpleDateFormat(format, Locale("pt", "BR")).parse(this)
    } catch (e: Exception) {
        null
    }
}

fun String.fromHtml(): Spanned = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
    Html.fromHtml(this, Html.FROM_HTML_MODE_COMPACT)
} else {
    Html.fromHtml(this)
}

fun String.toByteArray(): ByteArray {
    return android.util.Base64.decode(this, android.util.Base64.DEFAULT)
}

fun String.toBase64Bitmap(): Bitmap? {
    return try {
        val decodedString = this.toByteArray()
        val bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
        bitmap
    } catch (e: Exception) {
        null
    }
}

fun Bitmap.toBase64String(): String? {
    return try {
        val byteArrayOutputStream = ByteArrayOutputStream()
        this.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        val string = android.util.Base64.encodeToString(byteArray, android.util.Base64.DEFAULT)
        string
    } catch (e: Exception) {
        null
    }
}
fun String.toFrontendDateFormat() = toDate("yyyy-MM-dd'T'HH:mm:ss")?.toString("dd/MM/yyyy HH:mm")
fun String.toFrontendDateFormatWithoutHour() = toDate("yyyy-MM-dd'T'HH:mm:ss")?.toString("dd/MM/yyyy")

fun String.toFrontendDateFormatMonthAndYear() = toDate("yyyy-MM-dd'T'HH:mm:ss")?.toString("MM/yyyy")
fun String.toBackendDateFormat() = toDate("dd/MM/yyyy")?.toString("yyyy-MM-dd")

fun Date.toString(format: String): String? {
    return try {
        SimpleDateFormat(format, Locale("pt", "BR")).format(this)
    } catch (e: Exception) {
        null
    }
}

fun String.isCpf(): Boolean {
    val cpf = this.cleaned()
    if (cpf.length != 11) return false
    var rest =
        cpf.take(9).mapIndexed { index, char -> char.toString().toInt() * (10 - index) }.sum() % 11
    if (cpf[9].toString().toInt() == if (rest < 2) 0 else 11 - rest) {
        rest =
            cpf.take(10).mapIndexed { index, char -> char.toString().toInt() * (11 - index) }.sum() % 11
        return cpf[10].toString().toInt() == if (rest < 2) 0 else 11 - rest
    }
    return false
}

fun String.formatDate(): String {
    val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
    val formatter = SimpleDateFormat("dd/MM/yyyy - HH:mm")
    return formatter.format(parser.parse(this))
}
fun String.isCnpj(): Boolean {
    val cnpj = this.cleaned()
    if (cnpj.length != 14) return false

    val weightCNPJ = intArrayOf(6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2)
    val cnpjWithoutDigits = cnpj.substring(0, 12)

    val sum1 =
        11 - cnpjWithoutDigits.mapIndexed { index, char -> char.toString().toInt() * weightCNPJ[1 + index] }.sum() % 11
    val digit1 = if (sum1 > 9) 0 else sum1

    val sum2 =
        11 - (cnpjWithoutDigits + digit1).mapIndexed { index, char -> char.toString().toInt() * weightCNPJ[index] }.sum() % 11
    val digit2 = if (sum2 > 9) 0 else sum2

    return cnpj == cnpjWithoutDigits + digit1.toString() + digit2.toString()
}

fun String.addMask(mask: String, maskChar: Char = '#'): String {
    var stringIndex = 0
    return mask.map { char ->
        if (char == maskChar) this.get(stringIndex++) else char
    }.joinToString("")
}


fun <T> String?.toObjectClass(classOfT : Class<T>): T {
    return Gson().fromJson(this, classOfT)
}

fun String.fixUrl() = "https://$this".replace("https://http", "http")

