package com.mobiquity.miniapp.ui.productdetails

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mobiquity.miniapp.model.entities.Product

class ProductDetailsViewModel @ViewModelInject constructor() : ViewModel() {

    private val _product = MutableLiveData<Product>()
    val product: LiveData<Product> = _product

    fun setProduct(product: Product) {
        _product.value = product
    }
}