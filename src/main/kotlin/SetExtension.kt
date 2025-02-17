fun <E> Set<E>.pairSubset(): List<Set<E>> {
    return flatMap { a ->
        mapNotNull { b ->
            if (a != b) setOf(a, b) else null
        }
    }
}