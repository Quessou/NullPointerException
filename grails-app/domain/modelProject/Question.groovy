package modelProject

class Question extends Post{

	String title;

	boolean isLocked;


	@Override
	char getchar(){
    	return "q";
    }

    static constraints = {
		title(maxLength:2048)
    }

	static hasMany = [ answers: Answer, tags: Tag]

	public Question()
	{
//		answers = new ArrayList<Answer>();
//		tags = new ArrayList<Tag>();
	}

	public def shorter()
	{
		Question question = new Question();
		question.title=this.title;
		question.text=this.text;
		question.answers=this.answers;
		question.tags=this.tags;
		if(question.text.length() > 120)
		{
			question.text = question.text.substring(0,120) + "..."
		}
		return question;
	}

	
}
