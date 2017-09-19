import java.io.File

/**
 * @author Ihar Zharykau
 */

fun main(vararg args: String) {
    val inputDir = "input/";
    val dir = File(inputDir);
    dir.listFiles()
            .filter { it.isDirectory }
            .forEach {
                val locale = it.name
                it.listFiles()
                        .filter { it.isFile }
                        .forEach { encryptAndAnalyzeByTextLength(it.absolutePath, getKeyFromFileName(it.name, locale), locale) }
            }
    println("--------------")
    dir.listFiles()
            .filter { it.isDirectory }
            .forEach {
                val locale = it.name
                it.listFiles()
                        .filter { it.isFile }
                        .forEach { encryptAndAnalyzeByKeyLength(it.absolutePath, locale) }
            }

}

fun getKeyFromFileName(fileName: String, locale: String): String {
    val res = mutableListOf<Char>()
    for (c in fileName.toUpperCase()) {
        if (!c.isAlpha(locale)) {
            break
        }
        res.add(c)
    }
    return res.joinToString("")
}

fun encryptAndAnalyzeByKeyLength(fileName: String, locale: String) {
    val text = File(fileName).readLines().joinToString("\n")
    val map = mutableMapOf<Int, MutableSet<String>>()
    val words = text
            .toUpperCase()
            .toList()
            .map { if (it.isAlpha(locale)) it else ' ' }
            .joinToString("")
            .split(Regex("\\s+"))
            .filter { it.isNotBlank() }
            .forEach {
                if (!map.containsKey(it.length)) {
                    map[it.length] = mutableSetOf()
                }
                map[it.length]!!.add(it)
            }
    val cipher = when (locale) {
        "ru" -> RuVigenereCipher()
        else -> EnVigenereCipher()
    }
    println(map.map { it.key to it.value.size })
    for (i in 5..95 step 5) {
        var allCount = 0.0;
        var success = 0;
        val genKey = map.filter { it.key == 5 }
                .map { it.value }[0]
                .toList()
                .subList(0, i / 5)
                .joinToString("")
        val encryptedText = cipher.encrypt(text, genKey);
        val keyLength = predictLength(encryptedText, locale)
        val predKey = countFreq(encryptedText, keyLength, locale)

        if (predKey.advancedEquals(genKey)) {
            success += 1;
        }
        allCount += 1;
        println("Length: ${genKey.length} : ${success / allCount}")
    }
}

fun encryptAndAnalyzeByTextLength(fileName: String, key: String, locale: String) {
    val cipher = when (locale) {
        "ru" -> RuVigenereCipher()
        else -> EnVigenereCipher()
    }
    val text = File(fileName).readLines().joinToString("\n")
    for (i in 100..4000 step 300) {
        var allCount = 0.0;
        var success = 0;
        for (j in 0..3800 step i) {
            val encryptedText = cipher.encrypt(text.substring(j, j + i), key);
            val keyLength = predictLength(encryptedText, locale)
            val predKey = countFreq(encryptedText, keyLength, locale)
//            val f = File("encr-$i-$j");
//            f.writeText(encryptedText);
//            println("filelength : ${encryptedText.length}\t; real key: $key \t, predKey: $predKey")
            if (predKey.advancedEquals(key)) {
                success += 1;
            }
            allCount += 1;
        }
        println("Length: $i : ${success / allCount}")
    }
//    val encryptedText = cipher.encryptFile(fileName, key);
//    val keyLength = predictLength(encryptedText, locale)
//    val predKey = countFreq(encryptedText, keyLength, locale)
//    println("filelength : ${encryptedText.length}; real key: $key, predKey: $predKey")
}

fun String.advancedEquals(str: String): Boolean {
    var result = this == str;
    if (!result) {
        var s = "";
        for (i in 0..5) {
            s += str;
            if (this == s) {
                result = true;
                break
            }
        }

    }
    return result
}