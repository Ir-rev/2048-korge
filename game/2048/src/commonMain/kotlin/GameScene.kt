import Colors.EMPTY_CELL_COLOR
import Colors.GAME_FIELD_BACKGROUND
import Colors.GAME_PLACEHOLDER_BACKGROUND
import Constants.COLUMN_COUNT
import Constants.GAME_CELLS
import Constants.LINE_COUNT
import Constants.gameSceneSizeDouble
import com.soywiz.korge.input.keys
import com.soywiz.korge.view.RoundRect
import com.soywiz.korge.view.Stage
import com.soywiz.korge.view.Text
import com.soywiz.korge.view.addTo
import com.soywiz.korge.view.xy
import com.soywiz.korim.color.Colors
import com.soywiz.korim.paint.Paint
import com.soywiz.korma.geom.Point

class GameScene {

	private var gameCellsList: List<GameCell> = emptyList()
	private lateinit var gameField: RoundRect
	private val queueUpdate = mutableListOf<GameCell>()

	suspend fun createScene(stage: Stage) = with(stage) {
		gameField = createGameField().addTo(this)
		gameCellsList = createGameCells()
		createSquarePlaceHolders().forEachIndexed { index, roundRect ->
			roundRect.xy(gameCellsList[index].point).addTo(this)
		}
		gameCellsList.forEach {
			it.frame.addTo(this)
			it.text.addTo(this)
		}
		keys {
			this.down {
				addRandomValueInEmptyCell()
				queueUpdate.forEach {
					it.text.text = it.value.toString()
					it.frame.color = Colors["#5dff1d"]
				}
			}
		}
	}

	private fun addRandomValueInEmptyCell() {
		val randomItem = gameCellsList
			.filter { it.value == null }
			.getRandomItem()
		randomItem.value = 4
		queueUpdate.add(randomItem)
	}

	private fun createSquarePlaceHolders(): List<RoundRect> {
		val result = mutableListOf<RoundRect>()
		for (i in 0 until GAME_CELLS) {
			result.add(createCell(Colors[GAME_PLACEHOLDER_BACKGROUND]))
		}
		return result
	}

	private fun createCell(color: Paint) =
		RoundRect(
			width = getCellWidth(),
			height = getCellHeight(),
			rx = cellRadius,
			fill = color
		)

	private fun getCellWidth() = (gameField.width - (LINE_COUNT + 1) * dividerSize) / LINE_COUNT
	private fun getCellHeight() = (gameField.height - (COLUMN_COUNT + 1) * dividerSize) / COLUMN_COUNT

	private fun createGameCells(): List<GameCell> {
		val resultList = mutableListOf<GameCell>()
		val stepWidth = getCellWidth()
		val stepHeight = getCellHeight()
		for (positionY in 0 until LINE_COUNT) {
			for (positionX in 0 until COLUMN_COUNT) {
				val x = positionX * stepWidth + dividerSize * (positionX + 1) + marginFromMainFrame / 2
				val y = positionY * stepHeight + dividerSize * (positionY + 1) + marginFromMainFrame / 2
				resultList.add(
					GameCell(
						point = Point(
							x = x,
							y = y
						),
						value = null,
						text = createTextForCell().xy(x,y),
						frame = createCell(Colors[EMPTY_CELL_COLOR]).xy(x,y)
					)
				)
			}
		}
		return resultList
	}

	private fun createTextForCell(): Text = Text("")

	private fun createGameField(): RoundRect {
		return RoundRect(
			width = gameSceneSizeDouble - marginFromMainFrame,
			height = gameSceneSizeDouble - marginFromMainFrame,
			rx = gameFieldRadius,
			fill = Colors[GAME_FIELD_BACKGROUND]
		).xy(marginFromMainFrame / 2, marginFromMainFrame / 2)
	}

	private companion object {
		private const val magicNumberForDividerSize = 40
		private const val dividerSize = Constants.gameSceneSize / magicNumberForDividerSize

		private const val gameFieldRadius = 16.0
		private const val cellRadius = 8.0
		private const val marginFromMainFrame = 32

		private const val cellHeight = Constants.gameSceneSize - dividerSize * Constants.COLUMN_COUNT
		private const val cellWidth = Constants.gameSceneSize - dividerSize * Constants.LINE_COUNT
	}
}
