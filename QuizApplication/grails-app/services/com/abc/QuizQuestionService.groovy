package com.abc

import grails.transaction.Transactional

@Transactional
class QuizQuestionService {
  static String ALL_QUESTIONS = "all"

  def getQuestion(Quiz quiz, def questionId) throws Exception {
    convertToQuestionCmd(QuizQuestion.findByQuizAndId(quiz, questionId))
  }

  def getQuestionList(Quiz quiz, String includes) {
    def questionList = getQuestionListByCondition(includes, quiz).collect {
      convertToQuestionCmd(it)
    }
    questionList
  }

  def getQuestionListByCondition(String include, Quiz quiz) {
    boolean isConditionPresent = include != null && ( include == "all" || include == "active")
    def qList = isConditionPresent ? listQuestionsByCondition(quiz, include) : listQuestionsByRandom(quiz)
    qList.collect { convertToQuestionCmd(it) }
  }

  def buildQuizQuestionCreateCmd(def jsonObj) {
    QuestionCmd cmd = new QuestionCmd()
    cmd.name = CanaryUtils.getStringProperty(jsonObj, "name")
    cmd.questionType = CanaryUtils.getStringProperty(jsonObj, "questionType")
    cmd.isActive = CanaryUtils.getBooleanProp(jsonObj, "isQuestionActive")
    def optionsList = CanaryUtils.getListProperty(jsonObj, "options")
    cmd.optionsList = optionsList.collect { buildOptionCmd(it) }
    cmd.answer = buildOptionCmd(jsonObj.answer)
    cmd
  }

  def buildOptionCmd(def jsonOption) {
    QuizQuestionOptionCmd cmd = new QuizQuestionOptionCmd()
    cmd.guId = CanaryUtils.getStringProperty(jsonOption.guId)
    cmd.name = CanaryUtils.getStringProperty(jsonOption, "name")
    cmd
  }

  private def listQuestionsByCondition(Quiz quiz, String include)  {
    boolean includeCondition = include != ALL_QUESTIONS
    def c = QuizQuestion.createCriteria()
    def result = c.list {
      and {
        eq("quiz", quiz)
        if (includeCondition) {
          eq("isActive", true)
        }
      }
    }
    result
  }

  private def listQuestionsByRandom(Quiz quiz) {
    def questionList = queryRandomList(quiz)
    Collections.shuffle(questionList)
    questionList.take(quiz.questionCount)
  }

  private def queryRandomList(Quiz quiz) {
    int randomOffset= getRandomOffset(quiz)
    int max = quiz.questionCount * 2

    def c = QuizQuestion.createCriteria()
    def result = c.list {
      maxResults (max)
      firstResult(randomOffset)
      and {
        eq("quiz", quiz)
        eq("isActive", true)
      }
    }
    result
  }

  private getRandomOffset(Quiz quiz) {
    int offset
    int rowCount = QuizQuestion.countByQuiz(quiz)
    int max = rowCount - quiz.questionCount
    offset = max < 0 ? 0 : new Random().nextInt(max + 1)
    offset
  }


  def createQuestion(QuestionCmd cmd, Quiz quiz) {
    QuizQuestion question = new QuizQuestion()
    question.isActive = cmd.isActive
    question.name = cmd.name
    question.quiz = quiz
    question.save()

    cmd.optionsList.each {
      question.addToOptions(createQuestionOption(it))
    }
    question.save()
    convertToQuestionCmd(question)
  }

  def updateQuestion(QuestionCmd cmd, long questionId, Quiz quiz) throws Exception {
    QuizQuestion question = QuizQuestion.findByIdAndQuiz(questionId, quiz)
    if(!question) {
      throw new Exception("Invalid question")
    }
    question.isActive = cmd.isActive
    question.name = cmd.name
    question.options = cmd.optionsList.each {
      updateQuestionOption(question, it)
    }
    question.answer = updateQuestionOption(question, cmd.answer)
    question.save()
    convertToQuestionCmd(question)
  }

  private updateQuestionOption(QuizQuestion question, QuizQuestionOptionCmd cmd) {
    if(cmd && cmd.guId) {
      QuizQuestionOption option = QuizQuestionOption.findByIdAndQuizQuestion(cmd.guId, question)
      if(!option) {
        throw new Exception("Invalid question option found")
      }
      option.name = cmd.name
      option.save()
    } else {
      QuizQuestionOption option = new QuizQuestionOption(quizQuestion: question)
      option.name = cmd.name
      option.save()
    }
  }

  private def createQuestionOption(QuizQuestionOptionCmd cmd) {
    QuizQuestionOption option = new QuizQuestionOption()
    option.name = cmd.name
    option
  }


  def deleteQuestion(def qid, Quiz quiz) throws Exception{
    QuizQuestion quizQuestion = QuizQuestion.findByIdAndQuiz(qid, quiz)
    if(!quizQuestion) {
      throw new Exception("invalid question")
    }
    quizQuestion.isActive = false
    quizQuestion.save()
    convertToQuestionCmd(quizQuestion)
  }

  def convertToQuestionCmd(QuizQuestion quizQuestion) {
    QuestionCmd questionCmd = new QuestionCmd()
    questionCmd.guId = quizQuestion.id
    questionCmd.name = quizQuestion.name
    questionCmd.questionType = quizQuestion.questionType.toString()
    questionCmd.isActive = quizQuestion.isActive
    questionCmd.optionsList = quizQuestion.options.collect { convertToOptionCmd(it) }
    questionCmd.answer = convertToOptionCmd(quizQuestion.answer)
    questionCmd.dateCreated = CanaryUtils.toEpoch(quizQuestion.dateCreated)
    questionCmd.lastUpdated = CanaryUtils.toEpoch(quizQuestion.lastUpdated)
    questionCmd
  }

  def convertToOptionCmd(QuizQuestionOption option) {
    if(!option) return null
    QuizQuestionOptionCmd quizQuestionOptionCmd = new QuizQuestionOptionCmd()
    quizQuestionOptionCmd.guId = option.id
    quizQuestionOptionCmd.name = option.name
    quizQuestionOptionCmd
  }
}
