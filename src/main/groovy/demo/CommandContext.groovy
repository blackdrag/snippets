package demo

import groovy.transform.CompileStatic
import groovy.lang.Delegate
import groovy.lang.DelegatesTo

@CompileStatic(extensions = 'classpath:commandDsl.groovy')
class CommandContext {
    void run(@DelegatesTo(value = CommandContext, strategy = Closure.DELEGATE_FIRST) Closure<?> body) {
        body.delegate = this
        body.resolveStrategy = Closure.DELEGATE_FIRST
        body.call()
    }

    void emit(String text) {
        println text
    }
}
