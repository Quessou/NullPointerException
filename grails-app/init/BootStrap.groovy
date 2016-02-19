import modelProject.*;

class BootStrap {
  // Ligne présente dans le fichier BootStrap par défaut.
  //  def init = { servletContext ->
  def init = {
  def adminRole = new SecureRole('ROLE_ADMIN').save()
       def userRole = new SecureRole('ROLE_USER').save()

       def testUser = new SecureUser('me', 'password').save()
       def randomUser = new SecureUser('user', 'user').save()


       SecureUserSecureRole.create testUser, adminRole
       SecureUserSecureRole.create randomUser, userRole
       SecureUserSecureRole.create testUser, userRole

       SecureUserSecureRole.withSession {
          it.flush()
          it.clear()
       }

	   def question1 = new Question([date : new Date(), text : new String("Tout est dans le titre"), secureUser : randomUser, title : new String("Pourquoi on a une null pointer exception ?"), isLocked : false]).save()
	   def answer1 = new Answer([date : new Date(), text : new String("lol voila"), secureUser : testUser, question : question1]).save()
	   def answer2 = new Answer([date : new Date(), text : new String("non"), secureUser : testUser, question : question1]).save()

	   def question2 = new Question([date : new Date(), text : new String("C++, c'est quand même mieux"), secureUser : testUser, title : new String("Java, c'est si nul ?"), isLocked : false]).save()
	  def answer3 = new Answer([date : new Date(), text : new String("Oker"), secureUser : testUser, question : question2]).save()


       assert SecureUser.count() == 2
       assert SecureRole.count() == 2
       assert SecureUserSecureRole.count() == 3
    }
    def destroy = {
    }
}
