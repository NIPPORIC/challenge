package com.quo.challenge.repository

import com.quo.challenge.generated.jooq.tables.MBook
import com.quo.challenge.generated.jooq.tables.records.MBookRecord
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

// 書籍管理機能リポジトリ
@Repository
@Transactional
class  BookManagementRepository(
    val dslContext: DSLContext
) {

    /**
     * 書籍マスタInsert
     * @param mBookRecord 書籍マスタRecord
     */
    fun insert(mBookRecord: MBookRecord) =
        dslContext.insertInto(M_BOOK)
            .set(mBookRecord)
            .execute()

    /**
     * 書籍マスタUpdate
     * 一意の書籍IDで更新
     * @param mBookRecord 書籍マスタRecord
     */
    fun update(mBookRecord: MBookRecord) =
        dslContext.update(M_BOOK)
            .set(mBookRecord)
            .where(M_BOOK.BOOK_ID.eq(mBookRecord.bookId))
            .execute()

    /**
     * 書籍マスタSelect（部分一致）
     * @param name 書籍名
     * @param author 著者名
     */
    fun getInfoByNameAuthor(name: String, author: String?): List<MBookRecord> = dslContext.select()
        .from(M_BOOK)
        .where(M_BOOK.NAME.like(name), M_BOOK.AUTHOR.like(author))
        .fetch()
        .into(MBookRecord::class.java)

    /**
     * 書籍マスタSelect（完全一致）
     * @param author 著者名
     */
    fun getInfoByAuthor(author: String): List<MBookRecord> = dslContext.select()
        .from(M_BOOK)
        .where(M_BOOK.AUTHOR.eq(author))
        .fetch()
        .into(MBookRecord::class.java)

    /**
     * DB初期化
     */
    fun truncateMBook(): Any = dslContext.truncate("m_book")
        .restartIdentity().execute()

    companion object {
        private val M_BOOK = MBook.M_BOOK
    }
}