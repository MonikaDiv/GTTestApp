package com.example.gttest.gtenums

enum class FailureReasaonEnum(val value:String)
{

    GT_LESSTHANZERO("Incorrect money input. Please enter again."),
    GT_GREATER("Transferring USD larger than or equal to 10,000 is not supported yet.")

}