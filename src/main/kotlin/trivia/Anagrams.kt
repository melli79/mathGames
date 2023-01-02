package trivia

import xmasaoc.toReader
import java.io.File
import java.io.FileInputStream
import java.text.Collator
import java.util.*

object Anagrams {
    const val wordSource = "english.txt"
    const val germanWordSource = "german.txt"
    private var collator = Collator.getInstance(Locale.ENGLISH)

    init {
        collator.strength = Collator.PRIMARY
    }

    private var words :List<String>? =null

    fun isAnagram(phrase1 :String, phrase2 :String) :Boolean {
        val input1 = phrase1.filter { it.isLetter() }.toList()
        val input2 = phrase2.filter { it.isLetter() }.toList()
        return input1.counts() == input2.counts()
    }

    fun findAnagrams(letters :String) :Set<String> {
        if (words==null)
            loadWords()
        val requireds = letters.toList().counts().toMutableMap()
        return descend(requireds, "", false, 0, words!!.size-1)
    }

    fun findPartialAnagrams(letters :String) :Set<String> {
        if (words==null)
            loadWords()
        val reserve = letters.toList().counts().toMutableMap()
        return descend(reserve, "", true, 0, words!!.size-1)
    }

    private fun descend(reserve :MutableMap<Char, Int>, prefix :String, allowSubwords :Boolean, begin :Int, end :Int)
            :Set<String> {
        val result = mutableSetOf<String>()
        val options = reserve.keys.sorted()
        for (l0 in options) {
            val remaining = reserve[l0]!! -1
            if (remaining > 0)
                reserve[l0] = remaining
            else
                reserve.remove(l0)
            val (begin1, end1) = findInterval(prefix +l0, prefix +(l0+1), begin, end)
            val prefix1 = prefix+l0
            if (begin1 < end1 && reserve.isNotEmpty()) {
                result += descend(reserve, prefix1, allowSubwords, begin1, end1)
            }
            if ((allowSubwords||reserve.isEmpty()) && collator.compare(prefix1,words!![begin1])==0)
                result.add(words!![begin1])
            reserve[l0] = remaining +1
        }
        return result
    }

    private fun findInterval(from :String, to :String, begin :Int = 0, end :Int) :Pair<Int, Int> {
        var (min, max) = shrinkInterval(begin, end, from, to)
        val mid = (min+max) /2
        min = find(min, mid, from, true)
        max = find(mid, max, to, false)
        return Pair(min, max)
    }

    private fun find(begin :Int, end :Int, key :String, isLeft :Boolean) :Int {
        var min = begin;  var max = end
        while (min+1 < max) {
            val mid = (min + max) / 2
            val midWord = words!![mid]
            val cmp = collator.compare(midWord, key)
            if (cmp <= 0) {
                min = mid
                if (cmp == 0) {
                    max = mid
                    break
                }
            } else {
                max = mid -1;  val maxWord = words!![max]
                if (collator.compare(maxWord, key) == 0) {
                    min = max
                    break
                }
            }
        }
        return if (isLeft) min  else max
    }

    private fun shrinkInterval(begin :Int, end :Int,
            from :String, to :String) :Pair<Int, Int> {
        var min = begin;  var max = end
        while (min+1 < max) {
            val mid = (min + max) / 2
            val midWord = words!![mid]
            if (collator.compare(midWord, from) <= 0) {
                min = mid
            } else if (collator.compare(to, midWord) <= 0) {
                max = mid
            } else
                break
        }
        return Pair(min, max)
    }

    fun loadWords(lang :Locale =Locale.ENGLISH) {
        val fileName = when (lang) {
            Locale.GERMAN -> germanWordSource
            else -> wordSource
        }
        val input = FileInputStream(File("src/main/resources", fileName)).toReader()
        val result = input.readLines().map { it.trim() }.filter { it.isNotEmpty() }
        collator = Collator.getInstance(lang);  collator.strength = Collator.PRIMARY
        words = result.sortedWith(collator)
    }

    private fun Collection<Char>.counts() = groupBy { it.lowercaseChar() }
        .map { entry -> Pair(entry.key.lowercaseChar(), entry.value.size) }
        .toMap()

}
