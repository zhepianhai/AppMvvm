package com.gw.safty.common.network

class PagerResponse<T>(
    success: Boolean?,
    code: Int?,
    message: String?,
    path: String?,
    throwable: String?,
    accessToken: String?,
    val data: PagerWrapper<T>?
): BaseResponse(success, code, message, path, throwable, accessToken) {

    fun getRealData(): List<T>?{
        return data?.list
    }
}
data class PagerWrapper<T>(
    var pageNum: Int = 0,
    val pageSize: Int = 0,
    val size: Int = 0,
    val startRow: Int = 0,
    val endRow: Int = 0,
    val total: Int = 0,
    val pages: Int = 0,
    val prePage: Int = 0,
    val nextPage: Int = 0,
    val isFirstPage: Boolean = false,
    val isLastPage: Boolean = false,
    val hasPreviousPage: Boolean = false,
    val hasNextPage: Boolean =  false,
    val navigatePages: Int = 0,
    val navigateFirstPage: Int = 0,
    val navigateLastPage: Int = 0,
    val firstPage: Int =  0,
    val lastPage: Int = 0,
    var list: List<T>?)

fun <T> createEmptyPager(): PagerWrapper<T>{
    return PagerWrapper(pageNum = 1, pageSize = 0, total = 0, pages = 0, isFirstPage = true, isLastPage =  true, list = emptyList())
}