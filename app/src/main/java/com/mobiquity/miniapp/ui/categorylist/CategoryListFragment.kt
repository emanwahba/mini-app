package com.mobiquity.miniapp.ui.categorylist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobiquity.miniapp.R
import com.mobiquity.miniapp.utils.Result
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.category_list_fragment.*
import javax.inject.Inject

@AndroidEntryPoint
class CategoryListFragment : Fragment() {

    @Inject
    lateinit var viewModel: CategoryListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel.fetchCatalog()

        viewModel.navigateToProductDetails.observe(
            viewLifecycleOwner, Observer { product ->
                product?.let {
                    //TODO add navigation code here
                    viewModel.onProductDetailsNavigated()
                }
            })

        return inflater.inflate(R.layout.category_list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val linearLayoutManager = LinearLayoutManager(activity)
        category_list.layoutManager = linearLayoutManager

        val adapter = CatalogRecyclerViewAdapter(ProductClickListener { product ->
            viewModel.onProductClicked(product)
        })
        category_list.adapter = adapter

        viewModel.getCategories().observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Result.Status.SUCCESS -> {
                    progress_bar.visibility = View.GONE
                    category_list.visibility = View.VISIBLE
                    if (it.data != null) adapter.filterItemsAndSubmitList(it.data!!)
                }
                Result.Status.ERROR ->
                    Toast.makeText(activity, it.message, Toast.LENGTH_SHORT).show()
                Result.Status.LOADING -> {
                    progress_bar.visibility = View.VISIBLE
                    category_list.visibility = View.GONE
                }
            }
        })
    }
}