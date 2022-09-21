import kotlin.random.Random

fun <T> List<T>.getRandomItem(): T? {
	if (isEmpty()) return null
	if (size < 2 ) return first()
	return this[Random.nextInt(0,this.lastIndex)]
}
