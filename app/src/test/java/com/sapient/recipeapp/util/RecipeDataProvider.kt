package com.sapient.recipeapp.util

import com.sapient.recipeapp.domain.model.IngredientDomainModel
import com.sapient.recipeapp.domain.model.InstructionDomainModel
import com.sapient.recipeapp.domain.model.RecipeDomainModel
import com.sapient.recipeapp.domain.model.StepsDomainModel

class RecipeDataProvider {
    companion object {
        private val ingredientEntity =
            IngredientDomainModel(10511297, "fresh parsley", "fresh parsley", "parsley.jpg")
        private val stepEntity = StepsDomainModel(
            1,
            "Remove the cauliflower's tough stem and reserve for another use. Using a food processor, pulse cauliflower florets until they resemble rice or couscous. You should end up with around four cups of cauliflower rice.",
            listOf(ingredientEntity)
        )
        private val instructionEntity = InstructionDomainModel("instruction1", listOf(stepEntity))
        private val dishTypeList = listOf("side dish", "side dish")

        fun getTestFavouriteRecipe() = RecipeDomainModel(
            id = 8723648,
            title = "Test",
            summary = "Test Summary",
            imageUrl = "https://spoonacular.com/recipeImages/716426-312x231.jpg",
            sourceName = "Full Belly Sisters",
            analyzedInstructions = listOf(instructionEntity),
            isFavourite = true
        )
    }
}