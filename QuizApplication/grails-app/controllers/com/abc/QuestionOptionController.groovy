package com.abc

import grails.converters.JSON

class QuestionOptionController {
    def questionOptionService
    def index() { }
    def save (QuestionCmd questionCmd){
        def newquestionCmd = questionOptionService.saveQuizQuestionOption(questionCmd)
        render newquestionCmd as JSON

    }
}
