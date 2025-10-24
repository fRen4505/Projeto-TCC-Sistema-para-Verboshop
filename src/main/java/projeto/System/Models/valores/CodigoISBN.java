package projeto.System.Models.valores;

import org.apache.commons.validator.routines.ISBNValidator;

public class CodigoISBN {
    
    private String ISBN;

    public String valorISBN() {
        return ISBN;
    }

    public CodigoISBN(String codeIns){
        if (ISBNValidator.getInstance().isValid(codeIns) == true) {
            this.ISBN = codeIns;    
        }else{
            this.ISBN = null;
            throw new IllegalArgumentException("ISBN inserido invalido");
        }
       
    }

}
