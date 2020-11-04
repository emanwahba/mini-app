package com.mobiquity.miniapp.ui.categorylist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mobiquity.miniapp.R
import com.mobiquity.miniapp.model.entities.Category
import com.mobiquity.miniapp.model.entities.Product
import com.mobiquity.miniapp.utils.setProductImage
import kotlinx.android.synthetic.main.list_item_header.view.*
import kotlinx.android.synthetic.main.list_item_product.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val ITEM_VIEW_TYPE_CATEGORY = 0
private const val ITEM_VIEW_TYPE_PRODUCT = 1

class CatalogRecyclerViewAdapter(
    private val clickListener: ProductClickListener
) : ListAdapter<DataItem, RecyclerView.ViewHolder>(CatalogDiffCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    fun filterItemsAndSubmitList(categoryList: List<Category>) {
        adapterScope.launch {
            var items: List<DataItem> = listOf()
            for (category in categoryList) {
                items = items +
                        listOf(DataItem.CategoryHeaderItem(category.id, category.name)) +
                        category.products.map { DataItem.ProductItem(it) }
            }
            withContext(Dispatchers.Main) {
                submitList(items)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_CATEGORY -> CategoryHeaderViewHolder.from(
                parent
            )
            ITEM_VIEW_TYPE_PRODUCT -> ProductViewHolder.from(
                parent
            )
            else -> throw ClassCastException("Unknown view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CategoryHeaderViewHolder -> {
                val item = getItem(position) as DataItem.CategoryHeaderItem
                holder.bind(item.categoryName)
            }
            is ProductViewHolder -> {
                val item = getItem(position) as DataItem.ProductItem
                holder.bind(item.product, clickListener)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DataItem.CategoryHeaderItem -> ITEM_VIEW_TYPE_CATEGORY
            is DataItem.ProductItem -> ITEM_VIEW_TYPE_PRODUCT
        }
    }

    class CategoryHeaderViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        companion object {
            fun from(parent: ViewGroup): CategoryHeaderViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.list_item_header, parent, false)
                return CategoryHeaderViewHolder(view)
            }
        }

        fun bind(categoryName: String) {
            itemView.header.text = categoryName
        }
    }

    class ProductViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        companion object {
            fun from(parent: ViewGroup): ProductViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.list_item_product, parent, false)
                return ProductViewHolder(view)
            }
        }

        fun bind(item: Product, clickListener: ProductClickListener) {
            val context = itemView.context
            itemView.name.text = item.name
            itemView.image.setProductImage(
                item,
                context.resources.getDimension(R.dimen.image_width_thumbnail),
                context.resources.getDimension(R.dimen.image_height_thumbnail),
            )
            itemView.layout.setOnClickListener { clickListener.onClick(product = item) }
        }
    }
}

class CatalogDiffCallback : DiffUtil.ItemCallback<DataItem>() {
    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem == newItem
    }
}

sealed class DataItem {
    abstract val id: String

    data class ProductItem(val product: Product) : DataItem() {
        override val id = product.categoryId + "_" + product.id
    }

    data class CategoryHeaderItem(val categoryId: String, val categoryName: String) : DataItem() {
        override val id = categoryId
    }
}

class ProductClickListener(val clickListener: (product: Product) -> Unit) {
    fun onClick(product: Product) = clickListener(product)
}