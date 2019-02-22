package com.abc

import grails.converters.JSON

class QuizQuestionController {
  def beforeInterceptor = [action: this.&load]

  QuizQuestionService quizQuestionService
  def quizService

  def show = {
    def questionResult
    if (params.qid) {
      questionResult = quizQuestionService.getQuestion(params.quiz, params.qid)
    } else {
      def includes = CanaryUtils.getIncludeParamsString(params)
      questionResult = quizQuestionService.getQuestionList(params.quiz, includes)
    }
    render questionResult as JSON
  }

  def save = {
    def jsonObj = request.JSON
    QuestionCmd questionCmd = quizQuestionService.buildQuizQuestionCreateCmd(jsonObj)
    def result = quizQuestionService.createQuestion(questionCmd, params.quiz, params.tenant)
    render result as JSON
  }

  def update = {
    if(!params.qid) {
      throw new Exception("No valid question found")
    }
    def jsonObj = request.JSON
    QuestionCmd questionCmd = quizQuestionService.buildQuizQuestionCreateCmd(jsonObj)
    def updateQuestion = quizQuestionService.updateQuestion(questionCmd, params.qid, params.quiz)
    render updateQuestion as JSON
  }

  def delete = {
    if(!params.qid) {
      throw new Exception("No valid question found")
    }
    def result = quizQuestionService.deleteQuestion(params.qid, params.quiz)
    render result as JSON
  }

  private load() {
    Tenant tenant = Tenant.findByName("VC")
    params.tenant = tenant
    params.quiz = quizService.getQuiz(params.id, tenant)
  }
}

