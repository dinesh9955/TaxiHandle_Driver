/*
 *  Created By Sudesh Bishnoi
 */

package com.rydz.driver.adapters
import io.reactivex.disposables.Disposable

interface Connectable {
    fun connect(): Disposable
}
