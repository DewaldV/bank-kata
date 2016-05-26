package com.blah

import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Title

@Title("Account Unit Tests")
@Subject(Account)
class AccountSpec extends Specification {

    DateProvider dateProvider = Mock(DateProvider)
    Account account

    void setup() {
        account = new Account(dateProvider);
    }

    def "should deposit money in account"() {
        when:
        def balance = account.deposit(1000.0)

        then:
        balance == 1000.0
    }

    def "should sum all deposits into account"() {
        when:
        account.deposit(1000.0)
        def balance = account.deposit(500.0)

        then:
        balance == 1500.0
    }

    def "should withdraw money from account"() {
        given:
        account.deposit(500.0)

        when:
        def balance = account.withdraw(100.0)

        then:
        balance == 400.0
    }

    def "should return statement with date amount and balance for deposit"() {
        given:
        dateProvider.now() >> "01/04/2014"
        account.deposit(300.0)

        when:
        def statement = account.statement()

        then:
        statement == [
                transaction([date: "01/04/2014", amount: 300.0, balance: 300.0])
        ]
    }

    def "should return statement with date amount and balance for withdrawal"() {
        given:
        dateProvider.now() >> "01/04/2014"
        account.withdraw(300.0)

        when:
        def statement = account.statement()

        then:
        statement == [
                transaction([date: "01/04/2014", amount: -300.0, balance: -300.0])
        ]
    }

    def "should return statement with two deposits"() {
        given:
        dateProvider.now() >>> ["01/04/2014", "04/04/2014"]
        account.deposit(300.0)
        account.deposit(600.0)

        when:
        def statement = account.statement()

        then:
        statement == [
                transaction([date: "04/04/2014", amount: 600.0, balance: 900.0]),
                transaction([date: "01/04/2014", amount: 300.0, balance: 300.0])
        ]
    }

    def "should return statement with one deposit and one withdrawal"() {
        given:
        dateProvider.now() >>> ["05/04/2014", "07/04/2014"]
        account.deposit(1000.0)
        account.withdraw(300.0)

        when:
        def statement = account.statement()

        then:
        statement == [
                transaction([date: "07/04/2014", amount: -300.0, balance: 700.0]),
                transaction([date: "05/04/2014", amount: 1000.0, balance: 1000.0])
        ]
    }

    def "should return statement for deposit withdrawal deposit"() {
        given:
        dateProvider.now() >>> ["01/04/2014", "02/04/2014", "10/04/2014"]
        account.deposit(1000.00)
        account.withdraw(100.00)
        account.deposit(500.00)

        when:
        def statement = account.statement()

        then:
        statement == [
                transaction([date: "10/04/2014", amount: 500.0, balance: 1400.0]),
                transaction([date: "02/04/2014", amount: -100.0, balance: 900.0]),
                transaction([date: "01/04/2014", amount: 1000.0, balance: 1000.0])
        ]
    }

    def transaction(Map argMap) {
        new Transaction(argMap)
    }
}
