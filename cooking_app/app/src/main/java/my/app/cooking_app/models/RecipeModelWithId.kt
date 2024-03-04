package my.app.cooking_app.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import my.app.cooking_app.room.GsonConverter

@Entity("recipe_model_with_id")
@TypeConverters(GsonConverter::class)
data class RecipeModelWithId (
    @PrimaryKey()
    @ColumnInfo(name = "id")
    var id : String,
    @ColumnInfo(name = "model")
    var model :RecipeModel,
)