package com.anxhul10.contest_notifier_apk.utils

import retrofit2.http.GET

data class GithubRelease(
    val tag_name: String,
    val html_url: String,
    val name: String?
)

interface GithubApiService {

    @GET("repos/Anxhul10/contest-notifier-apk/releases/latest")
    suspend fun getLatestRelease(): GithubRelease

}