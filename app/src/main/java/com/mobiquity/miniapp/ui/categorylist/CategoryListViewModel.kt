package com.mobiquity.miniapp.ui.categorylist

import androidx.lifecycle.*
import com.mobiquity.miniapp.model.entities.Category
import com.mobiquity.miniapp.model.entities.Product
import com.mobiquity.miniapp.model.repository.CategoryRepository
import com.mobiquity.miniapp.utils.Result
import kotlinx.coroutines.launch
import javax.inject.Inject

class CategoryListViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    private var categories = MutableLiveData<Result<List<Category>>>()

    private val _navigateToProductDetails = MutableLiveData<Product>()
    val navigateToProductDetails
        get() = _navigateToProductDetails

    fun fetchCatalog() = viewModelScope.launch {
        categories.postValue(Result.Loading())

        val result = categoryRepository.getCategoryList()
        if (result.status == Result.Status.SUCCESS && result.data != null) {
            categories.postValue(Result.Success(result.data!!))
        } else if (result.status == Result.Status.ERROR) {
            categories.postValue(Result.Error(result.message))
        }
    }

    fun getCategories(): MutableLiveData<Result<List<Category>>> = categories

    fun onProductClicked(product: Product) {
        _navigateToProductDetails.value = product
    }

    fun onProductDetailsNavigated() {
        _navigateToProductDetails.value = null
    }

}