package modelProject

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional


import org.springframework.security.access.annotation.Secured

@Transactional(readOnly = true)
@Secured('permitAll')
class AnswerController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
		println 'Hello'
        params.max = Math.min(max ?: 10, 100)
        respond Answer.list(params), model:[answerCount: Answer.count()]
    }

    def show(Answer answer) {
        respond answer
    }

    def create() {
        respond new Answer(params)
    }

    @Transactional
    def save(Answer answer) {
        if (answer == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (answer.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond answer.errors, view:'create'
            return
        }

        answer.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'answer.label', default: 'Answer'), answer.id])
                redirect answer
            }
            '*' { respond answer, [status: CREATED] }
        }
    }

    def edit(Answer answer) {
        respond answer
    }

    @Transactional
    def update(Answer answer) {
        if (answer == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (answer.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond answer.errors, view:'edit'
            return
        }

        answer.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'answer.label', default: 'Answer'), answer.id])
                redirect answer
            }
            '*'{ respond answer, [status: OK] }
        }
    }

    @Transactional
    def delete(Answer answer) {

        if (answer == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        answer.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'answer.label', default: 'Answer'), answer.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'answer.label', default: 'Answer'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }

    @Secured(["ROLE_ADMIN","ROLE_USER"])
    def editAnswer()
    {
        return [answer : Answer.get(params.answerId)]
    }

    @Secured(["ROLE_ADMIN","ROLE_USER"])
    @Transactional
    def editOk()
    {
        Answer answer = Answer.get(params.answerId);
        answer.text=params.answerField;
        answer.save();
        redirect(uri : "/question/display/"+answer.question.id)
    }


    @Secured("ROLE_USER")
    @Transactional
    def upvote(int userId, int answerId)
    {
      boolean saveua=false;
        SecureUser user = SecureUser.get(userId);
        Answer answer = Answer.get(answerId);
        SecureUserAnswer ua = SecureUserAnswer.findBySecureUserAndAnswer(user,answer)
        if(ua == null)
        {

            ua = new SecureUserAnswer([secureUser: user, answer: answer, isUpvote: true, isDownvote: false])
            answer.mark++
            ua.save flush:true
            println "Upvote : ua created"
        }
        else
        {
            if(ua.isUpvote)
            {
                answer.mark--
                ua.delete flush:true
            }
            if(ua.isDownvote)
            {
                answer.mark+=2
            //    ua.isUpvote= true;
            //    ua.isDownvote= false;
            ua.delete flush:true
            ua = new SecureUserAnswer([secureUser: user, answer: answer, isUpvote: true, isDownvote: false])

                ua.save flush:true
                println "Upvote : ua modified"
            }

        }

        answer.save();
        redirect(uri : "/question/display/"+answer.question.id)
    }


    @Secured("ROLE_USER")
    @Transactional
    def downvote(int userId, int answerId)
    {
      boolean saveua = false;
      SecureUser user = SecureUser.get(userId);
      Answer answer = Answer.get(answerId);
      SecureUserAnswer ua = SecureUserAnswer.findBySecureUserAndAnswer(user,answer)
      if(ua == null)
      {
        
          ua = new SecureUserAnswer([secureUser: user, answer: answer, isUpvote: false, isDownvote: true])
          answer.mark--
          ua.save flush:true
          println "Downvote : ua created"
      }
      else
      {
          if(ua.isDownvote)
          {
              answer.mark++
              ua.delete flush:true
              println "Downvote : ua deleted"
          }
          if(ua.isUpvote)
          {
              answer.mark = answer.mark - 2;
//              ua.isUpvote= false;
//              ua.isDownvote= true;
            //  println ua.isDownvote
            ua.delete flush:true
            ua = new SecureUserAnswer([secureUser: user, answer: answer, isUpvote: false, isDownvote: true])

              ua.save flush:true
              println "Downvote : ua modified"
          }

      }

      answer.save flush:true
      redirect(uri : "/question/display/"+answer.question.id)
    }
}
