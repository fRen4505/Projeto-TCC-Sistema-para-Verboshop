package projeto.Interfaces;

import java.util.HashMap;
import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import projeto.Sessao;
import projeto.System.AdminDAO;
import projeto.System.DAO;
import projeto.System.UserDAO;
import projeto.System.Models.Livro;
import projeto.System.Models.Pedido;
import projeto.System.Models.valores.Permissoes;

public class PedidoPane {
    
    @FXML
    private Pane pedPane;

    @FXML
    private Button alterar, entregue;
    
    @FXML
    private Label client = new Label("Cliente: ");
    @FXML
    private Label idPedi = new Label("ID do pedido: ");
    @FXML
    private Label creator = new Label("Atendente: ");
    @FXML
    private Label data = new Label("Data: ");
    @FXML
    private Label pagaMeto = new Label("Pagamento: ");

    private DAO dao;
    private Pedido insPedido;
    
    @FXML
    private ListView<String> encoList = new ListView<>();
    private HashMap<String, Integer> encoVals = new HashMap<>();

    public PedidoPane(){}

    public Pane painel(Pedido insPed){
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUIs/PedidoPane.fxml"));
            Pane tela = loader.load();

            PedidoPane control = loader.getController();

            if (Sessao.getUser().getFunção() == Permissoes.ADMINISTRADOR) {
                control.dao = (AdminDAO) Sessao.getDAO();
            }else{
                control.dao = (UserDAO) Sessao.getDAO();
                control.creator.setVisible(false);
            }

            control.client.setText(client.getText() + insPed.getCliente().getNome());
            control.idPedi.setText(idPedi.getText() + insPed.getIDpedido().toString());
            control.creator.setText(creator.getText() + insPed.getCriador().getNome());
            control.data.setText(data.getText() + insPed.getDataCriação());
            control.pagaMeto.setText(pagaMeto.getText() + insPed.getPagamento().name());

            for (int l = 0; l < insPed.getEncomendas().size(); l++) {

                Livro liv = insPed.getEncomendas().get(l);
                
                if (control.encoVals.containsKey(liv.getTitulo())) {
                    control.encoVals.put(
                        liv.getTitulo(), 
                        (control.encoVals.get( liv.getTitulo() ) + 1)
                    );
                }else{
                    control.encoVals.putIfAbsent(liv.getTitulo(), 1);
                }
            }

            control.encoVals.forEach((liv, quan) -> {
                control.encoList.getItems().add( "Livros: " + liv + "\tQuantidades: " + quan );
            });   
              
            control.insPedido = insPed;

            return tela;

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        return this.pedPane;

    }
    
    public void pedidoEntregue(ActionEvent e){

    }

    public void pedidoAlterar(ActionEvent e){
        try {
            if (Sessao.getUser().getFunção() == Permissoes.ADMINISTRADOR) {
                AdminDAO daoAlt = ((AdminDAO) this.dao);
                AlterarPedido dialog = new AlterarPedido(insPedido, daoAlt.getLivros(), daoAlt.getUsers());
                Optional<Pedido> resultado = dialog.showAndWait();

                daoAlt.alterarPedido(this.insPedido.getIDpedido(), resultado.get());
            }else{
                UserDAO dao = ((UserDAO) this.dao);
                AlterarPedido dialog = new AlterarPedido(insPedido, dao.getLivros(), dao.getUsers());
                Optional<Pedido> resultado = dialog.showAndWait();

                dao.alterarPedido(this.insPedido.getIDpedido(), resultado.get());
            }

        } catch (Exception e1) {
            // TODO: handle exception
        }
    }

}
