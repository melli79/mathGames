package xmasaoc

import partitions.abs
import java.io.BufferedReader
import java.io.InputStream

private const val RESOURCE_NAME = "11small-monkeys.prg2"
private val debug = false // '-' in RESOURCE_NAME

fun main(args: Array<String>) {
    val input = getInput(args.firstOrNull(), RESOURCE_NAME)
    val monkeys = parseMonkeys(input)
    if (debug)
        println(monkeys.joinToString("\n"))
    listOf(1,20, 1000, 2000, 3000, 4000, 5000, 6000, 7000, 8000, 9000, 10000).forEach { round ->
        Interpreter.cycle(monkeys, round)
        Interpreter.printStats()
    }
    if (debug)
    println("The monkeys have: " +monkeys.mapIndexed{ n, monkey ->
        "Monkey $n: ${monkey.items}"
    }.joinToString("\n"))
}

private object Interpreter {
    private var round = 0

    private lateinit var numItems :MutableList<Int>

    fun cycle(monkeys :List<Monkey>, untilRound :Int) {
        numItems = (1..monkeys.size).map { 0 }.toMutableList()
        while (round<untilRound) {
            if (debug)
                println("\n\nRound $round:")
            monkeys.forEachIndexed { n, monkey -> distributeForMonkey(n, monkey, monkeys) }
            round++
        }
    }

    private fun distributeForMonkey(n :Int, monkey :Monkey, monkeys :List<Monkey>) {
        if (debug)
            println("\n  Monkey $n:")
        val items :List<Int> = monkey.items.toList()
        numItems[n] += items.size
        monkey.items.clear()
        items.forEach { item -> distributeItem(monkey, item, monkeys) }
    }

    private fun distributeItem(monkey :Monkey, item :Int, monkeys :List<Monkey>) {
        val newValue = monkey.operation.eval(item)
        val finalValue = newValue/3
        if (abs(finalValue) % monkey.testMod == 0u) {
            if (debug)
                println("    $item -> $finalValue throwing to Monkey ${monkey.ifTarget}.")
            monkeys[monkey.ifTarget].items.add(finalValue)
        } else {
            if (debug)
                println("    $item -> $finalValue throwing to Monkey ${monkey.elseTarget}.")
            monkeys[monkey.elseTarget].items.add(finalValue)
        }
    }

    fun printStats() {
        val numItems = numItems ?: emptyList()
        println("Round $round:  " +numItems.mapIndexed { n, num -> "Monkey $n inspected items $num times." }.joinToString("\n"))
        val busiest2 = numItems.sortedDescending().take(2)
        val level = busiest2.fold(1) { f, p -> f*p }
        println("The busiest 2 have ${busiest2[0]} and ${busiest2[1]}, respectively, which gives level $level.")
    }
}

private fun parseMonkeys(inputStream :InputStream) :List<Monkey> {
    val result = mutableListOf<Monkey>()
    val input = inputStream.toReader()
    while (true) {
        if (!input.ready())
            break
        val line = input.readLine().trim()
        if (line.isEmpty())
            continue
        if (!line.startsWith("Monkey")) {
            println("Unknown declaration on line: $line")
            continue
        }
        val arg = line.substring(line.indexOf(' ')+1, line.indexOf(':'))
        val num = arg.toInt()
        val monkey = Monkey.parse(input, num)
        if (monkey!=null)
            result.add(num, monkey)
    }
    return result
}

private data class Monkey(val items :MutableList<Int>, val operation :Term, val testMod :UInt, val ifTarget :Int, val elseTarget :Int) {
    internal data class Builder(
        var worriedness :MutableList<Int>? =null,
        var operation :Term? =null,
        var testMod :UInt? =null,
        var ifTarget :Int? =null,
        var elseTarget :Int? =null
    ) {
        fun tryBuild(n :Int) :Monkey? {
            if (worriedness!=null && operation!=null && testMod!=null && ifTarget!=null && elseTarget!=null)
                return Monkey(worriedness!!, operation!!, testMod!!, ifTarget!!, elseTarget!!)
            print("Incomplete Monkey $n, missing ")
            if (worriedness==null)
                print("'Starting items:'")
            if (operation==null)
                print(", 'Operation: new = ...'")
            if (testMod==null)
                print(", 'Test: divisible by ##'")
            if (ifTarget==null)
                println(", 'If true: ...'")
            if (elseTarget==null)
                println(", 'If false: ...'")
            return null
        }
    }

