package com.mobiquity.miniapp.ui.productdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.mobiquity.miniapp.R
import com.mobiquity.miniapp.utils.setProductImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.product_details_fragment.*

@AndroidEntryPoint
class ProductDetailsFragment : Fragment() {

    private val viewModel: ProductDetailsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.product_details_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val arguments = ProductDetailsFragmentArgs.fromBundle(requireArguments())
        viewModel.setProduct(arguments.product)
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.product.observe(viewLifecycleOwner, Observer {
            name.text = getString(R.string.product_name, it.name)
            price.text = getString(
                R.string.product_price, it.salePrice.amount, it.salePrice.currency
            )
            image.setProductImage(
                it,
                resources.getDimension(R.dimen.image_width_big),
                resources.getDimension(R.dimen.image_height_big),
            )
        })
    }
}