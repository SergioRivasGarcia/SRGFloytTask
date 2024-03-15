package com.example.presentation.main.products.list.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.products.entity.Product
import com.example.presentation.R
import com.example.presentation.databinding.ItemProductBinding
import com.example.presentation.databinding.ItemProductGroupBinding
import com.example.presentation.main.util.getCurrencySymbol
import com.example.presentation.main.util.round
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.flow.Flow

class ProductAdapter(
    private val context: Context,
    private val listener: UpdatedBasketProductsListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items: ArrayList<ProductListItem> = arrayListOf()
    private var deleteMode: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            ProductListItem.TYPE_GROUP ->
                GroupViewHolder(ItemProductGroupBinding.inflate(layoutInflater, parent, false))

            else ->
                ProductViewHolder(ItemProductBinding.inflate(layoutInflater, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            ProductListItem.TYPE_GROUP -> (holder as GroupViewHolder).bind(
                item = items[position] as GroupItem,
            )

            ProductListItem.TYPE_PRODUCT -> (holder as ProductViewHolder).bind(
                item = items[position] as ProductItem
            )
        }
    }

    override fun getItemViewType(position: Int): Int {
        return items[position].itemType
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class GroupViewHolder(val binding: ItemProductGroupBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: GroupItem) {
            binding.tvGroupTitle.text = item.typeName
            binding.tvGroupSubtotal.text = context.getString(
                R.string.price_currency,
                item.subtotal.round(2),
                getCurrencySymbol()
            )
        }
    }

    fun getSubtotal(typeName: String): Double {
        return items.filterIsInstance<ProductItem>().filter { it.type == typeName }
            .sumOf { (it.quantity * it.price).toDouble() }
    }

    inner class ProductViewHolder(val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ProductItem) {
            with(binding) {
                ivDelete.isVisible = deleteMode
                ivDelete.setOnClickListener {
                    MaterialAlertDialogBuilder(context)
                        .setTitle(context.getString(R.string.remove_dialog_title, item.name))
                        .setNegativeButton(context.getString(R.string.cancel)) { dialog, which ->

                        }
                        .setPositiveButton(context.getString(R.string.remove)) { dialog, which ->
                            listener.onDeleteProduct(item)
                            notifyDataSetChanged()
                        }
                        .show()
                }
                tvProductTitle.text = item.name
                tvUnitPrice.text =
                    context.getString(R.string.price_currency, item.price, getCurrencySymbol())
                componentQuantity.tvQuantity.text = item.quantity.toString()
                componentQuantity.btnDecrease.isEnabled = item.quantity != if (deleteMode) 1 else 0
                componentQuantity.tvQuantity.text = item.quantity.toString()
                componentQuantity.btnDecrease.setOnClickListener {
                    item.quantity = item.quantity.minus(1)
                    items.filterIsInstance<GroupItem>()
                        .find { it.typeName == item.type }!!.subtotal = getSubtotal(item.type)
                    listener.onUpdateProduct(item)
                    notifyDataSetChanged()
                }

                componentQuantity.btnIncrease.setOnClickListener {
                    item.quantity = item.quantity.plus(1)
                    items.filterIsInstance<GroupItem>()
                        .find { it.typeName == item.type }!!.subtotal = getSubtotal(item.type)
                    listener.onUpdateProduct(item)
                    notifyDataSetChanged()
                }
            }
        }
    }

    fun setData(list: List<Product>) {
        val groupedProducts: Map<String, List<Product>> = list.groupBy { it.type }
        val productList = mutableListOf<ProductListItem>()
        for (type: String in groupedProducts.keys) {
            val groupItems: List<Product>? = groupedProducts[type]
            val subtotal = groupItems?.sumOf { (it.quantity * it.price).toDouble() }
            productList.add(GroupItem(type, subtotal?.round(2) ?: 0.0))
            groupItems?.forEach {
                productList.add(ProductItem(it.id, it.name, it.price, it.type, it.quantity))
            }
        }
        items.clear()
        productList.let {
            items.addAll(productList)
        }
        notifyDataSetChanged()
    }

    /**
     * This mode shows a delete button and locks the minimum amount a product can be set to [1]
     * instead of [0].
     */
    fun setDeleteMode(option: Boolean) {
        deleteMode = option
    }

    interface UpdatedBasketProductsListener {
        fun onUpdateProduct(productItem: ProductItem)
        fun onDeleteProduct(productItem: ProductItem)
    }
}
