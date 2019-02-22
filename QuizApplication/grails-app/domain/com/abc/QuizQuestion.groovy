package com.abc

class QuizQuestion {
  enum QuestionType {
    TrueFalse, MultiChoice
  }

  static belongsTo = [quiz: Quiz]
  static mapping = {
    name sqlType: "text"
  }
  static constraints = {
    questionType nullable: false
    answer nullable: true
  }
  static hasMany = [options: QuizQuestionOption]

  QuestionType questionType
  QuizQuestionOption answer
  String name
  boolean isActive = true

  Date dateCreated
  Date lastUpdated
}
