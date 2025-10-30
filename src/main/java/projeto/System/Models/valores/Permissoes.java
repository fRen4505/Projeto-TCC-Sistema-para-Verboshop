package projeto.System.Models.valores;

public enum Permissoes {

    CLIENTE("CLIENTE"), USUARIO("USUARIO"), ADMINISTRADOR("ADMINISTRADOR");
    
    private String permissaoNome;

    private Permissoes(String nivel) {
        this.permissaoNome = nivel;
    } 

    public String getPermissaoNome() {
        return permissaoNome;
    }
    
}
