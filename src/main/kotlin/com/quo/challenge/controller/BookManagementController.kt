package com.quo.challenge.controller

import com.quo.challenge.handler.ErrorResponse
import com.quo.challenge.model.*
import com.quo.challenge.repository.BookManagementRepository
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*


/**
 * 書書籍管理コントローラー
 */
@RestController
class BookManagementController(
    private val bookManagementRepository: BookManagementRepository
) {
    val percent: String = "%"

    /**
     * 書籍情報登録
     * @param request 書籍情報登録リクエスト（JSON）
     * 書籍名と著者名を必須とした登録機能
     */
    @PostMapping("/registerBookInfo")
    fun registerBookInfo(@RequestBody request: BookInfoRegisterRequest, result: BindingResult): String {
        request.toBookInfoRecord()
            .let {bookManagementRepository.insert(it)}
        // 正常終了
        return """
            {"message": "success"}
        """.trimIndent()
    }

    /**
     * 書籍情報更新
     * @param request 書籍情報更新リクエスト（JSON）
     */
    @PostMapping("/updateBookInfo")
    fun updateBookInfo(@RequestBody request: BookInfoUpdateRequest): String {
        request.toBookInfoRecord()
            .let {bookManagementRepository.update(it)}
        // 正常終了
        return """
            {"message": "success"}
        """.trimIndent()
    }

    /**
     * 書籍情報検索
     * @param request 書籍情報検索リクエスト（JSON）
     */
    @GetMapping("/searchBookInfo")
    fun searchBookInfo(@RequestBody request: BookInfoSearchRequest) =
        bookManagementRepository.getInfoByNameAuthor(addPercent(request.name), addPercent(request.author))
            .toList()
            .map { it.toBookInfo() }

    /**
     * 著者紐づけ書籍情報取得
     * @param author 著者名
     */
    @GetMapping("/getBookNameByAuthor/{author}")
    fun getBookNameByAuthor(@PathVariable author: String) =
        bookManagementRepository.getInfoByAuthor(author)
            .toList()
            .map { it.toBookInfo() }

    /**
     * LIKE検索用%付与（部分一致）
     * @param condition 検索条件
     */
    fun addPercent(condition: String?): String {
        // nullの場合空白の条件を返却
        if(condition == null) {
            return percent + percent
        }
        // 検索条件の左右に%を付与
        val newWord = percent + condition + percent
        return newWord
    }
}
