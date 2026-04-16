package com.gym.ui;

import com.gym.model.Asistencia;
import com.gym.model.Cliente;
import com.gym.service.AsistenciaService;
import com.gym.service.ClienteService;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.time.LocalDate;
import java.util.List;

// Pantalla de asistencia: entrada/salida por cedula y registro del dia
public class AsistenciaUI {

    // Servicios para acceder a la logica de negocio
    private AsistenciaService asistenciaService = new AsistenciaService();
    private ClienteService    clienteService    = new ClienteService();

    // Campo de cedula en la caja rapida de entrada/salida
    private TextField tfCedula;

    // Tabla e historial de asistencia filtrado por fecha
    private TableView<Asistencia>      tablaAsistencia;
    private ObservableList<Asistencia> listaAsistencia = FXCollections.observableArrayList();

    // Selector de fecha para filtrar el registro del dia
    private DatePicker dpFiltro;

    // Etiqueta de retroalimentacion en la caja rapida
    private Label lblMensaje;

    // Construye y retorna la vista completa de la pantalla de asistencia
    public VBox getVista() {
        VBox vista = new VBox(20);
        vista.setPadding(new Insets(30));

        // Carga estilo css
        vista.getStylesheets().add(
            getClass().getResource("/com/gym/ui/CSS/asistencia.css").toExternalForm()
        );
        vista.getStyleClass().add("asistencia-root");

        // Titulo de la seccion
        Label titulo = new Label("Asistencia");
        titulo.getStyleClass().add("asistencia-titulo");

        HBox cajaRapida   = crearCajaRapida();
        VBox seccionTabla = crearRegistroDia();

        vista.getChildren().addAll(titulo, cajaRapida, seccionTabla);
        return vista;
    }

    // Crea la caja rapida de entrada/salida destacada visualmente
    private HBox crearCajaRapida() {
        HBox caja = new HBox(12);
        caja.setPadding(new Insets(18));
        caja.getStyleClass().add("asistencia-caja-rapida");
        caja.setAlignment(Pos.BOTTOM_LEFT);

        // Grupo del campo de cedula
        Label lblCedula = new Label("CEDULA DEL CLIENTE");
        lblCedula.getStyleClass().add("asistencia-label-cedula");

        tfCedula = new TextField();
        tfCedula.setPromptText("000-0000000-0");
        tfCedula.getStyleClass().add("asistencia-input-cedula");

        VBox grupoCedula = new VBox(6, lblCedula, tfCedula);

        // Boton de Entrada
        Button btnEntrada = new Button("Entrada");
        btnEntrada.getStyleClass().add("asistencia-btn-entrada");
        btnEntrada.setOnAction(e -> registrarEntrada());

        //Boton de Salida
        Button btnSalida = new Button("Salida");
        btnSalida.getStyleClass().add("asistencia-btn-salida");
        btnSalida.setOnAction(e -> registrarSalida());

        // Etiqueta de retroalimentacion junto a los botones
        lblMensaje = new Label("");

        VBox botonesYMsg = new VBox(8, new HBox(10, btnEntrada, btnSalida), lblMensaje);
        botonesYMsg.setAlignment(Pos.BOTTOM_LEFT);

        caja.getChildren().addAll(grupoCedula, botonesYMsg);
        return caja;
    }

