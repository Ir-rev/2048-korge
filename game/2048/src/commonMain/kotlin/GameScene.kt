import Colors.GAME_FIELD_BACKGROUND
import Constants.gameSceneSizeDouble
import com.soywiz.korge.view.RoundRect
import com.soywiz.korge.view.Stage
import com.soywiz.korge.view.addTo
import com.soywiz.korge.view.xy
import com.soywiz.korim.color.Colors

class GameScene {

	private var gameField: RoundRect? = null

	suspend fun createScene(stage: Stage) = with(stage) {
		gameField = createGameField().addTo(this)
	}

	private fun createSquarePlaceHolders(): List<RoundRect> {

	}

	private fun createGamePositions(): List<Int> {

	}

	private fun createGameField(): RoundRect {
		return RoundRect(
			width = gameSceneSizeDouble - marginFromMainFrame,
			height = gameSceneSizeDouble - marginFromMainFrame,
			rx = radius,
			fill = Colors[GAME_FIELD_BACKGROUND]
		).xy(marginFromMainFrame / 2, marginFromMainFrame / 2)
	}

	private fun createCellHolder() {

	}

	fun clear() {
		gameField = null
	}

	private companion object {
		private const val magicNumberForDividerSize = 40
		private const val dividerSize = Constants.gameSceneSize / magicNumberForDividerSize

		private const val radius = 16.0
		private const val marginFromMainFrame = 32

		private const val cellHeight = Constants.gameSceneSize - dividerSize * Constants.column
		private const val cellWidth = Constants.gameSceneSize - dividerSize * Constants.line
	}
}
