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
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception
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
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception
        }
    }

    public String getNome() {
        return Nome;
    }
    public void setNome(String nome) {
        this.Nome = nome;
    }

    public String getEmail() {
        return Email.getEmail();
    }
    public void setEmail(String email) {
        this.Email.setEmail(email);
    }

    public Permissoes getFunção() {
        return Função;
    }

    public UUID getID() {
        return ID;
    }

}
