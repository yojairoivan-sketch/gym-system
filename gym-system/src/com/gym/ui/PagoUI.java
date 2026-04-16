package com.gym.ui;

import com.gym.model.Cliente;
import com.gym.model.Membresia;
import com.gym.model.Pago;
import com.gym.service.ClienteService;
import com.gym.service.MembresiaService;
import com.gym.service.PagoService;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;

import java.time.LocalDate;
import java.util.List;

public class PagoUI {

    private PagoService pagoService           = new PagoService();
    private ClienteService clienteService     = new ClienteService();
    private MembresiaService membresiaService = new MembresiaService();

    // Componentes del formulario de registro
    private ComboBox<Cliente>   cbCliente;
    private ComboBox<Membresia> cbMembresia;
    private TextField           tfMonto;
    private DatePicker          dpFecha;
    private ComboBox<String>    cbMetodo;
    private TextField           tfReferencia;

    // Tabla del historial de pagos
    private TableView<Pago>         tablaPagos;
    private ObservableList<Pago>    listaPagos = FXCollections.observableArrayList();

    // Etiqueta de retroalimentacion al usuario
    private Label lblMensaje;

    //pantalla de pagos
    public VBox getVista() {
        VBox vista = new VBox(20);
        vista.setPadding(new Insets(30));

        // Carga estilo css
        vista.getStylesheets().add(
            getClass().getResource("/com/gym/ui/CSS/pagos.css").toExternalForm()
        );
        vista.getStyleClass().add("pago-root");

        // Titulo de la seccion
        Label titulo = new Label("Pagos");
        titulo.getStyleClass().add("pago-titulo");

        VBox seccionFormulario = crearFormularioRegistro();
        VBox seccionHistorial  = crearHistorialPagos();

        vista.getChildren().addAll(titulo, seccionFormulario, seccionHistorial);
        return vista;
    }

    // tarjeta para formulario nuevo pago
    private VBox crearFormularioRegistro() {
        VBox card = new VBox(14);
        card.setPadding(new Insets(22));
        card.getStyleClass().add("pago-card");

        Label subtitulo = new Label("REGISTRAR PAGO");
        subtitulo.getStyleClass().add("pago-subtitulo");

        // ── Fila 1: cliente y monto ──
        cbCliente = new ComboBox<>();
        cbCliente.setPromptText("-- Seleccionar --");
        cbCliente.setMaxWidth(Double.MAX_VALUE);
        cbCliente.getStyleClass().add("pago-combo");

        // Muestra el nombre completo en cada celda del combo
        cbCliente.setCellFactory(lv -> new ListCell<Cliente>() {
            @Override
            protected void updateItem(Cliente c, boolean empty) {
                super.updateItem(c, empty);
                setText(empty || c == null ? null : c.getNombreCompleto());
            }
        });
        cbCliente.setButtonCell(new ListCell<Cliente>() {
            @Override
            protected void updateItem(Cliente c, boolean empty) {
                super.updateItem(c, empty);
                setText(empty || c == null ? null : c.getNombreCompleto());
            }
        });
        // Carga membresia de cliente
        cbCliente.setOnAction(e -> cargarMembresiasDeCliente());

        tfMonto = new TextField();
        tfMonto.setPromptText("$0.00");
        tfMonto.getStyleClass().add("pago-input");

        HBox fila1 = crearFila(
            crearGrupo("Cliente", cbCliente),
            crearGrupo("Monto",   tfMonto)
        );

        // Fila 2: membresia asociada al pago
        cbMembresia = new ComboBox<>();
        cbMembresia.setPromptText("-- Seleccionar membresia --");
        cbMembresia.setMaxWidth(Double.MAX_VALUE);
        cbMembresia.getStyleClass().add("pago-combo");

        // Muestra tipo, estado y fecha fin de cada membresia
        cbMembresia.setCellFactory(lv -> new ListCell<Membresia>() {
            @Override
            protected void updateItem(Membresia m, boolean empty) {
                super.updateItem(m, empty);
                setText(empty || m == null ? null :
                    m.getTipo() + " — " + m.getEstado() + " — vence " + m.getFechaFin());
            }
        });
        cbMembresia.setButtonCell(new ListCell<Membresia>() {
            @Override
            protected void updateItem(Membresia m, boolean empty) {
                super.updateItem(m, empty);
                setText(empty || m == null ? null : m.getTipo() + " — " + m.getEstado());
            }
        });

        HBox fila2 = crearFila(crearGrupo("Membresia asociada", cbMembresia));

        // Fila 3: fecha, metodo de pago y referencia
        dpFecha = new DatePicker(LocalDate.now());
        dpFecha.setMaxWidth(Double.MAX_VALUE);
        dpFecha.getStyleClass().add("pago-datepicker");

        cbMetodo = new ComboBox<>(FXCollections.observableArrayList(
            "Efectivo", "Tarjeta", "Transferencia"
        ));
        cbMetodo.setValue("Efectivo");
        cbMetodo.setMaxWidth(Double.MAX_VALUE);
        cbMetodo.getStyleClass().add("pago-combo");

        tfReferencia = new TextField();
        tfReferencia.setPromptText("Numero de referencia");
        tfReferencia.getStyleClass().add("pago-input");

        HBox fila3 = crearFila(
            crearGrupo("Fecha de pago",  dpFecha),
            crearGrupo("Metodo de pago", cbMetodo),
            crearGrupo("Referencia",     tfReferencia)
        );

        lblMensaje = new Label("");

        // Boton para registrar el pago
        Button btnRegistrar = new Button("Registrar pago");
        btnRegistrar.getStyleClass().add("pago-btn-primary");
        btnRegistrar.setOnAction(e -> registrarPago());

        card.getChildren().addAll(subtitulo, fila1, fila2, fila3, lblMensaje, btnRegistrar);
        cargarClientes();
        return card;
    }

