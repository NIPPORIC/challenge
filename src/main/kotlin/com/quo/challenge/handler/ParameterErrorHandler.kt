package com.quo.challenge.handler

import jakarta.validation.ConstraintViolationException
import org.springframework.web.bind.MissingServletRequestParameterException

/** パス・クエリパラメータのエラーハンドラ */
class ParameterErrorHandler {
    /**
     * パス・クエリパラメータでバリデーションエラーとなった場合に利用するハンドラ
     * 前提として、パラメータはプリミティブ型であること
     */
    fun handleParameterException(ex: ConstraintViolationException) = InvalidError(
        0,
        ex.constraintViolations.map { err ->
            ItemError(
                // Path/Queryだと「xxx.プロパティ名」となるため、 末尾 . から先をフィールド名とする
                lastDotAfter(err.propertyPath.toString()),
                InvalidErrorTypeEnum.type(err.constraintDescriptor.annotation.annotationClass.simpleName ?: "Unknown"),
                err.message
            )
        }.toMutableList()
    )

    /**
     * 必須のクエリパラメータが存在しない場合に利用するハンドラ
     * 前提として、パラメータはプリミティブ型であること
     */
    fun handleParameterException(ex: MissingServletRequestParameterException) = InvalidError(
        0,
        mutableListOf(
            ItemError(
                ex.parameterName,
                // 必須エラーの場合だけであるため、固定で「Required」とする
                InvalidErrorTypeEnum.Required.name,
                ex.localizedMessage
            )
        )
    )

    /** 最後の . から先の文字列を取得 */
    private fun lastDotAfter(value: String) = value.substring(value.lastIndexOf(".") + 1)
}