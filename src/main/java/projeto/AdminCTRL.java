package projeto;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Label;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import javax.swing.JOptionPane;

import projeto.Interfaces.AlterarUser;
import projeto.Interfaces.CadastrarLivro;
import projeto.Interfaces.CadastroPedido;
import projeto.Interfaces.CadastroUser;
import projeto.Interfaces.LivroPane;
import projeto.Interfaces.PedidoPane;
import projeto.Interfaces.UserPane;
import projeto.System.AdminDAO;
import projeto.System.Models.User;
import projeto.System.Models.valores.Pagamentos;
import projeto.System.Models.valores.Permissoes;
import projeto.System.Models.Livro;
import projeto.System.Models.Pedido;

//Metodo controller para as interfaces de administrador, 
//dando funcionalidade a estas telas e acesso ao banco pelos objetos DAO
public class AdminCTRL {

    @FXML
    private Button usersPage, livrosPage, pedidosPage, novoUser, novoLivro, novoPedido, alterar, voltar, sair;
    @FXML
    private Label userName = new Label();
    @FXML
    private VBox users, livList, pedidoList;
   
    @FXML
    private Label total = new Label("Total perfis: "),
                admins = new Label("Administradores: "),
                usurs = new Label("Usuarios: "),
                clientes = new Label("Clientes: "),
                livroQtnd = new Label("Livros: "),
                totalLivros = new Label("Total de livros: "),
                livrosNone = new Label("Livros sem quantidade no estoque: "),
                pedidoCartao = new Label("Pedidos com cartão: "),
                pedidoDinher = new Label("Pedidos com dinheiro: "),
                pedidoPIX = new Label("Pedidos com PIX: "),
                qtndPedido = new Label("Pedidos: ");

    private Stage stage;

    //Classe do tipo PerfilDAO que realiza todas as operações do banco que um perfil padrão pode,
    //mais as permissões dadas ao administrador
    private AdminDAO dao;
    
    //lista de usuarios cadastrados
    private List<User> usuarios;

    //lista de livros cadastrados
    private List<Livro> livros;

    //lista de pedidos cadastrados
    private List<Pedido> pedidos;
    
    //Construtor da classe, que configura os principais dados deste controller
    public AdminCTRL(User usrIns, Stage insTela){

        if (usrIns == null) {
            usrIns = Sessao.getUser();
        }

        if (usrIns.getFunção() == Permissoes.ADMINISTRADOR) {
            try {
                this.stage = insTela;
                this.dao = (AdminDAO) Sessao.getDAO();

                this.stage.setTitle("Gerenciamento");
                this.userName.setText(this.userName.getText() + Sessao.getUser().getNome());
         
            }catch (NullPointerException e) {
                JOptionPane.showMessageDialog(
                    null, 
                    "Falha ao iniciar tela \nMotivo: "+e.getMessage(),
                    "Erro", 
                    0
                ); 
            }
        }else{
            this.stage.close();
        }
    }

    //===============================METODOS PARA GUI===============================

    //Metodo da tela inicial, define o layout (o fxml) e inclui os componentes da tela 
    //assim possibilitando acesso as outras interfaces e alteração de alguns dados do proprio cadastro do admin
    public void initADMtela(ActionEvent e){
        try {
            this.dao = (AdminDAO) Sessao.getDAO();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUIs/AdminGUI.fxml"));
            loader.setController(this);
            Parent tela = loader.load();                
                
            Stage currStage = (Stage)((Node) e.getSource()).getScene().getWindow();

            this.userName.setText(this.userName.getText() + Sessao.getUser().getNome());
            this.stage.setTitle("Gerenciamento");

            Scene cena = new Scene(tela);
            currStage.setScene(cena);
            currStage.show();

        }catch (IOException e1) {
            JOptionPane.showMessageDialog(
                null, 
                "Falha ao iniciar tela \nMotivo: "+e1.getMessage(),
                "Erro", 
                0
            ); 
        }
    }

    //Metodo para sair da tela inicial e terminar o programa, executado quando o botão de sair é pressionado
    public void sairTela(ActionEvent e){
        Sessao.deLog();
        this.stage.close();
    }

