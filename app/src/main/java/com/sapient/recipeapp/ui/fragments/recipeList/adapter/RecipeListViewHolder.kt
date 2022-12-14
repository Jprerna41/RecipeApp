package com.sapient.recipeapp.ui.fragments.recipeList.adapter

import androidx.recyclerview.widget.RecyclerView
import com.sapient.recipeapp.R
import com.sapient.recipeapp.databinding.ItemRecipeBinding
import com.sapient.recipeapp.ui.fragments.recipeList.RecipeListActionListener
import com.sapient.recipeapp.ui.model.RecipeUiState
import com.sapient.recipeapp.utils.extensions.loadRecipeImage

class RecipeListViewHolder(
    private val itemBinding: ItemRecipeBinding,
    private val recyclerItemListener: RecipeListActionListener
) : RecyclerView.ViewHolder(itemBinding.root) {

    fun bind(recipesItem: RecipeUiState) {
        itemBinding
            .apply {
                tvTitle.text = recipesItem.title
                tvAuthor.text = recipesItem.sourceName
                imgCover.loadRecipeImage(recipesItem.imageUrl)
                recipeItem.setOnClickListener {
                    recyclerItemListener.onItemSelected(recipesItem)
                }
                btnFavorite.apply {
                    setIconTintResource(getArchiveIconTint(recipesItem.isFavourite))
                    setOnClickListener {
                        recipesItem.isFavourite = !recipesItem.isFavourite
                        recyclerItemListener.onUpdateFavourite(
                            recipesItem,
                            adapterPosition
                        )
                    }
                }
            }
    }

    private fun getArchiveIconTint(isFavorite: Boolean): Int {
        return if (isFavorite) R.color.colorRed
        else R.color.colorBoulder
    }
}