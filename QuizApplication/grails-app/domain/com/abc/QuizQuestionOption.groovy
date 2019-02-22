package com.abc

class QuizQuestionOption {

  static belongsTo = [quizQuestion: QuizQuestion]
  static constraints = {
    name nullable: true
  }

  String name
}
