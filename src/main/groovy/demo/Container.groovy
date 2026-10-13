package demo

import groovy.transform.CompileStatic

@CompileStatic
class Container<T> {
    final List<T> values

    Container(List<T> values) {
        this.values = values
    }
}
