package com.example.actionbarsample_1

import android.util.Log

//제네릭 타입과 다른 객체가 와도 파라미터의 값을 읽기만 하기 때문에 문제가 생기지 않음
//(공변성)
//out키워드 사용
fun copyFromTo(from: Array<out Fruit>, to: Array<Fruit>) {
    for (i in from.indices) {
        to[i] = from[i]
    }

    //만약 내용을 추가하거나 변경하려 할 경우 에러가 발생.
//    from[i] = Fruit() // compile error !
}

fun main() {
    //Out 키워드를 사용했기 때문에 상속관계에 있는 자식클래스를
    //파라미터에 입력할 수 있음.
    // - Out키워드를 사용해 공변성을 사용할 경우 읽기만(Read-only)만
    //가능하다.
    val fruitsBasket1 = Array<Fruit.Apple>(3) { _ -> Fruit.Apple() }
    //Fruit가 아닌 Apple 객체를 넘길경우 에러가 발생한다.
    val fruitsBasket2 = Array<Fruit>(3) { _ -> Fruit() }
    copyFromTo(fruitsBasket1, fruitsBasket2)
}