    companion object {
        fun parse(input :BufferedReader, n :Int) :Monkey? {
            val builder = Builder()
            while (true) {
                val line = if (input.ready())
                    input.readLine().trim().split("\\s+".toRegex())
                  else
                      emptyList()
                if (line.isEmpty())
                    return builder.tryBuild(n)
                when (line[0]) {
                    "Starting" -> if (parseStarting(line, builder))
                        return null
                    "Operation:" -> if (parseOperation(line, builder))
                        return null
                    "Test:" -> if (parseTest(line, input, builder))
                        return null
                      else
                        break
                    else -> {
                        println("Unknown instruction: "+line.joinToString(" "))
                        return null
                    }
                }
            }
            return builder.tryBuild(n)
        }

        private fun parseTest(line :List<String>, input :BufferedReader, builder :Builder) :Boolean {
            if (line[1]!="divisible"||line[2]!="by") {
                println("expected 'divisible by ' on line " +line.joinToString(" "))
                return true
            }
            val mod = line[3].toUIntOrNull()
            if (mod==null) {
                println("unexpected '${line[3]}', expected positive integer on line: " +line.joinToString(" "))
                return true
            }
            if (mod<2u) {
                println("Test is probably useless. Modulus should be at least 2, but was $mod.")
            }
            builder.testMod = mod
            parseBranches(input, builder)
            return false
        }

        private fun parseBranches(input :BufferedReader, builder :Builder) {
            while (true) {
                val line = if (input.ready())
                    input.readLine().trim().split("\\s+".toRegex())
                  else
                    emptyList()
                if (line.isEmpty()||line[0].isEmpty()) {
                    if (builder.ifTarget == null || builder.elseTarget == null)
                        println("Unexpected end of Monkey spec.  Expected branches for the test result: 'if true/false: throw to monkey #'")
                    break
                }
                if (line.size < 6 || line[0]!="If" || line[2]!="throw" || line[3]!="to" || line[4]!="monkey")
                    println(
                        "Incomplete branching condition. Expected 'If true/false: throw to monkey #', but got '${
                            line.joinToString(" ")
                        }'."
                    )
                else {
                    val target = line[5].toInt()
                    when (line[1]) {
                        "true:" -> builder.ifTarget = target
                        "false:" -> builder.elseTarget = target
                        else -> println("Unknown if branch! Expected 'true:' or 'false:', but got '$line[1]'.")
                    }
                }
            }
        }

        private fun parseOperation(line :List<String>, builder :Builder) :Boolean {
            if (line[1]!="new"||line[2]!="=") {
                println("expected 'new = ' on line: " +line.joinToString(" "))
                return true
            }
            val term = Term.parse(line.subList(3, line.size))
            if (term!=null) {
                builder.operation = term
                return false
            } else
                return true
        }

        private fun parseStarting(line :List<String>, builder :Builder) :Boolean {
            val worriednesses = mutableListOf<Int>()
            if (line[1]!="items:")
                return true
            for (item in line.subList(2, line.size)) {
                val number = item.trim(',').toIntOrNull()
                if (number!=null)
                    worriednesses.add(number)
                else {
                    println("unknown number '$item' in line: " +line.joinToString(" "))
                    return true
                }
            }
            builder.worriedness = worriednesses
            return false
        }
    }
}

private data class Term(val op1 :Operand, val opcode :Operator, val op2 :Operand) {
    companion object {
        fun parse(expression :List<String>) :Term? {
            val op1 = Operand.parse(expression[0])
            if (op1==null) {
                println("unknown 1st operand '${expression[0]}' in: "+expression.joinToString(" "))
                return null
            }
            val opcode = Operator.parse(expression[1])
            if (opcode==null) {
                println("unknown operator '${expression[1]}' in: "+expression.joinToString(" "))
                return null
            }
            val op2 = Operand.parse(expression[2])
            if (op2==null) {
                println("Unknown 2nd operand '${expression[2]}' in: " +expression.joinToString(" "))
                return null
            }
            return Term(op1, opcode, op2)
        }
    }

    override fun toString() = "{new = $op1 ${opcode.symbol} $op2}"
    fun eval(old :Int) :Int = opcode(op1.eval(old), op2.eval(old))

    enum class Operator(val symbol :Char) {
        Plus('+') {
            override fun invoke(op1 :Int, op2 :Int) = op1 + op2
        }, Minus('-') {
            override fun invoke(op1 :Int, op2 :Int) = op1 - op2
        }, Times('*') {
            override fun invoke(op1 :Int, op2 :Int) = op1 * op2
        }, Divide('/') {
            override fun invoke(op1 :Int, op2 :Int) = op1 / op2
        };

        abstract operator fun invoke(op1 :Int, op2 :Int) :Int

        companion object {
            fun parse(expression :String) :Operator? = when(expression[0]) {
                Plus.symbol -> Plus
                Minus.symbol -> Minus
                Times.symbol -> Times
                Divide.symbol -> Divide
                else -> null
            }
        }
    }

    sealed interface Operand {
        fun eval(old :Int) :Int

        companion object {
            fun parse(expression :String) :Operand? {
                if (expression==oldValue.name)
                    return oldValue
                val value = expression.toIntOrNull()
                if (value!=null)
                    return Constant(value)
                return null
            }
        }
    }
    data class Constant(val value :Int) :Operand {
        override fun eval(old :Int) = value

        override fun toString() = value.toString()
    }

    object oldValue :Operand {
        const val name = "old"

        override fun eval(old :Int) = old

        override fun toString() = name
    }
}
