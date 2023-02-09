package com.example.lamdatest_1

fun main() {
   /* //mapOf: map으로 구성된 텍스트를, forEach 키워드를 통해 확인 가능, it 키워드를 사용할 수 있다.
    var map = mapOf<String,String>("key" to "value", "key2" to "value2")
    map.forEach{
        println("${it.key}, ${it.value}")
    }*/

    //코틀린 에는 invoke라는 특별한 함수 정확히는 연산자가 존재한다.
    //invoke 연산자는 이름 없이 호출될 수 있다.




}

object MyFunction {
    //이름 없이 호출
    operator fun invoke(str: String): String {
        return str.toUpperCase() // 모두 대문자로 바꿔줌
    }
}