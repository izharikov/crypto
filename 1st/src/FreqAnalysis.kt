/**
 * @author Ihar Zharykau
 */


fun countFreq(text: String, keyLength: Int, locale: String = "en") {
    val chars = text.toCharArray();
    for (i in 0 until keyLength) {
        val arr = Array<Int>(26, { _ -> 0 })
        (i..chars.size step keyLength)
                .asSequence()
                .filter { it < chars.size }
                .map { chars[it] }
                .filter { it.isAlpha() }
                .forEach { arr[it - firstChar(locale)]++ }
        val sum = arr.sum()
        val res = arr.map { it.toDouble().div(sum) }
        println()
        val ch = arr.indices.maxBy { arr[it] }
        if (ch != null) {
            println("char : $ch ; " +
                    "E -> ${(ch + firstChar(locale).toInt() * 2 - maxFreq(locale).toInt()).toChar()};" +
                    " approximated : ${(approximate(res, locale) + firstChar(locale).toInt()).toChar()}")
        }
    }
}

fun approximate(arr: List<Double>, locale: String): Int {
    var freq = freqByLocale(locale);
    var res = -1;
    var sum = arr.size.toDouble() * 100 * 100;
    for (i in 0 until freq.size) {
        val arrs = arr.indices
                .map { arr[(it + i) % freq.size] };
        val curSum = arrs.indices
                .map { (arrs[it] - freq[firstChar(locale) + it].ifNull()) * 100 }
                .map { it * it }
                .sum()
        if (curSum < sum) {
            sum = curSum;
            res = i
        }
    }
    return res;
}

fun Double?.ifNull(d: Double = 0.0): Double = this ?: d

fun freqByLocale(locale: String) = when (locale) {
    "ru" -> RuFreq
    else -> EnFreq
}

fun firstChar(locale: String) = when (locale) {
    "ru" -> 'А'
    else -> 'A'
}

fun maxFreq(locale:String) = when (locale) {
    "ru" -> 'О'
    else -> 'E'
}
val EnFreq = mapOf(
        'A' to 0.08167,
        'B' to 0.01492,
        'C' to 0.02782,
        'D' to 0.04253,
        'E' to 0.12702,
        'F' to 0.0228,
        'G' to 0.02015,
        'H' to 0.06094,
        'I' to 0.06966,
        'J' to 0.00153,
        'K' to 0.00772,
        'L' to 0.04025,
        'M' to 0.02406,
        'N' to 0.06749,
        'O' to 0.07507,
        'P' to 0.01929,
        'Q' to 0.00095,
        'R' to 0.05987,
        'S' to 0.06327,
        'T' to 0.09056,
        'U' to 0.02758,
        'V' to 0.00978,
        'W' to 0.0236,
        'X' to 0.0015,
        'Y' to 0.01974,
        'Z' to 0.00074
);

val RuFreq = mapOf(
        'А' to 0.07821,
        'Б' to 0.01732,
        'В' to 0.04491,
        'Г' to 0.01698,
        'Д' to 0.03103,
        'Е' to 0.08567,
        'Ё' to 0.0007,
        'Ж' to 0.01082,
        'З' to 0.01647,
        'И' to 0.06777,
        'Й' to 0.01041,
        'К' to 0.03215,
        'Л' to 0.04813,
        'М' to 0.03139,
        'Н' to 0.0685,
        'О' to 0.11394,
        'П' to 0.02754,
        'Р' to 0.04234,
        'С' to 0.05382,
        'Т' to 0.06443,
        'У' to 0.02882,
        'Ф' to 0.00132,
        'Х' to 0.00833,
        'Ц' to 0.00333,
        'Ч' to 0.01645,
        'Ш' to 0.00775,
        'Щ' to 0.00331,
        'Ъ' to 0.00023,
        'Ы' to 0.01854,
        'Ь' to 0.02106,
        'Э' to 0.0031,
        'Ю' to 0.00544,
        'Я' to 0.01979
);