    //Metodo para o admin alterar alguns de seus dados, executado quando o botão de alterar perfil é pressionado
    public void alterarPerfil(ActionEvent e){

        Optional<User> alterado = new AlterarUser(Sessao.getUser()).showAndWait();

        alterado.ifPresent(alt -> {
            try {
                if (alt.getFunção() == Permissoes.ADMINISTRADOR) {
                    this.dao.alterarUser(Sessao.getUser().getID(), alt);
                    this.initADMtela(e);
                }else{
                    JOptionPane.showMessageDialog(
                        null, 
                        "Não altere a função enquanto esta loggado, mude com outra conta de admionistrador",
                        "Atenção", 
                        2
                    );
                }
            }catch (SQLException e1) {
                JOptionPane.showMessageDialog(
                    null, 
                    "Falha a cadastrar usuario \nMotivo: "+e1.getMessage(),
                    "Erro", 
                    0
                ); 
            }catch (NullPointerException e1) {
                JOptionPane.showMessageDialog(
                    null, 
                    "Falha a cadastrar usuario por falta de dados",
                    "Erro", 
                    0
                ); 
            }
        });
    }

    //===============================METODOS PARA USUARIOS===============================

    //Metodo para tela principal da gestão de usuarios, define o layout (o fxml), inclui os componentes da tela, 
    //e insere os objetos UserPane para cada usuario cadastrado e seus dados, tambem possibilita o cadastro de novos usuarios
    public void usersTela(ActionEvent e){
        try {
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUIs/AdminUserGUI.fxml"));
            loader.setController(this);
            Parent tela = loader.load();

            this.stage.setTitle("Gerenciamento de usuarios");

            int adms = 0;
            int usrs = 0;
            int cliens = 0;

            usuarios = Sessao.getDAO().getUsers();
            for (User usr : usuarios) {
                if (usr.getFunção() == Permissoes.ADMINISTRADOR) {
                    adms++;
                }if (usr.getFunção() == Permissoes.USUARIO) {
                    usrs++;
                }if (usr.getFunção() == Permissoes.CLIENTE){
                    cliens++;
                }

                users.getChildren().add(new UserPane(usr).painel());
            }

            total.setText(total.getText() + usuarios.size());
            admins.setText(admins.getText() + adms);
            usurs.setText(usurs.getText() + usrs);
            clientes.setText(clientes.getText() + cliens);

            Scene cena = new Scene(tela);
            this.stage = (Stage)((Node)e.getSource()).getScene().getWindow(); 
            this.stage.setScene(cena);
            this.stage.show();

        }catch (SQLException e1) {
            JOptionPane.showMessageDialog(
                null, 
                "Falha ao adquirir dados \nMotivo: "+e1.getMessage(),
                "Erro", 
                0
            ); 
        }catch (IOException e1) {
            JOptionPane.showMessageDialog(
                null, 
                "Falha ao iniciar tela \nMotivo: "+e1.getMessage(),
                "Erro", 
                0
            ); 
        }
    }

    //Metodo para cadastro de novo usuario, abrindo uma tela de pop-up para inserção dos dados
    public void userNovo(ActionEvent e){

        Optional<User> cadastrado = new CadastroUser().showAndWait();
        
        cadastrado.ifPresent(novo -> {
            try {
                Sessao.getDAO().adicionarUser(novo);
                this.usersTela(e);
            } catch (SQLException e1) {
                JOptionPane.showMessageDialog(
                    null, 
                    "Falha a cadastrar usuario \nMotivo: "+e1.getMessage(),
                    "Erro", 
                    0
                ); 
            }
        });
            
    }

    //===============================METODOS PARA LIVROS===============================

