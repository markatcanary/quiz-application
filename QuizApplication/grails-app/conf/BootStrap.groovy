import com.abc.QuizQuestion
import com.abc.QuizQuestionOption
import com.abc.Tenant
import com.abc.Quiz

class BootStrap {

  def init = { servletContext ->
    Tenant tenant = Tenant.findByName("VC")
    if (!tenant) {
      tenant = new Tenant(name: "VC").save()
      Quiz quiz1 = new Quiz(name: "Quiz - 1", desc: "sample desc1", tenant: tenant).save()
      Quiz quiz2 = new Quiz(name: "Quiz - 2", desc: "sample desc2", tenant: tenant).save()
      Quiz quiz3 = new Quiz(name: "Quiz - 3", desc: "sample desc3", tenant: tenant).save()

      QuizQuestion quizQuestion =
        new QuizQuestion(
          questionType: QuizQuestion.QuestionType.MultiChoice,
          name: "What is the largest river in the world ?",
          active: true,
          quiz: quiz1)
      quizQuestion.save()

      QuizQuestionOption quizQuestionOptions1 = new QuizQuestionOption(name: "Option 1:  Mississippi – Missouri – Jefferson, North America")
      QuizQuestionOption quizQuestionOptions2 = new QuizQuestionOption(name: "Option 2:  Yangtze River, China")
      QuizQuestionOption quizQuestionOptions3 = new QuizQuestionOption(name: "Option 3:  Amazon River, South America")
      QuizQuestionOption quizQuestionOptions4 = new QuizQuestionOption(name: "Option 4:  Nile River, North-East Africa")
      quizQuestion.addToOptions(quizQuestionOptions1)
      quizQuestion.addToOptions(quizQuestionOptions2)
      quizQuestion.addToOptions(quizQuestionOptions3)
      quizQuestion.addToOptions(quizQuestionOptions4)
      quizQuestion.answer = quizQuestionOptions4
      quizQuestion.save()

      quiz1.save()

      println "value is printed"
    }
  }
  def destroy = {

  }
}