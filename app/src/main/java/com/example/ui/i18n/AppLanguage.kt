package com.example.ui.i18n

import androidx.compose.ui.unit.LayoutDirection

enum class AppLanguage(
    val code: String,
    val nativeName: String,
    val shortCode: String,
    val isRtl: Boolean
) {
    FA(code = "fa", nativeName = "فارسی", shortCode = "FA", isRtl = true),
    AR(code = "ar", nativeName = "العربية", shortCode = "AR", isRtl = true),
    EN(code = "en", nativeName = "English", shortCode = "EN", isRtl = false);

    val layoutDirection: LayoutDirection
        get() = if (isRtl) LayoutDirection.Rtl else LayoutDirection.Ltr

    companion object {
        fun fromCode(code: String?): AppLanguage =
            entries.firstOrNull { it.code.equals(code, ignoreCase = true) } ?: FA
    }
}
