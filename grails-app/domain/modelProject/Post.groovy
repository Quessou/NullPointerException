package modelProject


class Post {

	long mark;
	String text;
	Date date;

    static constraints = {
		text(maxSize:2048)
    }

    char getchar(){
    	return "p";
    }

	static belongsTo = [ secureUser : SecureUser]
}
