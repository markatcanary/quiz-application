package com.abc

class Quiz {
  static belongsTo = [tenant: Tenant]
  static hasMany = [quizQuestions: QuizQuestion]

  static constraints = {
    name nullable: false, blank: false
    desc nullable: true
  }

  String name
  String desc
  Integer questionCount = 10
  boolean isActive = true


  Date dateCreated
  Date lastUpdated
}
