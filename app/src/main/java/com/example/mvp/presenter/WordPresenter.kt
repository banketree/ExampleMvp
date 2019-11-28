package com.example.mvp.presenter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.util.TypedValue
import com.example.baselib.mvp.presenter.IPresenter
import com.example.mvp.main.HomeActivity
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock
import javax.inject.Inject

class WordPresenter @Inject constructor() : IPresenter {

    /*
    *  # Kotlin 的硬关键字
    * */
    fun testHard() {
        val testAs = 22 as Int //as ---- 用于做类型转换或为import语句指定别名
        val testAs_ = 22 as? Int //as? ---- 类型安全的类型转换运算符

        //break ---- 中断循环
        //忽略

        //class ---- 声明类
        //忽略

        //continue ---- 忽略本次循环剩下的语句，重新开始下一次循环
        //忽略

        //do ---- 用于 do while 循环
        //忽略

        //else ---- 在 if 分支中使用
        //忽略

        //false ---- 在 Boolean 类型中表式假的直接量
        //for ---- 用于 for 循环
        //fun ---- 声明函数
        //if ---- 在 if 分支中使用
        //in ---- 在 for 循环中使用；
        //        作为双目运算符，检查一个值是否处于区间或者集合内；
        //        用于修饰泛型参数，表式该泛型参数支持逆变。
        //!in ---- 可作为双目运算符 in 的反义词； !in 也可在 when 表达中使用
        //is ---- 用于做类型检查（类似 Java 的 instanceof）或在 when 表达式中使用
        //!is ---- 用于做类型检查（is 的反义词）或在 when 表达式中使用
        //null ----  代表空的直接量
        //object ----  用于声明对象表达式或定义命名对象
        //package ----  用于为当前文件指定包
        //return ----  声明函数的返回
        //super ----  用于引用父类实现的方法或属性，或者在子类构造器中调用父类构造器
        //this ----  代表当前类的对象或在构造器中调用当前类的其他构造器
        //throw ----  用于抛出异常
        //true ----  在 Boolean 类型中表式真的直接量
        //try ----  开始异常处理
        //typealias ----  用于定义类型别名
        //val ----  声明只读属性或变量
        //var ----  声明可变属性或变量
        //when ----  用于 when 表达式
        //while ----  用于 while 循环或 do while 循环   
    }

    /*
    *  # Kotlin 的软关键字
    * */
    fun testSoft() {
        //by ----  用于将接口或祖先类的实现代理给其他对象
        //catch ----  在异常处理中用于捕捉异常
        //constructor ----  用于声明构造器
        //delegate ----  用于指定该注解修饰委托属性存储其委托实例的字段
        //dynamic ----  主要用于在 Kotlin/JavaScript 中引用一个动态类型
        //fieId ----  用于指定该注解修饰属性的幕后字段
        //file ----  用于指定该注解修饰该源文件本身
        //finally ----  异常处理中的 finally 块
        //get ----  用于申明属性的 getter 方法，或者用于指定该注解修饰属性的 getter 方法
        //import ----  用于导包
        //init ----  用于声明初始化块
        //param ----  用于指定该注解修饰构造器参数
        //property ----  用于指定该注解修饰整个属性（这种目标的注解对 Java 不可见，因为 Java 并没有真正的属性）
        //receiveris ----  用于指定该注解修饰扩展方法或扩展属性的接收者
        //set ----  用于申明属性的 setter 方法，或者用于指定该注解修饰属性的 setter 方法
        //setparam ----  用于指定该注解修饰 setter 方法参数
        //where ----  用于为泛型参数增加限制
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    interface TestInterface {
        fun test(a: Int): Int
    }

    inline fun testInterface(crossinline t: (Int) -> Int): TestInterface = object : TestInterface {
        override fun test(a: Int): Int = t.invoke(a)
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////

    infix fun Int.add(x: Int): Int {
        return this + x
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////

    inline fun <T> check(lock: Lock, noinline body: () -> T): T {
        lock.lock()
        try {
            otherCheck(body)//OK
            return body()
        } finally {
            lock.unlock()
        }
    }

    fun <T> otherCheck(body: () -> T) {
        println("check test $body")
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    interface Production<out T> {
        fun produce(): T
    }

    interface Consumer<in T> {
        fun consume(item: T)
    }

    interface ProductionConsumer<T> {
        fun produce(): T
        fun consume(item: T)
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////

    inline fun <reified T : Activity> Activity.startActivity(context: Context) {
        startActivity(Intent(context, T::class.java))
    }

    private inline fun <reified T> Bundle.getDataOrNull(): T? {
        return getSerializable("") as? T
    }

    inline fun <reified T> Resources.dpToPx(value: Int): T {
        val result = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            value.toFloat(), displayMetrics)

        return when (T::class) {
            Float::class -> result as T
            Int::class -> result.toInt() as T
            else -> throw IllegalStateException("Type not supported")
        }
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////

    /*
    * Kotlin 的修饰符关键字
    * */
    fun testComm(activity: Activity) {
        //abstract ----  用于修饰抽象类或者抽象成员
        //annotation ----  用于修饰一个注解类
        //companion ----  用于声明一个伴生对象 -->静态变量
        //crossinline ----  用于禁止在传给内联函数的 Lambda 表达式中执行非局部返回
        testInterface {
            1
        }
        //data ----  用于声明数据类
        //enum ----  用于声明枚举
        //external ----  用于声明某个方法不由 Kotlin 实现（与 Java 的 native 相似）
        //使用关键字external标识该方法是JNI方法，在调用这个方时JVM会自动去调用Java_包名_类名_方法名的c++函数。

        //final ----  用于禁止被重写
        //infix ----  声明该函数能以双目运算符的格式执行
        println(100 add 200)

        //inline ----  用于声明 内联函数，Lambda 表达式可在内联函数中执行局部返回
        //inner ----  用于声明 内部类，内部类可以访问外部类的实例
        //internal ----  用于表示被修饰的声明只能在 当前模板内 可见
        //lateinit ----  用于修饰一个 non-null 非空属性，用于指定该属性可在构造器 以外的地方完成初始化
        //noinline ----  用于禁止内联函数中个别 Lambda 表达式被内联化
        val lock = ReentrantLock()
        check(lock) {
            println("funfun")
        }

        //open ----  用于修饰类，表示该类可派生子类； 或者用于修饰成员，表示该成员可以被重写
        //out ----  用于修饰泛型参数，表明该泛型参数支持协变
//        父类泛型对象可以赋值给子类泛型对象，用 in；
//        子类泛型对象可以赋值给父类泛型对象，用 out。

        //override ----  用于声明重写父类的成员
        //private ----  private 访问权限
        //protected ----  protected 访问权限
        //public ----  public访问权限
        //reified ----  用于修饰内联函数中的泛型形参，接下里在该函数中就可像 使用普通类型一样使用该类型参数
        activity.startActivity<HomeActivity>(activity)

        //vararg ----  用于修饰形参，表明该参数是个数可变的形参
        //sealed ----  用于声明一个密封类  类似枚举
        //suspend ----  用于标识一个函数后 Lambda 表达式可作为暂停

        //tailrec ----  用于修改一个函数可作为尾随递归函数使用

    }
}