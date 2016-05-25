package com.blah

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.TupleConstructor

@TupleConstructor
@EqualsAndHashCode
@ToString
class Transaction {
    final def date
    final def amount
    final def balance
}
