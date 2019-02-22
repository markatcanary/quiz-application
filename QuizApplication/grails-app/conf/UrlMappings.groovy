import com.abc.QuizQuestionController

class UrlMappings {

  static mappings = {
    "/$controller/$action?/$id?(.$format)?" {
      constraints {
        // apply constraints here
      }
    }
    "/rest/$controller/$id?"(parseRequest: true) {
      action = [GET: "show", PUT: "update", DELETE: "delete", POST: "save"]
      constraints {
        id matches: /^[a-zA-Z0-9]+$/
      }
    }
    "/rest/quiz/$id/questions/$qid?"(parseRequest: true, controller: QuizQuestionController) {
      action = [GET: "show", PUT: "update", DELETE: "delete", POST: "save"]
      constraints {
        id matches: /^[a-zA-Z0-9]+$/
        qid matches: /^[a-zA-Z0-9]+$/
      }
    }

    "/"(view: "/index")
    "500"(view: '/error')
  }
}
