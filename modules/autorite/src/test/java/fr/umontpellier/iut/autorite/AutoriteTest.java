package fr.umontpellier.iut.autorite;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class AutoriteTest {
    @Disabled
    @Test
    public void test_if_main_method_was_implemented() {

        assertDoesNotThrow(() -> Autorite.main(new String[1]));

    }

}
