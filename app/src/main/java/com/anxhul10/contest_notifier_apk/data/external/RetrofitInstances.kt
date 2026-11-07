package com.anxhul10.contest_notifier_apk.data.external

import com.anxhul10.contest_notifier_apk.data.external.api.APICodechef
import com.anxhul10.contest_notifier_apk.data.external.api.APICodeforces
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val codechefAPI: APICodechef = Retrofit.Builder()
    .baseUrl("https://www.codechef.com/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()
    .create(APICodechef::class.java)

val codeforcesAPI: APICodeforces = Retrofit.Builder()
    .baseUrl("https://codeforces.com/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()
    .create(APICodeforces::class.java)

