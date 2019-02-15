package com.abc

class Quiz {
    static belongsTo = [tenant:Tenant]
    static hasMany = [quizQuestions : QuizQuestion]

    static constraints = {
        name nullable: false, blank:false
    }

    String name
    Date dateCreated
    Date lastUpdated
    boolean isQuizActive = true
}
