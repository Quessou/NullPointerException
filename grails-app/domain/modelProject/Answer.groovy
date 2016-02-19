package modelProject

class Answer extends Post {


    static constraints = {
    }

    @Override
    char getchar(){
    	return "a";
    }


	static belongsTo = [ question : Question ]

  public def shorter()
	{
		Answer answer = new Answer();
		answer.text=this.text;
    answer.question = this.question;

		if(answer.text.length() > 120)
		{
			answer.text = answer.text.substring(0,120) + "..."
		}
		return answer;
	}

}
