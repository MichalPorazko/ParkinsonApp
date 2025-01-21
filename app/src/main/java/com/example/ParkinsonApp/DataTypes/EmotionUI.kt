import com.example.ParkinsonApp.DataTypes.EmotionState
import com.example.ParkinsonApp.R

val listoFFaces = listOf(
    Face(EmotionState.VERY_SAD, R.drawable.verysad),
    Face(EmotionState.SAD, R.drawable.sad),
    Face(EmotionState.NEUTRAL, R.drawable.medium),
    Face(EmotionState.GOOD, R.drawable.happy),
    Face(EmotionState.VERY_GOOD, R.drawable.veryhappy)
)


open class EmotionUI(
    open val name: String,
    open val faces: List<Face> = listoFFaces
)

data class Face(
    val emotionState: EmotionState,
    val imageResId: Int
)

data class Anxiety(
    override val name: String = "Anxiety",
    override val faces: List<Face> = listoFFaces
) : EmotionUI(name, faces)

data class Concentration(
    override val name: String = "Concentration",
    override val faces: List<Face> = listoFFaces
) : EmotionUI(name, faces)

data class Mood(
    override val name: String = "Mood",
    override val faces: List<Face> = listoFFaces
) : EmotionUI(name, faces)

val emotionsList = listOf(
    Anxiety(),
    Concentration(),
    Mood()
)