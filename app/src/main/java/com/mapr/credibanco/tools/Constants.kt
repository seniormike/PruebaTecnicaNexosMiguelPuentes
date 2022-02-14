package com.mapr.credibanco.tools

/**
 * Singleton reference object that manages constants in project.
 * @author {MAPR - Dec 2021}
 */
object Constants {

   const val BASE_URL = "http://192.168.0.11:8080/api/payments/"

   // Default values
   const val DEFAULT_COMMERCE_CODE = "000123"
   const val DEFAULT_TERMINAL_CODE = "000ABC"
   const val DEFAULT_AMOUNT = "12345"
   const val DEFAULT_CARD = "1234567890123456"

   // Authorization response status
   const val AUTH_APPROVED_STATUS = "00"
   const val AUTH_DECLINED_STATUS = "99"

   // Custom Dialog Tag
   const val CUSTOM_DIALOG_DETAIL = "CUSTOM_DIALOG_DETAIL"
}