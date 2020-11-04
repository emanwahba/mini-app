package com.mobiquity.miniapp.ui.categorylist

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobiquity.miniapp.model.entities.Category
import com.mobiquity.miniapp.model.entities.Product
import com.mobiquity.miniapp.model.repository.CategoryRepository
import com.mobiquity.miniapp.utils.Result
import kotlinx.coroutines.launch

class CategoryListViewModel @ViewModelInject constructor(
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    private val _categories = MutableLiveData<Result<List<Category>>>()
    val categories: LiveData<Result<List<Category>>> = _categories

    private val _navigateToProductDetails = MutableLiveData<Product>()
    val navigateToProductDetails: LiveData<Product> = _navigateToProductDetails

    fun fetchCatalog() = viewModelScope.launch {
        _categories.postValue(Result.Loading())

        val result = categoryRepository.getCategoryList()
        if (result.status == Result.Status.SUCCESS && result.data != null) {
            _categories.postValue(Result.Success(result.data!!))
        } else if (result.status == Result.Status.ERROR) {
            _categories.postValue(Result.Error(result.message))
        }
    }

    fun onProductClicked(product: Product) {
        _navigateToProductDetails.value = product
    }

    fun onProductDetailsNavigated() {
        _navigateToProductDetails.value = null
    }
}