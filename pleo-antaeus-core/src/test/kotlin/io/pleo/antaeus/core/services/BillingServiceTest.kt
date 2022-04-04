package io.pleo.antaeus.core.services

import io.mockk.every
import io.mockk.mockk
import io.pleo.antaeus.core.exceptions.CurrencyMismatchException
import io.pleo.antaeus.core.exceptions.CustomerNotFoundException
import io.pleo.antaeus.core.external.PaymentProvider
import io.pleo.antaeus.data.AntaeusDal
import io.pleo.antaeus.models.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal
import getPaymentProvider

class BillingServiceTest (){
    private val testInvoice = Invoice(id = 5, customerId = 1, amount = Money(BigDecimal.ONE, Currency.EUR), status = InvoiceStatus.PENDING)
    private val testCustomer = Customer(id = 1, currency = Currency.DKK)
    private val paymentProvider = getPaymentProvider()

    @Test
    fun `will throw if customer currency and invoice currency don't match`() {
        assertThrows<CurrencyMismatchException> {
            paymentProvider.charge(testInvoice, testCustomer)
        }
    }

}