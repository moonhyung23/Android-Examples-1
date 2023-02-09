package com.example.customdialogfragment

class CountingSet<T>(
    val innerSet: MutableCollection<T> = HashSet(),
) : MutableCollection<T> by innerSet { //MutableCollection 구현을 innerSet 에 위임

    var objectsAdded = 0 //카운트

    //override 함수는 HashSet() 대신 구현한 것을 사용
    override fun add(element: T): Boolean {
        objectsAdded++    //add할때마다 카운트 증가
        return innerSet.add(element)
    }

    //override 함수는 HashSet() 대신 구현한 것을 사용
    override fun addAll(c: Collection<T>): Boolean {
        objectsAdded += c.size  //add할때마다 카운트 증가
        return innerSet.addAll(c)
    }
}