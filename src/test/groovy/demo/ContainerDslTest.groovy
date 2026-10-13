package demo

import org.junit.Test

import static org.junit.Assert.assertEquals

class ContainerDslTest {
    @Test
    void selectsDisplayOverloadFromArgumentType() {
        assertEquals(
            ['alpha | beta | gamma', 'alpha, beta'],
            Main.evaluateDsl()
        )
    }
}
