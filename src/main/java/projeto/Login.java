package projeto;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JOptionPane;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import projeto.System.InitDAO;
import projeto.System.Models.User;
import projeto.System.Models.valores.Permissoes;

//Classe de login, que possui metodos relacionados a esta função, acesso ao InitDAO, 
//e que pode loggar um usuario e seu tipo de DAO relacionado, assim iniciando a sessão atual
public class Login {

    private Stage stage;

    //Classe do tipo DAO para acesso inicial do dados do banco, servindo para pegar a lista de usuarios a fim de comparar, 
    //ou adicionar um usuario administrador caso não haja nenhum
    private static InitDAO verificação = InitDAO.getInstancia(); 

    //Lista de usuarios
    private List<User> usuarios;

    @FXML
    private TextField insNome, insMail;
    @FXML
    private Button logButton, telaLogin;
    @FXML
    private ToggleGroup funcao = new ToggleGroup();
    @FXML
    private RadioButton adminButton = new RadioButton("Administrador"),
                        userButton = new RadioButton("Usuario");
        
    //Construtor do login, eles instancia sua variavel usuarios para checkar se esta vazia ou não,
    //se esta ele abre o metodo de primeiro cadastro, se não abre o de login normal
    public Login(Stage inStage){

        try {
            this.stage = inStage;
            this.usuarios = verificação.getUsers();

            if (this.usuarios.isEmpty()) {
                this.telaCadastro();
            } else {
                this.telaLogin();
            }
            
        }catch (SQLException e1) {
            JOptionPane.showMessageDialog(
                null, 
                "Falha ao iniciar e recolher os dados \nMotivo: "+e1.getMessage(),
                "Erro", 
                0
            ); 
        }
    }
    
    //Metodo de login padrão, define o layout (o fxml) e inclui os componentes da tela
    public void telaLogin(){
        try {
            adminButton.setToggleGroup(funcao);
            userButton.setToggleGroup(funcao);

            this.stage.setTitle("Login de perfil");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUIs/LoginGUI.fxml"));
            loader.setController(this);
            Parent tela = loader.load();

            Scene cena = new Scene(tela);
            this.stage.setScene(cena);
            this.stage.show();

        } catch (IOException e1) {
            JOptionPane.showMessageDialog(
                null, 
                "Erro ao exibir tela \nMotivo "+ e1.getMessage(),
                "Erro", 
                0
            ); 
        }
    }

    //Metodo de login de primeiro cadastro, que é na primeira utilização geral do sistema onde não há usuarios loggados,
    //mas aqui so se define o layout (o fxml) e inclui os componentes da tela
    public void telaCadastro(){
        try {
            this.stage.setTitle("Cadastro Inicial");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUIs/PrimeiroCadastroGUI.fxml"));
            loader.setController(this);
            Parent tela = loader.load();

            Scene cena = new Scene(tela);
            this.stage.setScene(cena);
            this.stage.show();

        } catch (IOException e1) {
            JOptionPane.showMessageDialog(
                null, 
                "Erro ao carregar tela \nMotivo " + e1.getMessage(),
                "Erro", 
                0
            ); 
        }
    }

    //Metodo de confirmação de primeiro cadastro, executado quando o botão de cadastro da interface de primeiro cadastro é pressionado
    //assim cadastrando o primeiro usuario no sistema, sempre de tipo administrador, e depois abrindo a tela de login padrão
    public void CadastroInicial(ActionEvent e){

        try {
            verificação.adicionarUser(
                new User(
                    insNome.getText(), 
                    insMail.getText(), 
                    null
                )
            );

            new Login(this.stage).telaLogin();

        } catch (SQLException e1) {
            JOptionPane.showMessageDialog(
                null, 
                "Erro ao acessar dados \nMotivo " +e1.getMessage(),
                "Erro", 
                0
            ); 
        }catch (NullPointerException e2) {
            JOptionPane.showMessageDialog(
                null, 
                "Erro por falta de dados, cadastro cancelado",
                "Erro", 
                0
            ); 
        }
        
    }

    //Metodo de confirmação de login, executado quando o botão de confirmação da interface de login é pressionado
    //assim pegando os dados inseridos e os comparando com os usuarios ja cadastrados no banco de dados, 
    //e depois, caso confirmado o cadastro, é permitido o acesso ao resto do sistema
    public void logging(ActionEvent e){

        try {
            if (logButton.isArmed()) {

                RadioButton logFuncao = (RadioButton) funcao.getSelectedToggle();
        
                User temp = new User(
                    insNome.getText(), 
                    insMail.getText(), 
                    Permissoes.valueOf(logFuncao.getText().toUpperCase())
                );
        
                boolean tem = false;
                for (User usr : usuarios) {
                    if ( temp.getNome().equals(usr.getNome()) 
                        && temp.getEmail().equals(usr.getEmail()) 
                        && temp.getFunção().getPermissaoNome().equals(usr.getFunção().name())
                    ) {       
                        Sessao.setLoggado(usr);
                        new Main().entrar(stage);

                        tem = true;
                        break;
                    }
                }
                if (tem == false) {
                    JOptionPane.showMessageDialog(
                        null, 
                        "dados inseridos não constam com nenhum usuarios cadastrado",
                        "Informações erradas", 
                        2
                    );
                }
            }
    
        }catch (NullPointerException e2) {
            JOptionPane.showMessageDialog(
                null, 
                "Erro por falta de dados, login cancelado",
                "Erro", 
                0
            ); 
        }
    }

    //Metodo para fechar a tela
    public void telaClose(){
        usuarios.clear();
        this.stage.close();
    }

}
