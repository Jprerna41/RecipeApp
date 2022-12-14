package com.sapient.recipeapp.ui.fragments.recipeList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.sapient.recipeapp.databinding.FragmentRecipeListBinding
import com.sapient.recipeapp.domain.utils.Resource
import com.sapient.recipeapp.ui.base.BaseFragment
import com.sapient.recipeapp.ui.fragments.recipeList.adapter.RecipeListAdapter
import com.sapient.recipeapp.ui.model.RecipeDetailUiState
import com.sapient.recipeapp.ui.model.RecipeUiState
import com.sapient.recipeapp.utils.extensions.gone
import com.sapient.recipeapp.utils.extensions.showToast
import com.sapient.recipeapp.utils.extensions.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipeListFragment : BaseFragment<FragmentRecipeListBinding>(), RecipeListActionListener {

    private val recipeListViewModel: RecipeListViewModel by viewModels()
    private lateinit var recipeListAdapter: RecipeListAdapter

    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentRecipeListBinding {
        return FragmentRecipeListBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recipeListViewModel.getRecipes()
        super.onViewCreated(view, savedInstanceState)

        binding.rvRecipes.apply {
            recipeListAdapter = RecipeListAdapter(this@RecipeListFragment)
            adapter = recipeListAdapter
            layoutManager = GridLayoutManager(context, 2)
            (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
            setHasFixedSize(true)
        }

        initObserver()
    }

    private fun initObserver() = with(recipeListViewModel) {
        recipesData.observe(viewLifecycleOwner) { data ->
            when (data) {
                is Resource.Success -> {
                    binding.pbLoading.gone()
                    bindListData(data.data)
                }
                is Resource.Error -> {
                    binding.pbLoading.gone()
                    data.errorMessage?.showToast(context)
                }
                is Resource.Loading -> binding.pbLoading.visible()
            }
        }
    }

    private fun bindListData(recipes: List<RecipeUiState>?) =
        if (recipes.isNullOrEmpty())
            binding.tvNotFound.visible()
        else recipeListAdapter.submitList(recipes)

    override fun onItemSelected(recipe: RecipeUiState) {
        val detailModel = createRecipeDetailModel(recipe)
        val action = RecipeListFragmentDirections.actionNavigateToDetailView(detailModel)
        findNavController().navigate(action)
    }

    override fun onUpdateFavourite(recipesItem: RecipeUiState, position: Int) {
        with(recipeListViewModel) {
            if (recipesItem.isFavourite) addFavourite(recipesItem) else removeFavourite(recipesItem)
        }.also { recipeListAdapter.notifyItemChanged(position) }
    }

    private fun createRecipeDetailModel(recipe: RecipeUiState) = RecipeDetailUiState(
        id = recipe.id,
        title = recipe.title,
        summary = recipe.summary,
        imageUrl = recipe.imageUrl,
        sourceName = recipe.sourceName,
        analyzedInstructions = recipe.analyzedInstructions
    )
}