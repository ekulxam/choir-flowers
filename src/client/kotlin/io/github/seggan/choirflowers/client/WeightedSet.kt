package io.github.seggan.choirflowers.client

import java.util.*
import java.util.concurrent.ThreadLocalRandom


class WeightedSet<E>(private val innerSet: MutableSet<Element<E>> = mutableSetOf()) :
    AbstractMutableSet<WeightedSet.Element<E>>() {

    var totalWeight: Float = innerSet.fold(0f) { acc, element -> acc + element.weight }
        private set

    override val size by innerSet::size

    val elements: Set<E>
        get() = innerSet.map { it.element }.toSet()
    
    fun updateWeight(element: E, updater: (Float) -> Float) {
        val toUpdate = innerSet.firstOrNull { it.element == element } ?: return
        val newWeight = updater(toUpdate.weight)
        if (newWeight != toUpdate.weight) {
            innerSet.remove(toUpdate)
            innerSet.add(Element(element, newWeight))
            totalWeight += newWeight - toUpdate.weight
        }
    }

    fun getRandom(random: Random = ThreadLocalRandom.current()): E {
        require(this.isNotEmpty()) { "Set must not be empty" }
        val r = random.nextFloat(totalWeight)
        var cumulativeWeight = 0.0
        for (element in innerSet) {
            cumulativeWeight += element.weight
            if (r < cumulativeWeight) {
                return element.element
            }
        }
        throw AssertionError("Should not reach here")
    }

    fun add(element: E, weight: Float): Boolean {
        return add(Element(element, weight))
    }

    override fun add(element: Element<E>): Boolean {
        if (innerSet.add(element)) {
            totalWeight += element.weight
            return true
        }
        return false
    }

    override fun iterator(): MutableIterator<Element<E>> {
        val innerIterator = innerSet.iterator()
        return object : MutableIterator<Element<E>> by innerIterator {
            private lateinit var lastItem: Element<E>

            override fun next(): Element<E> {
                lastItem = innerIterator.next()
                return lastItem
            }

            override fun remove() {
                innerIterator.remove()
                totalWeight -= lastItem.weight
            }
        }
    }

    data class Element<E>(val element: E, val weight: Float)
}