package modelProject

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional


import org.springframework.security.access.annotation.Secured

@Transactional(readOnly = true)
class SecureUserAnswerController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]
    @Secured('permitAll')
    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond SecureUserAnswer.list(params), model:[secureUserAnswerCount: SecureUserAnswer.count()]
    }

    @Secured('permitAll')
    def show(SecureUserAnswer secureUserAnswer) {
        respond secureUserAnswer
    }

    def create() {
        respond new SecureUserAnswer(params)
    }

    @Transactional
    def save(SecureUserAnswer secureUserAnswer) {
        if (secureUserAnswer == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (secureUserAnswer.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond secureUserAnswer.errors, view:'create'
            return
        }

        secureUserAnswer.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'secureUserAnswer.label', default: 'SecureUserAnswer'), secureUserAnswer.id])
                redirect secureUserAnswer
            }
            '*' { respond secureUserAnswer, [status: CREATED] }
        }
    }

    def edit(SecureUserAnswer secureUserAnswer) {
        respond secureUserAnswer
    }

    @Transactional
    def update(SecureUserAnswer secureUserAnswer) {
        if (secureUserAnswer == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (secureUserAnswer.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond secureUserAnswer.errors, view:'edit'
            return
        }

        secureUserAnswer.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'secureUserAnswer.label', default: 'SecureUserAnswer'), secureUserAnswer.id])
                redirect secureUserAnswer
            }
            '*'{ respond secureUserAnswer, [status: OK] }
        }
    }

    @Transactional
    def delete(SecureUserAnswer secureUserAnswer) {

        if (secureUserAnswer == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        secureUserAnswer.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'secureUserAnswer.label', default: 'SecureUserAnswer'), secureUserAnswer.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'secureUserAnswer.label', default: 'SecureUserAnswer'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
