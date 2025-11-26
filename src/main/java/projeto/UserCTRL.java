package projeto;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.swing.JOptionPane;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import projeto.Interfaces.CadastroPedido;
import projeto.Interfaces.LivroPane;
import projeto.Interfaces.NovoClientePane;
import projeto.Interfaces.NovoLotePane;
import projeto.Interfaces.PedidoPane;
import projeto.System.UserDAO;
import projeto.System.Models.Livro;
import projeto.System.Models.Pedido;
import projeto.System.Models.User;
import projeto.System.Models.valores.Pagamentos;
import projeto.System.Models.valores.Permissoes;

//Metodo controller para as interfaces do usuario padrão/caixa, 
//dando funcionalidade a estas telas e acesso ao banco pelos objetos DAO
public class UserCTRL {
    
    @FXML
    private Button livrosPage, pedidosPage, voltar, sair, novoPedido, novoCliente, novoLote;
    @FXML
    private RadioButton insAdmin, insUser, insClient;
    @FXML
    private Button alterar, deletar;
    @FXML
    private Label userName = new Label();
    @FXML
    private VBox livList, pedList;

    @FXML
    private Label livroQtnd = new Label("Livros: ");
    @FXML
    private Label totalLivros = new Label("Total de livros: ");
    @FXML
    private Label livrosNone = new Label("Livros sem quantidade no estoque: ");

    @FXML
    private Label pedidoCartao = new Label("Pedidos com cartão: ");
    @FXML
    private Label pedidoDinher = new Label("Pedidos com dinheiro: ");
    @FXML
    private Label pedidoPIX = new Label("Pedidos com PIX: ");
    @FXML
    private Label qtndPedido = new Label("Pedidos: ");

    @FXML
    private ListView<String> clientList;

    private Stage stage;

    //Classe do tipo PerfilDAO que realiza todas as operações do banco o um perfil padrão é permitido
    private UserDAO dao;

    //lista de pedidos cadastrados
    private List<Livro> livros;

    //lista de pedidos cadastrados
    private List<Pedido> pedidos;
    
    //lista de usuarios cadastrados
    private List<User> usuarios;

