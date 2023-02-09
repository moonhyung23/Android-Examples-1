package com.example.customdialogfragment

interface Base {
    fun print()
}

class BaseImpl(val x: Int) : Base {
    override fun print() {
        print(x)
    }
}

/*Derived 클래스는 Base 인터페이스를 상속할 수 있으며,
모든 public 메서드를 지정한 객체로 위임한다.*/
class Derived(b: Base) : Base by b

fun main() {
    val b = BaseImpl(10)
    Derived(b).print()
}