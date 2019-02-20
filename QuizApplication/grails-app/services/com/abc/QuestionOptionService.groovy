package com.abc

import grails.transaction.Transactional

@Transactional
class QuestionOptionService {
    def questionService

    def saveQuizQuestionOption(QuestionCmd questionCmd) {
        def tenant = Tenant.findByName("VC")
        def quizQuestion = new QuizQuestion()
        quizQuestion.question = questionCmd.question
        quizQuestion.questionType = QuizQuestion.QuestionType.valueOf(questionCmd.questionType)
        quizQuestion.isQuestionActive = questionCmd.isQuestionActive
        questionCmd.optionsList.each { quizQuestionOption ->
            quizQuestion.addToQuizQuestionOptions(quizQuestionOption)
        }
        def answerOption = new QuizQuestionOption(option: questionCmd.quizSelectedOption)
        quizQuestion.answer = answerOption
        questionCmd = questionService.createQuestion(quizQuestion, Long.valueOf(questionCmd.guId), tenant)
        return questionCmd
    }
}
