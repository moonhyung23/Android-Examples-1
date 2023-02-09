package com.example.a220127_retrofit_2.genericSample

//Comparable 클래스를 사용해 두 인자의 값의 크기를 비교
fun <T : Comparable<T>> compare(a: T, b: T): Boolean {
    if (a < b) return true
    return false
}

fun main() {

    print(compare(1, 2)) // true
    print(compare(3, 2)) // false

    print(compare("1", "2")) // true
    print(compare("aaaa", "aaab")) // true
    print(compare("cat", "dog")) // true
}
