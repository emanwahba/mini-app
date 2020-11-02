package com.mobiquity.miniapp.model.repository

import com.mobiquity.miniapp.model.remote.CategoryService

class CategoryRepository constructor(
    private val categoryService: CategoryService
) : BaseRepository() {

    suspend fun getCategoryList() = getResult { categoryService.getCategoryList() }

}