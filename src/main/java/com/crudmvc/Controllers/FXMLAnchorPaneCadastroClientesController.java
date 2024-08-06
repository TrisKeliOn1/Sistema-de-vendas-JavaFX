package com.crudmvc.Controllers;

import com.crudmvc.Models.dao.ClienteDAO;
import com.crudmvc.Models.database.Database;
import com.crudmvc.Models.database.DatabaseFactory;
import com.crudmvc.Models.domain.Cliente;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.ResourceBundle;

public class FXMLAnchorPaneCadastroClientesController implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        clienteDAO.setConnection(connection);
        carregarTableViewCliente();

        tableViewClientes.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarItemTableViewClientes(newValue));
    }

    @FXML
    private TableView<Cliente> tableViewClientes;

    @FXML
    private TableColumn<Cliente, String> tableColumnClienteNome;

    @FXML
    private  TableColumn<Cliente, String> tableColumnClienteCPF;

    @FXML
    private Button buttonInserir;

    @FXML
    private Button buttonAlterar;

    @FXML
    private Button buttonDeletar;

    @FXML
    private Label labelClienteCodigo;

    @FXML
    private Label labelClienteNome;

    @FXML
    private Label labelClienteCPF;

    @FXML
    private Label labelClienteTelefone;

    private List<Cliente> listClientes;
    private ObservableList<Cliente> observableListClientes;

    private final Database database = DatabaseFactory.getDatabase("postgresql");
    private final Connection connection = database.conectar();
    private final ClienteDAO clienteDAO = new ClienteDAO();

    public void carregarTableViewCliente() {
        tableColumnClienteNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        tableColumnClienteCPF.setCellValueFactory(new PropertyValueFactory<>("cpf"));

        listClientes = clienteDAO.listar();

        observableListClientes = FXCollections.observableArrayList(listClientes);
        tableViewClientes.setItems(observableListClientes);
    }

    public void selecionarItemTableViewClientes(Cliente cliente) {
        if (cliente == null) {
            labelClienteCodigo.setText("");
            labelClienteNome.setText("");
            labelClienteCPF.setText("");
            labelClienteTelefone.setText("");
            return;
        }

        labelClienteCodigo.setText(String.valueOf(cliente.getCdCliente()));
        labelClienteNome.setText(cliente.getNome());
        labelClienteCPF.setText(cliente.getCpf());
        labelClienteTelefone.setText(cliente.getTelefone());
    }

    @FXML
    public void handleButtonInserir() throws IOException {
        Cliente cliente = new Cliente();
        boolean buttonConfirmarClicked = showFXMLAnchorPaneCadastrarClientesDialog(cliente);
        if (buttonConfirmarClicked) {
            clienteDAO.inserir(cliente);
            carregarTableViewCliente();
        }
    }

    @FXML
    public void handleButtonAlterar() throws IOException{
        Cliente cliente = tableViewClientes.getSelectionModel().getSelectedItem();
        if (cliente != null) {
            boolean buttonConfirmarClicked = showFXMLAnchorPaneCadastrarClientesDialog(cliente);
            if (buttonConfirmarClicked) {
                clienteDAO.alterar(cliente);
                carregarTableViewCliente();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha um cliente na tabela!");
            alert.show();
        }
    }

    @FXML
    public void handleButtonRemover() {
        Cliente cliente = tableViewClientes.getSelectionModel().getSelectedItem();
        if (cliente != null) {
            clienteDAO.remover(cliente);
            carregarTableViewCliente();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha um cliente da Tabela!");
            alert.show();
        }
    }

    public boolean showFXMLAnchorPaneCadastrarClientesDialog(Cliente cliente) throws IOException{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(FXMLAnchorPaneCadastroClientesDialogController.class.getResource("/FXML/FXMLAnchorPaneCadastroClientesDialog.fxml"));
        AnchorPane page = (AnchorPane) loader.load();

        Stage dialogStage = new Stage();
        dialogStage.setTitle("Cadastro de Clientes");
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        FXMLAnchorPaneCadastroClientesDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setCliente(cliente);

        dialogStage.showAndWait();

        return controller.isButtonConfirmarClicked();
    }
}
