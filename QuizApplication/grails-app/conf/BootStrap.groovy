import com.abc.QuizQuestion
import com.abc.QuizQuestionOption
import com.abc.Tenant
import com.abc.Quiz

class BootStrap {

    def init = { servletContext ->
        Tenant tenant = Tenant.findByName("VC")
        if(!tenant){
            tenant = new Tenant(name:"VC").save()
            Quiz quiz1 = new Quiz(name:"Quiz - 1", dateCreated: new Date(), lastUpdated: new Date(), isQuizActive: true)
            Quiz quiz2 = new Quiz(name:"Quiz - 2", dateCreated: new Date(), lastUpdated: new Date(), isQuizActive: true)
            Quiz quiz3 = new Quiz(name:"Quiz - 3", dateCreated: new Date(), lastUpdated: new Date(), isQuizActive: true)
            Quiz quiz4 = new Quiz(name:"Quiz - 4", dateCreated: new Date(), lastUpdated: new Date(), isQuizActive: true)
            Quiz quiz5 = new Quiz(name:"Quiz - 5", dateCreated: new Date(), lastUpdated: new Date(), isQuizActive: false)
            Quiz quiz6 = new Quiz(name:"Quiz - 6", dateCreated: new Date(), lastUpdated: new Date(), isQuizActive: false)
            tenant.addToQuiz(quiz1)
            tenant.addToQuiz(quiz2)
            tenant.addToQuiz(quiz3)
            tenant.addToQuiz(quiz4)
            tenant.addToQuiz(quiz5)
            tenant.addToQuiz(quiz6)
            tenant.save()
            QuizQuestion quizQuestion = new QuizQuestion(questionType:QuizQuestion.QuestionType.MultiChoice,
                    question:"What is the largest river in the world ?", createdDate:new Date(), updatedDate:new Date(),
                    isQuestionActive:true)
            QuizQuestionOption quizQuestionOptions1 = new QuizQuestionOption(option:"Option 1:  Mississippi – Missouri – Jefferson, North America")
            QuizQuestionOption quizQuestionOptions2 = new QuizQuestionOption(option:"Option 2:  Yangtze River, China")
            QuizQuestionOption quizQuestionOptions3 = new QuizQuestionOption(option:"Option 3:  Amazon River, South America")
            QuizQuestionOption quizQuestionOptions4 = new QuizQuestionOption(option:"Option 4:  Nile River, North-East Africa")
            quizQuestion.addToQuizQuestionOptions(quizQuestionOptions1)
            quizQuestion.addToQuizQuestionOptions(quizQuestionOptions2)
            quizQuestion.addToQuizQuestionOptions(quizQuestionOptions3)
            quizQuestion.addToQuizQuestionOptions(quizQuestionOptions4)
            QuizQuestionOption answerOption = new QuizQuestionOption(option:"Option 4:  Nile River, North-East Africa")
            answerOption.save()
            quizQuestion.answer = answerOption
            quizQuestion.save()
            quiz1.addToQuizQuestions(quizQuestion)
            quiz1.save()
        }
    }
    def destroy = {

    }
}