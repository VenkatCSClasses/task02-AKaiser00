# Bank Account Specification


## Overview

This Bank Account project handles simple Bank Account functionality, creating objects that store a balance as well as an connected email. This object is also able to carry out withdrawals, deposits, and transfers. This project also creates an extra exception used specifically when overdrafts occur.

### Design Principles

- Use the most recent version of the **JUnit API** for testing.
- Should have **JavaDoc** comments explaining what each portion does, as well as smaller comments if explanation of the code is needed.
- Methods must always return the same output when given the same input.
- All edge cases should be covered if not specified in the method description from this file.

### Output Structure

Generate the following files needed to use and test the Bank Account class:
- BankAccount.java (the actual class)
- BankAccountTest.java (tester class)
- InsufficientFundsException.java (special exception used when overdrafts occur)
- README.md (overview about how to use the bank accounts)

Do not generate any excess files, or any extra functionality other than what is stated in this document.


## BankAccount.java
- BankAccount class, main functionality of this project is here.

### Constructor

#### BankAccount(String email, double startingBalance)
- Takes in both the starting balance and an email, creates a new Bank Account object.
- If the email is not valid according to the isEmailValid static method, throw an IllegalArgumentException.
- If the starting balance is not valid according to the isAmountValid static method, throw an IllegalArgumentException.

### Methods

#### double getBalance()
- Returns the balance attribute of the Bank Account.

#### String getEmail()
- Returns the email attribute of the Bank Account.

#### void withdraw(double amount)
- Removes parameter amount from the balance attribute.
- If the amount is not valid according to the isAmountValid static method, throw an IllegalArgumentException.
- If the amount is larger than the balance attribute, throw an InsufficientFundsException.

#### void deposit(double amount)
- Adds parameter amount to the balance attribute.
- If the amount is not valid according to the isAmountValid static method, throw an IllegalArgumentException.

#### void transfer(double amount, BankAccount reciever)
- Removes amount from the called bank account object, adds the same amount to the reciever bank account.
- If the amount is not valid or the reciever bank account is null, throw an IllegalArgumentException.
- If the amount is larger than the called bank account balance attribute, throw an InsufficientFundsException.

#### *static* boolean isEmailValid(String email)
- Returns true if the email is valid, false if not.
- Should follow email standards from this website: https://help.xmatters.com/ondemand/trial/valid_email_format.html 

#### *static* boolean isAmountValid(double amount)
- Returns true if the amount is valid, false if not.
- Amount is not valid if it is not positive, or includes more than 2 decimal places.


## BankAccountTest.java
- Each method in BankAccount should have a corresponding test method.
- Tests should be directly based off of the tests.yaml file, using Junit.


## InsufficientFundsException.java
- Custom exception that should be thrown when an overdraft occurs.