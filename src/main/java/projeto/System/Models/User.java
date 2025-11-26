package projeto.System.Models;

import java.util.UUID;
import projeto.System.Models.valores.Permissoes;
import projeto.System.Models.valores.Email;

public class User {
    
    private String Nome;
    private Email Email;
    private Permissoes Função;
    private UUID ID;

    public User(String insNome, String insEmail, Permissoes insPermissão){
        if(insNome != "" && insEmail != "") {
            this.Nome = insNome;
            this.Email = new Email(insEmail); 
            this.Função = insPermissão;
            this.ID = UUID.randomUUID();
        }else{
            throw new IllegalArgumentException("Dados inseridos para usuario são invalidos \n");
        }
    }

    public User(String insNome, String insEmail, String insID, Permissoes insPermissão){
        if(insNome != "" && insEmail != "" && insID != "") {
            this.Nome = insNome;
            this.Email = new Email(insEmail); 
            this.Função = insPermissão;
            this.ID = UUID.fromString(insID);
        }else {
            throw new IllegalArgumentException("Dados inseridos para usuario são invalidos \n");
        }
    }

    public String getNome() {
        return Nome;
    }

    @Override
    public String toString(){
        return " " + Nome + " ";
    }

    public String getEmail() {
        return Email.getEmail();
    }

    public Permissoes getFunção() {
        return Função;
    }

    public UUID getID() {
        return ID;
    }

}
