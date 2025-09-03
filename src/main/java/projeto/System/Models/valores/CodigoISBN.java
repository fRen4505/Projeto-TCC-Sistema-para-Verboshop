package projeto.System.Models.valores;

import org.apache.commons.validator.routines.ISBNValidator;

public class CodigoISBN {
    
    private String ISBN;

    public String getISBN() {
        return ISBN;
    }

    public CodigoISBN(String codeIns){
        try {
            if (ISBNValidator.getInstance().isValid(codeIns) == true) {
                this.ISBN = codeIns;    
            }
        } catch (Exception e) {
            e.printStackTrace();
        }        

    }

}
