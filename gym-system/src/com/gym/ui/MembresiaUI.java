package com.gym.ui;

import com.gym.model.Membresia;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.time.LocalDate;

public class MembresiaUI {

    // Colores del tema oscuro 
    private static final String COLOR_FONDO     = "#1a1a2e"; // fondo general
    private static final String COLOR_PANEL     = "#16213e"; // fondo de paneles
    private static final String COLOR_CAMPO     = "#0f3460"; // fondo de inputs
    private static final String COLOR_BOTON     = "#7c3aed"; // color botón principal
    private static final String COLOR_TEXTO     = "#ffffff"; // texto blanco
    private static final String COLOR_SUBTITULO = "#a78bfa"; // morado claro para títulos

    // --- CONEXIÓN CON BASE DE DATOS ---
    private com.gym.dao.MembresiaDAO membresiaDAO = new com.gym.dao.MembresiaDAO();
    // ----------------------------------

    // Lista observable: cuando se agrega un item, la tabla se actualiza automáticamente
    private ObservableList<Membresia> listaMembresias = FXCollections.observableArrayList();

    // Tabla que muestra las membresías registradas
    private TableView<Membresia> tabla = new TableView<>();

    // Campos del formulario de asignación
    private ComboBox<String> cmbTipo     = new ComboBox<>(); // tipo de membresía
    private ComboBox<String> cmbDuracion = new ComboBox<>(); // duración en meses
    private DatePicker dpFechaInicio     = new DatePicker(LocalDate.now()); // fecha de inicio
    private ComboBox<String> cmbEstado   = new ComboBox<>(); // estado de la membresía

    // Método principal que devuelve toda la vista de Membresías
    public VBox getVista() {
        VBox root = new VBox(20); // contenedor vertical con 20px de espacio entre elementos
        root.setStyle("-fx-background-color: " + COLOR_FONDO + ";");
        root.setPadding(new Insets(20));

        // --- CARGAR DATOS AL INICIAR ---
        cargarDatosDesdeBD();
        // -------------------------------

        // Agregamos el formulario arriba y la tabla abajo
        root.getChildren().addAll(
            crearFormulario(),
            crearTabla()
        );

        return root;
    }

    // Método para cargar la información real de SQL
    private void cargarDatosDesdeBD() {
        try {
            listaMembresias.setAll(membresiaDAO.listarTodos());
        } catch (Exception e) {
            System.err.println("Error al cargar membresías: " + e.getMessage());
        }
    }

    // Crea el panel superior con el formulario de asignación
    private VBox crearFormulario() {
        VBox panel = new VBox(15); // 15px entre cada fila
        panel.setStyle("-fx-background-color: " + COLOR_PANEL + "; -fx-background-radius: 10;");
        panel.setPadding(new Insets(20));

        // Título del formulario
        Label titulo = new Label("ASIGNAR MEMBRESÍA");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        titulo.setTextFill(Color.web(COLOR_SUBTITULO));

        HBox fila1 = new HBox(15);

        // Opciones de tipo de membresía con precio
        cmbTipo.getItems().addAll(
            "Básico — $1,800/mes",
            "Pro — $2,500/mes",
            "Elite — $4,200/mes"
        );
        cmbTipo.setValue("Básico — $1,800/mes");
        cmbTipo.setStyle(estiloInput());
        cmbTipo.setMaxWidth(Double.MAX_VALUE);

        // Opciones de duración
        cmbDuracion.getItems().addAll("1 mes", "3 meses", "6 meses", "12 meses");
        cmbDuracion.setValue("1 mes");
        cmbDuracion.setStyle(estiloInput());
        cmbDuracion.setMaxWidth(Double.MAX_VALUE);

        fila1.getChildren().addAll(
            crearCampoCombo("Tipo de membresía", cmbTipo),
            crearCampoCombo("Duración", cmbDuracion)
        );

        HBox fila2 = new HBox(15);
        dpFechaInicio.setStyle(estiloInput());
        dpFechaInicio.setMaxWidth(Double.MAX_VALUE);

        // Opciones de estado de la membresía
        cmbEstado.getItems().addAll("Activa", "Vencida", "Suspendida");
        cmbEstado.setValue("Activa");
        cmbEstado.setStyle(estiloInput());
        cmbEstado.setMaxWidth(Double.MAX_VALUE);

        fila2.getChildren().addAll(
            crearCampoFecha("Fecha de inicio", dpFechaInicio),
            crearCampoCombo("Estado", cmbEstado)
        );

        // Botón para guardar la membresía
        Button btnAsignar = new Button("Asignar");
        btnAsignar.setStyle(
            "-fx-background-color: " + COLOR_BOTON + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 8;" +
            "-fx-padding: 8 20;"
        );
        // Al hacer click llama al método asignarMembresia
        btnAsignar.setOnAction(e -> asignarMembresia());

        panel.getChildren().addAll(titulo, fila1, fila2, btnAsignar);
        return panel;
    }

