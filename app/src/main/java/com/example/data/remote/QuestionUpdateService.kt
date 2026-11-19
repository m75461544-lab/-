package com.example.data.remote

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit

sealed class UpdateFetchResult {
    data object NoInternet : UpdateFetchResult()
    data class InvalidUrl(val message: String) : UpdateFetchResult()
    data class Success(val pack: QuestionPackDto) : UpdateFetchResult()
    data class Failure(val reason: String) : UpdateFetchResult()
}

class QuestionUpdateService(private val context: Context) {

    private val client = OkHttpClient.Builder()
        .connectTimeout(12, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

    private val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    fun isNetworkAvailable(): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
            ?: return false
        val network = cm.activeNetwork ?: return false
        val caps = cm.getNetworkCapabilities(network) ?: return false
        return caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
            caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }

    suspend fun fetchQuestionPack(url: String): UpdateFetchResult = withContext(Dispatchers.IO) {
        val trimmed = url.trim()
        if (!trimmed.startsWith("https://")) {
            return@withContext UpdateFetchResult.InvalidUrl(
                "نشانی باید معتبر و با پیشوند امن https:// آغاز شود."
            )
        }
        if (!isNetworkAvailable()) {
            return@withContext UpdateFetchResult.NoInternet
        }

        try {
            val request = Request.Builder()
                .url(trimmed)
                .header("Accept", "application/json")
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    return@withContext UpdateFetchResult.Failure(
                        "پاسخ ناموفق از سرور (کد ${response.code})"
                    )
                }
                val body = response.body?.string()
                if (body.isNullOrBlank()) {
                    return@withContext UpdateFetchResult.Failure("فایل JSON دریافتی خالی است.")
                }

                val adapter = moshi.adapter(QuestionPackDto::class.java)
                val parsed = adapter.fromJson(body)
                    ?: return@withContext UpdateFetchResult.Failure("ساختار JSON معتبر نیست.")

                val validQuestions = parsed.questions.filter { q ->
                    q.id.isNotBlank() &&
                        q.lesson in 1..2 &&
                        q.prompt.isNotBlank() &&
                        q.correctIndex in 0..3
                }

                if (validQuestions.isEmpty()) {
                    return@withContext UpdateFetchResult.Failure(
                        "هیچ سوال معتبری مربوط به گفتار ۱ یا ۲ در بسته یافت نشد."
                    )
                }

                UpdateFetchResult.Success(parsed.copy(questions = validQuestions))
            }
        } catch (e: Exception) {
            UpdateFetchResult.Failure(e.localizedMessage ?: "خطا در برقراری ارتباط با نشانی JSON")
        }
    }
}
