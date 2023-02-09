package com.example.a220127_retrofit_2.genericSample

import kotlin.reflect.KProperty

interface Speaker {
    val subject: String
    val script: String
    fun say()
}

class DroidKnights : Speaker {
    override val subject = "Jetpack Compose 내부 작동 원리"
    override val script = """
        안녕하세요, 저는 $subject 에 대해 발표할 지성빈 입니다.
        Jetpack Compose 는 작년 7월달에 stable 로 출시되었습니다.
        ...
    """.trimIndent()

    override fun say() {
        println("[$subject] $script")
    }
}

//내가 바로 하는게아닌 다른 객체에게 내가 할일을 위임
class Sungbin(private val presentation: Speaker) : Speaker {
    override val subject = presentation.subject
    override val script = presentation.script
    override fun say() {
        presentation.say()
    }
}

class CustomString {
    private var value = ""

    /*
    * thisRef: 호출된 인스턴스
    * properfy: 호출된 프로퍼티
    * value: 사용된 값
    * */


    operator fun getValue(thisRef: Any?, property: KProperty<*>) = "[CustomString] $value"

    //A.B = "C"라는 delegate가 있을 때 A 부분이 thisRef B부분이 properfy,
    // "C" 부분이 value로 들어가게 된다.
    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
        this.value = value
    }
}

fun main() {

    var customString by CustomString()
    customString = "Bye, world."

    println(customString)
}

