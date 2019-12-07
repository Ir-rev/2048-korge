import com.soywiz.kds.*
import com.soywiz.korag.*
import com.soywiz.korge.component.*
import com.soywiz.korge.input.*
import com.soywiz.korge.render.*
import com.soywiz.korge.view.*
import com.soywiz.korim.bitmap.*
import com.soywiz.korim.format.*
import com.soywiz.korio.util.encoding.*
import kotlin.properties.*

open class UISkin(
	val normal: BmpSlice,
	val hover: BmpSlice,
	val down: BmpSlice
)

private val DEFAULT_UI_SKIN_IMG by lazy {
	PNG.decode(
		"iVBORw0KGgoAAAANSUhEUgAAAQAAAAEACAYAAABccqhmAAAG/klEQVR42uzXNVAkQRTG8beOu82SXnzu7u7uZtm5++HuLrmdu1uEW34Z5LjLuzdTNLXV18XG29Nf1a8g5F9NN6x9+dL1IJiT7J60hLiJE3xrfaSDtJBX5A0ZAcHM0h8aGi7qN/H5q1kED8AekklmgFz7S+6Ql8DNTP30CAj71fmrB8BGUsltkHuZ5D4ZBzYT9tNDYOp+Qv1qdmAzx+GDR+MdAmbuV/1qVqJvv+jwT5w8AhWVhfD562v48euDT3n34TmUVxYaDYLdnmyG6fpPnjoKldXF8PX7W/j155NP+fDpJVRWFekNwv7u7k6v/UHBLoiMDoRYLQTi3L4lVgs2fnZqmO781egjgJO0E2ROnriATU0tODg4KAW95djRs6yP6SBOUf+pExextaUNR0ZGpKC3HD96Tti/bcsBXTtBZvfOw3jy5Bk8f/78lAsXLuCNGzcwISEBs7KyMC8vD/Pz831CwtMkPHzwNOtjOogTQD0Axwh6amxsxoGBAanU1tTrbbzjov6W5jYcHh6WSn19k7CfLsExgp5OnTpjXHjCLr5x6QsKCnxWQkKy3sY7DqAegGcEmbLSKuzr65NOd3cPJidl8RfgOd9fXlat/9cgnb6+fkxJzv6vny7BM4LM4UMn8OLFi1MeP36MhYWFUrh86Qb/ADwHk89KFoDHli9fAhMTE9JBnIBZs2cCt/l8/8pVywARpQOAMHvubK/9AYEusFgsBk3TICYmBqxWqxTc7hhBv3oA4sBjsXGx+oWRUkRkJHDT+H5NiwNElFJ0dJTXfofDZlz+sLAw4/LT99IIDAoQ9KsHwAUes9msMD4+LiWr1QLcnHy/3W6X9gG0Wq1e+9lfy/j4eP2rVPSz5fvVA8CNflGk5m2IKDVv0y9KeHg4uFz/2LtjHYRhIAbDiAwMff/HzQWlYm9Yz98vRWVlwNi+a/vZn9sdRAvA0w/A9//Z/31tefAgAHPOtqdqcgAHAnBd1/63bHkgAsQ6gLXqRAC62n8CwAFUeAQ4E4AxBgHoBwdAAI5KwNYHsQ5gn+wO4HXWAQQJAN4ycJADWMUBINkB6AA4AOSWgHMSAA4AsQ4gfgyoA4BFIA6AAMAUgACIALAIZArCASRAAOrQAVgEGmO0PeAARAARADqAwAhQIgCi9wBsAhIABO8B6AB0ALAJaAqgA4BNQCWgCJADAeAAanEA4ACUgDoAGAOKACJADASg0m8GWrmLQASAANR9dTuwCAC3A4sAIgBsAhIAAhCFKYAxoA4A9gCMAXUAsAcgAogASRAAtwOLADAFyI0AngoMLwYRATgAGAOKABwAjAFNAQhACF4O6vXgIgCMAUNfD74PB4BIAbAItMI7AAIgApQHgogAEAESBWDdVxEASsDEDuCv5wEQgC+7ZrElRQxG4fs+vAGHJe7usGIJS3SF2xp3d3sH3N2hxqddS1NCyN/UoiaEU2wrk3vON9Kar+QmLaYAtCOOk5wlsP7+XOWvXgVoi8nfBWAjkyiKtDwBGGOqNwFD2Z+xUMsVQBAwJPn+dBttZ/8k+aXwNwVQQia+H2j32j+OY7iujyAIIaUk+9u2rV350Tbo2g4cQZ6/5/raFkCtWlP4mwJ4h0yajY52J4DtuIiTGI7tQsoL2d/6OaDd7N9qd5HECUqjlVz//v5hbZf/T568UPibAriHTEqjNdi2q8nM9+fkZwFD4DO0Wzak3JP937z+gGqlps0bf+12F74XoNVq48d3K9f/7ZtP+PHD0m72J6ebN+4p/E0B3BQMI5PBgRK6XaewS/4oiuD1DvoOnfi9pX+j3oGUEXJX+T+4/wylUgVxXDx3GjMLw16Jl8s1OK6HdruDjx8+/7f/yePnYf3s12bmt6x+cvqXv8mkiZOXCLjMzBnz+KLFy/iy5av48hXFgsZMYycHlRs55/mvXbuOb9y0jW/fsZvv2LmnSNCYaezkkOs/a8aSJQIuc/7cFf79+0/ueR5njBUKGjONnRxUbuQMkzElcFDAxwkHjf+YUAkcFPBxguRPMd8D2Co4BP1zOHXNxPgbf1MAiWCDYLHgB/QLOS0RrE9d0xh/428KIJvbggmC1enf/YIIxUsk6E8dVqdOt5Cf3+3PAQEAAAQAIPB/sx/Ug179/QEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABgAgAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAgBsWW/M0jYx8rEYAAAAASUVORK5CYII=".fromBase64()
	)
}

