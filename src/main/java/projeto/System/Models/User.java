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
        try {
            if(insNome != "" && insEmail != "") {
                this.Nome = insNome;
                this.Email = new Email(insEmail); 
                this.Função = insPermissão;
                this.ID = UUID.randomUUID();
            }
        }catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Dados inseridos para usuario são invalidos \n" + e.getMessage());
        }catch (NullPointerException e) {
            throw new NullPointerException("Falta de dados para usuario \n" + e.getMessage());
        }
    }

    public User(String insNome, String insEmail, String insID, Permissoes insPermissão){
        try {
            if(insNome != "" && insEmail != "" && insID != "") {
                this.Nome = insNome;
                this.Email = new Email(insEmail); 
                this.Função = insPermissão;
                this.ID = UUID.fromString(insID);
            }
        }catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Dados inseridos para usuario são invalidos \n" + e.getMessage());
        }catch (NullPointerException e) {
            throw new NullPointerException("Falta de dados para usuario \n" + e.getMessage());
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
