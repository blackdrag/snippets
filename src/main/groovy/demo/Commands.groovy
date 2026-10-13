package demo

import groovy.transform.CompileStatic

/**
 * DSL operations are ordinary static methods with extension-method-style
 * signatures: the receiver is the first parameter.
 */
@CompileStatic
class Commands {
    static <T> String display(Container<T> self, String separator) {
        self.values.join(separator)
    }

    static <T> String display(Container<T> self, int limit) {
        self.values.take(limit).join(', ')
    }
}
