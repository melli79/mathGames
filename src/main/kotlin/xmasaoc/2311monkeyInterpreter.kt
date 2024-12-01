package xmasaoc

import java.io.BufferedReader
import java.io.InputStream
import java.math.BigInteger

private const val RESOURCE_NAME = "11small-monkeys.prg2"
private val debug = false // '-' in RESOURCE_NAME

fun main(args: Array<String>) {
    val input = getInput(args.firstOrNull(), RESOURCE_NAME)
    val I = BigInteger::class.java
    val monkeys = parseMonkeys(input, I)
    if (debug)
        println(monkeys.joinToString("\n"))
    listOf(1,20, 1000, 2000, 3000, 4000, 5000, 6000, 7000, 8000, 9000, 10000).forEach { round ->
        Interpreter.cycle(monkeys, I, round)
        Interpreter.printStats()
    }
    if (debug)
    println("The monkeys have: " +monkeys.mapIndexed { n, monkey ->
        "Monkey $n: ${monkey.items}"
    }.joinToString("\n"))
}

private object Interpreter {
    private var round = 0

    private lateinit var numItems :MutableList<Int>

    fun <I> cycle(monkeys :List<Monkey<I>>, clazz :Class<I>, untilRound :Int) {
        numItems = (1..monkeys.size).map { 0 }.toMutableList()
        while (round<untilRound) {
            if (debug)
                println("\n\nRound $round:")
            monkeys.forEachIndexed { n, monkey -> distributeForMonkey(n, monkey, clazz, monkeys) }
            round++
        }
    }

    private fun <I> distributeForMonkey(n :Int, monkey :Monkey<I>, clazz :Class<I>, monkeys :List<Monkey<I>>) {
        if (debug)
            println("\n  Monkey $n:")
        val items :List<I> = monkey.items.toList()
        numItems[n] += items.size
        monkey.items.clear()
        items.forEach { item -> distributeItem(monkey, item, clazz, monkeys) }
    }

