package demo

import groovy.transform.CompileStatic

@CompileStatic(extensions = 'classpath:commandDsl.groovy')
class Main {
    static void main(String[] args) {
        new CommandContext().run {
            say 'Hello from the DSL'
            pause 2
            finish()
        }
    }
}
