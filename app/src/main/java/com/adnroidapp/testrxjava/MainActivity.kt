package com.adnroidapp.testrxjava

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.schedulers.Schedulers

const val TAG = "Test_RxJava"
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Create().exec()
        Operators().exec()

//        val result = Observable.just(0, 1, 2, 3)
//            .map { it + 1 }
//            .filter { it <= 3 }
//            .doOnNext {
//               Log.v(TAG, "$it")
//            }
//            .subscribe()
//    }

//    val test = Observable.just(1, 2, 3, 4)
//        .subscribeOn(Schedulers.io())
//        .map { Log.i(TAG, "Map thread: ${Thread.currentThread().name} - $it") }
//        .observeOn(AndroidSchedulers.mainThread())
//        .filter {
//            Log.i(TAG, "Filter thread: ${Thread.currentThread().name} - $it")
//            it % 2 == 0
//        }
//        .subscribeOn(Schedulers.computation())
//        .observeOn(Schedulers.io())
//        .subscribe()
    }
}