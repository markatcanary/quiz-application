package com.abc

class QuizQuestion {
    enum QuestionType {
        TrueFalse, MultiChoice
    }

    static belongsTo = [quiz :Quiz]
    static hasMany = [quizQuestionOptions : QuizQuestionOption]
    static mapping = {
        question sqlType: "text"
    }
    static constraints = {
        questionType nullable: false
        answer nullable: true
    }

    QuestionType questionType
    String question

//    QuizQuestionOption questionOptions
    QuizQuestionOption answer
    Date createdDate
    Date updatedDate
    boolean isQuestionActive
}
