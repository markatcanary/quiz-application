package com.abc

import grails.converters.JSON
import groovy.json.JsonSlurper

class QuestionController {

    QuestionService questionService
    public QuestionController ( ) { }

    def index() { }

    def show = {
        Tenant tenant = Tenant.findByName("VC")
        def questionResult
        if(params.id) {
            questionResult = questionService.getQuestionById(Long.valueOf(params.id))
        } else {
            def includesList = getIncludeParams(params)
            String include = includesList.size() > 0 ? includesList[0] : null
            questionResult = questionService.getQuestionList(Long.valueOf(params.quizId), include, tenant)
        }
        render questionResult as JSON
    }

    def save = {
        def jsonObj = request.JSON
        def tenant = Tenant.findByName("VC")
        QuizQuestion quizQuestionInput = inputQuizQuestionCmd(jsonObj)
        questionService.createQuestion(quizQuestionInput, Long.valueOf(params.id), tenant)
        def result = new SuccessCmd()
        render result as JSON
    }

    def update = {
        def jsonObj = request.JSON
        def tenant = Tenant.findByName("VC")
        QuizQuestion quizQuestionInput = inputQuizQuestionCmd(jsonObj)
        def updateQuestion = questionService.updateQuestion(quizQuestionInput, Long.valueOf(params.id), tenant)
        render updateQuestion as JSON
    }

    def delete = {
        def tenant = Tenant.findByName("VC")
        def deleteQuestion = questionService.deleteQuestion(Long.valueOf(params.id), tenant)
        render deleteQuestion as JSON
    }

    static def getStringProperty(def jsonObj, String property) {
        String value
        if (jsonObj.containsKey(property)) {
            value = jsonObj[property]
            if (!value || value == "" || value.isEmpty()) {
                value = null
            }
            return value
        }
        return value
    }

    static def getListProperty(def jsonObj) {
        return jsonObj.collect { it }
    }

    def getIncludeParams(params) {
        def include = []
        if (params.containsKey('include')) {
            include = params.include.split(",")
        }
        return include
    }

    def inputQuizQuestionCmd(def jsonObj){
        def jsonObjectOptions = jsonObj.optionsList.options
        boolean isQuestionActive = getStringProperty(jsonObj, "isQuestionActive")
        String question = getStringProperty(jsonObj, "question")
        String questionType = getStringProperty(jsonObj, "questionType")
        def optionsList = getListProperty(jsonObjectOptions)
        QuizQuestion quizQuestion = new QuizQuestion()
        quizQuestion.isQuestionActive = isQuestionActive
        quizQuestion.question = question
        quizQuestion.questionType = QuizQuestion.QuestionType.valueOf(questionType)
        ArrayList<QuizQuestionOption> quizQuestionOptionList = new ArrayList<QuizQuestionOption>()
        optionsList.each{
            def options = inputQuizQuestionOptionsCmd(it)
            quizQuestionOptionList.add(options)
        }
        quizQuestion.quizQuestionOptions = quizQuestionOptionList
        return quizQuestion
    }

    def inputQuizQuestionOptionsCmd(String quizQuestionOption){
        QuizQuestionOption questionOption = new QuizQuestionOption()
        questionOption.option = quizQuestionOption.toString()
        return questionOption
    }

}
