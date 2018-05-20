package com.fifthgen.labelprinter.network

import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.loopj.android.http.RequestParams

object WebClient {

    private val httpClient = AsyncHttpClient()

    fun httpGet(url: String, params: RequestParams, responseHandler: AsyncHttpResponseHandler) {
        httpClient.get(url, params, responseHandler)
    }

    fun httpPost(url: String, params: RequestParams, responseHandler: AsyncHttpResponseHandler) {
        httpClient.post(url, params, responseHandler)
    }
}