package com.adnroidapp.testrxjava

import android.util.Log
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observable.*
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.functions.Function4
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class Operators {
    fun exec() {
        Consumer(Producer()).execZip()
    }

    class Producer {
        fun createJust1() = just("1", "2", "3", "3")
        fun createJust2() = just("4", "5", "6")

    }

    class Consumer(private val producer: Producer) {
        fun execTake() {
            producer.createJust1().take(2)  //возмет первые n элементов
                    .subscribe { Log.v(TAG, "execTake onNext - $it") }
        }

        fun execSkip() {
            producer.createJust1().skip(2)  //пропустит n элементов
                    .subscribe { Log.v(TAG, "execTake onNext - $it") }
        }

        fun execMap() {
            producer.createJust1().map { it + it }  //выполнить действие над объектом
                    .subscribe { Log.v(TAG, "execTake onNext - $it") }
        }

        fun execDistinct() {
            producer.createJust1().distinct()  //отсеить дубликаты
                    .subscribe { Log.v(TAG, "execTake onNext - $it") }
        }

        fun execFilter() {
            producer.createJust1().filter { it.toInt() > 1 }  //фильтрует по заданному условию
                    .subscribe { Log.v(TAG, "execTake onNext - $it") }
        }

        fun execMerge() {
            producer.createJust1().mergeWith(producer.createJust2())  //соеденить с 2 источником данных (не горантирует последовательность)
                    .subscribe { Log.v(TAG, "execTake onNext - $it") }
        }

        fun execFlatMap() {                                 //каждый элемент который мы получили становится Observable и применяет к ним map
            producer.createJust1().flatMap {
                val delay = Random.nextInt(3000).toLong()
                return@flatMap just(it + "x").delay(delay, TimeUnit.MILLISECONDS)
            }
                    .subscribe { Log.v(TAG, "execTake onNext - $it") }
        }

        fun execZip() {
            val observable1 = just("1", "7").delay(1, TimeUnit.SECONDS)
            val observable2 = just("2", "8").delay(2, TimeUnit.SECONDS)
            val observable3 = just("3", "9").delay(3, TimeUnit.SECONDS)
            val observable4 = just("4", "10").delay(4, TimeUnit.SECONDS)

            zip(observable1, observable2, observable3, observable4,  //позволяет подписаться на несколько источников паралельно
                    Function4<String, String, String, String, List<String>> { t1, t2, t3, t4 -> //и получить их значение одновременно
                        return@Function4 listOf(t1, t2, t3, t4)
                    }).subscribeOn(Schedulers.computation())
                    .subscribe({
                        Log.v(TAG, "execZip onNext - $it")
                    }, {
                        Log.v(TAG, "execZip onError - $it")
                    })
        }
    }
}