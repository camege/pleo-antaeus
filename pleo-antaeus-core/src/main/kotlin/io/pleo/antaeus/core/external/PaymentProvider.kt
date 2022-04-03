

/*
    This is the payment provider. It is a "mock" of an external service that you can pretend runs on another system.
    With this API you can ask customers to pay an invoice.
    This mock will succeed if the customer has enough money in their balance,
    however the documentation lays out scenarios in which paying an invoice could fail.
 */

package io.pleo.antaeus.core.external

import io.pleo.antaeus.core.exceptions.CurrencyMismatchException
import io.pleo.antaeus.core.exceptions.NetworkException
import io.pleo.antaeus.core.services.CustomerService
import io.pleo.antaeus.models.Customer
import io.pleo.antaeus.models.Invoice
import mu.KLogger
import mu.KotlinLogging
import kotlin.random.Random

interface PaymentProvider {
    /*
        Charge a customer's account the amount from the invoice.
        Returns:
          `True` when the customer account was successfully charged the given amount.
          `False` when the customer account balance did not allow the charge.
        Throws:
          `CustomerNotFoundException`: when no customer has the given id.
          `CurrencyMismatchException`: when the currency does not match the customer account.
          `NetworkException`: when a network error happens.
     */
    private val log: KLogger
        get() = KotlinLogging.logger {}



    fun charge(invoice: Invoice, customer: Customer): Boolean {
        if (customer.currency != invoice.amount.currency) {
            log.error("Currency mismatch between Invoice ${invoice.id} and customer ${customer.id}")
            throw CurrencyMismatchException(invoice.id, customer.id)
        }
        if (Random.nextInt(1, 11) == 10){
            throw NetworkException()
        }
        return Random.nextInt(1, 11) == 9

    }
}