    // tarjeta con  tabla del historial de pagos
    private VBox crearHistorialPagos() {
        VBox card = new VBox(14);
        card.setPadding(new Insets(22));
        card.getStyleClass().add("pago-card");

        Label subtitulo = new Label("HISTORIAL DE PAGOS");
        subtitulo.getStyleClass().add("pago-subtitulo");

        // Columna ID 
        TableColumn<Pago, Integer> colId = new TableColumn<>("#");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colId.setPrefWidth(55);

        // Columna nombre del cliente
        TableColumn<Pago, String> colCliente = new TableColumn<>("Cliente");
        colCliente.setCellValueFactory(data ->
            new SimpleStringProperty(
                data.getValue().getCliente() != null ?
                data.getValue().getCliente().getNombreCompleto() : "—"
            )
        );
        colCliente.setPrefWidth(180);

        // Columna monto 
        TableColumn<Pago, Double> colMonto = new TableColumn<>("Monto");
        colMonto.setCellValueFactory(new PropertyValueFactory<>("monto"));
        colMonto.setPrefWidth(100);
        colMonto.setCellFactory(col -> new TableCell<Pago, Double>() {
            @Override
            protected void updateItem(Double monto, boolean empty) {
                super.updateItem(monto, empty);
                if (empty || monto == null) {
                    setText(null);
                } else {
                    setText(String.format("$%.2f", monto));
                    // Usa la clase CSS del monto en verde
                    getStyleClass().add("pago-monto-verde");
                }
            }
        });

        //  Columna metodo de pago 
        TableColumn<Pago, String> colMetodo = new TableColumn<>("Metodo");
        colMetodo.setCellValueFactory(new PropertyValueFactory<>("metodoPago"));
        colMetodo.setPrefWidth(120);

        // Columna fecha del pago
        TableColumn<Pago, LocalDate> colFecha = new TableColumn<>("Fecha");
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fechaPago"));
        colFecha.setPrefWidth(120);

        // Columna estado 
        TableColumn<Pago, String> colEstado = new TableColumn<>("Estado");
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colEstado.setPrefWidth(110);
        colEstado.setCellFactory(col -> new TableCell<Pago, String>() {
            @Override
            protected void updateItem(String estado, boolean empty) {
                super.updateItem(estado, empty);
                getStyleClass().removeAll("pago-estado-completado", "pago-estado-pendiente");
                if (empty || estado == null) {
                    setText(null);
                } else {
                    setText(estado);
                    // estilo css para estado
                    getStyleClass().add(
                        estado.equals("Completado") ?
                        "pago-estado-completado" : "pago-estado-pendiente"
                    );
                }
            }
        });

        tablaPagos = new TableView<>(listaPagos);
        tablaPagos.getColumns().addAll(colId, colCliente, colMonto, colMetodo, colFecha, colEstado);
        tablaPagos.getStyleClass().add("pago-tabla");
        tablaPagos.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Boton para refrescar el historial 
        Button btnRefrescar = new Button("Refrescar");
        btnRefrescar.getStyleClass().add("pago-btn-secondary");
        btnRefrescar.setOnAction(e -> cargarPagos());

        cargarPagos();

        card.getChildren().addAll(subtitulo, btnRefrescar, tablaPagos);
        return card;
    }

