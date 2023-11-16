enum class ForkStatus {
    FREE, // вилка свободна
    TAKEN // вилка занята
}
data class Philosopher(
    val id: Int,//идентификатор философа
    val left: Fork,
    val right: Fork
) : Runnable {
    override fun run() {
        while (true) {//бесконечный цикл, который будет повторяться, пока не будет прерван выполнением потока.
            if (left.take(id) && right.take(id)) {
                println("Филосов $id ест")
                left.put(id)//возвращение вилок в исходное состояние
                right.put(id)
                break
            } else {
                left.put(id)
                right.put(id)
                println("Филосов $id думает")
            } } } }
class Fork {//описывает вилку, которая может быть свободной или занятой.

    private var status = ForkStatus.FREE
    private var takenBy: Int? = null

    @Synchronized
    fun take(id: Int): Boolean {
        if (status == ForkStatus.FREE) {
            status = ForkStatus.TAKEN
            takenBy = id
            return true
        }
        return false
    }
    @Synchronized
    fun put(id: Int) {
        if (status == ForkStatus.TAKEN && takenBy == id) {
            status = ForkStatus.FREE
            takenBy = null
        } } }
fun main() {
    val philosophersCount = readlnOrNull()?.toIntOrNull()
    if (philosophersCount == null || philosophersCount <= 0) {
        println("Не правильный ввод")
        return
    }
    val forks = List(philosophersCount) { Fork() }
    val philosophers = List(philosophersCount) { id ->
        val leftFork = forks[id]
        val rightFork = forks[(id + 1) % philosophersCount]
        Philosopher(id, leftFork, rightFork)
    }
    philosophers.shuffled().forEach { philosopher ->// перемешиваем элементы коллекции и для каждого перемешанного элемента выполняем определенное действие
// для каждого философа в массиве запускается новый поток
        Thread(philosopher).start()
    }
}