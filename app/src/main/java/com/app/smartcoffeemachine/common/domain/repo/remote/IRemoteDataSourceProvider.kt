package com.app.smartcoffeemachine.common.domain.repo.remote

import com.app.smartcoffeemachine.common.data.model.NetworkMethods
import kotlinx.serialization.KSerializer

interface IRemoteDataSourceProvider {
    suspend  fun <ResponseBody, RequestBody> request(
        method: NetworkMethods,
        path: String,
        params: Map<String, Any>?= null,
        headers: Map<String, Any>?= null,
        requestBody: RequestBody?= null,
        serializer: KSerializer<ResponseBody>,
    ): ResponseBody
}