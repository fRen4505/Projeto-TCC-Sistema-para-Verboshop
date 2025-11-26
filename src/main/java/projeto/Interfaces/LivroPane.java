package projeto.Interfaces;

import java.sql.SQLException;
import java.util.Optional;

import javax.swing.JOptionPane;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import projeto.Sessao;
import projeto.System.AdminDAO;
import projeto.System.PerfilDAO;
import projeto.System.Models.Livro;
import projeto.System.Models.valores.Permissoes;

//Classe tipo Pane (painel) do JavaFX para organização e exibição dos dados de um Livro cadastrado,
//disponibilizando tambem metodos para gestão destes, como alterar e deletar,
//sendo utilizada dentro do AdminCTRL e UserCTRL
public class LivroPane {

    @FXML
    private Pane livPane = new Pane();

    @FXML
    private Button alterar = new Button(), deletar = new Button();

    @FXML
    private Label livTitulo = new Label("Titulo: ");
    @FXML
    private Label livAutor = new Label("Autor: ");
    @FXML
    private Label livEditor = new Label("Editora: ");
    @FXML
    private Label livISBN = new Label("ISBN: ");
    @FXML
    private Label livPreco = new Label("Preço: ");
    @FXML
    private Label livQtnd = new Label("Quantidade: ");

    private PerfilDAO dao = Sessao.getDAO();
    private Livro insLivro;

    //Representação/objeto desta interface Pane para uso em outras interfaces
    private Pane painelLivro;

    //Contrutor da classe, carrega o layout (o fxml) e inclui os componentes da tela, e completa os dados com o informções do livro
    public LivroPane(Livro insLiv){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUIs/LivroPane.fxml"));
            loader.setController(this);
            Pane tela = loader.load();

            if (Sessao.getUser().getFunção() != Permissoes.ADMINISTRADOR) {
                alterar.setVisible(false);
                deletar.setVisible(false);
            }
            
            livTitulo.setText(livTitulo.getText() + insLiv.getTitulo());
            livAutor.setText(livAutor.getText() + insLiv.getAutor());
            livEditor.setText(livEditor.getText() + insLiv.getEditora());
            livISBN.setText(livISBN.getText() + insLiv.getISBN().valorISBN());
            livPreco.setText(livPreco.getText() + insLiv.getPreço().valor());
            if (insLiv.getQuantidade() > 0) {
                livQtnd.setText(livQtnd.getText() + insLiv.getQuantidade().toString());
            } else {
                livQtnd.setText(livQtnd.getText() + "Não há no estoque");
                tela.setStyle("-fx-background-color: #ff0000");
            }

            insLivro = insLiv;
            
            this.painelLivro = livPane;

        } catch (Exception e1) {
            JOptionPane.showMessageDialog(
                null, 
                "Erro ao acessar dados \n motivo "+e1.getMessage(),
                "erro", 
                0
            );
        }
    }

    //Metodo que retorna a variavel PainelLivro para que se possa ser inserido em outra interface, em listas de livros especialmente
    public Pane painel(){
        if (painelLivro != null) {
            return painelLivro;
        } else {
            return null;
        }
    }

    //Metodo para exclusão do livro exibido pelo Pane, acionado ao pressionar o botão deletar,
    //se este não estiver sendo utilizado, este é exclusivo a usuarios admin
    public void livroDelete(ActionEvent e){
        try {
            String[] vals = {"sim", "não"};
            String opt = (String)JOptionPane.showInputDialog(null, 
                "Deseja mesmo excluir?",
                "Deletar", 
                2, 
                null, 
                vals,vals[1]
            );

            if (Sessao.getUser().getFunção() == Permissoes.ADMINISTRADOR && opt == "sim") {
                AdminDAO daoAlt = ((AdminDAO) this.dao);
                if (daoAlt.pedidoContemLivro(this.insLivro.getISBN()) == false) {
                    daoAlt.deletarLivro(insLivro.getISBN());
                }else{
                    JOptionPane.showMessageDialog(
                        null, 
                        "Proibida a exclusão pelo livro ainda estar sendo utilizado nos registros de pedidos",
                        "Proibido", 
                        2
                    ); 
                }
            }
        } catch (SQLException e1) {
            JOptionPane.showMessageDialog(
                null, 
                "Erro por utilização, exclusão cancelada \n motivo: "+e1.getMessage() ,
                "Erro", 
                0
            );   
        }
    }

    //Metodo para alteração do livro exibido pelo Pane, acionado ao pressionar o botão alterar,
    //exibindo o AlterarLivro dialog (pop-up) para inserção dos dados alternativos, este é exclusivo a usuarios admin
    public void livroAlterar(ActionEvent e){
        try {
            if (Sessao.getUser().getFunção() == Permissoes.ADMINISTRADOR) {

                Optional<Livro> resultado = new AlterarLivro(this.insLivro).showAndWait();

                this.dao.alterarLivro(this.insLivro.getISBN(), resultado.get());
            }
            
        } catch (SQLException e2) {
            JOptionPane.showMessageDialog(
                null, 
                "Erro por falta de dados, alteração cancelada \n motivo: "+e2.getMessage() ,
                "Erro", 
                0
            );         
        }

    }

}