object DefaultUISkin : UISkin(
	normal = DEFAULT_UI_SKIN_IMG.sliceWithSize(0, 0, 64, 64),
	hover = DEFAULT_UI_SKIN_IMG.sliceWithSize(64, 0, 64, 64),
	down = DEFAULT_UI_SKIN_IMG.sliceWithSize(127, 0, 64, 64)
)

open class UIButton(
	width: Double = 128.0,
	height: Double = 64.0,
	label: String = "Button",
	skin: UISkin = DefaultUISkin
) : UIView() {
	var skin: UISkin by Delegates.observable(skin) { _, _, _ -> updateState() }
	private val rect = ninePatch(skin.normal, width, height, 16.0 / 64.0, 16.0 / 64.0, (64.0 - 16.0) / 64.0, (64.0 - 16.0) / 64.0) {}
	private var bover by Delegates.observable(false) { _, _, _ -> updateState() }
	private var bpressing by Delegates.observable(false) { _, _, _ -> updateState() }
	init {
		this.width = width
		this.height = height
		mouse {
			onOver {
				bover = true
			}
			onOut {
				bover = false
			}
			onDown {
				bpressing = true
			}
			onUpAnywhere {
				bpressing = false
			}
		}
	}

	private fun updateState() {
		when {
			bpressing -> {
				rect.tex = skin.down
			}
			bover -> {
				rect.tex = skin.hover
			}
			else -> {
				rect.tex = skin.normal
			}
		}
	}

	override fun updatedSize() {
		super.updatedSize()
		rect.width = width
		rect.height = height
	}
}

open class UIView : Container() {
	override var width: Double by Delegates.observable(96.0) { prop, old, new -> updatedSize() }
	override var height: Double by Delegates.observable(32.0) { prop, old, new -> updatedSize() }

	protected open fun updatedSize() {
	}

	override fun renderInternal(ctx: RenderContext) {
		registerUISupportOnce(stage)
		super.renderInternal(ctx)
	}
}

fun registerUISupportOnce(stage: Stage?) {
	if (stage == null) return
	if (stage.getExtra("uiSupport") == true) return
	stage.setExtra("uiSupport", true)
	stage.keys {
		onKeyDown {

		}
	}
	stage?.getOrCreateComponent { stage ->
		object : UpdateComponentWithViews {
			override val view: View = stage
			override fun update(views: Views, ms: Double) {
			}
		}
	}
}