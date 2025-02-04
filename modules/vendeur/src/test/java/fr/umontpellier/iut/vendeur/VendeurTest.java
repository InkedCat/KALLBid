package fr.umontpellier.iut.vendeur;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class VendeurTest {

    @Disabled
    @Test
    public void test_if_main_method_was_implemented() {

        assertDoesNotThrow(() -> Vendeur.main(new String[1]));

    }
}
