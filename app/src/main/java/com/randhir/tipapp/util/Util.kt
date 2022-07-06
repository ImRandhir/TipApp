package com.randhir.tipapp.util

fun calTotalTip(totalBill: Double, tipP: Int): Double {

    return if(totalBill > 1 && totalBill.toString().isNotEmpty()) (totalBill*tipP)/100
    else 0.0

}

fun calTotalPerPerson(totalBill: Double , splitBy: Int , tipP: Int  ): Double{

    val bill = calTotalTip(totalBill =  totalBill , tipP = tipP) + totalBill

    return (bill/splitBy)

}