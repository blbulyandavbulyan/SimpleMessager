package ui.exceptions;

public class PersonalMessageIsEmpty extends RuntimeException{
    public PersonalMessageIsEmpty(String errMsg){
        super(errMsg);
    }
}
