import br.com.locar.api.LocarApiApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

@SpringBootTest(classes = LocarApiApplication.class)
class LocarApiApplicationTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void deveriaSubirUmaAplicacaoSeOsArgsFossemInformados() {
        String[] args = new String[]{};
        assertDoesNotThrow(() -> LocarApiApplication.main(args));
    }

    @Test
    void naoDeveriaSubirUmaAplicacaoSeOsArgsNulos() {
        String[] args = null;
        assertThrowsExactly(IllegalArgumentException.class,() -> LocarApiApplication.main(args));
    }

}