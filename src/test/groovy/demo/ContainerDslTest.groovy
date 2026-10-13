package demo

import groovy.transform.CompileStatic
import org.junit.Test

import static org.junit.Assert.assertEquals

@CompileStatic(extensions = 'demo/ContainerDslExtension.groovy')
class ContainerDslTest {
    @Test
    void selectsDisplayOverloadFromArgumentType() {
        assertEquals(
            ['alpha | beta | gamma', 'alpha, beta'],
            Main.evaluateDsl()
        )
    }

    @Test
    void stringOverloadJoinsAllValuesWithProvidedSeparator() {
        Container<String> container = new Container<String>(['one', 'two'])

        assertEquals('one / two', container.display(' / '))
    }

    @Test
    void integerOverloadLimitsTheNumberOfValues() {
        Container<String> container = new Container<String>(['one', 'two', 'three'])

        assertEquals('one, two', container.display(2))
    }
}
