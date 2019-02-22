package com.abc

import grails.converters.JSON

class QuizController {
  def beforeInterceptor = [action: this.&load]

  QuizService quizService

  def index() {}

  def show = {
    def quizResult
    if (params.id) {
      quizResult = quizService.getQuizDetails(Long.valueOf(params.id), params.tenant)
    } else {
      def include = CanaryUtils.getIncludeParamsString(params)
      quizResult = quizService.getQuizList(include, params.tenant)
    }
    render quizResult as JSON
  }

  def save = {
    def jsonObj = request.JSON
    InputQuizCmd inputQuizCmd = quizService.buildInputQuizCmd(jsonObj)

    def result = quizService.createQuiz(inputQuizCmd, params.tenant)
    render result as JSON
  }

  def update = {
    def jsonObj = request.JSON
    InputQuizCmd inputQuizCmd = quizService.buildInputQuizCmd(jsonObj)
    def result = quizService.updateQuiz(params.quiz, inputQuizCmd)
    render result as JSON
  }

  def delete = {
    if (params.id != null) {
      def deleteQuiz = quizService.deleteQuiz(params.quiz)
      render deleteQuiz
    }
  }

  private load() {
    Tenant tenant = Tenant.findByName("VC")
    params.tenant = tenant
    if(params.id) {
      params.quiz = quizService.getQuiz(params.id, tenant)
    }
  }
}

class QuizDetailCmd {
  String guId
  String name
  String desc
  boolean isActive
  List<QuestionCmd> questionList
  def dateCreated
  def lastUpdated
}

class QuizListCmd {
  String guId
  String name
  String desc
  boolean isActive
  def dateCreated
  def lastUpdated
}

class QuestionCmd {
  String guId
  String name
  String questionType
  boolean isActive
  List<QuizQuestionOptionCmd> optionsList
  QuizQuestionOptionCmd answer
  def dateCreated
  def lastUpdated
}

class QuizQuestionOptionCmd {
  String guId
  String name
}

class InputQuizCmd {
  String name
  String desc
  boolean isActive
}