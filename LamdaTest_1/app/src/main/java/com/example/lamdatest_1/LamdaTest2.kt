package com.example.lamdatest_1

fun main() {
    //람다식안에 람다식이 있는 경우
    //참조 자료형의 반환타입 람다식을 사용함
    //입력타입: () 없음
    //반환타입: () -> Unit
    //이 함수를 사용하면 반환 값으로 되어있는 함수를 실행

    val nestedLambda: () -> () -> Unit = { { println("nested") } }

    callByName2(otherLambda2)
    println(nestedLambda)

    //다른 함수의 참조에 의한 호출
    //1.인자와 반환값이 있는 함수
//    val res1 = funcParam(3, 2, sum)

}

//이름에 의한 호출
fun callByName2(b: () -> Boolean): Boolean {
    println("callByName function")
    //입력값에서 선언했던 람다함수를 반환 값으로 사용
    return b()
}

val otherLambda2: () -> Boolean = {
    println("otherLambda function")
    true
}


//다른 함수의 참조에 의한 호출
fun sum(a: Int, b: Int) = a + b

fun text(a: String, b: String) = "Hi! $a $b"

fun funcParam(a: Int, b: Int, c: (Int, Int) -> Int): Int {
    return c(a, b)
}

fun hello(body: (String, String) -> String): Unit {
    println(body("Hello", "World"))
}


















