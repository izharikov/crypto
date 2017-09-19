import java.io.File

/**
 * @author Ihar Zharykau
 */

interface VigenereCipher {
    fun encrypt(text: String, key: String): String;

    fun decrypt(text: String, key: String): String;

    fun encrypt(file: File, key: String): String {
        return encrypt(file.readLines().joinToString("\n"), key);
    }

    fun encryptFile(text: String, key: String): String {
        return encrypt(File(text), key)
    }
}

abstract class BaseVigenereCipher : VigenereCipher {
    abstract fun range(): CharRange;

    fun buildEncryptMatrix(key: String): Map<Char, List<Char>> {
        var currentKey = key.toList();
        val result = mutableMapOf<Char, List<Char>>();
        val range = range().toList();
        for (c in range()) {
            result.put(c, currentKey.toList())
            currentKey = currentKey.map { c -> incrementChar(c, range.first(), range.last()) };
        }
        return result
    }

    override fun encrypt(text: String, key: String): String {
        val range = range();
        val chars = text.toUpperCase().toList()
        val keys = key.toUpperCase().toList()
        return chars
                .mapIndexed { index, char -> char.encryptChar(keys, index, range.first, range.last) }
                .joinToString("")
    }

    override fun decrypt(text: String, key: String): String {
        val chars = text.toUpperCase().toList()
        val range = range();
        val keys = key.toUpperCase().toList()
        return chars.mapIndexed { index, char -> char.decryptChar(keys, index, range.first, range.last) }.joinToString("")
    }

    fun incrementChar(c: Char, first: Char, last: Char): Char {
        return if (c + 1 > last) {
            first;
        } else {
            c + 1;
        }
    }
}

fun Char.decryptChar(key: List<Char>, index: Int, first: Char, last: Char): Char {
    if (this < first || this > last) {
        return this;
    }
    val c = key[index % key.size];
    return if (this >= c) {
        (first + this.toInt() - c).toChar();
    } else {
        (last.toInt() - c.toInt() + this.toInt() + 1).toChar();
    }
}

fun Char.encryptChar(key: List<Char>, index: Int, first: Char, last: Char) : Char{
    if (this < first || this > last) {
        return this;
    }
    val c = key[index % key.size];
    val delta = this - first;
    return if ( c + delta <= last){
        c + delta
    } else {
        first + delta - (last - c) - 1
    }
}

class RuVigenereCipher : BaseVigenereCipher() {
    override fun range(): CharRange {
        return 'А'..'Я'
    }
}

class EnVigenereCipher : BaseVigenereCipher() {
    override fun range(): CharRange {
        return 'A'..'Z';
    }

}

fun Map<Char, List<Char>>.getByIndex(c: Char, index: Int, key: String): Char {
    val list = this.get(c);
    return list?.get(index % key.length) ?: c;
}

fun main(vararg args: String) {
    val KEY = "apples";
    println(KEY.length)
    val fileName = "input.txt";
    val cipher = EnVigenereCipher();
    val encr = cipher.encryptFile(fileName, KEY)
    val decr = cipher.decrypt(encr, KEY)
    val predKeyLength = predictLength(encr)
    println("----")
    print(encr)
    println("----")
    print(decr)
    println("----")
    println(predKeyLength)
    countFreq(encr,predKeyLength)
}