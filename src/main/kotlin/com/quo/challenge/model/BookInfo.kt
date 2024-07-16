package com.quo.challenge.model

import com.quo.challenge.generated.jooq.tables.records.MBookRecord

// 書籍情報
data class BookInfo (val bookId: Int, val name: String, val author: String)
// 書籍情報登録リクエスト
data class BookInfoRegisterRequest (val name: String, val author: String)
// 書籍情報更新リクエスト
data class BookInfoUpdateRequest (val bookId: String, val name: String?, val author: String?)
// 書籍情報検索リクエスト
data class BookInfoSearchRequest (val name: String?, val author: String?)

/**
 * 書籍情報レスポンス
 */
fun MBookRecord.toBookInfo() = BookInfo(
    bookId = bookId, name = name, author = author
)

/**
 * 書籍情報登録レコード
 * 書籍名、著者名以外はDB側で自動設定のためnull
 */
fun BookInfoRegisterRequest.toBookInfoRecord(): MBookRecord {
    return MBookRecord(
        null,
        name,
        author,
        null,
        null,
        null,
    )
}

/**
 * 書籍情報更新レコード
 * 書籍ID、書籍名、著者名以外はDB側で自動設定のためnull
 */
fun BookInfoUpdateRequest.toBookInfoRecord(): MBookRecord {
    return MBookRecord(
        bookId.toInt(),
        name,
        author,
        null,
        null,
        null,
    )
}