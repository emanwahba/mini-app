package com.mobiquity.miniapp.model.repository

import com.mobiquity.miniapp.model.remote.CategoryService
import javax.inject.Inject

class CategoryRepository @Inject constructor(
    private val categoryService: CategoryService
) : BaseRepository() {

    suspend fun getCategoryList() = getResult { categoryService.getCategoryList() }

}