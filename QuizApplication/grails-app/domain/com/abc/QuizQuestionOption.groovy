package com.abc

class QuizQuestionOption {

    static belongsTo = [quizQuestion: QuizQuestion]
    static constraints = {
        option nullable: true
    }

    String option
}
