import com.soywiz.kds.*
import com.soywiz.klock.*
import com.soywiz.korge.*
import com.soywiz.korge.input.*
import com.soywiz.korge.scene.*
import com.soywiz.korge.view.*
import com.soywiz.korim.color.*
import com.soywiz.korma.geom.*
import com.soywiz.korma.geom.ds.*
import com.soywiz.korma.geom.shape.*
import com.soywiz.korma.geom.vector.*
import com.soywiz.korma.random.*
import kotlin.random.*

var SolidRect.movingDirection by extraProperty { -1 }

suspend fun main() = Korge(width = 1280, height = 720, bgcolor = Colors["#2b2b2b"], clipBorders = false) {
	val bvh = BVH2D<View>()
	val rand = Random(0)
	val rects = arrayListOf<SolidRect>()
	for (n in 0 until 2_000) {
		val x = rand[0.0, width]
		val y = rand[0.0, height]
		val width = rand[1.0, 50.0]
		val height = rand[1.0, 50.0]
		val view = solidRect(width, height, rand[Colors.RED, Colors.BLUE]).xy(x, y)
		view.movingDirection = if (rand.nextBoolean()) -1 else +1
		rects += view
		bvh.insertOrUpdate(view.globalBounds, view)
	}
	addUpdater {
		for (n in rects.size - 100 until rects.size) {
			val view = rects[n]
			if (view.x < 0) {
				view.movingDirection = +1
			}
			if (view.x > stage.width) {
				view.movingDirection = -1
			}
			view.x += view.movingDirection
			bvh.insertOrUpdate(view.globalBounds, view)
		}
	}
	val center = Point(width / 2, height / 2)
	val dir = Point(-1, -1)
	val ray = Ray(center, dir)
	val statusText = text("", font = debugBmpFont())
	var selectedRectangle = Rectangle(Point(100, 100) - Point(50, 50), Size(100, 100))
	val rayLine = line(center, center + (dir * 1000), Colors.WHITE)
	val selectedRect = outline(buildPath { rect(selectedRectangle) })
	//outline(buildPath { star(5, 50.0, 100.0, x = 100.0, y = 100.0) })
	//debugLine(center, center + (dir * 1000), Colors.WHITE)
	fun updateRay() {
		var allObjectsSize = 0
		var rayObjectsSize = 0
		var rectangleObjectsSize = 0
		val allObjects = bvh.search(Rectangle(0.0, 0.0, width, height))
		val time = measureTime {
			val rayObjects = bvh.intersect(ray)
			val rectangleObjects = bvh.search(selectedRectangle)
			for (result in allObjects) result.value?.alpha = 0.2
			for (result in rectangleObjects) result.value?.alpha = 0.8
			for (result in rayObjects) result.obj.value?.alpha = 1.0
			allObjectsSize = allObjects.size
			rayObjectsSize = rayObjects.size
			rectangleObjectsSize = rectangleObjects.size
		}
		statusText.text = "All objects: ${allObjectsSize}, raycast = ${rayObjectsSize}, rect = ${rectangleObjectsSize}, time = $time"
	}
	updateRay()

	addUpdater {
		//println("moved")
		val mousePos = stage.mouseXY
		val angle = Point.angleFull(center, mousePos)
		//println("center=$center, mousePos=$mousePos, angle = $angle")
		dir.setTo(angle.cosine, angle.sine)
		rayLine.setPoints(center, center + (dir * 1000))

		updateRay()
	}

	mouse {
		onDown {
			selectedRectangle = Rectangle(stage.mouseXY - Point(50, 50), Size(100, 100))
			selectedRect.vectorPath = buildPath { rect(selectedRectangle) }
		}
		onMouseDrag {
			selectedRectangle = Rectangle(stage.mouseXY - Point(50, 50), Size(100, 100))
			selectedRect.vectorPath = buildPath { rect(selectedRectangle) }
		}
	}
}
