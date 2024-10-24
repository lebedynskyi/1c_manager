package com.simple.games.tradeassist.data.api

import com.simple.games.tradeassist.data.api.request.RequestPublishOrder
import com.simple.games.tradeassist.data.api.response.CustomerData
import com.simple.games.tradeassist.data.api.response.MeasureData
import com.simple.games.tradeassist.data.api.response.DataResponse
import com.simple.games.tradeassist.data.api.response.DebtRecordData
import com.simple.games.tradeassist.data.api.response.EmptyResponse
import com.simple.games.tradeassist.data.api.response.GodsData
import com.simple.games.tradeassist.data.api.response.OrderHistoryData
import com.simple.games.tradeassist.data.api.response.PriceData
import com.simple.games.tradeassist.data.api.response.PriceTypeData
import com.simple.games.tradeassist.data.api.response.ResponsibleData
import com.simple.games.tradeassist.data.api.response.StorageData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface C1Api {
    @GET("?\$format=json")
    suspend fun login(@Header("Authorization") auth: String?): Response<EmptyResponse>

    @GET("Catalog_Номенклатура?\$format=json")
    suspend fun fetchGods(@Header("Authorization") auth: String?): Response<DataResponse<GodsData>>

    @GET("Catalog_Контрагенты?\$format=json")
    suspend fun fetchCustomers(@Header("Authorization") auth: String?): Response<DataResponse<CustomerData>>

    @GET("AccumulationRegister_ЗапасыНаСкладах?\$format=json")
    suspend fun fetchStorage(@Header("Authorization") auth: String?): Response<DataResponse<StorageData>>

    @GET("Catalog_КлассификаторЕдиницИзмерения?\$format=json")
    suspend fun fetchMeasure(@Header("Authorization") auth: String?): Response<DataResponse<MeasureData>>

    @GET("Catalog_Сотрудники?\$format=json")
    suspend fun fetchResponsible(@Header("Authorization") auth: String?): Response<DataResponse<ResponsibleData>>

    @GET("InformationRegister_ЦеныНоменклатуры?\$format=json")
    suspend fun fetchPrices(@Header("Authorization") auth: String?): Response<DataResponse<PriceData>>

    @GET("Catalog_ВидыЦен?\$format=json")
    suspend fun fetchPriceTypes(@Header("Authorization") auth: String?): Response<DataResponse<PriceTypeData>>

    @GET("Document_РасходнаяНакладная?\$format=json")
    suspend fun fetchOrderHistory(
        @Header("Authorization") auth: String?,
        @Query("\$filter") customerFilter: String
    ): Response<DataResponse<OrderHistoryData>>

    @POST("Document_ЗаказПокупателя?\$format=json")
    suspend fun publishOrder(
        @Header("Authorization") auth: String?,
        @Body request: RequestPublishOrder
    ): Response<EmptyResponse>

    @GET("AccumulationRegister_РасчетыСПокупателями_RecordType?\$format=json")
    suspend fun getCustomerDebt(
        @Header("Authorization") auth: String?,
        @Query("\$filter") customerFilter: String
    ) :Response<DataResponse<DebtRecordData>>
}