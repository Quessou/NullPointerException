package modelProject

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
//import java.time.LocalDateTime;

import org.springframework.security.access.annotation.Secured

@Transactional(readOnly = true)
	@Secured('permitAll')
class QuestionController {


	  static defaultAction = "index"
    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Question.list(params), model:[questionCount: Question.count()]
    }

    def show(Question question) {
        respond question
    }

    def create() {
        respond new Question(params)
    }


  @Secured('permitAll')
	def display(Question question) {
		return [ question : question ]
	}

    @Transactional
    def save(Question question) {
        if (question == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (question.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond question.errors, view:'create'
            return
        }

        question.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'question.label', default: 'Question'), question.id])
                redirect question
            }
            '*' { respond question, [status: CREATED] }
        }
    }

    def edit(Question question) {
        respond question
    }

    @Transactional
    def update(Question question) {
        if (question == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (question.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond question.errors, view:'edit'
            return
        }

        question.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'question.label', default: 'Question'), question.id])
                redirect question
            }
            '*'{ respond question, [status: OK] }
        }
    }

    @Transactional
    def delete(Question question) {

        if (question == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        question.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'question.label', default: 'Question'), question.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'question.label', default: 'Question'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }

    @Secured(["ROLE_OPERATOR", "ROLE_USER"])
    @Transactional
    def addAnswer(){
      Question question = Question.get(params.questionId);
      Answer answer = new Answer([text : params.answerField , question : question, secureUser : SecureUser.get(params.userId)]);
      println "id :"
      println params.questionId
      println answer
     Date date = new Date();
     println date
    answer.setDate(date)
//      println question.text
      answer.save();
            println answer
      question.answers.add(answer);
    question.save();
    redirect(uri : "/question/display/"+question.id)

    }


@Secured(["ROLE_ADMIN", "ROLE_USER"])
@Transactional
    def askQuestion(){}

    @Secured(["ROLE_ADMIN", "ROLE_USER"])
    @Transactional
    def addQuestion(){
			  List<String> tagNames = new ArrayList<String>(Arrays.asList(params.tags.split(" ")));
				Question question = new Question([text: params.questionField , title : params.title, secureUser : getAuthenticatedUser(), date : new Date(), isLocked : false])
				for(String tagname : tagNames)
				{
					def tag = Tag.findByName(tagname);
					if(tag==null)
					{
						tag= new Tag();
						tag.name = tagname.toUpperCase();
					}
					tag.save flush:true
					question.addToTags(tag);
				}


//        Question question = new Question([text: params.questionField , title : params.title, secureUser : getAuthenticatedUser(), date : new Date(), isLocked : false])
/*
				question.text = params.questionField;
				question.title=params.title;
				question.secureUser = getAuthenticatedUser();
				question.date = new Date();
				question.isLocked=false;
*/
				question.save flush:true;

				redirect(uri : "/question/display/"+question.id)
    }

// Book.findAll("from Book as b where b.author=:author",
//             [author: 'Dan Brown'], [max: 10, offset: 5])


	@Secured('permitAll')
	def search(){
		String[] value = params.searchValue.split(" ");
    List<String> values= new ArrayList<String>(Arrays.asList(value))
    def tmp = new HashSet<Question>()
    for(String search : values)
    {
		tmp += Question.findAllByTitleLike("%${search}%")
		tmp += Question.findAllByTextLike("%${search}%")   // Si la recherche plante, tenter de virer cette ligne.
    }
	//	List<Question> questions = tmp ? tmp.list(): null;
	  for(Question question : tmp)
		{
			if(question.text.length() > 120)
			{
				question.text = question.text.substring(0,120) + "..."
			}
		}
		return [ questions : tmp, value : params.searchValue ]
	}

	@Secured('permitAll')
	def searchByTag(){
		String[] value = params.searchValue.split(" ");
		Set<Question> matchingQuestions = new HashSet<Question>();
		for(String tagName : value){
			def tag = Tag.findByName(tagName);
			if(tag!=null)
			{

			def questions = Question.findAllByTitleIsNotNull();
			println "Question size :"
			println new Integer(questions.size()).toString()
			for(Question question : questions)
			{
				if(question.tags.contains(tag))
				{
					matchingQuestions.add(question);
				}
			}
			if(matchingQuestions == null)
			{
					println "Questions à NULL"
			}
			else
			{
				println (new Integer(matchingQuestions.size()).toString());
			}
			}

		}
		return [questions : matchingQuestions]
	}

    @Secured('ROLE_USER')
    def editQuestion()
    {
        return [question : Question.get(params.questionId)]
    }

    @Secured(["ROLE_ADMIN","ROLE_USER"])
    @Transactional
    def editOk()
    {
        Question question = Question.get(params.questionId);
        question.text=params.questionField;
        question.title=params.title;
        question.save();
        redirect(uri : "/question/display/"+question.id)
    }

   // @Secured("ROLE_ADMIN")
        @Secured("ROLE_ADMIN")
         @Transactional
    def lock()
    {
        Question question = Question.get(params.questionId);
        question.isLocked=true;
        question.save();
        //redirect(action: "display", controller: "question", params: [questionId : Question.get(params.questionId)])
        redirect(uri : "/question/display/"+question.id)
        /*
        println "On passe dans le lock"
        println question.id
        println question.isLocked
        */
}


 //   @Secured("ROLE_ADMIN")
       @Secured("ROLE_ADMIN")
        @Transactional
    def unlock()
    {
        Question question = Question.get(params.questionId);
        question.isLocked=false;
        question.save();
        redirect(uri : "/question/display/"+question.id)

    }

}
