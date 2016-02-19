package modelProject

import java.util.*;
import grails.plugin.springsecurity.annotation.Secured

@Secured('permitAll')
class HomeController {

  static defaultAction="index"

    def index() {
	List<Question> questions = Question.list(max:10)
	return [questions : questions]
	}
}
