package com.abc

class QuizResult {
  static belongsTo = [contact: Contact]
  static constraints = {
  }
  Quiz quiz
  BigDecimal percentage
//    selectedOptions : a,d,c,b
//  QuizSelectedOption selectedOptions
}
