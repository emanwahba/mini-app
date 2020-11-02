package com.mobiquity.miniapp.model.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Product(
    val id: String,
    val categoryId: String,
    val name: String,
    val url: String,
    val description: String,
    val salePrice: Price
) : Parcelable

@Parcelize
data class Price(
    val amount: String,
    val currency: String
) : Parcelable