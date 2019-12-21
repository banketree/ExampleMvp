package com.example.mvp.main

import kotlin.properties.Delegates
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

class GenericPresenter

class Box<T>(t: T) {
    var value = t
}

fun <T> boxIn(value: T) = Box(value)

//对泛型的类型上限进行约束。
fun <T : Comparable<T>> sort(list: List<T>) {
    // ……
}

//多个上界约束条件，可以用 where 子句：
fun <T> copyWhenGreater(list: List<T>, threshold: T): List<String>
        where T : CharSequence,
              T : Comparable<T> {
    return list.filter { it > threshold }.map { it.toString() }
}

//声明处的类型变异使用协变注解修饰符：in、out，消费者 in, 生产者 out。
//使用 out 使得一个类型参数协变，协变类型参数只能用作输出，可以作为返回值类型但是无法作为入参的类型：
// 定义一个支持协变的类
class Runoob<A>(var a: A) {
    fun foo(): A {
        return a
    }
}

// 定义一个支持逆变的类
class Runoob2<in A>(a: A) {
    fun foo(a: A) {
    }
}

//星号投射
//Function<*, String> , 代表 Function<in Nothing, String> ;
//Function<Int, *> , 代表 Function<Int, out Any?> ;
//Function<, > , 代表 Function<in Nothing, out Any?> .


//委托
// 创建接口
interface Base {
    fun print()
}

// 实现此接口的被委托的类
class BaseImpl(val x: Int) : Base {
    override fun print() {
        print(x)
    }
}

// 通过关键字 by 建立委托类
// 难以理解---》
class Derived(b: Base) : Base by b

fun test() {
    val b = BaseImpl(10)
    Derived(b).print() // 输出 10
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// 定义包含属性委托的类
class Example {
    var p: String by Delegate()
}

// 委托的类
class Delegate {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): String {
        return "$thisRef, 这里委托了 ${property.name} 属性"
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
        println("$thisRef 的 ${property.name} 属性赋值为 $value")
    }
}

val lazyValue: String by lazy {
    println("computed!")     // 第一次调用输出，第二次调用不执行
    "Hello"
}

class User {
    var name: String by Delegates.observable("初始值") { _, old, new ->
        println("旧值：$old -> 新值：$new")
    }
}

class Foo {
    var notNullBar: String by Delegates.notNull<String>()
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
class ResourceID<T> {
    companion object {
        var image_id: Any? = null
        var text_id: Any? = null
    }
}

class ResourceLoader<T>(id: ResourceID<T>) {
    operator fun provideDelegate(
        thisRef: MyUI,
        prop: KProperty<*>
    ): ReadOnlyProperty<MyUI, T>? {
        checkProperty(thisRef, prop.name)
        // 创建委托
        return null
    }

    private fun checkProperty(thisRef: MyUI, name: String) {
//        ……
    }
}

fun <T> bindResource(id: ResourceID<T>): ResourceLoader<T>? {
//    ……
    return null
}

class MyUI {
//    val image by bindResource(ResourceID.image_id)
//    val text by bindResource(ResourceID.text_id)
}

/////////////////////////////////////////////////////////////////////////////////
public interface List2<out E> : Collection<E> {
    //@UnsafeVariance --> 便可入参？
    override val size: Int

    override fun isEmpty(): Boolean
    override fun contains(element: @UnsafeVariance E): Boolean
    override fun iterator(): Iterator<E>
    override fun containsAll(elements: Collection<@UnsafeVariance E>): Boolean
    public operator fun get(index: Int): E
    public fun indexOf(element: @UnsafeVariance E): Int
    public fun lastIndexOf(element: @UnsafeVariance E): Int
    public fun listIterator(): ListIterator<E>
    public fun listIterator(index: Int): ListIterator<E>
    public fun subList(fromIndex: Int, toIndex: Int): List<E>
}

fun <T : Any> getClassName(clzObj: T): String {
    return clzObj.javaClass.simpleName
}

val readers: MutableList<String> = mutableListOf()
val readers2 = mutableListOf<String>()
//等价

//
//public inline fun <reified R> Iterable<*>.filterIsInstance(): List<@NoInfer R> {
//    return filterIsInstanceTo(ArrayList<R>())
//}
//
//public inline fun <reified R, C : MutableCollection<in R>> Iterable<*>.filterIsInstanceTo(destination: C): C {
//    for (element in this) if (element is R) destination.add(element)
//    return destination
//}
//
//inline fun <reified T : Activity> Context.startActivity() {
//    val intent = Intent(this,T::class.java)
//    startActivity(intent)
//}

//点变型:在类型出现的地方指定变型
fun <T : R, R> copyTo(source: MutableList<out T>, destination: MutableList<in T>) {
    source.forEach { item -> destination.add(item) }
}
//source不是一个普通的MutableList，而是一个投影(受限)的MutableList，只能调用返回类型是泛型参数的那些方法。
//Kotlin中的MutableList<out T>和Java中的MutableList<? extends T>是一个意思。
//Kotlin中的MutableList<in T>和Java中的MutableList<? super T>是一个意思。
//MutableList<*>的投影为MutableList<out Any?>。
//Kotlin中MyType<*>对应Java中的MyType<?>。


inline fun <reified T> create(): T = print(T::class.java.canonicalName) as T


//协变，逆变，点变形，星号投影
//-->协变
//: 存在两个类Son和Dad，且Son : Dad；存在泛型类A<T>，则类型A<Dad>和类型A<Son>之间没有任何子类型化关系。

class Zoom<out T>(t: T) //则可以解决父子关系
//协变：泛型类的继承关系来源于子类型的继承关系，但这不是没有任何限制的......
//如果一个泛型类是协变的，那泛型参数类型的对象，只能出现在Zoom类中函数的返回值的位置上，不能出现在函数的参数里；

//存在两个类Son和Dad，且Son : Dad；存在泛型类A<out T>，则类型A<Son>是类型A<Dad>的子类型，且A<out T>中不存在包含T作为参数类型（或作为参数类型的泛型参数）的函数。

//-->逆变
//存在两个类Son和Dad，且Son : Dad；存在泛型类A<in T>，则类型A<Dad>是类型A<Son>的子类型，且A<in T>中不存在包含T作为返回值类型（或作为返回值类型的泛型参数）的函数。

//-->使用点协变
//我们可以把泛型类声明为不型变的，但是在使用它的时候，加上out或者in，让它在使用的时候产生型变。

//-->星号投影
//实现省略
fun function222(class2: KClass<*>) {
}
//任何类型的Class对象都可以传入function函数。

//function(Cat::class)
//function(Dog::class)







