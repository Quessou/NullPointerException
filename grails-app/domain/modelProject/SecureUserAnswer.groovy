package modelProject


import grails.gorm.DetachedCriteria
import groovy.transform.ToString

import org.apache.commons.lang.builder.HashCodeBuilder


class SecureUserAnswer implements Serializable{

  private static final long serialVersionUID = 1


    SecureUser secureUser
    Answer answer
    boolean isUpvote
    boolean isDownvote

    SecureUserAnswer(SecureUser u, Answer a)
    {
        this()
        secureUser=u;
        answer=a;

    }


    @Override
  	boolean equals(other) {
  		if (!(other instanceof SecureUserAnswer)) {
  			return false
  		}

  		other.secureUser?.id == secureUser?.id && other.answer?.id == answer?.id
  	}

    @Override
  	int hashCode() {
  		def builder = new HashCodeBuilder()
  		if (secureUser) builder.append(secureUser.id)
  		if (answer) builder.append(answer.id)
  		builder.toHashCode()
  	}

  	static SecureUserSecureRole get(long secureUserId, long answerId) {
  		criteriaFor(secureUserId, answerId).get()
  	}

  	static boolean exists(long secureUserId, long answerId) {
  		criteriaFor(secureUserId, answerId).count()
  	}


    	private static DetachedCriteria criteriaFor(long secureUserId, long answerId) {
    		SecureUserAnswer.where {
    			secureUser == SecureUser.load(secureUserId) &&
    			answer == Answer.load(answerId)
    		}
    	}

    	static SecureUserAnswer create(SecureUser secureUser, Answer answer, boolean flush = false) {
    		def instance = new SecureUserAnswer(secureUser: secureUser, answer : answer)
    		instance.save(flush: flush, insert: true)
    		instance
    	}


      static boolean remove(SecureUser u, Answer a, boolean flush = false) {
    		if (u == null || a == null) return false

    		int rowCount = SecureUserAnswer.where { secureUser == u && answer == a }.deleteAll()

    		if (flush) { SecureUserAnswer.withSession { it.flush() } }

    		rowCount
    	}

    	static void removeAll(SecureUser u, boolean flush = false) {
    		if (u == null) return

    		SecureUserAnswer.where { secureUser == u }.deleteAll()

    		if (flush) { SecureUserAnswer.withSession { it.flush() } }
    	}

    	static void removeAll(Answer a, boolean flush = false) {
    		if (a == null) return

    		SecureUserAnswer.where { answer == a }.deleteAll()

    		if (flush) { SecureUserAnswer.withSession { it.flush() } }
    	}

    static constraints = {
      answer validator: { Answer a, SecureUserAnswer ua ->
        if (ua.secureUser == null || ua.answer.id == null) return
        boolean existing = false

        SecureUserAnswer.withNewSession {
          existing = SecureUserAnswer.exists(ua.secureUser.id, a.id)
        }
        if (existing) {
          return 'userAnswer.exists'
        }

      }

    }
}
