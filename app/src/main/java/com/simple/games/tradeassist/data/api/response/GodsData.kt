package com.simple.games.tradeassist.data.api.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class GodsData {
    @SerialName("Ref_Key")
    lateinit var refKey: String

    @SerialName("Parent_Key")
    var parentKey: String? = null

    @SerialName("ФайлКартинки_Key")
    var imageKey: String? = null

    @SerialName("ЕдиницаИзмерения_Key")
    var measureKey: String? = null

    @SerialName("IsFolder")
    var isFolder: Boolean = false

    @SerialName("Code")
    var code: String? = null

    @SerialName("Description")
    var description: String? = null

    @kotlinx.serialization.Transient
    var amount: Float = 0F
    @kotlinx.serialization.Transient
    var measure: MeasureData? = null
}

/**
 * Ref_Key": "0395b8f1-1332-11ee-8c17-982cbc31dbf6",
 *             "DataVersion": "AAAB9QAAAAA=",
 *             "DeletionMark": false,
 *             "Parent_Key": "0013adb0-289b-11ee-8c1f-982cbc31dbf6",
 *             "IsFolder": true,
 *             "Code": "ФР-00000001",
 *             "Description": "кабель",
 *             "Артикул": null,
 *             "ВидАлкогольнойПродукции_Key": null,
 *             "ДатаИзменения": "2023-07-22T17:42:45",
 *             "ЕдиницаИзмерения_Key": null,
 *             "ИмпортнаяАлкогольнаяПродукция": null,
 *             "ИспользоватьПартии": null,
 *             "ИспользоватьХарактеристики": null,
 *             "Комментарий": null,
 *             "МетодОценки": null,
 *             "НаименованиеПолное": null,
 *             "НаправлениеДеятельности_Key": null,
 *             "КатегорияНоменклатуры_Key": null,
 *             "НормаВремени": null,
 *             "ОбъемДАЛ": null,
 *             "Поставщик_Key": null,
 *             "ПроизводительИмпортерАлкогольнойПродукции_Key": null,
 *             "Склад_Key": null,
 *             "Спецификация_Key": null,
 *             "СпособПополнения": null,
 *             "СрокИсполненияЗаказа": null,
 *             "СрокПополнения": null,
 *             "СтавкаНДС_Key": null,
 *             "СтранаПроисхождения_Key": null,
 *             "СчетУчетаЗапасов_Key": null,
 *             "СчетУчетаЗатрат_Key": null,
 *             "ТипНоменклатуры": null,
 *             "УдалитьВидАлкогольнойПродукции_Key": null,
 *             "УдалитьИмпортнаяАлкогольнаяПродукция": null,
 *             "УдалитьОбъемДАЛ": null,
 *             "УдалитьПроизводительИмпортерАлкогольнойПродукции_Key": null,
 *             "ФайлКартинки_Key": null,
 *             "ФиксированнаяСтоимость": null,
 *             "ЦеноваяГруппа_Key": null,
 *             "Ячейка_Key": null,
 *             "РекомендуемЗаказать": null,
 *             "ИсключитьИзПрайсЛистов": null,
 *             "ЭтоНовинка": null,
 *             "НижняяГраницаОстатков": null,
 *             "ВерхняяГраницаОстатков": null,
 *             "СрокДействияФлагаНовинка": null,
 *             "ТранспортнаяУслуга": null,
 *             "НоменклатураГТД_Key": null,
 *             "ПодакцизныйТовар": null,
 *             "СтатьяДекларацииПоАкцизномуНалогу_Key": null,
 *             "ЛьготаНДС": null,
 *             "КодЛьготы": null,
 *             "ДополнительныеРеквизиты": [],
 *             "Predefined": false,
 *             "PredefinedDataName": "",
 *             "Parent@navigationLinkUrl": "Catalog_Номенклатура(guid'0395b8f1-1332-11ee-8c17-982cbc31dbf6')/Parent"
 * */