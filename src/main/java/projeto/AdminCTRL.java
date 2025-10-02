package projeto;

import com.gluonhq.charm.glisten.control.CardPane;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Label;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.validator.routines.ISBNValidator;

import projeto.Interfaces.LivroPane;
import projeto.Interfaces.UserPane;
import projeto.System.AdminDAO;
import projeto.System.Models.User;
import projeto.System.Models.valores.Permissoes;
import projeto.System.Models.Livro;
import projeto.System.Models.Pedido;

public class AdminCTRL {

    private Stage stage;

    @FXML
    private Button usersPage, livrosPage, pedidosPage, novoUser, novoLivro, voltar, sair;
    @FXML
    private TextField insNome, insMail, insTitulo, insAutor, insEdit, insCode, insPreco ;
    @FXML
    private RadioButton insAdmin, insUser, insClient;
    @FXML
    private Label userName = new Label();
    @FXML
    private VBox users, livList;

    private User loggedUser;
    private AdminDAO dao;

    private Alert alert = new Alert(Alert.AlertType.ERROR);
                
    private List<User> usuarios;
    private List<Livro> livros;
    private List<Pedido> pedidos;
    
    public AdminCTRL(){}

    public void tela(User usrIns, Stage insTela){

        if (usrIns == null) {
            usrIns = Sessao.getUser();
        }

        if (usrIns.getFunção() == Permissoes.ADMINISTRADOR) {

            try {
                this.stage = insTela;
                this.loggedUser = usrIns;
                this.dao = (AdminDAO) Sessao.getDAO();

                this.userName.setText(this.userName.getText() + loggedUser.getNome());
                this.stage.setTitle("Gerenciamento");
         
                //AdminCTRL control = loader.getController();
                //control.setLoggedUser(usrIns);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }if (insTela == null) {
            System.out.println("tela é nula");
        }else{
            this.stage.close();
        }

    }

    //===============================METODOS PARA GUI===============================

    public void initADMtela(ActionEvent e){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUIs/AdminGUI.fxml"));
            Parent tela = loader.load();                
                
            AdminCTRL control = loader.getController();

            Stage currStage = (Stage)((Node) e.getSource()).getScene().getWindow();

            control.tela(Sessao.getUser(), currStage);
            this.dao = (AdminDAO) Sessao.getDAO();

            Scene cena = new Scene(tela);
            currStage.setScene(cena);
            currStage.show();

        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public void pedidosTela(ActionEvent e){

    }

    public void sairTela(){
        Sessao.deLog();
        this.loggedUser = null;
        this.stage.close();
    }

    //===============================METODOS PARA USUARIOS===============================


    public void usersTela(ActionEvent e){
        try {
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUIs/AdminUserGUI.fxml"));
            Parent tela = loader.load();
            AdminCTRL ctrl = loader.getController();

            Stage currStage = (Stage)((Node) e.getSource()).getScene().getWindow();
            
            ctrl.tela(Sessao.getUser(), currStage);
            this.dao = (AdminDAO) Sessao.getDAO();

            usuarios = ctrl.dao.getUsers();

            for (User usr : usuarios) {
                System.out.println("Nome: " + usr.getNome());

                UserPane pane = new UserPane();
                Pane usrPane = pane.painel(usr,this.dao);

                ctrl.users.getChildren().add(usrPane);
            }

            Scene cena = new Scene(tela);
            ctrl.stage = (Stage)((Node)e.getSource()).getScene().getWindow(); 
            ctrl.stage.setScene(cena);
            ctrl.stage.show();

        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }


    public void userNovo(ActionEvent e){

        String username = insNome.getText();
        String email = insMail.getText();
        Permissoes permissao = null;
        User novo;

        if (novoUser.isArmed()) {
        
            if (insAdmin.isSelected()) {
                permissao = Permissoes.ADMINISTRADOR;
            }if (insUser.isSelected()) {
                permissao = Permissoes.USUARIO;
            }if (insClient.isSelected()) {
                permissao = Permissoes.CLIENTE;  
            }

            novo = new User(username, email, permissao);

            try {
                this.dao.adicionarUser(novo);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
        
    }

    //===============================METODOS PARA LIVROS===============================

    public void livrosTela(ActionEvent e){
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUIs/AdminLivroGUI.fxml"));
            Parent tela = loader.load();
            AdminCTRL ctrl = loader.getController();

            Stage currStage = (Stage)((Node) e.getSource()).getScene().getWindow();
            
            ctrl.tela(Sessao.getUser(), currStage);
            this.dao = (AdminDAO) Sessao.getDAO();

            livros = ctrl.dao.getLivros();

            for (Livro liv : livros) {
                System.out.println("Titulo: " + liv.getTitulo());

                LivroPane pane = new LivroPane();
                Pane libPane = pane.painel(liv, this.dao);

                ctrl.livList.getChildren().add(libPane);
            }

            Scene cena = new Scene(tela);
            ctrl.stage = (Stage)((Node)e.getSource()).getScene().getWindow(); 
            ctrl.stage.setScene(cena);
            ctrl.stage.show();

        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public void livroNovo(ActionEvent e){

        String titulo = insTitulo.getText();
        String autor = insAutor.getText();
        String editora = insEdit.getText();
        String isbn = insCode.getText();
        double valor = Double.valueOf(insPreco.getText());

        try {
            Livro novo = new Livro(titulo, autor, editora, valor, isbn);

            try {
                this.dao.adicionarLivro(novo);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } catch (IllegalArgumentException ia) {
            alert.setTitle("Erro");
            alert.setHeaderText(null);
            alert.setContentText( ia.getMessage() + ", cadastro cancelado");
            alert.showAndWait();
        }

    }

    //===============================METODOS PARA PEDIDOS===============================

    public void pedidoNovo(ActionEvent e){

    }

    public void pedidoDeletar(ActionEvent e){

    }

    public void pedidoAlterar(ActionEvent e){

    }


}
