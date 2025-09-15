import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.api.Test;

import com.example.DigitalWallet;

class Estorno {
    DigitalWallet carteira;
    static Stream<Arguments> valoresEstorno() {
        return Stream.of(
            Arguments.of(100.0, 10.0, 110.0),
            Arguments.of(0.0,   5.0,  5.0),
            Arguments.of(50.0,  0.01, 50.01)
        );
    }

    @ParameterizedTest
    @MethodSource("valoresEstorno")
    void refundComCarteiraValida(double inicial, double valor, double saldoEsperado) {
        DigitalWallet carteira = new DigitalWallet("Elcio", inicial);
        carteira.verify();
        carteira.unlock();

        assumeTrue(carteira.isVerified() && !carteira.isLocked(), "O teste só deve rodar se a carteira estiver verificada e não bloqueada.");
        
        carteira.refund(valor);
        
        assertEquals(saldoEsperado, carteira.getBalance(), "O saldo após o estorno não é o esperado.");
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, -10.0, -0.01})
    void deveLancarExcecaoParaRefundInvalido(double valor) {
        DigitalWallet carteira = new DigitalWallet("Elcio", 100.0);
        carteira.verify();
        carteira.unlock();

        assumeTrue(carteira.isVerified() && !carteira.isLocked(), "Pré-condição para o teste: a carteira deve ser válida.");

        assertThrows(IllegalArgumentException.class, () -> {
            carteira.refund(valor);
        }, "Deveria ter lançado uma exceção para valores de estorno inválidos.");
    }

    @Test
    void deveLancarSeNaoVerificadaOuBloqueada() {
        DigitalWallet carteiraNaoVerificada = new DigitalWallet("naoVerificado", 100.0);
        assertThrows(IllegalStateException.class, () -> {
            carteiraNaoVerificada.refund(10.0);
        }, "Deveria lançar exceção para carteira não verificada.");
        

    
        DigitalWallet carteiraBloqueada = new DigitalWallet("bloqueado", 100.0);
        carteiraBloqueada.isVerified();
        carteiraBloqueada.isLocked();
        assertThrows(IllegalStateException.class, () -> {
            carteiraBloqueada.refund(10.0);
        }, "Deveria lançar exceção para carteira bloqueada.");
    }
}