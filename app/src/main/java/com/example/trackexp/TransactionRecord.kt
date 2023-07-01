package com.example.trackexp

import java.time.Month

data class TransactionRecord(

    val amount:Int=-1,

    val type:String="",
    val category:String="",
    val mode:String="",
    val day:Int=0,
    val month: Int=0,
    val year:Int=0,
    val description:String=""
)
