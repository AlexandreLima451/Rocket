package com.company.util

import io.reactivex.Observer
import io.reactivex.subjects.PublishSubject

class Logger {

    private var observable: PublishSubject<String> = PublishSubject.create<String>()

    fun observe(subscriber: Observer<String>) {
        observable.subscribe(subscriber)
    }

    fun log(message: String) {
        observable.onNext(message)
    }

    fun closeLog() {
        observable = PublishSubject.create<String>()
    }
}