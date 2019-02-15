package com.abc

import grails.transaction.Transactional
import grails.validation.ValidationException

@Transactional
class QuizService {

    def getQuizList(String include, Tenant tenant) {
        def quizList = getQuizListByCondition(include, tenant).collect{
            convertToQuizCmd(it)
        }
        return quizList
    }

    def getQuizListByCondition(String include, Tenant tenant){
        boolean isConditionAbsent = include!=null && include == "all"
        boolean isQuizInActive = !isConditionAbsent && include == "inactive"
        return getQuizByCriteria(isConditionAbsent, isQuizInActive, tenant)
    }

    def getQuizByCriteria(boolean isConditionAbsent, boolean isQuizInActive, Tenant tenant){
        def c = Quiz.createCriteria()
        def result = c.list {
            and {
                eq("tenant", tenant)
                if(!isConditionAbsent){
                    eq("isQuizActive", !isQuizInActive)
                }
            }
        }
        return result
    }

    def getQuizById(long quizId, Tenant tenant) {
        Quiz quiz = Quiz.findByIdAndTenant(quizId, tenant)
        def result =  convertToQuizCmdById(quiz)
        return result
    }

    def createQuiz(String quizName, Tenant tenant) throws Exception{
        Quiz quiz = new Quiz(name:quizName, tenant: tenant, dateCreated: new Date(), lastUpdated: new Date())
        def result
        try {
            quiz.save()
            result =  convertToQuizCmd(quiz)
        } catch (ValidationException ve) {
            def msg = ve.message
            throw new Exception(msg)
        }
        return result
    }

    def updateQuiz(InputQuizCmd quizCommand, Tenant tenant) throws  Exception{
        def updateResponse
        Quiz quiz = Quiz.findByIdAndTenant(Long.valueOf(quizCommand.guId), tenant)
        quiz.name = quizCommand.name
        try{
            updateResponse = quiz.save()
        } catch (ValidationException ve) {
            def msg = ve.message
            throw new Exception(msg)
        }
        return updateResponse!=null ? "Quiz updated successfully" : "Quiz not updated, please try again"
    }

    def deleteQuiz(InputQuizCmd quizCommand, Tenant tenant){
        def deleteResponse
        Quiz quiz = Quiz.findByIdAndTenant(Long.valueOf(quizCommand.guId), tenant)
        quiz.isQuizActive = false
        try{
            deleteResponse = quiz.save()
        } catch (ValidationException ve) {
            def msg = ve.message
            throw new Exception(msg)
        }
        return deleteResponse!=null ? "Quiz deleted successfully" : "Quiz not deleted, please try again"
    }

    def convertToQuizCmd(Quiz quiz) {
        QuizResultCmd quizResultCmd = new QuizResultCmd()
        quizResultCmd.guId = quiz.id
        quizResultCmd.name = quiz.name
        quizResultCmd.createdDate = quiz.dateCreated
        quizResultCmd.updateDate = quiz.lastUpdated
        quizResultCmd.isQuizActive = quiz.isQuizActive
        return quizResultCmd
    }

    def convertToQuizCmdById(Quiz quiz) {
        QuizResultCmdById quizResultCmdById = new QuizResultCmdById()
        quizResultCmdById.guId = quiz.id
        quizResultCmdById.name = quiz.name
        quizResultCmdById.questionList = quiz.quizQuestions.collect {
            convertToQuestionCmd(it)
        }
        quizResultCmdById.createdDate = quiz.dateCreated
        quizResultCmdById.updateDate = quiz.lastUpdated
        quizResultCmdById.isQuizActive = quiz.isQuizActive
        return quizResultCmdById
    }

    def convertToQuestionCmd(QuizQuestion quizQuestion){
        QuestionCmd questionCmd = new QuestionCmd()
        questionCmd.guId = quizQuestion.id
        questionCmd.question = quizQuestion.question
        questionCmd.questionType = quizQuestion.questionType.toString()
        questionCmd.optionsList = quizQuestion.quizQuestionOptions.collect{
            convertToOptionCmd(it)
        }
        questionCmd.quizSelectedOption = quizQuestion.answer.option
        questionCmd.createdDate = quizQuestion.createdDate
        questionCmd.updateDate = quizQuestion.updatedDate
        questionCmd.isQuestionActive = quizQuestion.isQuestionActive
        return questionCmd
    }

    def convertToOptionCmd(QuizQuestionOption quizQuestionOptions){
        QuizQuestionOptionCmd quizQuestionOptionCmd = new QuizQuestionOptionCmd()
        quizQuestionOptionCmd.guId = quizQuestionOptions.id
        quizQuestionOptionCmd.options = quizQuestionOptions.option
        return  quizQuestionOptionCmd
    }

}
