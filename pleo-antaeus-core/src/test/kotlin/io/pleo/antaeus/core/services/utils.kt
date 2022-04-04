
import io.pleo.antaeus.core.external.PaymentProvider


// This is the mocked instance of the payment provider
internal fun getPaymentProvider(): PaymentProvider {
    return object : PaymentProvider {
    }
}
