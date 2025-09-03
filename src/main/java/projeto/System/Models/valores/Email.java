package projeto.System.Models.valores;

import org.apache.commons.validator.routines.EmailValidator;

public class Email {
    
    private String email;

    public String getEmail() {
        return email;
    }
    
    public void setEmail(String insMail){
        
        try {
            EmailValidator.getInstance().isValid(insMail);
            this.email = insMail;
            
        } catch (Exception e) {
            System.out.println("email invalido");
            e.printStackTrace(); 
        }

    }

    public Email(String insMail){

        try {
            EmailValidator.getInstance().isValid(insMail);
            this.email = insMail;
            
        } catch (Exception e) {
            System.out.println("email invalido");
            e.printStackTrace(); 
        }

    }

}
