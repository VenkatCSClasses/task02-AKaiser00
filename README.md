# Bank Account Project

This project implements a simple Bank Account system as per requirements.

## Features
- Create bank accounts with email and starting balance.
- Withdraw funds (with overdraft protection).
- Deposit funds.
- Transfer funds between accounts.
- Email and Amount validation logic.

## Usage
### Creating an Account
```java
BankAccount account = new BankAccount("user@example.com", 100.00);
```

### Operations
```java
account.deposit(50.00);
account.withdraw(20.00);
account.transfer(10.00, otherAccount);
```

## Running Tests
Run the provided JUnit tests to verify functionality.
```bash
mvn test
```
