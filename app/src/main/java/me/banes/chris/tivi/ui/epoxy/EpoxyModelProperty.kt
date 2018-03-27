/*
 * Copyright 2018 Google, Inc.
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

package me.banes.chris.tivi.ui.epoxy

import com.airbnb.epoxy.EpoxyController
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Property delegate for [EpoxyController] which will automatically call
 * [EpoxyController.requestModelBuild] when it's value changes.
 */
class EpoxyModelProperty<T> : ReadWriteProperty<EpoxyController, T?> {
    private var propertyValue: T? = null

    override fun getValue(thisRef: EpoxyController, property: KProperty<*>): T? {
        return propertyValue
    }

    override fun setValue(thisRef: EpoxyController, property: KProperty<*>, value: T?) {
        val v = propertyValue
        if (v != value) {
            propertyValue = value
            thisRef.requestModelBuild()
        }
    }
}
