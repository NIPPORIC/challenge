package com.quo.challenge

import com.quo.challenge.controller.BookManagementController
import com.quo.challenge.model.BookInfo
import com.quo.challenge.model.BookInfoRegisterRequest
import com.quo.challenge.model.BookInfoSearchRequest
import com.quo.challenge.model.BookInfoUpdateRequest
import com.quo.challenge.repository.BookManagementRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class ChallengeApplicationTests() {

	@Autowired
	lateinit var testTargetClass : BookManagementController

	@Autowired
	lateinit var testRepo : BookManagementRepository

	/**
	 * DB初期化
	 */
	@BeforeEach
	@AfterEach
	fun setup() {
		testRepo.truncateMBook()
		//BookInfoRegisterRequest("testName", "testAuthor")
	}

	/**
	 * 書籍情報登録テスト
	 */
	@Test
	fun registerBookInfoTest() {
		val request = BookInfoRegisterRequest("testName", "testAuthor")
		val result = testTargetClass.registerBookInfo(request)
		assertEquals("""
            {"message": "success"}
        """.trimIndent(), result)
	}

	/**
	 * 書籍情報更新テスト（書籍名、著者名）
	 */
	@Test
	fun updateBookInfoTestNameAndAuthor() {
		val registerRequest = BookInfoRegisterRequest("testName", "testAuthor")
		testTargetClass.registerBookInfo(registerRequest)
		val request = BookInfoUpdateRequest("1", "updateName", "updateAuthor")
		val result = testTargetClass.updateBookInfo(request)
		assertEquals("""
            {"message": "success"}
        """.trimIndent(), result)
	}

	/**
	 * 書籍情報更新テスト（書籍名）
	 */
	@Test
	fun updateBookInfoTestName() {
		val registerRequest = BookInfoRegisterRequest("testName", "testAuthor")
		testTargetClass.registerBookInfo(registerRequest)
		val request = BookInfoUpdateRequest("1", "testName", null)
		val result = testTargetClass.updateBookInfo(request)
		assertEquals("""
            {"message": "success"}
        """.trimIndent(), result)
	}

	/**
	 * 書籍情報更新テスト（著者名）
	 */
	@Test
	fun updateBookInfoTestAuthor() {
		val registerRequest = BookInfoRegisterRequest("testName", "testAuthor")
		testTargetClass.registerBookInfo(registerRequest)
		val request = BookInfoUpdateRequest("1", null, "testAuthor")
		val result = testTargetClass.updateBookInfo(request)
		assertEquals("""
            {"message": "success"}
        """.trimIndent(), result)
	}

	/**
	 * 書籍情報更新テスト（空更新）
	 */
	@Test
	fun updateBookInfoTestBlank() {
		val registerRequest = BookInfoRegisterRequest("testName", "testAuthor")
		testTargetClass.registerBookInfo(registerRequest)
		val request = BookInfoUpdateRequest("1", null, null)
		val result = testTargetClass.updateBookInfo(request)
		assertEquals("""
            {"message": "success"}
        """.trimIndent(), result)
	}

	/**
	 * 著者紐づけ書籍情報取得テスト
	 */
	@Test
	fun getBookNameByAuthorTest() {
		val registerRequest = BookInfoRegisterRequest("testName", "testAuthor")
		testTargetClass.registerBookInfo(registerRequest)
		val result = testTargetClass.getBookNameByAuthor("testAuthor")
		val testList = listOf(BookInfo(1, "testName", "testAuthor"))
		assertEquals(testList, result)
	}

	/**
	 * 著者紐づけ書籍情報取得テスト（0件取得）
	 */
	@Test
	fun getBookNameByAuthorTestEmpty() {
		val registerRequest = BookInfoRegisterRequest("testName", "testAuthor")
		testTargetClass.registerBookInfo(registerRequest)
		val result = testTargetClass.getBookNameByAuthor("a")
		val testList: List<String> = listOf()
		assertEquals(testList, result)
	}

	/**
	 * 書籍情報検索テスト（書籍名、著者名）
	 */
	@Test
	fun searchBookInfoTestNameAndAuthor() {
		val registerRequest = BookInfoRegisterRequest("testName", "testAuthor")
		testTargetClass.registerBookInfo(registerRequest)
		val request = BookInfoSearchRequest("test", "test")
		val result = testTargetClass.searchBookInfo(request)
		val testList = listOf(BookInfo(1, "testName", "testAuthor"))
		assertEquals(testList, result)
	}

	/**
	 * 書籍情報検索テスト（書籍名）
	 */
	@Test
	fun searchBookInfoTestName() {
		val registerRequest = BookInfoRegisterRequest("testName", "testAuthor")
		testTargetClass.registerBookInfo(registerRequest)
		val request = BookInfoSearchRequest("test", null)
		val result = testTargetClass.searchBookInfo(request)
		val testList = listOf(BookInfo(1, "testName", "testAuthor"))
		assertEquals(testList, result)
	}

	/**
	 * 書籍情報検索テスト（著者名）
	 */
	@Test
	fun searchBookInfoTestAuthor() {
		val registerRequest = BookInfoRegisterRequest("testName", "testAuthor")
		testTargetClass.registerBookInfo(registerRequest)
		val request = BookInfoSearchRequest(null, "test")
		val result = testTargetClass.searchBookInfo(request)
		val testList = listOf(BookInfo(1, "testName", "testAuthor"))
		assertEquals(testList, result)
	}

}
