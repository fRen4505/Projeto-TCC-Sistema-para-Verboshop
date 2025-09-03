package projeto.System.Contracts;

import java.sql.SQLException;
import java.util.List;

import projeto.System.Models.Livro;
import projeto.System.Models.valores.CodigoISBN;

public interface LivroInterface {

    public void alterarLivro(CodigoISBN insLivID, Livro insLivAlt) throws SQLException;

    public List<Livro> getLivros() throws SQLException;
    
}
