package com.adnroidapp.testrxjava

import android.util.Log
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableEmitter
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class Create {
    fun exec() {
        Consumer(Producer()).create()
    }
}

class Producer {
    fun just(): Observable<String> {
        return Observable.just("1", "2", "3")
    }

    fun fromIterable(): Observable<String> {
        val observable = Observable.fromIterable(listOf("1", "2", "3"))
        return observable
    }

    fun interval(): Observable<Long> {  //выдавать значение будет бесконечно
        val observable = Observable.interval(1, TimeUnit.SECONDS)
        return observable
    }

    fun timer() : Observable<Long> {    //выдаст значение только один раз
        val observable = Observable.timer(1, TimeUnit.SECONDS)
        return observable
    }

    fun range() : Observable<Int>  {    //выдаст значение от указанного значения
        val observable = Observable.range(1, 10)
        return observable
    }

    fun randomResultOperation() : Boolean {
        Thread.sleep(Random.nextLong(500))
        return listOf(true, false, false) [Random.nextInt(2)]
    }

    fun fromCallable() = Observable.fromCallable {  //принимает функцию
        val result = randomResultOperation()
        return@fromCallable result
    }

    fun create() : Observable<String> {
        return Observable.create(fun(emitter: ObservableEmitter<String>) {
            for (i in 0..10) {
                randomResultOperation().let {
                    if (it) {
                        emitter.onNext("Success - $i")
                    } else {
                        emitter.onNext("Error - $i")
                    }
                }
            }
        })
    }
 }

class Consumer(private val producer: Producer) {

    private val stringObserver = object : Observer<String> {
        var disposable: Disposable? = null
        override fun onSubscribe(d: Disposable?) {
            disposable = d
            Log.v(TAG, "onSubscribe")
        }

        override fun onNext(t: String?) {
            Log.v(TAG, "onNext - $t")
        }

        override fun onError(e: Throwable?) {
            Log.v(TAG, "onError")
        }

        override fun onComplete() {
            Log.v(TAG, "onComplete")
        }
    }

    fun execJust() {
        producer.just().subscribe(stringObserver)
    }

    fun execLambda() {
        val disposable = producer.just().subscribe({ s ->
            Log.v(TAG, "onNext - $s")
        }, {
            Log.v(TAG, "onError")
        }, {
            Log.v(TAG, "onComplete")
        })
    }

    fun execIterable() {
        val disposable = producer.fromIterable().subscribe({ s ->
            Log.v(TAG, "onNext - $s")
        }, {
            Log.v(TAG, "onError")
        }, {
            Log.v(TAG, "onComplete")
        })
    }

    fun interval() {
        val disposable = producer.interval().subscribe { s ->
            Log.v(TAG, "onNext interval - $s")
        }
    }

    fun timer () {
        val disposable = producer.timer().subscribe {
            Log.v(TAG, "onNext timer - $it")
        }
    }

    fun range() {
        val disposable = producer.range().subscribe {
            Log.v(TAG, "onNext range - $it")
        }
    }

    fun fromCallable() {
        val disposable = producer.fromCallable().subscribe {
            Log.v(TAG, "onNext fromCallable - $it")
        }
    }

    fun create() {
        val disposable = producer.create().subscribe({s ->
            Log.v(TAG, "onNext create - $s")
        }, {
            Log.v(TAG, "onNext onError - $it")
        })
    }
}