    private fun <I> distributeItem(monkey :Monkey<I>, item :I, clazz :Class<I>, monkeys :List<Monkey<I>>) {
        val newValue = monkey.operation.eval(clazz, item)
        val finalValue = if (clazz==Int::class.java) (newValue as Int/3) as I else newValue
        val rem = when (clazz) {
            Int::class.java -> (finalValue as Int) % (monkey.testMod as Int)
            BigInteger::class.java -> (finalValue as BigInteger).mod(monkey.testMod as BigInteger).toInt()
            else -> TODO("unimplemented")
        }
        if (rem == 0) {
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

private fun <I> parseMonkeys(inputStream :InputStream, clazz :Class<I>) :List<Monkey<I>> {
    val result = mutableListOf<Monkey<I>>()
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
        val monkey = when (clazz) {
            Int::class.java -> SmallMonkey.Parser()
            BigInteger::class.java -> BigMonkey.Parser()
            else -> TODO("unimplemented")
        }.parse(input, num)

        if (monkey!=null)
            result.add(num, monkey as Monkey<I>)
    }
    return result
}

class BigMonkey(items :MutableList<BigInteger>, operation :Term<BigInteger>, testMod :BigInteger, ifTarget :Int, elseTarget :Int)
        :Monkey<BigInteger>(items, operation, testMod, ifTarget, elseTarget) {
    class Builder() :Monkey.Builder<BigInteger>() {
        override fun tryBuild(n :Int) :Monkey<BigInteger>? {
            if (validate(n))
                return BigMonkey(worriedness!!, operation!!, testMod!!, ifTarget!!, elseTarget!!)
            return null
        }
    }

    class Parser() :Monkey.Parser<BigInteger>(Builder(), BigInteger::class.java) {}
}

class SmallMonkey(items :MutableList<Int>, operation :Term<Int>, testMod :Int, ifTarget :Int, elseTarget :Int)
        :Monkey<Int>(items, operation, testMod, ifTarget, elseTarget) {

    class Builder() :Monkey.Builder<Int>() {
        override fun tryBuild(n :Int) :SmallMonkey? {
            return if (validate(n))
                SmallMonkey(worriedness!!, operation!!, testMod!!, ifTarget!!, elseTarget!!)
            else
                null
        }
    }

    class Parser() :Monkey.Parser<Int>(Builder(), Int::class.java) {}
}

abstract class Monkey<I>(val items :MutableList<I>, val operation :Term<I>, val testMod :I, val ifTarget :Int, val elseTarget :Int) {

    abstract class Builder<I> {
        var worriedness :MutableList<I>? =null
        var operation :Term<I>? =null
        var testMod :I? =null
        var ifTarget :Int? =null
        var elseTarget :Int? =null

        abstract fun tryBuild(n :Int) :Monkey<I>?

        fun validate(n :Int) :Boolean {
            val missings = mutableListOf<String>()
            if (worriedness == null)
                missings.add("'Starting items:'")
            if (operation == null)
                missings.add(", 'Operation: new = ...'")
            if (testMod == null)
                missings.add(", 'Test: divisible by ##'")
            if (ifTarget == null)
                missings.add(", 'If true: ...'")
            if (elseTarget == null)
                missings.add(", 'If false: ...'")
            if (missings.isNotEmpty())
                println("Incomplete Monkey $n, missing $missings")
            return missings.isEmpty()
        }
    }

    abstract class Parser<I>(private val builder :Builder<I>, val clazz :Class<I>) {
        fun parse(input :BufferedReader, n :Int) :Monkey<I>? {
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
                    "Operation:" -> if (parseOperation(line, clazz, builder))
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

        internal fun parseTest(line :List<String>, input :BufferedReader, builder :Builder<I>) :Boolean {
            if (line[1]!="divisible"||line[2]!="by") {
                println("expected 'divisible by ' on line " +line.joinToString(" "))
                return true
            }
            val mod = line[3].toIntOrNull()
            if (mod==null) {
                println("unexpected '${line[3]}', expected positive integer on line: " +line.joinToString(" "))
                return true
            }
            if (mod.toInt()<2) {
                println("Test is probably useless. Modulus should be at least 2, but was $mod.")
            }
            builder.testMod = when (clazz) {
                Int::class.java -> mod as I
                BigInteger::class.java -> BigInteger.valueOf(mod.toLong()) as I
                else -> TODO("unimplemented")
            }
            parseBranches(input, builder)
            return false
        }

        private fun parseBranches(input :BufferedReader, builder :Builder<I>) {
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

        private fun parseOperation(line :List<String>, clazz :Class<I>, builder :Builder<I>) :Boolean {
            if (line[1]!="new"||line[2]!="=") {
                println("expected 'new = ' on line: " +line.joinToString(" "))
                return true
            }
            val components = line.subList(3, line.size)
            val term :Term<I>? = when (clazz) {
                Int::class.java -> Term.parseInt(components) as Term<I>?
                BigInteger::class.java -> Term.parseBigInt(components) as Term<I>?
                else -> TODO("unimplemented")
            }
            if (term!=null) {
                builder.operation = term
                return false
            } else
                return true
        }

        private fun parseStarting(line :List<String>, builder :Builder<I>) :Boolean {
            val worriednesses = mutableListOf<I>()
            if (line[1]!="items:")
                return true
            for (item in line.subList(2, line.size)) {
                val number = item.trim(',').toIntOrNull()
                if (number!=null) {
                    when (clazz) {
                        Int::class.java -> worriednesses.add(number as I)
                        BigInteger::class.java -> worriednesses.add(BigInteger.valueOf(number.toLong()) as I)
                        else -> TODO("unimplemented")
                    }
                }
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

data class Term<I>(val op1 :Operand<I>, val opcode :Operator, val op2 :Operand<I>) {
    companion object {
        fun parseInt(expression :List<String>) :Term<Int>? {
            val op1 = Operand.parseInt(expression[0])
            if (op1==null) {
                println("unknown 1st operand '${expression[0]}' in: "+expression.joinToString(" "))
                return null
            }
            val opcode = Operator.parse(expression[1])
            if (opcode==null) {
                println("unknown operator '${expression[1]}' in: "+expression.joinToString(" "))
                return null
            }
            val op2 = Operand.parseInt(expression[2])
            if (op2==null) {
                println("Unknown 2nd operand '${expression[2]}' in: " +expression.joinToString(" "))
                return null
            }
            return Term(op1, opcode, op2)
        }

        fun parseBigInt(expression :List<String>) :Term<BigInteger>? {
            val op1 = Operand.parseBigInt(expression[0])
            if (op1==null) {
                println("unknown 1st operand '${expression[0]}' in: "+expression.joinToString(" "))
                return null
            }
            val opcode = Operator.parse(expression[1])
            if (opcode==null) {
                println("unknown operator '${expression[1]}' in: "+expression.joinToString(" "))
                return null
            }
            val op2 = Operand.parseBigInt(expression[2])
            if (op2==null) {
                println("Unknown 2nd operand '${expression[2]}' in: " +expression.joinToString(" "))
                return null
            }
            return Term(op1, opcode, op2)
        }
    }

    override fun toString() = "{new = $op1 ${opcode.symbol} $op2}"
    fun eval(clazz :Class<I>, old :I) :I = opcode(clazz, op1.eval(old), op2.eval(old))

    enum class Operator(val symbol :Char) {
        Plus('+') {
            override fun invoke(op1 :Int, op2 :Int) = op1 + op2
            override fun invoke(op1 :BigInteger, op2 :BigInteger) = op1 + op2
        }, Minus('-') {
            override fun invoke(op1 :Int, op2 :Int) = op1 - op2
            override fun invoke(op1 :BigInteger, op2 :BigInteger) = op1 - op2
        }, Times('*') {
            override fun invoke(op1 :Int, op2 :Int) = op1 * op2
            override fun invoke(op1 :BigInteger, op2 :BigInteger) = op1 * op2
        }, Divide('/') {
            override fun invoke(op1 :Int, op2 :Int) = op1 / op2
            override fun invoke(op1 :BigInteger, op2 :BigInteger) = op1 / op2
        };

        abstract operator fun invoke(op1 :Int, op2 :Int) :Int
        abstract operator fun invoke(op1 :BigInteger, op2 :BigInteger) :BigInteger
        operator fun <I> invoke(clazz :Class<I>, op1 :I, op2 :I) :I = when (clazz) {
            Int::class.java -> invoke(op1 as Int, op2 as Int) as I
            BigInteger::class.java -> invoke(op1 as BigInteger, op2 as BigInteger) as I
            else -> TODO("unimplemented")
        }

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

    sealed interface Operand<I> {
        fun eval(old :I) :I

        companion object {
            fun parseInt(expression :String) :Operand<Int>? {
                if (expression==OldValue.name)
                    return OldValue()
                val value = expression.toIntOrNull()
                if (value!=null)
                    return Constant(value)
                return null
            }

            fun parseBigInt(expression :String) :Operand<BigInteger>? {
                if (expression==OldValue.name)
                    return OldValue()
                val value = expression.toIntOrNull()
                if (value!=null)
                    return Constant(value.toBigInteger())
                return null
            }
        }
    }
    data class Constant<I>(val value :I) :Operand<I> {
        override fun eval(old :I) = value

        override fun toString() = value.toString()
    }

    class OldValue<I> :Operand<I> {
        companion object {
            const val name = "old"
        }

        override fun eval(old :I) = old

        override fun toString() = name
    }
}
