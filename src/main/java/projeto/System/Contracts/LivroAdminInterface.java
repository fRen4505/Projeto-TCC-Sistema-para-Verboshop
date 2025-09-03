package projeto.System.Contracts;

import java.sql.SQLException;
import projeto.System.Models.Livro;
import projeto.System.Models.valores.CodigoISBN;

public interface LivroAdminInterface {

    public void adicionarLivro(Livro livroIns) throws SQLException;

    public void deletarLivro(CodigoISBN insLivID) throws SQLException;

}
