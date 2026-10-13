package demo

import groovy.transform.CompileStatic

@CompileStatic(extensions = 'src/main/groovy/demo/ContainerDslExtension.groovy')
class Main {
    static void main(String[] args) {
        Container<String> myContainer =
            new Container<String>(['alpha', 'beta', 'gamma'])

        // Same DSL method name, selected by the argument's static type.
        println myContainer.display(' | ')
        println myContainer.display(2)
    }
}
