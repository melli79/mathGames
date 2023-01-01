package trivia

fun distribute(workers :List<UInt>, tasks :List<UInt>) :Pair<Collection<Pair<Int, Int>>, Collection<Int>> {
    val remainingTasks = tasks.mapIndexed { i, d -> Pair(d, i) }.groupBy { it.first }
        .map { e -> Pair(e.key, e.value.map { p -> p.second }.toMutableList()) }.toMap().toMutableMap()
    val remainingWorkers = workers.mapIndexed { i, c -> Pair(c, i) }.groupBy { it.first }
        .map { e -> Pair(e.key, e.value.map { p -> p.second}.toMutableList()) }.toMap().toMutableMap()
    val result = mutableListOf<Pair<Int, Int>>()
    val rejectedTasks = mutableListOf<Int>()
    while (remainingTasks.isNotEmpty()&&workers.isNotEmpty()) {
        val tasks = remainingTasks.entries.maxBy { e -> e.key }
        val task = tasks.value.removeAt(0)
        if (tasks.value.isEmpty()) {
            remainingTasks.entries.remove(tasks)
        }
        val workers = remainingWorkers.entries.filter { e -> e.key>=tasks.key }.minByOrNull { it.key }
        if (workers==null) {
            rejectedTasks.add(task)
            rejectedTasks.addAll(tasks.value)
            remainingTasks.entries.remove(tasks)
            continue
        }
        val worker = workers.value.removeAt(0)
        if (workers.value.isEmpty())
            remainingWorkers.entries.remove(workers)
        result.add(Pair(worker, task))
        val remaining = workers.key -tasks.key
        if (remaining>0u)
            remainingWorkers.computeIfAbsent(remaining){ mutableListOf() }.add(worker)
    }
    return Pair(result, rejectedTasks+remainingTasks.values.flatten())
}
