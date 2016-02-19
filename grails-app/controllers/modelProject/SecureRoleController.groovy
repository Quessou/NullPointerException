package modelProject

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

import org.springframework.security.access.annotation.Secured

@Transactional(readOnly = true)
class SecureRoleController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond SecureRole.list(params), model:[secureRoleCount: SecureRole.count()]
    }

    def show(SecureRole secureRole) {
        respond secureRole
    }

    def create() {
        respond new SecureRole(params)
    }

    @Transactional
    def save(SecureRole secureRole) {
        if (secureRole == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (secureRole.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond secureRole.errors, view:'create'
            return
        }

        secureRole.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'secureRole.label', default: 'SecureRole'), secureRole.id])
                redirect secureRole
            }
            '*' { respond secureRole, [status: CREATED] }
        }
    }

    def edit(SecureRole secureRole) {
        respond secureRole
    }

    @Transactional
    def update(SecureRole secureRole) {
        if (secureRole == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (secureRole.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond secureRole.errors, view:'edit'
            return
        }

        secureRole.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'secureRole.label', default: 'SecureRole'), secureRole.id])
                redirect secureRole
            }
            '*'{ respond secureRole, [status: OK] }
        }
    }

    @Transactional
    def delete(SecureRole secureRole) {

        if (secureRole == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        secureRole.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'secureRole.label', default: 'SecureRole'), secureRole.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'secureRole.label', default: 'SecureRole'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
