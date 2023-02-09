package com.example.a220127_retrofit_2

/*fun <T> add(a: T, b: T): T {
    //모든 타입을 받은 후에 String 변환 후  Double 타입으로 변환시킨다.
    return (a.toString().toDouble() + b.toString().toDouble()) as T
}

fun main() {

    print(add("1", 2.1)) // 3.1
    print(add(1, 3)) // 4
    print(add("10", "235")) // 245
    print(add(1.5, 3.1)) // 4.6
}*/
fun <T> add(a: T, b: T, op: (T, T) -> T): T {
    return op(a, b)
}


//범위가 지정되지 않았기 때문에 .toString()메서드를 거쳐야함.
fun <T> add2(a: T, b: T): T {
    return (a.toString().toDouble() + b.toString().toDouble()) as T
}

//범위가 Number로 지정되었기 때문에 바로 toDouble()메서드 사용가능
fun <T : Number> add3(a: T, b: T): T {
    return (a.toDouble() + b.toDouble()) as T
}


fun main() {
    print(add("1", "2") { a, b -> a + b }) // 12
    print(add(1, 2) { a, b -> a + b }) // 3
}