package com.abc

import grails.transaction.Transactional

@Transactional
class QuestionOptionService {
  def quizQuestionService

  def saveQuizQuestionOption(QuestionCmd questionCmd) {
    def tenant = Tenant.findByName("VC")
    def quizQuestion = new QuizQuestion()
    quizQuestion.question = questionCmd.question
    quizQuestion.questionType = QuizQuestion.QuestionType.valueOf(questionCmd.questionType)
    quizQuestion.isActive = questionCmd.isActive
    questionCmd.optionsList.each { quizQuestionOption ->
      quizQuestion.addToQuizQuestionOptions(quizQuestionOption)
    }
    def answerOption = new QuizQuestionOption(name: questionCmd.quizSelectedOption)
    quizQuestion.answer = answerOption
    questionCmd = quizQuestionService.createQuestion(quizQuestion, Long.valueOf(questionCmd.guId), tenant)
    return questionCmd
  }
}
