import com.soywiz.klock.*
import com.soywiz.klock.hr.*
import com.soywiz.korge.*
import com.soywiz.korge.input.*
import com.soywiz.korge.view.*
import com.soywiz.korim.bitmap.*
import com.soywiz.korim.color.*
import com.soywiz.korio.async.*
import com.soywiz.korio.file.*
import com.soywiz.korio.file.std.*
import com.soywiz.korio.util.*
import com.soywiz.korvi.*

suspend fun main() = Korge(width = 1280, height = 720, bgcolor = Colors["#2b2b2b"]) {
	val view = korviView(views, resourcesVfs["video.mp4"])
	if (OS.isJs) {
		val text = text("Click to start playing the video...")
		mouse.click.once {
			text.removeFromParent()
			view.play()
		}
	} else {
		view.play()
	}
}

inline fun Container.korviView(views: Views, video: KorviVideo, callback: KorviView.() -> Unit = {}): KorviView = KorviView(views, video).also { addChild(it) }.also { callback(it) }
suspend inline fun Container.korviView(views: Views, video: VfsFile, autoPlay: Boolean = true, callback: KorviView.() -> Unit = {}): KorviView = KorviView(views, video, autoPlay).also { addChild(it) }.also { callback(it) }
class KorviView(val views: Views, val video: KorviVideo) : Image(Bitmaps.transparent), AsyncCloseable, BaseKorviSeekable by video {
	val onCompleted = Signal<Unit>()
	var autoLoop = true

	companion object {
		suspend operator fun invoke(views: Views, file: VfsFile, autoPlay: Boolean = true): KorviView {
			return KorviView(views, KorviVideo(file)).also {
				if (autoPlay) {
					it.video.play()
				}
			}
		}
	}

	val elapsedTime: TimeSpan get() = video.elapsedTime
	val elapsedTimeHr: HRTimeSpan get() = video.elapsedTimeHr

	fun play() {
		views.launchImmediately {
			video.play()
		}
	}

	init {
		var n = 0
		video.onVideoFrame {
			//println("VIDEO FRAME! : ${it.position.timeSpan},  ${it.duration.timeSpan}")
			bitmap = it.data.slice()
			n++
			//it.data.writeTo(tempVfs["image$n.png"], PNG)
			//delayFrame()
		}
		video.onComplete {
			views.launchImmediately {
				if (autoLoop) {
					seek(0L)
					video.play()
				}
			}
		}
	}
}