package com.blah

class Account {

    private BigDecimal availableBalance = 0;
    private def transactions = [];
    private final DateProvider dateProvider

    Account(DateProvider dateProvider) {
        this.dateProvider = dateProvider
    }

    def deposit(BigDecimal amount) {
        def latestBalance = availableBalance + amount
        def date = dateProvider.now()
        transactions.add new Transaction([ date: date, amount: amount, balance: latestBalance])
        availableBalance = latestBalance
    }

    def withdraw(BigDecimal amount) {
        deposit(amount.negate())
    }

    def statement() {
        transactions.reverse().asImmutable()
    }
}
