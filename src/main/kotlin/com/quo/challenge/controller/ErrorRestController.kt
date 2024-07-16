package com.quo.challenge.controller

import com.quo.challenge.handler.ErrorResponse
import com.quo.challenge.handler.FormErrorHandler
import com.quo.challenge.handler.ParameterErrorHandler
import jakarta.servlet.RequestDispatcher
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.ConstraintViolationException
import org.apache.catalina.connector.ClientAbortException
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController
import org.springframework.boot.web.servlet.error.ErrorAttributes
import org.springframework.context.MessageSource
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.BindException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.context.request.ServletWebRequest
import org.springframework.web.servlet.resource.NoResourceFoundException


/**
 * すべてのエラーを補足する共通エラーハンドラ
 */
@RestController
@RequestMapping("\${server.error.path:\${error.path:/error}}")
class ErrorRestController(
    private val errorAttributes: ErrorAttributes,
    private val messageSource: MessageSource
) : AbstractErrorController(errorAttributes) {
    companion object {
        private const val INVALID_ERROR = "InvalidError"
    }

    // バリデーションエラーハンドラ 詳細は後述
    private val formErrorHandler = FormErrorHandler()
    private val parameterErrorHandler = ParameterErrorHandler()

    /** 404レスポンス */
    private val notFound = ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(
            ErrorResponse(
                errorCause = "ResourceNotFound",
                message = "Not Found"
            )
        )

    @RequestMapping
    fun handleError(request: HttpServletRequest): ResponseEntity<*>? {
        // 補足できるエラーかチェックし、そうでなければ存在しないリソースへのリクエストとして 404 エラーを返す
        val error = errorAttributes.getError(ServletWebRequest(request)) ?: return notFound

        // リクエストしたクライアント側起因のエラーはハンドリングできないため Null を返す
        if (error.cause is ClientAbortException) return null

        val status = getStatus(request)
        val body = when (error) {
            // @RequestBody でバリデーションエラーが発生
            is BindException -> {
                ErrorResponse(
                    errorCause = INVALID_ERROR,
                    invalidErrors = formErrorHandler.handleFormException(error, messageSource)
                )
            }
            // @PathVariable, @RequestParam でバリデーションエラーが発生
            is ConstraintViolationException -> {
                ErrorResponse(
                    errorCause = INVALID_ERROR,
                    invalidErrors = mutableListOf(parameterErrorHandler.handleParameterException(error))
                )
            }
            // @RequestParam(require = true)（デフォルト true） でパラメータ指定無し
            is MissingServletRequestParameterException -> {
                ErrorResponse(
                    errorCause = INVALID_ERROR,
                    invalidErrors = mutableListOf(parameterErrorHandler.handleParameterException(error))
                )
            }
            // @NoResourceFoundException(リクエストメソッドの値が不正)
            is NoResourceFoundException -> {
                val reqMessage = request.getAttribute(RequestDispatcher.ERROR_MESSAGE)
                ErrorResponse(
                    errorCause = "NoResourceFoundException",
                    message = if (reqMessage is String && reqMessage.isNotBlank()) reqMessage else error.message
                )
            }
            // @HttpMessageNotReadableException(リクエストJSONの値が不正)
            is HttpMessageNotReadableException -> {
                val reqMessage = request.getAttribute(RequestDispatcher.ERROR_MESSAGE)
                ErrorResponse(
                    errorCause = "HttpMessageNotReadableException",
                    message = if (reqMessage is String && reqMessage.isNotBlank()) reqMessage else error.message
                )
            }
            // 想定外の例外が発生
            else -> {
                val reqMessage = request.getAttribute(RequestDispatcher.ERROR_MESSAGE)
                ErrorResponse(
                    errorCause = "SystemException",
                    message = if (reqMessage is String && reqMessage.isNotBlank()) reqMessage else error.message
                )
            }
        }

        return ResponseEntity.status(status).body(body)
    }
}