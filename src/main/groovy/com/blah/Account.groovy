package com.blah

class Account {

    private BigDecimal availableBalance = 0;
    private List<Transaction> transactions = [];
    private final DateProvider dateProvider

    Account(DateProvider dateProvider) {
        this.dateProvider = dateProvider
    }

    def deposit(BigDecimal amount) {
        transact(amount)
    }

    def withdraw(BigDecimal amount) {
        transact(amount.negate())
    }

    def statement() {
        transactions.reverse().asImmutable()
    }

    private def transact(BigDecimal amount) {
        def latestBalance = availableBalance + amount
        def date = dateProvider.now()
        transactions.add(new Transaction([date: date, amount: amount, balance: latestBalance]))
        availableBalance = latestBalance
    }
}
