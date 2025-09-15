import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import com.example.DigitalWallet;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assumptions.assumeTrue;


public class Pagamento {

    private DigitalWallet carteira;

    @BeforeEach
    void setup() {
        carteira = new DigitalWallet("Teste", 100.0);
    }
    
    @ParameterizedTest
    @CsvSource(
        {"100.0, 30.0, true", "50.0, 80.0, false", "10.0, 10.0, true"}
    )
    
    void pagamentoComCarteiraVerificadaENaoBloqueada(double saldoInicial, double valorPagamento, boolean resultadoEsperado) {
        carteira = new DigitalWallet("Teste", saldoInicial);
        carteira.verify();
        
        assumeTrue(carteira.isVerified() && !carteira.isLocked());

        boolean sucesso = carteira.pay(valorPagamento);
        assertEquals(resultadoEsperado, sucesso);

        if (sucesso) {
            assertEquals(saldoInicial - valorPagamento, carteira.getBalance());
        } else {
            assertEquals(saldoInicial, carteira.getBalance());
        }
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, -10.0, -0.01})
    void deveLancarExcecaoParaPagamentoInvalido(double valor) {
        carteira.verify();
        assumeTrue(carteira.isVerified() && !carteira.isLocked());
        
        assertThrows(IllegalArgumentException.class, () -> {
            carteira.pay(valor);
        });
    }

    @Test
    void deveLancarSeNaoVerificada() {
        assertThrows(IllegalStateException.class, () -> carteira.pay(10.0));
    }
    
    @Test
    @DisplayName("Deve lançar exceção se estiver bloqueada")
    void deveLancarSeBloqueada() {
        carteira.verify();
        carteira.lock();
        
        Assertions.assertThrows(IllegalStateException.class, () -> carteira.pay(10.0));
    }
}