package fr.umontpellier.iut.encherisseur;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class EncherisseurTest {

    @Disabled
    @Test
    public void test_if_main_method_was_implemented() {

        assertDoesNotThrow(() -> Encherisseur.main(new String[1]));

    }
}
