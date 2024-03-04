package my.app.cooking_app.models

data class RecipeModel (
    var title: String ="",
    var recipes : MutableList<String> = mutableListOf(),
    var ingredients: MutableList<RecipeIngredient> = mutableListOf(),
    var image : String = "",
)