    // Crea el panel inferior con la tabla de membresías registradas
    private VBox crearTabla() {
        VBox panel = new VBox(15);
        panel.setStyle("-fx-background-color: " + COLOR_PANEL + "; -fx-background-radius: 10;");
        panel.setPadding(new Insets(20));

        Label titulo = new Label("MEMBRESÍAS REGISTRADAS");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        titulo.setTextFill(Color.web(COLOR_SUBTITULO));

        // Columnas de la tabla 
        TableColumn<Membresia, String> colTipo = columna("PLAN", "tipo", 150);

        // Columna de fecha de inicio
        TableColumn<Membresia, LocalDate> colInicio = new TableColumn<>("INICIO");
        colInicio.setCellValueFactory(new PropertyValueFactory<>("fechaInicio"));
        colInicio.setPrefWidth(120);

        // Columna de fecha de vencimiento
        TableColumn<Membresia, LocalDate> colFin = new TableColumn<>("VENCIMIENTO");
        colFin.setCellValueFactory(new PropertyValueFactory<>("fechaFin"));
        colFin.setPrefWidth(120);

        TableColumn<Membresia, String> colEstado = columna("ESTADO", "estado", 100);

        // Columna con botón Renovar
        TableColumn<Membresia, Void> colRenovar = new TableColumn<>("ACCIÓN");
        colRenovar.setPrefWidth(100);
        colRenovar.setCellFactory(col -> new TableCell<>() {
            final Button btn = new Button("Renovar");
            {
                btn.setStyle(
                    "-fx-background-color: #374151;" +
                    "-fx-text-fill: white;" +
                    "-fx-background-radius: 5;" +
                    "-fx-padding: 4 10;"
                );
                btn.setOnAction(e -> {
                    Membresia m = getTableView().getItems().get(getIndex());
                    renovarMembresia(m);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        // 🔧 CORREGIDO AQUÍ
        tabla.getColumns().addAll(
            java.util.Arrays.asList(colTipo, colInicio, colFin, colEstado, colRenovar)
        );

        tabla.setItems(listaMembresias);
        tabla.setStyle("-fx-background-color: " + COLOR_CAMPO + ";");
        tabla.setPrefHeight(250);

        panel.getChildren().addAll(titulo, tabla);
        return panel;
    }

    private VBox crearCampoCombo(String etiqueta, ComboBox<String> combo) {
        VBox box = new VBox(4);
        HBox.setHgrow(box, Priority.ALWAYS);
        Label lbl = new Label(etiqueta);
        lbl.setTextFill(Color.web(COLOR_TEXTO));
        box.getChildren().addAll(lbl, combo);
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

    private TableColumn<Membresia, String> columna(String titulo, String propiedad, double ancho) {
        TableColumn<Membresia, String> col = new TableColumn<>(titulo);
        col.setCellValueFactory(new PropertyValueFactory<>(propiedad));
        col.setPrefWidth(ancho);
        return col;
    }

    private String estiloInput() {
        return "-fx-background-color: " + COLOR_CAMPO + ";" +
               "-fx-text-fill: white;" +
               "-fx-prompt-text-fill: #6b7280;" +
               "-fx-background-radius: 6;" +
               "-fx-padding: 8;";
    }

    private void asignarMembresia() {
        String tipo          = cmbTipo.getValue();
        String duracionTexto = cmbDuracion.getValue();
        LocalDate fechaInicio = dpFechaInicio.getValue();
        String estado        = cmbEstado.getValue();

        int duracionMeses;
        switch (duracionTexto) {
            case "3 meses":  duracionMeses = 3;  break;
            case "6 meses":  duracionMeses = 6;  break;
            case "12 meses": duracionMeses = 12; break;
            default:         duracionMeses = 1;  break;
        }

        LocalDate fechaFin = fechaInicio.plusMonths(duracionMeses);

        double precio;
        switch (tipo) {
            case "Pro — $2,500/mes":   precio = 2500; break;
            case "Elite — $4,200/mes": precio = 4200; break;
            default:                   precio = 1800; break;
        }

        Membresia m = new Membresia(tipo, duracionMeses, precio, fechaInicio, null);
        m.setFechaFin(fechaFin);
        m.setEstado(estado);

        try {
            membresiaDAO.guardar(m);
            listaMembresias.add(m);
            limpiar();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error de Conexión");
            alert.setContentText("No se pudo guardar la membresía: " + e.getMessage());
            alert.showAndWait();
        }
    }

    private void renovarMembresia(Membresia m) {
        m.setFechaInicio(LocalDate.now());
        m.setFechaFin(LocalDate.now().plusMonths(1));
        m.setEstado("Activa");

        try {
            membresiaDAO.actualizar(m);
            tabla.refresh();
        } catch (Exception e) {
            System.err.println("Error al renovar en BD: " + e.getMessage());
        }
    }

    private void limpiar() {
        cmbTipo.setValue("Básico — $1,800/mes");
        cmbDuracion.setValue("1 mes");
        dpFechaInicio.setValue(LocalDate.now());
        cmbEstado.setValue("Activa");
    }
}