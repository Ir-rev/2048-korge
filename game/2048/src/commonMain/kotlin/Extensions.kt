import kotlin.random.Random

fun <T> List<T>.getRandomItem(): T = this[Random.nextInt(0,this.lastIndex)]