    // Crea la tarjeta con la tabla del registro de asistencia del dia
    private VBox crearRegistroDia() {
        VBox card = new VBox(14);
        card.setPadding(new Insets(22));
        card.getStyleClass().add("asistencia-card");

        Label subtitulo = new Label("REGISTRO DEL DIA");
        subtitulo.getStyleClass().add("asistencia-subtitulo");

        // Filtro de fecha
        Label lblFiltro = new Label("FILTRAR POR FECHA");
        lblFiltro.getStyleClass().add("asistencia-label-filtro");

        dpFiltro = new DatePicker(LocalDate.now());
        dpFiltro.getStyleClass().add("asistencia-datepicker");
        // Al cambiar la fecha se recarga la tabla automaticamente
        dpFiltro.setOnAction(e -> cargarAsistenciaPorFecha(dpFiltro.getValue()));

        VBox grupoFiltro = new VBox(6, lblFiltro, dpFiltro);
        grupoFiltro.setMaxWidth(220);

        // Columna nombre del cliente
        TableColumn<Asistencia, String> colCliente = new TableColumn<>("Cliente");
        colCliente.setCellValueFactory(data ->
            new SimpleStringProperty(
                data.getValue().getCliente() != null ?
                data.getValue().getCliente().getNombreCompleto() : "—"
            )
        );
        colCliente.setPrefWidth(180);

        // Columna cedula del cliente
        TableColumn<Asistencia, String> colCedula = new TableColumn<>("Cedula");
        colCedula.setCellValueFactory(data ->
            new SimpleStringProperty(
                data.getValue().getCliente() != null ?
                data.getValue().getCliente().getCedula() : "—"
            )
        );
        colCedula.setPrefWidth(140);

        // Columna hora de entrada
        TableColumn<Asistencia, String> colEntrada = new TableColumn<>("Entrada");
        colEntrada.setCellValueFactory(data ->
            new SimpleStringProperty(
                data.getValue().getHoraEntrada() != null ?
                data.getValue().getHoraEntrada().toString() : "—"
            )
        );
        colEntrada.setPrefWidth(110);

        // Columna hora de salida (puede ser vacia si sigue dentro)
        TableColumn<Asistencia, String> colSalida = new TableColumn<>("Salida");
        colSalida.setCellValueFactory(data ->
            new SimpleStringProperty(
                data.getValue().getHoraSalida() != null ?
                data.getValue().getHoraSalida().toString() : "—"
            )
        );
        colSalida.setPrefWidth(110);

        //Columna estado: "En gym" si no ha salido, "Completado" si salio
        TableColumn<Asistencia, String> colEstado = new TableColumn<>("Estado");
        colEstado.setCellValueFactory(data ->
            new SimpleStringProperty(data.getValue().yaSalio() ? "Completado" : "En gym")
        );
        colEstado.setPrefWidth(110);
        colEstado.setCellFactory(col -> new TableCell<Asistencia, String>() {
            @Override
            protected void updateItem(String estado, boolean empty) {
                super.updateItem(estado, empty);
                getStyleClass().removeAll(
                    "asistencia-estado-completado", "asistencia-estado-engym"
                );
                if (empty || estado == null) {
                    setText(null);
                } else {
                    setText(estado);
                    // estilo estado cliente ya salio o sigue en el gym
                    getStyleClass().add(
                        estado.equals("Completado") ?
                        "asistencia-estado-completado" : "asistencia-estado-engym"
                    );
                }
            }
        });

        tablaAsistencia = new TableView<>(listaAsistencia);
        tablaAsistencia.getColumns().addAll(
            colCliente, colCedula, colEntrada, colSalida, colEstado
        );
        tablaAsistencia.getStyleClass().add("asistencia-tabla");
        tablaAsistencia.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Carga el registro del dia actual al abrir la pantalla
        cargarAsistenciaPorFecha(LocalDate.now());

        card.getChildren().addAll(subtitulo, grupoFiltro, tablaAsistencia);
        return card;
    }

    // Busca el cliente por cedula y registra su hora de entrada
    private void registrarEntrada() {
        String cedula = tfCedula.getText().trim();
        if (cedula.isBlank()) {
            mostrarMensaje("Ingresa una cedula.", false); return;
        }

        // Busca el cliente por cedula en la lista activa
        Cliente cliente = clienteService.listarClientes().stream()
            .filter(c -> c.getCedula().equalsIgnoreCase(cedula))
            .findFirst()
            .orElse(null);

        if (cliente == null) {
            mostrarMensaje("Cliente no encontrado.", false); return;
        }

        // El servicio valida que tenga membresia activa antes de registrar
        boolean exito = asistenciaService.registrarEntrada(cliente);
        if (exito) {
            mostrarMensaje("Entrada registrada: " + cliente.getNombreCompleto(), true);
            cargarAsistenciaPorFecha(dpFiltro.getValue());
        } else {
            mostrarMensaje("Acceso rechazado: membresia inactiva o vencida.", false);
        }
        tfCedula.clear();
    }

    // Busca el cliente por cedula y registra su hora de salida
    private void registrarSalida() {
        String cedula = tfCedula.getText().trim();
        if (cedula.isBlank()) {
            mostrarMensaje("Ingresa una cedula.", false); return;
        }

        Cliente cliente = clienteService.listarClientes().stream()
            .filter(c -> c.getCedula().equalsIgnoreCase(cedula))
            .findFirst()
            .orElse(null);

        if (cliente == null) {
            mostrarMensaje("Cliente no encontrado.", false); return;
        }

        // El servicio busca la entrada de hoy para este cliente
        boolean exito = asistenciaService.registrarSalida(cliente);
        if (exito) {
            mostrarMensaje("Salida registrada: " + cliente.getNombreCompleto(), true);
            cargarAsistenciaPorFecha(dpFiltro.getValue());
        } else {
            mostrarMensaje("No se encontro entrada para este cliente hoy.", false);
        }
        tfCedula.clear();
    }

    // Filtra y carga los registros de asistencia por la fecha indicada
    private void cargarAsistenciaPorFecha(LocalDate fecha) {
        if (fecha == null) return;
        List<Asistencia> registros = asistenciaService.listarPorFecha(fecha);
        listaAsistencia.setAll(registros);
    }

    private void mostrarMensaje(String texto, boolean exito) {
        lblMensaje.setText(texto);
        lblMensaje.getStyleClass().removeAll(
            "asistencia-mensaje-exito", "asistencia-mensaje-error"
        );
        lblMensaje.getStyleClass().add(
            exito ? "asistencia-mensaje-exito" : "asistencia-mensaje-error"
        );
    }
}