package com.gym.ui;

import com.gym.model.Cliente;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.time.LocalDate;

public class ClienteUI {

    // Colores del tema oscuro
    private static final String COLOR_FONDO = "#1a1a2e";
    private static final String COLOR_PANEL = "#16213e";
    private static final String COLOR_CAMPO = "#0f3460";
    private static final String COLOR_BOTON = "#7c3aed";
    private static final String COLOR_TEXTO = "#ffffff";
    private static final String COLOR_SUBTITULO = "#a78bfa";

    private ObservableList<Cliente> listaClientes = FXCollections.observableArrayList();
    private TableView<Cliente> tabla = new TableView<>();

    // Campos del formulario
    private TextField txtNombre = new TextField();
    private TextField txtApellido = new TextField();
    private TextField txtCedula = new TextField();
    private TextField txtTelefono = new TextField();
    private TextField txtCorreo = new TextField();
    private DatePicker dpFechaInscripcion = new DatePicker(LocalDate.now());
    private ComboBox<String> cmbEstado = new ComboBox<>();
    private TextField txtBuscar = new TextField();

    private Cliente clienteEditando = null;

    public VBox getVista() {
        VBox root = new VBox(20);
        root.setStyle("-fx-background-color: " + COLOR_FONDO + ";");
        root.setPadding(new Insets(20));

        root.getChildren().addAll(
            crearFormulario(),
            crearTabla()
        );

        return root;
    }

    private VBox crearFormulario() {
        VBox panel = new VBox(15);
        panel.setStyle("-fx-background-color: " + COLOR_PANEL + "; -fx-background-radius: 10;");
        panel.setPadding(new Insets(20));

        Label titulo = new Label("NUEVO CLIENTE");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        titulo.setTextFill(Color.web(COLOR_SUBTITULO));

        // Fila 1: Nombre y Apellido
        HBox fila1 = new HBox(15);
        fila1.getChildren().addAll(
            crearCampo("Nombre", txtNombre),
            crearCampo("Apellido", txtApellido)
        );

        // Fila 2: Cédula y Teléfono
        HBox fila2 = new HBox(15);
        txtCedula.setPromptText("000-0000000-0");
        txtTelefono.setPromptText("+1 809");
        fila2.getChildren().addAll(
            crearCampo("Cédula", txtCedula),
            crearCampo("Teléfono", txtTelefono)
        );

        // Fila 3: Correo y Fecha
        HBox fila3 = new HBox(15);
        txtCorreo.setPromptText("correo@email.com");
        dpFechaInscripcion.setStyle(estiloInput());
        fila3.getChildren().addAll(
            crearCampo("Correo", txtCorreo),
            crearCampoFecha("Fecha de inscripción", dpFechaInscripcion)
        );

        // Fila 4: Estado y Botón
        HBox fila4 = new HBox(15);
        fila4.setAlignment(Pos.CENTER_LEFT);
        cmbEstado.getItems().addAll("Activo", "Inactivo");
        cmbEstado.setValue("Activo");
        cmbEstado.setStyle(estiloInput());
        cmbEstado.setPrefWidth(200);

        Button btnGuardar = new Button("Guardar");
        btnGuardar.setStyle(
            "-fx-background-color: " + COLOR_BOTON + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 8;" +
            "-fx-padding: 8 20;"
        );
        btnGuardar.setOnAction(e -> guardarCliente());

        HBox estadoBox = new HBox(10);
        VBox estadoLabel = new VBox(3);
        Label lbEstado = new Label("Estado");
        lbEstado.setTextFill(Color.web(COLOR_TEXTO));
        estadoLabel.getChildren().addAll(lbEstado, cmbEstado);

        HBox btnBox = new HBox();
        btnBox.setAlignment(Pos.BOTTOM_RIGHT);
        HBox.setHgrow(btnBox, Priority.ALWAYS);
        btnBox.getChildren().add(btnGuardar);

        fila4.getChildren().addAll(estadoLabel, btnBox);

        panel.getChildren().addAll(titulo, fila1, fila2, fila3, fila4);
        return panel;
    }

