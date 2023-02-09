package com.example.customdialogfragment

interface IWindow {
    fun getWidth(): Int
    fun getHeight(): Int
}

class TransparentWindow : IWindow {
    override fun getWidth(): Int {
        return 100
    }

    override fun getHeight(): Int {
        return 150
    }
}

//TransParentWindow 객체를 입력받음.
//TransParentWindow의 기능을 위임
//1) 각각의 클래스에 동일한 인터페이스를 구현
//2) 인터페이스를 구현한 클래스 중에 위임할 클래스를 선택
//3) 위임할 클래스의 입력값에 인터페이스를 입력값으로 놓는다.
//4) 해당 클래스를 생성시 생성자에 위임할 클래스의 이름을 적는다. (다형성 사용)
//5)
class UI(window: IWindow) : IWindow {
    //위임한 클래스의 객체
    private val mWindow = window

    override fun getWidth(): Int {
        return mWindow.getWidth()
    }

    override fun getHeight(): Int {
        return mWindow.getHeight()
    }

    fun getTransWindowWidth(): Int {
        return mWindow.getWidth()
    }

    fun getTransWindowHeight(): Int {
        return mWindow.getHeight()
    }
}
//Iwindow -> 위임할 클래스가 구현한 인터페이스
//window는 -> 위임할 클래스 (다형성 이용)
class UI2(window: IWindow) : IWindow by window {
    //위임한 클래스의 객체
    private val mWindow = window

    override fun getWidth(): Int {
        return mWindow.getWidth()
    }

    override fun getHeight(): Int {
        return mWindow.getHeight()
    }

    fun getTransWindowWidth(): Int {
        return mWindow.getWidth()
    }

    fun getTransWindowHeight(): Int {
        return mWindow.getHeight()
    }
}

fun main() {
    val window: IWindow = TransparentWindow()
    val ui = UI(window)
    System.out.println("Width : ${ui.getWidth()}, height: ${ui.getHeight()}")
//    System.out.println("Width : ${ui.getTransWindowWidth()}, height: ${ui.getTransWindowHeight()}")
}