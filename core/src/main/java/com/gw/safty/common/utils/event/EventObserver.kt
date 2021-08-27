/*
 * Copyright 2018 Google LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gw.safty.common.utils.event

import androidx.lifecycle.Observer

/**
 * 一个事件观察者，便于校验Event到内容是否已经被消费过(是否已经被使用过)
 * An [Observer] for [Event]s, simplifying the pattern of checking if the [Event]'s content has
 * already been consumed.
 * [onEventUnconsumedContent] 仅当 [Event] 当内容没有被消费过才会被调用。
 * [onEventUnconsumedContent] is *only* called if the [Event]'s contents has not been consumed.
 *
 * 因为LiveData本身设计问题，观察者无法知道LiveData的数据是否被使用过。
 * 比如在获取到数据跳转activity，然后按下返回键退回，有可能还会收到上一次到值，导致再次跳转。
 * 详情请看 [Event]
 */
class EventObserver<T>(private val onEventUnconsumedContent: (T) -> Unit) : Observer<Event<T>> {
    override fun onChanged(event: Event<T>?) {
        event?.consume()?.run(onEventUnconsumedContent)
    }
}