    private VBox crearTabla() {
        VBox panel = new VBox(15);
        panel.setStyle("-fx-background-color: " + COLOR_PANEL + "; -fx-background-radius: 10;");
        panel.setPadding(new Insets(20));

        Label titulo = new Label("LISTADO DE CLIENTES");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        titulo.setTextFill(Color.web(COLOR_SUBTITULO));

        // Buscador
        txtBuscar.setPromptText("🔍 Buscar por nombre o cédula...");
        txtBuscar.setStyle(estiloInput());
        txtBuscar.setPrefWidth(Double.MAX_VALUE);
        txtBuscar.textProperty().addListener((obs, old, nuevo) -> filtrarClientes(nuevo));

        // Columnas
        TableColumn<Cliente, String> colNombre = columna("NOMBRE", "nombre", 150);
        TableColumn<Cliente, String> colCedula = columna("CÉDULA", "cedula", 130);
        TableColumn<Cliente, String> colTelefono = columna("TELÉFONO", "telefono", 120);
        TableColumn<Cliente, String> colEstado = columna("ESTADO", "estado", 100);

        // Columna Editar
        TableColumn<Cliente, Void> colEditar = new TableColumn<>("ACCIÓN");
        colEditar.setPrefWidth(100);
        colEditar.setCellFactory(col -> new TableCell<>() {
            final Button btn = new Button("Editar");
            {
                btn.setStyle(
                    "-fx-background-color: #374151;" +
                    "-fx-text-fill: white;" +
                    "-fx-background-radius: 5;" +
                    "-fx-padding: 4 10;"
                );
                btn.setOnAction(e -> {
                    Cliente c = getTableView().getItems().get(getIndex());
                    cargarClienteEnFormulario(c);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        tabla.getColumns().addAll(colNombre, colCedula, colTelefono, colEstado, colEditar);
        tabla.setItems(listaClientes);
        tabla.setStyle(
            "-fx-background-color: " + COLOR_CAMPO + ";" +
            "-fx-text-fill: white;"
        );
        tabla.setPrefHeight(250);

        panel.getChildren().addAll(titulo, txtBuscar, tabla);
        return panel;
    }

    // helpers
    private VBox crearCampo(String etiqueta, TextField campo) {
        VBox box = new VBox(4);
        HBox.setHgrow(box, Priority.ALWAYS);
        Label lbl = new Label(etiqueta);
        lbl.setTextFill(Color.web(COLOR_TEXTO));
        campo.setStyle(estiloInput());
        campo.setMaxWidth(Double.MAX_VALUE);
        box.getChildren().addAll(lbl, campo);
        return box;
    }

    private VBox crearCampoFecha(String etiqueta, DatePicker dp) {
        VBox box = new VBox(4);
        HBox.setHgrow(box, Priority.ALWAYS);
        Label lbl = new Label(etiqueta);
        lbl.setTextFill(Color.web(COLOR_TEXTO));
        dp.setMaxWidth(Double.MAX_VALUE);
        box.getChildren().addAll(lbl, dp);
        return box;
    }

    private TableColumn<Cliente, String> columna(String titulo, String propiedad, double ancho) {
        TableColumn<Cliente, String> col = new TableColumn<>(titulo);
        col.setCellValueFactory(new PropertyValueFactory<>(propiedad));
        col.setPrefWidth(ancho);
        col.setStyle("-fx-text-fill: white;");
        return col;
    }

    private String estiloInput() {
        return "-fx-background-color: " + COLOR_CAMPO + ";" +
               "-fx-text-fill: white;" +
               "-fx-prompt-text-fill: #6b7280;" +
               "-fx-background-radius: 6;" +
               "-fx-padding: 8;";
    }

    private void guardarCliente() {
        String nombre = txtNombre.getText().trim();
        String apellido = txtApellido.getText().trim();
        String cedula = txtCedula.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String correo = txtCorreo.getText().trim();
        LocalDate fecha = dpFechaInscripcion.getValue();
        String estado = cmbEstado.getValue();

        if (nombre.isEmpty() || apellido.isEmpty() || cedula.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Campos obligatorios");
            alert.setContentText("Nombre, apellido y cédula son obligatorios.");
            alert.showAndWait();
            return;
        }

        if (clienteEditando != null) {
            clienteEditando.setNombre(nombre);
            clienteEditando.setApellido(apellido);
            clienteEditando.setCedula(cedula);
            clienteEditando.setTelefono(telefono);
            clienteEditando.setCorreo(correo);
            clienteEditando.setFechaInscripcion(fecha);
            clienteEditando.setEstado(estado);
            tabla.refresh();
            clienteEditando = null;
        } else {
            Cliente c = new Cliente(nombre, apellido, cedula, null, telefono, correo, fecha, estado);
            listaClientes.add(c);
        }
        limpiar();
    }

    private void cargarClienteEnFormulario(Cliente c) {
        clienteEditando = c;
        txtNombre.setText(c.getNombre());
        txtApellido.setText(c.getApellido());
        txtCedula.setText(c.getCedula());
        txtTelefono.setText(c.getTelefono());
        txtCorreo.setText(c.getCorreo());
        dpFechaInscripcion.setValue(c.getFechaInscripcion());
        cmbEstado.setValue(c.getEstado());
    }

    private void filtrarClientes(String texto) {
        if (texto == null || texto.isEmpty()) {
            tabla.setItems(listaClientes);
            return;
        }
        ObservableList<Cliente> filtrados = FXCollections.observableArrayList();
        for (Cliente c : listaClientes) {
            if (c.getNombre().toLowerCase().contains(texto.toLowerCase()) ||
                c.getCedula().contains(texto)) {
                filtrados.add(c);
            }
        }
        tabla.setItems(filtrados);
    }

    private void limpiar() {
        txtNombre.clear();
        txtApellido.clear();
        txtCedula.clear();
        txtTelefono.clear();
        txtCorreo.clear();
        dpFechaInscripcion.setValue(LocalDate.now());
        cmbEstado.setValue("Activo");
    }
}