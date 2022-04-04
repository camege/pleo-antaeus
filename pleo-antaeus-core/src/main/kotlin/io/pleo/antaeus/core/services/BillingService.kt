package io.pleo.antaeus.core.services

import io.pleo.antaeus.core.exceptions.CurrencyMismatchException
import io.pleo.antaeus.core.exceptions.CustomerNotFoundException
import io.pleo.antaeus.core.exceptions.NetworkException
import io.pleo.antaeus.core.external.PaymentProvider
import io.pleo.antaeus.models.Invoice


class BillingService(
    private val paymentProvider: PaymentProvider,
    private val invoiceService: InvoiceService,
    private val customerService: CustomerService
) {
// TODO - Add code e.g. here
    fun chargeInvoices(): List<Invoice> {
        val pendingInvoices = invoiceService.fetchPending()
        val paidInvoices = mutableListOf<Invoice>()

        pendingInvoices.forEach{
            try{
                val customer = customerService.fetch(it.customerId) //will throw exception if customer not found
                if (paymentProvider.charge(it, customer)){
                    paidInvoices.add(it)
                    invoiceService.updateStatusToPaid(it.id) //made some printlns instead of log.info in order to read exceptions better in console
                    println(it.id.toString() + " is paid")
                }
                else{
                    println(it.id.toString() + " is NOT paid")
                }

            }
            catch (e: CurrencyMismatchException){ //made some printlns instead of log.errors in order to read exceptions better in console
                println("mismatch")
            }
            catch (e: CustomerNotFoundException){
                println("notfound")
            }
            catch (e: NetworkException) {
                println("network")
            }
        }
        println("${paidInvoices.size} have been paid on ${pendingInvoices.size}")
        return paidInvoices
    }
}

