package com.example.filmsearch.ui.main.model.contentprovider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.example.filmsearch.ui.main.model.database.HistoryDao
import com.example.filmsearch.ui.main.view.App
import com.example.filmsearch.ui.main.view.App.Companion.getHistoryDao

const val URI_ALL = 1 //URI для всех записей, если запрашивают всю историю
const val URI_ID = 2 //URI для конкретных записей
const val ENTITY_PATH = "HistoryEntity" //контент провайдер будет отдавать только хистори


class HistoryContentProvider : ContentProvider() {

    private var authorities: String? = null //адрес URI
    private lateinit var uriMatcher: UriMatcher //помогает определить тип адреса URI

    //типы данных
    private var entityContentType: String? = null //набор строк
    private var entityContentItemType: String? = null //одна строка

    private lateinit var contentUri: Uri //адрес URI провайдер

    override fun onCreate(): Boolean {
        authorities = "filmsearch.provider"
        uriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        uriMatcher.addURI(
            authorities,
            "$ENTITY_PATH/#",
            URI_ID
        ) // получение конкретного одного объекта

        // Тип содержимого — все объекты
        entityContentType = "vnd.android.cursor.dir/vnd.$authorities.$ENTITY_PATH"
        // Тип содержимого — один объект
        entityContentItemType = "vnd.android.cursor.item/vnd.$authorities.$ENTITY_PATH"
        // Строка для доступа к Provider
        contentUri = Uri.parse("content://$authorities/$ENTITY_PATH")
        return true
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        require(uriMatcher.match(uri) == URI_ID) { "Wrong URI: $uri" }
        // Получаем доступ к данным
        val historyDao = getHistoryDao()
        // Получаем идентификатор записи по адресу
        val id = ContentUris.parseId(uri)
        // Удаляем запись по идентификатору
        historyDao.deleteById(id)
        // Нотификация на изменение Cursor
        context?.contentResolver?.notifyChange(uri, null)
        return 1 //0 - ничего не сделали, 1 - если удалили

    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        // Получаем доступ к данным
        val historyDao: HistoryDao = getHistoryDao()
        // При помощи UriMatcher определяем, запрашиваются все элементы или один
        val cursor = when (uriMatcher.match(uri)) {
            URI_ALL -> historyDao.getHistoryCursor() // Запрос к базе данных для всех элементов
            URI_ID -> {
                // Определяем id из URI адреса. Класс ContentUris помогает это сделать
                val id = ContentUris.parseId(uri)
                // Запрос к базе данных для одного элемента
                historyDao.getHistoryCursor(id)
            }
            else -> throw IllegalArgumentException("Wrong URI: $uri")
        }
        // Устанавливаем нотификацию при изменении данных в content_uri
        cursor.setNotificationUri(context!!.contentResolver, contentUri)
        return cursor
    }

    override fun getType(uri: Uri): String? { //когда к нам пришел какой-то урл,
        when (uriMatcher.match(uri)) { //проверяем, можем ли мы его распознать
            URI_ALL -> return entityContentType
            URI_ID -> return entityContentItemType
        }
        return null

    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
       // TODO("Implement this to handle requests to insert a new row.")
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        return  0
       // TODO("Implement this to handle requests to update one or more rows.")
    }
}