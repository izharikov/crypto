import java.lang.Integer.min

/**
 * @author Ihar Zharykau
 */

fun predictLength(text: String, locale:String = "en",minCount: Int = 3, len: Int = 3): Int {
    var index = 0;
    val strLength = text.length
    var indexes = listOf<Int>()
    while(true) {
        if ( index +1 >= strLength){
            break;
        }
        val key = text.substring(index, min(index + len, strLength));
        if ( !key.allAreAlpha(locale)){
            index++
            if ( index > strLength){
                break
            }
            continue
        }
        indexes = text.substrIndexes(key);
        index++
        if ( indexes.size > 1) {
            println("$indexes : ${gcd(indexes)}")
        }
        if ( indexes.size > minCount && gcd(indexes) > 2){
            break
        }
    }
    return gcd(indexes);
}

fun String.substrIndexes(substr: String): List<Int> {
    val list = mutableListOf<Int>();
    var startIndex = 0;
    do {
        startIndex = this.indexOf(substr, startIndex)
        if (startIndex != -1) {
            list.add(startIndex);
            startIndex += substr.length
        }
    } while (startIndex != -1);
    if ( list.size > 1){
        val resultList = mutableListOf<Int>()
        for ( i in 1 until list.size){
            resultList.add(list[i] - list[i-1])
        }
        println("$substr : $resultList")
        return resultList
    }
    return emptyList();
}

fun gcd(a: Int, b: Int): Int {
    val factor = Math.min(a, b)
    for (loop in factor downTo 2) {
        if (a % loop == 0 && b % loop == 0) {
            return loop
        }
    }
    return 1
}

fun gcd(list: List<Int>): Int {
    return if ( list.isEmpty()){
        return 1
    } else {
        list.reduce(::gcd)
    }
}

fun Char.isAlpha(locale: String = "en"): Boolean {
    val range = when (locale) {
        "ru" -> 'А'..'Я'
        else -> 'A'..'Z'
    }
    return range.contains(this);
}

fun String.allAreAlpha(locale: String = "en"): Boolean {
    return this.toList().all { c -> c.isAlpha(locale) }
}

fun Int?.ifNull(def: Int = 0): Int = this ?: def;

fun main(vararg args: String) {
    println(gcd(listOf(21, 18, 33, 51)))
}