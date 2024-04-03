package com.simple.games.tradeassist.data.api.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestPublishOrder(
    @SerialName("Контрагент_Key")
    val customerKey: String,
    @SerialName("Ответственный_Key")
    val orderResponsibleKey: String,
    @SerialName("Date")
    val date: String, //  = "2024-04-01T12:44:57"
    @SerialName("ДатаОтгрузки")
    val dateOfDelivery: String, // "2024-04-01T00:00:00",
    @SerialName("СуммаДокумента")
    val orderSum: Float, //774,

    @SerialName("Запасы")
    val orderGods: List<RequestPublishGod>,

    @SerialName("СостояниеЗаказа_Key")
    val orderStateKey: String = "d7d20e22-1330-11ee-8c17-982cbc31dbf6",

    @SerialName("DeletionMark")
    val deletionMark: Boolean = false,
    @SerialName("Posted")
    val posted: Boolean = true,

    @SerialName("ВалютаДокумента_Key")
    val currencyKey: String = "d7d20dc4-1330-11ee-8c17-982cbc31dbf6",

    @SerialName("ВидОперации")
    val operationType: String = "ЗаказНаПродажу",

    @SerialName("Договор_Key")
    val orderDocumentTypeKey: String = "47143b5c-c056-11ee-8c3a-982cbc31dbf6",

    @SerialName("Закрыт")
    val closed: Boolean = false,

    @SerialName("ЗапланироватьОплату")
    val schedulePayment: Boolean = false,

    @SerialName("Комментарий")
    val comment: String = "",

    @SerialName("Кратность")
    val karatnost: String = "1",

    @SerialName("Курс")
    val course: Int = 1,

    @SerialName("НДСВключатьВСтоимость")
    val includeNDS: Boolean = true,
    @SerialName("НалогообложениеНДС")
    val ndsStatus: String = "ОблагаетсяНДС",
    @SerialName("СуммаВключаетНДС")
    val orderSumIncludesNds: Boolean = true,

    @SerialName("ПроцентСкидкиПоДисконтнойКарте")
    val percentAmount: Int = 0,
    @SerialName("СкидкиРассчитаны")
    val discountCalculated: Boolean = false,
    @SerialName("АвторасчетНДС")
    val autoCalculateNDs: Boolean = true,
)

@Serializable
data class RequestPublishGod(
    @SerialName("Номенклатура_Key")
    val godRefKey: String,
    @SerialName("ДатаОтгрузки")
    val deliveryDate: String,//"2024-04-01T00:00:00"

    @SerialName("Количество")
    val godsItemsAmount: Float,
    @SerialName("ЕдиницаИзмерения")
    val measureKey: String,

    @SerialName("Цена")
    val price: Float,
    @SerialName("Сумма")
    val godsPriceSum: Float,
    @SerialName("Всего")
    val godsPriceSumAll: Float,
    @SerialName("LineNumber")
    val sortNumber: String,

    @SerialName("Пометка")
    val mark: Boolean = true,
    @SerialName("РезервОтгрузка")
    val godsReserveDeliver: Int = 0,

    @SerialName("ЕдиницаИзмерения_Type")
    val measureType: String = "StandardODATA.Catalog_КлассификаторЕдиницИзмерения",


    @SerialName("ПроцентСкидкиНаценки")
    val percentDecreaseincreaseAmount: Int = 0,

    @SerialName("СтавкаНДС_Key")
    val ndsStavkaKey: String = "d7d20dc7-1330-11ee-8c17-982cbc31dbf6",
    @SerialName("СуммаНДС")
    val ndsPriceSum: Float = 0F,
)