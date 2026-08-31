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

    @Test
    void identifiesRepresentationsByTheirFormattedDisplayName() {
        assertEquals(
            NumberRepresentation.HUNDRED_THIRTY_ONE_THOUSAND_SEVENTY_TWO,
            NumberRepresentation.getRepresentationByDisplayName("131,072").orElseThrow()
        );
    }
}
