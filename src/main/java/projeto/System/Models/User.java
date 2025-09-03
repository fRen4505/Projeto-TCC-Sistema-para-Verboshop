package projeto.System.Models;

import java.util.UUID;

import projeto.System.Models.valores.Permissoes;
import projeto.System.Models.valores.Email;

public class User {
    
    private String Nome;
    private Email Email;
    private Permissoes Função;
    private UUID ID;

    public User(String insNome, String insEmail, Permissoes insPermissão) {
        this.Nome = insNome;
        this.Email = new Email(insEmail); 
        this.Função = insPermissão;
        this.ID = new UUID(900, 9000);
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
