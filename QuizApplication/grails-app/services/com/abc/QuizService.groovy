package com.abc

import grails.transaction.Transactional

@Transactional
class QuizService {
  def quizQuestionService

  def getQuiz(def guId, Tenant tenant) throws Exception {
    if(!guId) {
      throw new Exception("no quiz found")
    }
    def quiz = Quiz.findByIdAndTenant(guId, tenant)
    if(quiz) {
      throw new Exception("no quiz found")
    }
    quiz
  }
  def getQuizList(String include, Tenant tenant) {
    def quizList = getQuizListByCondition(include, tenant).collect {
      convertToListCmd(it)
    }
    return quizList
  }

  def getQuizListByCondition(String include, Tenant tenant) {
    boolean isConditionAbsent = include != null && include == "all"
    boolean isQuizInActive = !isConditionAbsent && include == "inactive"
    return getQuizByCriteria(isConditionAbsent, isQuizInActive, tenant)
  }

  def getQuizByCriteria(boolean isConditionAbsent, boolean isQuizInActive, Tenant tenant) {
    def c = Quiz.createCriteria()
    def result = c.list {
      and {
        eq("tenant", tenant)
        if (!isConditionAbsent) {
          eq("isQuizActive", !isQuizInActive)
        }
      }
    }
    result
  }

  def getQuizDetails(long quizId, Tenant tenant) {
    Quiz quiz = Quiz.findByIdAndTenant(quizId, tenant)
    convertToQuizDetailCmd(quiz)
  }

  def createQuiz(InputQuizCmd cmd, Tenant tenant) throws Exception {
    Quiz quiz = new Quiz(name: cmd.name, desc: cmd.desc, tenant: tenant)
    quiz.save()
    convertToListCmd(quiz)
  }

  def updateQuiz(Quiz quiz, InputQuizCmd cmd) throws Exception {
    quiz.name = cmd.name
    quiz.desc = cmd.desc
    quiz.isActive = cmd.isActive
    quiz.save()
    convertToListCmd(quiz)
  }

  def deleteQuiz(Quiz quiz) {
    quiz.isActive = false
    quiz.save()
    convertToListCmd(quiz)
  }

  def buildInputQuizCmd(def jsonObj) {
    InputQuizCmd cmd = new InputQuizCmd()
    cmd.name = jsonObj.name
    cmd.desc = jsonObj.desc
    cmd.isActive = CanaryUtils.getBooleanProp(jsonObj, "isActive") //convert this boolean later
    cmd
  }

  def convertToListCmd(Quiz quiz) {
    QuizListCmd quizListCmd = new QuizListCmd()
    quizListCmd.guId = quiz.id
    quizListCmd.name = quiz.name
    quizListCmd.desc = quiz.desc
    quizListCmd.isActive = quiz.isActive
    quizListCmd.dateCreated = CanaryUtils.toEpoch(quiz.dateCreated)
    quizListCmd.lastUpdated = CanaryUtils.toEpoch(quiz.lastUpdated)
    quizListCmd
  }

  def convertToQuizDetailCmd(Quiz quiz) {
    QuizDetailCmd quizDetailCmd = new QuizDetailCmd()
    quizDetailCmd.guId = quiz.id
    quizDetailCmd.name = quiz.name
    quizDetailCmd.desc = quiz.desc
    quizDetailCmd.isActive = quiz.isActive

    quizDetailCmd.questionList = quiz.quizQuestions.collect {
      quizQuestionService.convertToQuestionCmd(it)
    }
    quizDetailCmd.dateCreated = CanaryUtils.toEpoch(quiz.dateCreated)
    quizDetailCmd.lastUpdated = CanaryUtils.toEpoch(quiz.lastUpdated)
    quizDetailCmd
  }

}
