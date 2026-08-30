package io.stealingdapenta.mc2048.utils.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class NumberRepresentationTest {

    @Test
    void returnsTheNextRepresentation() {
        assertEquals(NumberRepresentation.FOUR, NumberRepresentation.getNextRepresentation(2).orElseThrow());
    }

    @Test
    void doesNotWrapTheMaximumRepresentation() {
        assertTrue(NumberRepresentation.getNextRepresentation(262144).isEmpty());
    }
}
