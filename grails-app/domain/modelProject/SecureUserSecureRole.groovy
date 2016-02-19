package modelProject

import grails.gorm.DetachedCriteria
import groovy.transform.ToString

import org.apache.commons.lang.builder.HashCodeBuilder

@ToString(cache=true, includeNames=true, includePackage=false)
class SecureUserSecureRole implements Serializable {

	private static final long serialVersionUID = 1

	SecureUser secureUser
	SecureRole secureRole

	SecureUserSecureRole(SecureUser u, SecureRole r) {
		this()
		secureUser = u
		secureRole = r
	}

	@Override
	boolean equals(other) {
		if (!(other instanceof SecureUserSecureRole)) {
			return false
		}

		other.secureUser?.id == secureUser?.id && other.secureRole?.id == secureRole?.id
	}

	@Override
	int hashCode() {
		def builder = new HashCodeBuilder()
		if (secureUser) builder.append(secureUser.id)
		if (secureRole) builder.append(secureRole.id)
		builder.toHashCode()
	}

	static SecureUserSecureRole get(long secureUserId, long secureRoleId) {
		criteriaFor(secureUserId, secureRoleId).get()
	}

	static boolean exists(long secureUserId, long secureRoleId) {
		criteriaFor(secureUserId, secureRoleId).count()
	}

	private static DetachedCriteria criteriaFor(long secureUserId, long secureRoleId) {
		SecureUserSecureRole.where {
			secureUser == SecureUser.load(secureUserId) &&
			secureRole == SecureRole.load(secureRoleId)
		}
	}

	static SecureUserSecureRole create(SecureUser secureUser, SecureRole secureRole, boolean flush = false) {
		def instance = new SecureUserSecureRole(secureUser: secureUser, secureRole: secureRole)
		instance.save(flush: flush, insert: true)
		instance
	}

	static boolean remove(SecureUser u, SecureRole r, boolean flush = false) {
		if (u == null || r == null) return false

		int rowCount = SecureUserSecureRole.where { secureUser == u && secureRole == r }.deleteAll()

		if (flush) { SecureUserSecureRole.withSession { it.flush() } }

		rowCount
	}

	static void removeAll(SecureUser u, boolean flush = false) {
		if (u == null) return

		SecureUserSecureRole.where { secureUser == u }.deleteAll()

		if (flush) { SecureUserSecureRole.withSession { it.flush() } }
	}

	static void removeAll(SecureRole r, boolean flush = false) {
		if (r == null) return

		SecureUserSecureRole.where { secureRole == r }.deleteAll()

		if (flush) { SecureUserSecureRole.withSession { it.flush() } }
	}

	static constraints = {
		secureRole validator: { SecureRole r, SecureUserSecureRole ur ->
			if (ur.secureUser == null || ur.secureUser.id == null) return
			boolean existing = false
			SecureUserSecureRole.withNewSession {
				existing = SecureUserSecureRole.exists(ur.secureUser.id, r.id)
			}
			if (existing) {
				return 'userRole.exists'
			}
		}
	}

	static mapping = {
		id composite: ['secureUser', 'secureRole']
		version false
	}
}
