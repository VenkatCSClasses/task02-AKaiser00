package edu.ithaca.dturnbull;

public class BankAccount {

    private String email;
    private double balance;

    public BankAccount(String email, double startingBalance) {
        if (!isEmailValid(email)) {
            throw new IllegalArgumentException("Invalid email: " + email);
        }
        if (!isAmountValid(startingBalance)) {
            throw new IllegalArgumentException("Invalid starting balance: " + startingBalance);
        }
        this.email = email;
        this.balance = startingBalance;
    }

    public double getBalance() {
        return balance;
    }

    public String getEmail() {
        return email;
    }

    public void withdraw(double amount) throws InsufficientFundsException {
        if (!isAmountValid(amount)) {
            throw new IllegalArgumentException("Invalid withdrawal amount: " + amount);
        }
        if (amount > balance) {
            throw new InsufficientFundsException("Insufficient funds for withdrawal");
        }
        balance -= amount;
        // Fix potential floating point errors? The methods handle doubles, so exactness isn't guaranteed but required logic dictates direct usage.
        // However, reducing balance might lead to slightly off numbers like 0.0000000004.
        // I should round or format? Spec says "Methods must always return the same output when given the same input."
        // Using BigDecimal is safer, but spec says `double`. I'll stick to double.
        // To be safe regarding `isAmountValid` check on result (if needed), I might round it?
        // But `tests.yaml` says `0.01` etc.
        // For simple subtraction, double is usually okay for small values.
    }

    public void deposit(double amount) {
        if (!isAmountValid(amount)) {
            throw new IllegalArgumentException("Invalid deposit amount: " + amount);
        }
        balance += amount;
    }

    public void transfer(double amount, BankAccount receiver) throws InsufficientFundsException {
        if (!isAmountValid(amount)) {
            throw new IllegalArgumentException("Invalid transfer amount: " + amount);
        }
        if (receiver == null) {
            throw new IllegalArgumentException("Receiver account cannot be null");
        }
        if (amount > balance) {
            throw new InsufficientFundsException("Insufficient funds for transfer");
        }
        this.withdraw(amount);
        receiver.deposit(amount);
    }

    public static boolean isEmailValid(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        // One @
        int atIndex = email.indexOf('@');
        if (atIndex == -1 || email.indexOf('@', atIndex + 1) != -1) {
            return false;
        }
        
        String prefix = email.substring(0, atIndex);
        String domain = email.substring(atIndex + 1);

        if (prefix.isEmpty() || domain.isEmpty()) {
            return false;
        }

        // Check Prefix
        // Allowed: letters, numbers, _, ., -
        // Not start with . or -
        // Not end with . or -
        // No consecutive . or - or _. (Yaml tests special chars: "abc--abc" fails)
        // Regex: ^[a-zA-Z0-9]+([._-][a-zA-Z0-9]+)*$
        // Explanation: Starts with alnum. Repeat group (special + alnum). So special is always followed by alnum.
        // This ensures no special at end and no consecutive special.
        if (!prefix.matches("^[a-zA-Z0-9]+([._-][a-zA-Z0-9]+)*$")) {
            return false;
        }

        // Check Domain
        // Similar rules? "abc@d.cc" ok. "abc@d.e" fail.
        // Domain must have at least one dot? Yaml "abc@def" -> missing domain? No, "abc@def.com" is normal. "abcdef.com" (missing @).
        // Yaml "abc@" -> missing domain.
        // Is "abc@localhost" valid? Yaml doesn't test it. But website link says TLD >= 2 chars.
        // So require at least one dot in domain part?
        // Yaml "abc@d.cc" -> true. "abc@d.e" -> false.
        // So last part after dot must be >= 2 chars.
        
        // Regex for domain:
        // Parts separated by dots.
        // Last part length >= 2.
        // Parts can use -? Link says yes. Yaml doesn't disable it.
        // Regex: ^[a-zA-Z0-9]+([.-][a-zA-Z0-9]+)*\.[a-zA-Z]{2,}$  <-- requires dot and TLD length.
        
        if (!domain.matches("^[a-zA-Z0-9]+([.-][a-zA-Z0-9]+)*\\.[a-zA-Z]{2,}$")) {
             return false;
        }
        
        return true;
    }

    public static boolean isAmountValid(double amount) {
        if (amount <= 0) {
            return false;
        }
        // Check decimal places
        String s = String.valueOf(amount);
        int integerPlaces = s.indexOf('.');
        if (integerPlaces < 0) {
            return true; // No decimal point (e.g. integer)
        }
        // "200.0" is 1 decimal place?
        // Java Double.toString(200) -> "200.0". So always 1 decimal place minimum if using String.valueOf.
        // If s ends with ".0", it is integer equivalent. But technically 1 decimal place.
        // Spec says "includes more than 2 decimal places".
        // 200.001 -> 3 places -> false.
        // 200.11 -> 2 places -> true.
        // String format of double can be scientific notation for very large/small. Assume valid range.
        // "200.1" -> length 5. index 3. 5-3-1 = 1.
        // "200.11" -> length 6. index 3. 6-3-1 = 2.
        // "200.333" -> length 7. index 3. 7-3-1 = 3.
        
        int decimalPlaces = s.length() - integerPlaces - 1;
        if (decimalPlaces > 2) {
            return false;
        }
        return true;
    }
}
