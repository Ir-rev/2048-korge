import com.soywiz.korev.*
import com.soywiz.korge.*
import com.soywiz.korge.view.*
import com.soywiz.korim.color.*

suspend fun main() = Korge(width = 512, height = 512, bgcolor = Colors["#2b2b2b"]) {
	circle(25.0, fill = Colors.GREEN)
}


class GameRestartEvent : Event()
