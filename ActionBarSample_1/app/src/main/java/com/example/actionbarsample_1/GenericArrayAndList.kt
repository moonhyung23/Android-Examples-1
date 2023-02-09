package com.example.actionbarsample_1

class GenericArrayAndList {
    //Array가 아닌 리스트의 경우에는 상속관계인 Apple클래스를
    //매개변수에 넣을 수 있음.
    fun receiveFruits(fruits: List<Fruit>) {
        println("Number of fruits: ${fruits.size}")
    }

    fun main() {
        val fruits: List<Fruit.Apple> = listOf(Fruit.Apple(), Fruit.Apple())
        //List는 불변이고 Array는 가변적이기 때문에
        //List는 데이터가 바뀌지 않아서 상속받은 자식클래스를
        //허용해줌.

        //List에는 제네릭 타입이 <Out E>로 되어있음.
        receiveFruits(fruits)   // Number of fruits: 2
// 컴파일 가능
        println(isA<String>("a"))


    }

    inline fun <reified T> isA(value: Any) = value is T


}