    //Metodo para tela principal da gestão de livros, define o layout (o fxml), inclui os componentes da tela, 
    //e insere os objetos LivroPane para cada livro presente e seus dados, tambem possibilita o cadastro de novos livros
    public void livrosTela(ActionEvent e){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUIs/AdminLivroGUI.fxml"));
            loader.setController(this);
            Parent tela = loader.load();

            this.stage.setTitle("Gerenciamento de livros");

            int val = 0;
            int total = 0;

            livros = Sessao.getDAO().getLivros();
            for (Livro liv : livros) {
                if(liv.getQuantidade() == 0){
                    val = val + 1;
                }
                total = total + liv.getQuantidade(); 

                livList.getChildren().add(new LivroPane(liv).painel());
            }

            totalLivros.setText(totalLivros.getText() + total);
            livroQtnd.setText(livroQtnd.getText() + livros.size());
            livrosNone.setText(livrosNone.getText() + val);

            Scene cena = new Scene(tela);
            this.stage = (Stage)((Node)e.getSource()).getScene().getWindow(); 
            this.stage.setScene(cena);
            this.stage.show();

        }catch (SQLException e1) {
            JOptionPane.showMessageDialog(
                null, 
                "Falha ao adquirir dados \nMotivo: "+e1.getMessage(),
                "Erro", 
                0
            ); 
        }catch (IOException e1) {
            JOptionPane.showMessageDialog(
                null, 
                "Falha ao iniciar tela \nMotivo: "+e1.getMessage(),
                "Erro", 
                0
            ); 
        }
    }

    //Metodo para cadastro de novo livro, abrindo uma tela de pop-up para inserção dos dados
    public void livroNovo(ActionEvent e){

        Optional<Livro> cadastro = new CadastrarLivro().showAndWait();

        cadastro.ifPresent(novo -> {
            try {
                this.dao.adicionarLivro(novo);
                this.livrosTela(e);
            } catch (SQLException e1) {
                JOptionPane.showMessageDialog(
                    null, 
                    "Falha a cadastrar livro \nMotivo: "+e1.getMessage(),
                    "Erro", 
                    0
                ); 
            }
        });
        
    }

    //===============================METODOS PARA PEDIDOS===============================
    
    //Metodo para tela principal da gestão de pedidos, define o layout (o fxml), inclui os componentes da tela, 
    //e insere os objetos PedidoPane para cada pedido realizado e não entregue e seus dados,
    //tambem possibilita o cadastro de novos pedidos
    public void pedidosTela(ActionEvent e){

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUIs/AdminPedidoGUI.fxml"));
            loader.setController(this);
            Parent tela = loader.load();

            this.stage.setTitle("Gerenciamento de pedidos");

            int pix = 0;
            int cartao = 0;
            int dinheiro = 0;

            //pedidoList.setSpacing(27.5);

            pedidos = Sessao.getDAO().getPedidos();
            for (Pedido ped : pedidos) {
                if (ped.getEntregue() == 1) {
                    Sessao.getDAO().entreguePedido(ped.getIDpedido());
                }if(ped != null){
                    if (ped.getPagamento() == Pagamentos.DINHEIRO) {
                        dinheiro = dinheiro + 1;
                    }
                    if (ped.getPagamento() == Pagamentos.PIX) {
                        pix = pix + 1;
                    }
                    if (ped.getPagamento() == Pagamentos.CREDITO || ped.getPagamento() == Pagamentos.DEBITO) {
                        cartao = cartao + 1;
                    }
                    pedidoList.getChildren().add( new PedidoPane(ped).painel() );
                }
            }
            
            qtndPedido.setText(qtndPedido.getText()+pedidos.size());
            pedidoDinher.setText(pedidoDinher.getText()+dinheiro);
            pedidoPIX.setText(pedidoPIX.getText() + pix);
            pedidoCartao.setText(pedidoCartao.getText() + cartao);

            Scene cena = new Scene(tela);
            this.stage = (Stage)((Node)e.getSource()).getScene().getWindow(); 
            this.stage.setScene(cena);
            this.stage.show();

        }catch (SQLException e1) {
            JOptionPane.showMessageDialog(
                null, 
                "Falha ao adquirir dados \nMotivo: "+e1.getMessage(),
                "Erro", 
                0
            ); 
        }catch (IOException e1) {
            JOptionPane.showMessageDialog(
                null, 
                "Falha ao iniciar tela \nMotivo: "+e1.getMessage(),
                "Erro", 
                0
            ); 
        }

    }

    //Metodo para cadastro de novo pedido, abrindo uma tela de pop-up para inserção dos dados
    public void pedidoNovo(ActionEvent e){
        
        try {
            Optional<Pedido> cadastro = new CadastroPedido(
                Sessao.getDAO().getLivros(),
                Sessao.getDAO().getUsers()
            ).showAndWait();
            
            cadastro.ifPresent(novo ->{
                try {
                    Sessao.getDAO().criarPedido(novo);
                    this.pedidosTela(e);
                } catch (SQLException e1) {
                    JOptionPane.showMessageDialog(
                        null, 
                        "Falha a cadastrar pedido \nMotivo: "+e1.getMessage(),
                        "Erro", 
                        0
                    ); 
                }
            });
       
        } catch (SQLException e2) {
            JOptionPane.showMessageDialog(
                null, 
                "Falha a iniciar cadastro de pedido por problema no banco \nMotivo: "+e2.getMessage(),
                "Erro", 
                0
            ); 
        }

    }

}
