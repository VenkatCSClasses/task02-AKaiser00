package edu.ithaca.dturnbull;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BankAccountTest {

    @Test
    void getBalanceTest() {
        BankAccount bankAccount = new BankAccount("a@b.com", 200);
        assertEquals(200, bankAccount.getBalance(), 0.001);
        
        BankAccount bankAccount2 = new BankAccount("a@b.com", 0.01);
        assertEquals(0.01, bankAccount2.getBalance(), 0.001);
    }

    @Test
    void getEmailTest() {
        BankAccount bankAccount = new BankAccount("a@b.com", 200);
        assertEquals("a@b.com", bankAccount.getEmail());
    }

    @Test
    void withdrawTest() throws InsufficientFundsException {
        BankAccount bankAccount = new BankAccount("a@b.com", 200);
        bankAccount.withdraw(100);
        assertEquals(100, bankAccount.getBalance(), 0.001);

        bankAccount.withdraw(100);
        assertEquals(0, bankAccount.getBalance(), 0.001);

        assertThrows(InsufficientFundsException.class, () -> bankAccount.withdraw(300));
        
        // $0.01 overdraft
        BankAccount bankAccount2 = new BankAccount("a@b.com", 200);
        assertThrows(InsufficientFundsException.class, () -> bankAccount2.withdraw(200.01));

        // Negative
        assertThrows(IllegalArgumentException.class, () -> bankAccount.withdraw(-100));

        // 3 decimal places
        assertThrows(IllegalArgumentException.class, () -> bankAccount.withdraw(100.001));
    }

    @Test
    void depositTest() {
        BankAccount bankAccount = new BankAccount("a@b.com", 200);
        bankAccount.deposit(100);
        assertEquals(300, bankAccount.getBalance(), 0.001);

        bankAccount.deposit(0.01);
        assertEquals(300.01, bankAccount.getBalance(), 0.001);

        assertThrows(IllegalArgumentException.class, () -> bankAccount.deposit(0));

        assertThrows(IllegalArgumentException.class, () -> bankAccount.deposit(-100));

        assertThrows(IllegalArgumentException.class, () -> bankAccount.deposit(100.001));
    }

    @Test
    void transferTest() throws InsufficientFundsException {
        BankAccount bankAccount = new BankAccount("a@b.com", 200);
        BankAccount receiver = new BankAccount("c@d.com", 200);

        bankAccount.transfer(100, receiver);
        assertEquals(100, bankAccount.getBalance(), 0.001);
        assertEquals(300, receiver.getBalance(), 0.001);

        bankAccount.transfer(100, receiver);
        assertEquals(0, bankAccount.getBalance(), 0.001);
        assertEquals(400, receiver.getBalance(), 0.001);

        assertThrows(InsufficientFundsException.class, () -> bankAccount.transfer(100, receiver));
        
        // Overdraft by 1
        BankAccount bankAccount2 = new BankAccount("a@b.com", 1);
        assertThrows(InsufficientFundsException.class, () -> bankAccount2.transfer(2, receiver));

        // Negative
        assertThrows(IllegalArgumentException.class, () -> bankAccount.transfer(-100, receiver));
        
        // Null receiver
        assertThrows(IllegalArgumentException.class, () -> bankAccount.transfer(100, null));
    }

    @Test
    void isEmailValidTest() {
        // Valid
        assertTrue(BankAccount.isEmailValid("a@b.com"));
        assertTrue(BankAccount.isEmailValid("a@b.cc"));
        assertTrue(BankAccount.isEmailValid("abc@def.com"));
        assertTrue(BankAccount.isEmailValid("a@def.com"));
        assertTrue(BankAccount.isEmailValid("abc@d.cc"));
        assertTrue(BankAccount.isEmailValid("abc.def@mail.com"));
        assertTrue(BankAccount.isEmailValid("abc_def@mail.com"));
        assertTrue(BankAccount.isEmailValid("abc-def@mail.com"));
        assertTrue(BankAccount.isEmailValid("abc-abc@def.com")); // Non-consecutive special

        // Invalid
        assertFalse(BankAccount.isEmailValid(""));
        assertFalse(BankAccount.isEmailValid("abc@"));
        assertFalse(BankAccount.isEmailValid("@mail.com"));
        assertFalse(BankAccount.isEmailValid("abc@d.e")); // Tag length 1
        assertFalse(BankAccount.isEmailValid("abc:abc@def.com")); // Invalid char
        assertFalse(BankAccount.isEmailValid("-abc@def.com")); // Special @ start
        assertFalse(BankAccount.isEmailValid("abc-@def.com")); // Special @ end
        assertFalse(BankAccount.isEmailValid("abc--abc@def.com")); // Consecutive special
        assertFalse(BankAccount.isEmailValid("abcdef.com")); // 0 @
        assertFalse(BankAccount.isEmailValid("abc@def@def.com")); // 2 @
    }

    @Test
    void isAmountValidTest() {
        assertTrue(BankAccount.isAmountValid(200));
        assertTrue(BankAccount.isAmountValid(200.11));
        assertTrue(BankAccount.isAmountValid(0.01));
        
        assertFalse(BankAccount.isAmountValid(200.001));
        assertFalse(BankAccount.isAmountValid(-200));
        assertFalse(BankAccount.isAmountValid(0));
        assertFalse(BankAccount.isAmountValid(200.3333));
    }
    
    @Test
    void constructorTest() {
        BankAccount bankAccount = new BankAccount("a@b.com", 200);
        assertEquals("a@b.com", bankAccount.getEmail());
        assertEquals(200, bankAccount.getBalance(), 0.001);

        assertThrows(IllegalArgumentException.class, ()-> new BankAccount("", 100));
        assertThrows(IllegalArgumentException.class, ()-> new BankAccount("a@b.com", -100));
        assertThrows(IllegalArgumentException.class, ()-> new BankAccount("a@b.com", 100.001));
    }
}
