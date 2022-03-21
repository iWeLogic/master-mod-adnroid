package com.iwelogic.minecraft.mods.data

class MultiMap<K, V> : MutableMap<K, V> {

    private val keysOverride: MutableList<K> = ArrayList()
    private val valuesOverride: MutableList<V> = ArrayList()

    override val entries: MutableSet<MutableMap.MutableEntry<K, V>>
        get() {
            val set: MutableSet<MutableMap.MutableEntry<K, V>> = HashSet()
            for (i in keysOverride.indices) {
                set.add(Value(keysOverride[i], valuesOverride[i]))
            }
            return set
        }


    override val keys: MutableSet<K>
        get() = keysOverride.toMutableSet()
    override val values: MutableCollection<V>
        get() = ValuesSet(valuesOverride)

    override fun clear() {
        keysOverride.clear()
        valuesOverride.clear()
    }

    override fun put(key: K, value: V): V? {
        keysOverride.add(key)
        valuesOverride.add(value)
        return value
    }

    override val size: Int
        get() = keysOverride.size

    override fun putAll(from: Map<out K, V>) {
        for ((key, value) in from) {
            keysOverride.add(key)
            valuesOverride.add(value)
        }
    }

    override fun remove(key: K): V? {
        val index = keysOverride.indexOf(key)
        var value: V? = null
        if (index != -1) {
            keysOverride.removeAt(index)
            value = valuesOverride[index]
            valuesOverride.removeAt(index)
        }
        return value
    }

    override fun containsKey(key: K): Boolean {
        val index = keysOverride.indexOf(key)
        return index != -1
    }

    override fun containsValue(value: V): Boolean {
        val index = valuesOverride.indexOf(value)
        return index != -1
    }

    override fun get(key: K): V? {
        val index = keysOverride.indexOf(key)
        var value: V? = null
        if (index != -1) {
            value = valuesOverride[index]
        }
        return value
    }

    override fun isEmpty(): Boolean = keysOverride.isEmpty()

    private inner class ValuesSet<E>(val values: MutableList<E>) : AbstractMutableSet<E>() {

        override val size: Int get() = values.size

        override fun add(element: E): Boolean {
            values.add(element)
            return true
        }

        override fun iterator(): MutableIterator<E> = object : MutableIterator<E> {
            var index = -1
            override fun hasNext(): Boolean = index < size - 1

            override fun next(): E {
                index++
                return values[index]
            }

            override fun remove() {
                values.removeAt(index)
                index--
            }
        }
    }
}

class Value<K, V>(override val key: K, override var value: V) : MutableMap.MutableEntry<K, V> {

    override fun setValue(newValue: V): V {
        value = newValue
        return value
    }
}