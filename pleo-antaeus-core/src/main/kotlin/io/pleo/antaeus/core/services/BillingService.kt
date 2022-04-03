package io.pleo.antaeus.core.services

import io.pleo.antaeus.core.exceptions.CurrencyMismatchException
import io.pleo.antaeus.core.exceptions.CustomerNotFoundException
import io.pleo.antaeus.core.exceptions.NetworkException
import io.pleo.antaeus.core.external.PaymentProvider
import io.pleo.antaeus.data.AntaeusDal
import io.pleo.antaeus.models.Customer
import io.pleo.antaeus.models.Invoice
import io.pleo.antaeus.models.InvoiceStatus
import mu.KLogger
import mu.KotlinLogging
import java.beans.Customizer

class BillingService(
    private val paymentProvider: PaymentProvider,
    private val invoiceService: InvoiceService,
    private val customerService: CustomerService,
    private val dal:AntaeusDal
) {
// TODO - Add code e.g. here
    private val log: KLogger
        get() = KotlinLogging.logger {}
    fun chargeInvoices(): List<Invoice> {
        val pendingInvoices = invoiceService.fetchPending()
        val paidInvoices = mutableListOf<Invoice>()

        pendingInvoices.forEach{
            try{
                val customer = customerService.fetch(it.customerId) //will throw exception if customer not found
                if (paymentProvider.charge(it, customer)){
                    paidInvoices.add(it)
                    invoiceService.updateStatusToPaid(it.id)
                    println(it.id.toString() + " is paid")
                }
                else{
                    println(it.id.toString() + " is NOT paid")
                }

            }
            catch (e: CurrencyMismatchException){
                println("mismatch")
            }
            catch (e: CustomerNotFoundException){
                println("notfound")
            }
            catch (e: NetworkException) {
                println("network")
            }
        }
        log.info("${paidInvoices.size} have been paid on ${pendingInvoices.size}")
        return paidInvoices
    }
}