    // Mostrar todos los clientes activos en el combo
    private void cargarClientes() {
        List<Cliente> clientes = clienteService.listarClientes();
        cbCliente.setItems(FXCollections.observableArrayList(clientes));
    }

    // Filtra y carga las membresias del cliente seleccionado
    private void cargarMembresiasDeCliente() {
        Cliente seleccionado = cbCliente.getValue();
        if (seleccionado == null) return;
        List<Membresia> todas = membresiaService.listarTodas();
        List<Membresia> delCliente = todas.stream()
            .filter(m -> m.getCliente() != null &&
                    m.getCliente().getId() == seleccionado.getId())
            .toList();
        cbMembresia.setItems(FXCollections.observableArrayList(delCliente));
    }

    // Recarga todos los pagos en la tabla del historial
    private void cargarPagos() {
        listaPagos.setAll(pagoService.listarTodos());
    }

    // Valida el formulario y registra el pago v
    private void registrarPago() {
        if (cbCliente.getValue() == null) {
            mostrarMensaje("Selecciona un cliente.", false); return;
        }
        if (cbMembresia.getValue() == null) {
            mostrarMensaje("Selecciona una membresia.", false); return;
        }
        if (tfMonto.getText().isBlank()) {
            mostrarMensaje("Ingresa el monto.", false); return;
        }

        double monto;
        try {
            monto = Double.parseDouble(tfMonto.getText().replace(",", "."));
        } catch (NumberFormatException ex) {
            mostrarMensaje("El monto debe ser un numero valido.", false); return;
        }

        Pago pago = new Pago(
            monto,
            dpFecha.getValue(),
            cbMetodo.getValue(),
            tfReferencia.getText(),
            cbCliente.getValue(),
            cbMembresia.getValue()
        );

        boolean exito = pagoService.registrarPago(pago);
        if (exito) {
            mostrarMensaje("Pago registrado correctamente.", true);
            limpiarFormulario();
            cargarPagos();
        } else {
            mostrarMensaje("No se pudo registrar. Verifica la membresia activa.", false);
        }
    }

    // Limpia todos los campos del formulario tras guardar
    private void limpiarFormulario() {
        cbCliente.setValue(null);
        cbMembresia.setItems(FXCollections.observableArrayList());
        cbMembresia.setValue(null);
        tfMonto.clear();
        dpFecha.setValue(LocalDate.now());
        cbMetodo.setValue("Efectivo");
        tfReferencia.clear();
    }

    private void mostrarMensaje(String texto, boolean exito) {
        lblMensaje.setText(texto);
        lblMensaje.getStyleClass().removeAll("pago-mensaje-exito", "pago-mensaje-error");
        lblMensaje.getStyleClass().add(exito ? "pago-mensaje-exito" : "pago-mensaje-error");
    }

    // Crea una fila horizontal con todos los grupos recibidos
    private HBox crearFila(VBox... grupos) {
        HBox fila = new HBox(14);
        for (VBox g : grupos) {
            HBox.setHgrow(g, Priority.ALWAYS);
            fila.getChildren().add(g);
        }
        return fila;
    }

    // Crea un grupo etiqueta + control apilados verticalmente
    private VBox crearGrupo(String etiqueta, javafx.scene.Node control) {
        Label lbl = new Label(etiqueta.toUpperCase());
        lbl.getStyleClass().add("pago-label");
        VBox grupo = new VBox(6, lbl, control);
        VBox.setVgrow(control, Priority.ALWAYS);
        return grupo;
    }
}