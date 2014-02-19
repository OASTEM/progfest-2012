package src;

public enum ErrorMessages {
	
	ERR00 ("ERR00: COMMAND NOT FOUND"),
    COMMAND ("ERR01: Command "),
    NOTFOUND (" not found"),
    //ERR03 ("ERR03: ACCOUNT NOT FOUND"),
    //ERRO4 ("ERR04: INPUT LIST EMPTY"),
    //ERR05 ("ERR05: INVALID SELECTION"),
    //ERRO6 ("ERR06: NEGATIVE INPUT FOUND"),
    //ERR07 ("ERR07: OPTION FLAG MISSING"),
    //ERR08 ("ERR08: OPTION NOT FOUND"),
    //ERRO9:",
    //ERR10:",
    //ERR11:",
    //ERR12:",
    ERR13 ("FATAL ERROR"),
	//ERR14
	//ERR15
	//ERR16
	//ERR17
	//ERR18
	//ERR19
	//ERR20
	ERR21 ("lol what the hell");
	
	
	public String err;
	private ErrorMessages(String message){
		this.err = message;
	}
	
}
