package demo

import groovy.transform.CompileStatic

@CompileStatic(extensions = 'src/main/groovy/demo/ContainerDslExtension.groovy')
class Main {
    static void main(String[] args) {
        Container<String> myContainer = new Container<String>(['alpha', 'beta', 'gamma'])

        myContainer.each { String item ->
            println "DSL item: $item"
        }
    }
}
