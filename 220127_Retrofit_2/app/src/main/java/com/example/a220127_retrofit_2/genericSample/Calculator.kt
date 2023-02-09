package com.example.a220127_retrofit_2.genericSample

class Calculator<T>(val a: T, val b: T) {

    fun add(): Double {
        return (a.toString().toDouble() + b.toString().toDouble())
    }

    fun sub(): Double {
        return (a.toString().toDouble() - b.toString().toDouble())
    }

    fun mul(): Double {
        return (a.toString().toDouble() * b.toString().toDouble())
    }

    fun div(): Double {
        return (a.toString().toDouble() / b.toString().toDouble())
    }
}


fun main() {
    print(Calculator<Int>(1, 2).add()) // 3.0
    print(Calculator<Double>(2.0, 5.1).sub()) // -3.0999999999999996
    print(Calculator("10", 2).mul()) // 20.0
    print(Calculator<Any>("9", 2).div()) // 4.5

}