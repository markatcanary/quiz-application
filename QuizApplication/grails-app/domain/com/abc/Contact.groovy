package com.abc

class Contact {

  static belongsTo = [tenant: Tenant]
  static hasMany = [quizResults: QuizResult]

  static constraints = {
    name nullable: false
    email nullable: false, email: true
    phoneNumber nullable: false
  }

  String name
  String email
  String phoneNumber
}