    //Construtor da classe, que configura os principais dados deste controller
    public UserCTRL(User usrIns, Stage insTela){

        if (usrIns == null) {
            usrIns = Sessao.getUser();
        }

        if (usrIns.getFunção() == Permissoes.USUARIO) {
            try {
                this.stage = insTela;
                this.dao = (UserDAO) Sessao.getDAO();

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
    //assim possibilitando acesso as outras interfaces
    public void initUSRtela(ActionEvent e){
        try {
            this.dao = (UserDAO) Sessao.getDAO();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUIs/UserGUI.fxml"));
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

    //===============================METODOS PARA LIVROS===============================
    
    //Metodo para tela principal da gestão de livros, define o layout (o fxml), inclui os componentes da tela, 
    //e insere os objetos LivroPane para cada livro presente e seus dados, tambem possibilita o cadastro de novos livros
    public void livrosTela(ActionEvent e){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUIs/UserLivroGUI.fxml"));
            loader.setController(this);
            Parent tela = loader.load();
            
            this.stage.setTitle("Gerenciamento de livros");
                        
            int val = 0;
            int total = 0;

            livros = Sessao.getDAO().getLivros();
            for (Livro livro : livros) {
                if(livro.getQuantidade() == 0){
                    val = val + 1;
                }
                total = total + livro.getQuantidade(); 

                livList.getChildren().add(new LivroPane(livro).painel());
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

    //Metodo para cadastro de novo lote,
    //uma quantidade de certos livros selecionados que ao cadastrar aumentam a quantidade presente no estoque,
    //se abre uma tela de pop-up para seleção dos livros e quantidades, 
    public void novoLote(ActionEvent e){

        try {
            Optional<ArrayList<Livro>> lote = new NovoLotePane(
                this.dao.getLivros()
            ).showAndWait();

            lote.ifPresent(loteContent ->{
                try {
                    for (Livro loteLivro : loteContent) {
                        this.dao.alterarLivro(loteLivro.getISBN(), loteLivro);
                    }                    
                    this.livrosTela(e);
                } catch (SQLException e1) {
                    JOptionPane.showMessageDialog(
                        null, 
                        "Falha a cadastrar novo lote \nMotivo: "+e1.getMessage(),
                        "Erro", 
                        0
                    ); 
                }
               
            });

        } catch (SQLException e1) {
            JOptionPane.showMessageDialog(
                null, 
                "Falha ao iniciar cadastro de lote \nMotivo: "+e1.getMessage(),
                "Erro", 
                0
            ); 
        }

    }

    //===============================METODOS PARA PEDIDOS===============================

    //Metodo para tela principal da gestão de pedidos, define o layout (o fxml), inclui os componentes da tela, 
    //e insere os objetos PedidoPane para cada pedido realizado e não entregue e seus dados,
    //tambem possibilita o cadastro de novos pedidos
    public void pedidosTela(ActionEvent e){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUIs/UserPedidoGUI.fxml"));
            loader.setController(this);
            Parent tela = loader.load();
            
            this.stage.setTitle("Gerenciamento de pedidos");
            
            int pix = 0;
            int cartao = 0;
            int dinheiro = 0;

            //pedList.setSpacing(27.5);

            pedidos = Sessao.getDAO().getPedidos();
            for (Pedido pedido : pedidos) {
                if (pedido.getEntregue() == 1) {
                    Sessao.getDAO().entreguePedido(pedido.getIDpedido());
                }if(pedido != null){
                    if (pedido.getPagamento() == Pagamentos.DINHEIRO) {
                        dinheiro = dinheiro + 1;
                    }
                    if (pedido.getPagamento() == Pagamentos.PIX) {
                        pix = pix + 1;
                    }
                    if (pedido.getPagamento() == Pagamentos.CREDITO || pedido.getPagamento() == Pagamentos.DEBITO) {
                        cartao = cartao + 1;
                    }
                    pedList.getChildren().add( new PedidoPane(pedido).painel() );
                }
            }

            qtndPedido.setText(qtndPedido.getText()+pedidos.size());
            pedidoDinher.setText(pedidoDinher.getText()+dinheiro);
            pedidoPIX.setText(pedidoPIX.getText() + pix);
            pedidoCartao.setText(pedidoCartao.getText() + cartao);

            usuarios = Sessao.getDAO().getUsers();
            for (User user : usuarios) {
                if (user.getFunção() == Permissoes.CLIENTE) {
                    clientList.getItems().add("Nome: " + user.getNome() );
                }
            }

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
                this.dao.getLivros(),
                this.dao.getUsers()
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
                "Falha a iniciar cadastro de pedido \nMotivo: "+e2.getMessage(),
                "Erro", 
                0
            ); 
        }catch (NullPointerException e2) {
            JOptionPane.showMessageDialog(
                null, 
                "Falha a iniciar cadastro de pedido por problema nos dados \nMotivo: "+e2.getMessage(),
                "Erro", 
                0
            ); 
        }
    }

    //Metodo para cadastro de novo usuario exclusivamente do tipo cliente,
    //exitindo para agilizar e facilitar o cadastro de pedido com um cliente novo ao sistema,
    //este abre uma tela de pop-up para inserção dos dados do novo cliente
    public void clienteNovo(ActionEvent e){

        Optional<User> cadastrado = new NovoClientePane().showAndWait();
        
        cadastrado.ifPresent(novo -> {
            try {
                this.dao.adicionarUser(novo);
                this.pedidosTela(e);
            } catch (SQLException e1) {
                JOptionPane.showMessageDialog(
                    null, 
                    "Falha a cadastrar cliente \nMotivo: "+e1.getMessage(),
                    "Erro", 
                    0
                ); 
            }
        });
        
    }
    
}
