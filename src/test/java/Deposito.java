import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import com.example.DigitalWallet;

class Deposito {
    
    @ParameterizedTest
    @ValueSource(doubles = {10.0, 50.0, 100.50})
    void deveDepositarValoresValidos(double amount) {
        DigitalWallet carteira = new DigitalWallet("Elcio", 0.0);
        
        assertEquals(0.0, carteira.getBalance(), "O saldo inicial deve ser 0.0.");
        
        carteira.deposit(amount);
        
        assertEquals(amount, carteira.getBalance(), "O saldo deve ser atualizado corretamente após o depósito.");
    }
  
    @ParameterizedTest
    @ValueSource(doubles = {0.0, -10.0, -0.01})
    void deveLancarExcecaoParaDepositoInvalido(double amount) {
        DigitalWallet carteira = new DigitalWallet("Elcio", 0.0);
        
        assertThrows(IllegalArgumentException.class, () -> {
            carteira.deposit(amount);
        }, "Deveria ter lançado uma exceção para depósito com valor menor ou igual a zero.");
        
        assertEquals(0.0, carteira.getBalance(), "O saldo não deve ser alterado em caso de depósito inválido.");
    }
}