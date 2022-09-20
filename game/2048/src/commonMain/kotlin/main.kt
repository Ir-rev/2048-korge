import Colors.MAIN_BACKGROUND
import Constants.gameSceneSize
import com.soywiz.korge.*
import com.soywiz.korim.color.Colors

suspend fun main() = Korge(width = gameSceneSize, height = gameSceneSize, bgcolor = Colors[MAIN_BACKGROUND]) {
	GameScene().createScene(this)
}

