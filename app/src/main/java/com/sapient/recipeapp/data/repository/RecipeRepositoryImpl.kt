package com.sapient.recipeapp.data.repository

import com.sapient.recipeapp.data.local.LocalDataSource
import com.sapient.recipeapp.data.mapper.RecipeEntityMapper
import com.sapient.recipeapp.data.model.RecipeEntity
import com.sapient.recipeapp.data.remote.RemoteDataSource
import com.sapient.recipeapp.data.remote.network.ApiResponse
import com.sapient.recipeapp.domain.utils.Resource
import com.sapient.recipeapp.domain.model.RecipeDomainModel
import com.sapient.recipeapp.domain.repository.RecipeRepository
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipeRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val mapper: RecipeEntityMapper
) : RecipeRepository {

    override fun requestRecipes(): Flow<Resource<List<RecipeDomainModel>>> {
        return flow {
            emitAll(remoteDataSource.getRecipes().map {
                when (it) {
                    is ApiResponse.Success -> {
                        Resource.Success(it.data.map { recipeResponse ->
                            recipeResponse.isFavourite = getIsFavouriteFromDb(recipeResponse)
                            mapper.mapToDomainModel(recipeResponse)
                        })
                    }
                    is ApiResponse.Empty -> {
                        Resource.Success(emptyList())
                    }
                    is ApiResponse.Error -> {
                        Resource.Error(it.errorMessage)
                    }
                }
            })
        }
    }

    private suspend fun getIsFavouriteFromDb(recipeResponse: RecipeEntity): Boolean {
        return localDataSource.getFavorite(recipeResponse.id).first() != null
    }

    override fun insertFavorite(recipe: RecipeDomainModel) {
        localDataSource.insertFavorite(mapper.mapToDataModel(recipe))
    }

    override fun deleteFavorite(recipe: RecipeDomainModel) {
        localDataSource.removeFavourite(mapper.mapToDataModel(recipe))
    }
}