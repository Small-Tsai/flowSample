package com.tsai.flowsample

internal interface IPayment {
    fun pay()
}


internal class CreditCard : IPayment {
    override fun pay() {
        println("信用卡付款")
    }
}

internal class QRCode : IPayment {
    override fun pay() {
        println("QRCode付款")
    }
}

internal class Payment {
    fun pay(iPayment: IPayment) {
        iPayment.pay()
    }
}

object Bank {
    @JvmStatic
    fun main(args: Array<String>) {
        val p = Payment()
        p.pay(CreditCard())
        p.pay(QRCode())
    }
}