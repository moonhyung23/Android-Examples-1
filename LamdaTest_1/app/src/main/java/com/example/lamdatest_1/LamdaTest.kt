package com.example.lamdatest_1

fun main() {
    var result: Int

    //입력값에 람다식을 사용함
    //선언자료형에 맞는 매개변수 값을 입력
    //-함수를 처음 작성할 때 선언 자료형을 입력했음
    //-함수를 호출할 때는 선언자료형에 맞는 매개변수를 입력
    result = highOrder({ x, y -> x * y }, 10, 20)
    println(result)


    //반환값이 없는 람식의 선언
    //입력 값 없음
    //Unit(반환값 없음)
    val out: () -> Unit = { println("Hello World") }
    //val out = {println{"Hello World!"} // 생략가능
    //추론이 가능하므로 val out = {println{"Hello World!"}와 같이 생략 가능 }
    //람다식이 들어있는 변수를 다른 변수에 할당
    out() //함수처럼 사용가능


    val moon: () -> Unit = { println("moon") }
    val moon1 = { println("moon2") }

    moon()
    moon1()

    //3.값에 의한 호출: 함수가 인자로 전달된 경우
    //-람다식 함수는 값으로 처리되며 그 즉시 함수가 수행된 후 값을 전달
    val result2 = callByValue(lamda())
    println(result2)

    //4.이름에 의한 호출(CallByName)
    //람다식 이름으로 호출
    //함수를 처음 만들 때 입력값에 람다식을 입력함으로써
    //호출 시 이전에 만들었던 람다함수를 입력함.
    val result3 = callByName(otherLambda)

}


//1)입력 값에 람다함수를 사용
fun callByName(b: () -> Boolean): Boolean {
    println("callByName function")
    //반환값에 람다함수를 사용해서 값을 반환
    return b()
}

val otherLambda: () -> Boolean = {
    println("otherLamda function")
    true
}


//함수로 값을 전달
//2)입력값에 단순 기본형을 입력
fun callByValue(b: Boolean): Boolean {
    println("callByValue function")
    return b
}

val lamda: () -> Boolean = {
    //람다표현식이 두줄이상이면 마지막 줄이 결과값을 반환
    println("lamda function")
    //반환 값
    true
}

fun highOrder(calculate: (Int, Int) -> Int, a: Int, b: Int): Int {
    return calculate(a, b)
}





