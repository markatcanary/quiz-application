package com.abc


import grails.transaction.Transactional
import grails.validation.ValidationException

@Transactional
class QuestionService {

    QuizService quizService

    def getQuestionById(long questionId){
        QuizQuestion quizQuestion = QuizQuestion.findById(questionId)
        def result =  quizService.convertToQuestionCmd(quizQuestion)
        return result
    }

    def getQuestionList(long quizId, String include, Tenant tenant){
        Quiz quizByTenant = Quiz.findByIdAndTenant(quizId, tenant)
        def questionList = getQuestionListByCondition(include, quizByTenant).collect{
            quizService.convertToQuestionCmd(it)
        }
        return questionList
    }

    def getQuestionListByCondition(String include, Quiz quizByTenant){
        boolean isConditionAbsent = include!=null && include == "all"
        boolean isQuestionActive = !isConditionAbsent && include == "inactive"
        return getQuestionByCriteria(isConditionAbsent, isQuestionActive, quizByTenant)
    }

    def getQuestionByCriteria(boolean isConditionAbsent, boolean isQuestionActive, Quiz quizByTenant){
        def c = QuizQuestion.createCriteria()
        def result = c.list {
            and {
                eq("quiz", quizByTenant)
                if(!isConditionAbsent){
                    eq("isQuestionActive", !isQuestionActive)
                }
            }
        }
        return result
    }

    def createQuestion(QuizQuestion quizQuestionInput, long quizId, Tenant tenant) {
        def result
        Quiz quizByTenant = Quiz.findByIdAndTenant(quizId, tenant)
        QuizQuestion quizQuestion = new QuizQuestion(isQuestionActive: quizQuestionInput.isQuestionActive, question: quizQuestionInput.question,
                questionType: quizQuestionInput.questionType, createdDate: new Date(), updatedDate: new Date())
        quizQuestionInput.quizQuestionOptions.option.collect {
            quizQuestionOptionInput(it, quizQuestion)
        }
        quizByTenant.addToQuizQuestions(quizQuestion)
        tenant.addToQuiz(quizByTenant)
        try {
            quizQuestion.save()
            result = quizService.convertToQuestionCmd(quizQuestion)
        } catch (ValidationException ve) {
            def msg = ve.message
            throw new Exception(msg)
        }
        return result
    }

    def updateQuestion(QuizQuestion quizQuestionInput,long questionId, Tenant tenant) {
        def result
        QuizQuestion quizQuestionExists = QuizQuestion.findById(id: questionId)
        Quiz quiz = Quiz.findByQuizQuestions(quizQuestionExists)
        quizQuestionExists.isQuestionActive = quizQuestionInput.isQuestionActive
        quizQuestionExists.question = quizQuestionInput.question
        quizQuestionExists.questionType = quizQuestionInput.questionType
        quizQuestionExists.createdDate = new Date()
        quizQuestionExists.updatedDate = new Date()
        quizQuestionInput.quizQuestionOptions.option.collect{
            quizQuestionOptionInput(it, quizQuestionExists)
        }
        quiz.addToQuizQuestions(quizQuestionExists)
        tenant.addToQuiz(quiz)
        try {
        quizQuestionExists.save()
        result = quizService.convertToQuestionCmd(quizQuestionExists)
        } catch (ValidationException ve) {
            def msg = ve.message
            throw new Exception(msg)
        }
        return result
    }

    def deleteQuestion(long questionId, Tenant tenant) {
        def result
        QuizQuestion quizQuestionExists = QuizQuestion.findById(id: questionId)
        Quiz quiz = Quiz.findByQuizQuestions(quizQuestionExists)
        quizQuestionExists.isQuestionActive = false
        quiz.addToQuizQuestions(quizQuestionExists)
        tenant.addToQuiz(quiz)
        try {
            quizQuestionExists.save()
            result = quizService.convertToQuestionCmd(quizQuestionExists)
        } catch (ValidationException ve) {
            def msg = ve.message
            throw new Exception(msg)
        }
        return result
    }

    def quizQuestionOptionInput(String it, QuizQuestion quizQuestion) {
        QuizQuestionOption quizQuestionOption = new QuizQuestionOption(option: it)
        quizQuestionOption.option = it
        quizQuestion.addToQuizQuestionOptions(quizQuestionOption)
    }

}
