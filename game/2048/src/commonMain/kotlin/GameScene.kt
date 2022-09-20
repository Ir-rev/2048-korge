import Colors.GAME_FIELD_BACKGROUND
import Colors.GAME_PLACEHOLDER_BACKGROUND
import Constants.COLUMN_COUNT
import Constants.GAME_CELLS
import Constants.LINE_COUNT
import Constants.gameSceneSizeDouble
import com.soywiz.korge.view.RoundRect
import com.soywiz.korge.view.Stage
import com.soywiz.korge.view.addTo
import com.soywiz.korge.view.xy
import com.soywiz.korim.color.Colors
import com.soywiz.korma.geom.Point

class GameScene {

	private var gamePositions: List<Point> = emptyList()
	private lateinit var gameField: RoundRect

	suspend fun createScene(stage: Stage) = with(stage) {
		gameField = createGameField().addTo(this)
		gamePositions = createGamePositions(gameField.width, gameField.height)
		createSquarePlaceHolders().forEachIndexed { index, roundRect ->
			roundRect.xy(gamePositions[index]).addTo(this)
		}
	}

	private fun createSquarePlaceHolders(): List<RoundRect> {
		val result = mutableListOf<RoundRect>()
		for (i in 0 until GAME_CELLS) {
			result.add(
				RoundRect(
					width = getCellWidth(),
					height = getCellHeight(),
					rx = 1.0,
					fill = Colors[GAME_PLACEHOLDER_BACKGROUND]
				)
			)
		}
		return result
	}

	private fun getCellWidth() = (gameField.width - (LINE_COUNT + 1) * dividerSize) / LINE_COUNT
	private fun getCellHeight() = (gameField.height - (COLUMN_COUNT + 1) * dividerSize) / COLUMN_COUNT

	private fun createGamePositions(fieldWidth: Double, fieldHeight: Double): List<Point> {
		val resultList = mutableListOf<Point>()
		val stepWidth = getCellWidth()
		val stepHeight = getCellHeight()
		for (positionY in 0 until LINE_COUNT) {
			for (positionX in 0 until COLUMN_COUNT) {
				resultList.add(
					Point(
						x = positionX * stepWidth + dividerSize * (positionX + 1) + marginFromMainFrame / 2,
						y = positionY * stepHeight + dividerSize * (positionY + 1) + marginFromMainFrame / 2
					)
				)
			}
		}
		return resultList
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

	private companion object {
		private const val magicNumberForDividerSize = 40
		private const val dividerSize = Constants.gameSceneSize / magicNumberForDividerSize

		private const val radius = 16.0
		private const val marginFromMainFrame = 32

		private const val cellHeight = Constants.gameSceneSize - dividerSize * Constants.COLUMN_COUNT
		private const val cellWidth = Constants.gameSceneSize - dividerSize * Constants.LINE_COUNT
	}
}
