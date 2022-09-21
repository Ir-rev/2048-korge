import com.soywiz.korge.view.RoundRect
import com.soywiz.korge.view.Text
import com.soywiz.korma.geom.Point

data class GameCell(
	val point: Point,
	var value: Int?,
	val text: Text,
	val frame: RoundRect
)


