package projeto.System.Models.valores;

import org.apache.commons.validator.routines.EmailValidator;

public class Email {
    
    private String email;

    public String getEmail() {
        return email;
    }
    
    public Email(String insMail){
        if (EmailValidator.getInstance().isValid(insMail)) {
            this.email = insMail;
        } else {
            this.email = null;
            throw new IllegalArgumentException("Email inserido invalido");
        }

    }

}
