package demo

import groovy.transform.CompileStatic

/**
 * DSL operations use extension-method-style signatures: the receiver is
 * the first parameter, but these are ordinary static methods, not methods
 * on Container and not closure-delegate methods.
 */
@CompileStatic
class Commands {
    static <T> void each(Container<T> self, Closure<?> action) {
        self.values.each { T value -> action.call(value) }
    }
}
