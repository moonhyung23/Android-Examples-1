package com.example.actionbarsample_1

open class Fruit {
    class Apple : Fruit()
    class Banana : Fruit()
}
class Melon{

}

//Fruit 클래스를 제네릭 타입<>으로 선언된 배열을
//파라미터로 받음.
//위의 함수로는 Apple이나 Banana객체를 전달할 수 없다.

//만약이 함수에 Array<Apple>함수가 전달된다면 에러가 발생할 것이다.
fun receiveFruits(fruits: Array<Fruit>) {
    fruits[0] = Fruit.Banana()//Array<Apple>로 할 경우 에러 발생
    println("Number of fruits: ${fruits.size}")
}

fun main() {
    val fruits: Array<Fruit.Apple> = arrayOf(Fruit.Apple())
    //제네릭의 타입 불변성 때문에 상속관계여도 불가함.
//        receiveFruits(fruits)
}