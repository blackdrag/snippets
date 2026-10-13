package demo

import groovy.transform.CompileStatic

@CompileStatic(extensions = 'src/main/groovy/demo/ContainerDslExtension.groovy')
class Main {
    static List<String> evaluateDsl() {
        Container<String> myContainer =
            new Container<String>(['alpha', 'beta', 'gamma'])

        // Same DSL method name, selected by the argument's static type.
        [myContainer.display(' | '), myContainer.display(2)]
    }

    static void main(String[] args) {
        evaluateDsl().each { String result -> println result }
    }
}
