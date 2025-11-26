package projeto.Interfaces;

import java.io.IOException;
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
import projeto.System.Models.User;
import projeto.System.Models.valores.Permissoes;

//Classe tipo Pane (painel) do JavaFX para organização e exibição dos dados de um Usuario cadastrado,
//disponibilizando tambem metodos para gestão deste, como alterar e deletar,
//sendo utilizada dentro do AdminCTRL
public class UserPane {
    
    @FXML
    private Pane usrPane = new Pane();

    @FXML
    private Button alterar, deletar;

    @FXML
    private Label usrNom = new Label("Nome: ");
    @FXML
    private Label usrMail = new Label("Email: ");
    @FXML
    private Label usrFunc = new Label("Função: ");
    @FXML
    private Label usrID = new Label("ID: ");

    private AdminDAO dao = (AdminDAO)Sessao.getDAO();
    private User usr;

    //Representação/objeto desta interface Pane para uso em outras interfaces
    private Pane painel;

    //Contrutor da classe, carrega o layout (o fxml) e inclui os componentes da tela, e completa os dados com o informções do usuario
    public UserPane(User insUsr){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUIs/UserPane.fxml"));
            loader.setController(this);
            Pane tela = loader.load();

            //if (insUsr.getFunção() == Permissoes.CLIENTE) {
            //    alterar.setVisible(false);
            //}

            usrNom.setText(usrNom.getText() + insUsr.getNome());
            usrMail.setText(usrMail.getText() + insUsr.getEmail());
            usrFunc.setText(usrFunc.getText() + insUsr.getFunção().getPermissaoNome());
            usrID.setText(usrID.getText() + insUsr.getID().toString());

            usr = insUsr;
            dao = (AdminDAO) Sessao.getDAO();
            
            this.painel = usrPane;

        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                null, 
                "Erro ao acessar dados \n motivo "+e.getMessage(),
                "erro", 
                0
            );
        }
    }

    //Metodo que retorna a variavel painel para que se possa ser inserido em outra interface, em listas de usuarios especialmente
    public Pane painel(){
        if (painel != null) {
            return painel;
        } else {
            return null;
        }
    }

    //Metodo para exclusão do usuario exibido pelo Pane, acionado ao pressionar o botão deletar,
    //se este não estiver sendo utilizado nem for o usuario loggado, este é exclusivo a usuarios admin
    public void userDelete(ActionEvent e){
        if (this.usr.getID().compareTo(Sessao.getUser().getID()) != 0 ) {
            String[] vals = {"sim", "não"};
            String opt = (String)JOptionPane.showInputDialog(null, 
                "Deseja mesmo excluir?",
                "Deletar", 
                2, 
                null, 
                vals,vals[1]
            );
            if( opt == "sim") {
                try {
                    this.dao.deletarUser(this.usr.getID());
                } catch (SQLException e1) {
                    JOptionPane.showMessageDialog(
                        null, 
                        "Proibida a exclusão por cadastro estar sendo utilizado em pedido, exclusão cancelada \n motivo: "+e1.getMessage() ,
                        "Proibição", 
                        2
                    );
                }
            }
        }else{
            JOptionPane.showMessageDialog(
                null, 
                "Não é permitido um usuario excluir o proprio cadastro desta forma",
                "Erro", 
                2
            );
        }
        
    }

    //Metodo para alteração do usuario exibido pelo Pane, acionado ao pressionar o botão alterar,
    //exibindo o AlterarUser dialog (pop-up) para inserção dos dados alternativos, este é exclusivo a usuarios admin
    public void userAlterar(ActionEvent e){
        if( this.usr.getID().compareTo(Sessao.getUser().getID()) != 0 ) {

            AlterarUser dialog = new AlterarUser(this.usr);
            Optional<User> alterado = dialog.showAndWait();

            alterado.ifPresent(alter ->{
                try {
                    this.dao.alterarUser(this.usr.getID(), alterado.get());

                } catch (SQLException e2) {
                    e2.printStackTrace();
                    JOptionPane.showMessageDialog(
                        null, 
                        "Erro por falta de dados, alteração cancelada \n motivo: "+e2.getMessage() ,
                        "Erro", 
                        0
                    ); 
                }catch (NullPointerException e3) {
                    e3.printStackTrace();
                    JOptionPane.showMessageDialog(
                        null, 
                        "Erro por falta de dados, alteração cancelada \n motivo: "+e3.getMessage() ,
                        "Erro", 
                        0
                    ); 
                }
            });

        }else{
            JOptionPane.showMessageDialog(
                null, 
                "Não é permitido um usuario alterar o proprio cadastro desta forma",
                "Erro", 
                2
            );
        }
        
    }

}
