package modelProject


import org.springframework.security.access.annotation.Secured

@Secured('ROLE_ADMIN')
class UserController extends grails.plugin.springsecurity.ui.UserController {
  /** Dependency injection for the 'uiUserStrategy' bean. */
//	UserStrategy uiUserStrategy

	def create() {
		super.create()
	}

	def save() {
		doSave uiUserStrategy.saveUser(params, roleNamesFromParams(), params.password)
	}

	def edit() {
		super.edit()
	}

	def update() {
		doUpdate { user ->
			uiUserStrategy.updateUser params, user, roleNamesFromParams()
		}
	}

	def delete() {
		tryDelete { user ->
			uiUserStrategy.deleteUser user
		}
	}

	def search() {
		if (!isSearch()) {
			// show the form
			return
		}

		def results = doSearch { ->
			like 'username', delegate
			eqBoolean 'accountExpired', delegate
			eqBoolean 'accountLocked', delegate
			eqBoolean 'enabled', delegate
			eqBoolean 'passwordExpired', delegate
		}

		renderSearch results: results, totalCount: results.totalCount,
		            'accountExpired', 'accountLocked', 'enabled', 'passwordExpired', 'username'
	}

	protected lookupFromParams() {
		findUserByUsername(params.username) ?: byId()
	}

	protected List<String> roleNamesFromParams() {
		params.keySet().findAll { it.contains('ROLE_') && params[it] == 'on' } as List
	}

	protected Map buildUserModel(user) {

		Set userRoleNames = user[authoritiesPropertyName].collect { it[authorityNameField] }
		def granted = [:]
		def notGranted = [:]
		for (role in sortedRoles()) {
			String authority = role[authorityNameField]
			if (userRoleNames.contains(authority)) {
				granted[(role)] = userRoleNames.contains(authority)
			}
			else {
				notGranted[(role)] = userRoleNames.contains(authority)
			}
		}

		[roleMap: granted + notGranted, tabData: tabData, user: user]
	}

	protected List sortedRoles() {
		Role.list().sort { it[authorityNameField] }
	}

	protected getTabData() {[
		[name: 'userinfo', icon: 'icon_user', message: message(code: 'spring.security.ui.user.info')],
		[name: 'roles',    icon: 'icon_role', message: message(code: 'spring.security.ui.user.roles')]
	]}

	protected Class<?> getClazz() { SecureUser }
	protected String getClassLabelCode() { 'user.label' }

	protected Map model(user, String action) {
		if (action == 'edit' || action == 'update') {
			buildUserModel user
		}
		else {
			[user: user, authorityList: sortedRoles(), tabData: tabData]
		}
	}

  @Secured('permitAll')
  def displayUser() {
      SecureUser user = SecureUser.get(params.userId);
			ArrayList<Post> posts = Post.findAllBySecureUser(user)//.sort{it.date}.max(5)
      return [user : user, posts : posts]

  }

	protected String authoritiesPropertyName

	void afterPropertiesSet() {
		super.afterPropertiesSet()

		if (!conf.userLookup.userDomainClassName) {
			return
		}

		authoritiesPropertyName = conf.userLookup.authoritiesPropertyName ?: ''
	}
}
