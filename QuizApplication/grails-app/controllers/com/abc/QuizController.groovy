package com.abc

import grails.converters.JSON

class QuizController {
    QuizService quizService

    def index() {}

    def show = {
        Tenant tenant = Tenant.findByName("VC")
        def quizResult
        if(params.id){
            quizResult = quizService.getQuizById(Long.valueOf(params.id), tenant)
        } else {
            def includesList = getIncludeParams(params)
            String include = includesList.size() > 0 ? includesList[0] : null
            quizResult = quizService.getQuizList(include, tenant)
        }
        render quizResult as JSON
    }

    def save = {
        def jsonObj = request.JSON
        String quizName = getStringProperty(jsonObj, "name")
        def tenant = Tenant.findByName("VC")
        quizService.createQuiz(quizName, tenant)
        def result = new SuccessCmd()
        render result as JSON
    }

    def update = {
        def jsonObj = request.JSON
        String quizName = getStringProperty(jsonObj, "name")
        def tenant = Tenant.findByName("VC")
        InputQuizCmd inputQuizCmd = new InputQuizCmd()
        inputQuizCmd.guId = params.id
        inputQuizCmd.name = quizName
        quizService.updateQuiz(inputQuizCmd, tenant)
        def result = new SuccessCmd()
        render result as JSON
    }

    def delete = {
        if (params.id != null) {
            def tenant = Tenant.findByName("VC")
            InputQuizCmd inputQuizCmd = new InputQuizCmd()
            inputQuizCmd.guId = params.id
            def deleteQuiz = quizService.deleteQuiz(inputQuizCmd, tenant)
            render deleteQuiz
        }
    }

    static String getStringProperty(def jsonObj, String property) {
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

    def getIncludeParams(params) {
        def includes = []
        if (params.containsKey('include')) {
            includes = params.include.split(",")
        }
        return includes
    }
}

class SuccessCmd{
    String success = "Success"
}

class QuizResultCmdById {
    String guId
    String name
    List<QuestionCmd> questionList
    Date createdDate
    Date updateDate
    boolean isQuizActive
}

class QuizResultCmd {
    String guId
    String name
    Date createdDate
    Date updateDate
    boolean isQuizActive
}

class QuestionCmd {
    String guId
    String question
    String questionType
    List<QuizQuestionOption> optionsList
    String quizSelectedOption
    Date createdDate
    Date updateDate
    boolean isQuestionActive
}

class QuizQuestionOptionCmd {
    String guId
    String options
}

class InputQuizCmd{
    String guId
    String name
}