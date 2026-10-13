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

public class VentaUI {

    // Colores del tema oscuro 
    private static final String COLOR_FONDO     = "#1a1a2e"; 
    private static final String COLOR_PANEL     = "#16213e"; 
    private static final String COLOR_CAMPO     = "#0f3460"; 
    private static final String COLOR_BOTON     = "#7c3aed"; 
    private static final String COLOR_TEXTO     = "#ffffff"; 
    private static final String COLOR_SUBTITULO = "#a78bfa"; 

    private com.gym.dao.MembresiaDAO membresiaDAO = new com.gym.dao.MembresiaDAO();

    private ObservableList<Membresia> listaMembresias = FXCollections.observableArrayList();
    private TableView<Membresia> tabla = new TableView<>();

    private ComboBox<String> cmbTipo     = new ComboBox<>(); 
    private ComboBox<String> cmbDuracion = new ComboBox<>(); 
    private DatePicker dpFechaInicio     = new DatePicker(LocalDate.now()); 
    private ComboBox<String> cmbEstado   = new ComboBox<>(); 

    public VBox getVista() {
        VBox root = new VBox(20); 
        root.setStyle("-fx-background-color: " + COLOR_FONDO + ";");
        root.setPadding(new Insets(20));

        cargarDatosDesdeBD();

        root.getChildren().addAll(
            crearFormulario(),
            crearTabla()
        );

        return root;
    }

    private void cargarDatosDesdeBD() {
        try {
            listaMembresias.setAll(membresiaDAO.listarTodos());
        } catch (Exception e) {
            System.err.println("Error al cargar membresías: " + e.getMessage());
        }
    }

    private VBox crearFormulario() {
        VBox panel = new VBox(15); 
        panel.setStyle("-fx-background-color: " + COLOR_PANEL + "; -fx-background-radius: 10;");
        panel.setPadding(new Insets(20));

        Label titulo = new Label("ASIGNAR MEMBRESÍA");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        titulo.setTextFill(Color.web(COLOR_SUBTITULO));

        HBox fila1 = new HBox(15);
        cmbTipo.getItems().addAll("Básico — $1,800/mes", "Pro — $2,500/mes", "Elite — $4,200/mes");
        cmbTipo.setValue("Básico — $1,800/mes");
        cmbTipo.setStyle(estiloInput());
        cmbTipo.setMaxWidth(Double.MAX_VALUE);

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

        cmbEstado.getItems().addAll("Activa", "Vencida", "Suspendida");
        cmbEstado.setValue("Activa");
        cmbEstado.setStyle(estiloInput());
        cmbEstado.setMaxWidth(Double.MAX_VALUE);

        fila2.getChildren().addAll(
            crearCampoFecha("Fecha de inicio", dpFechaInicio),
            crearCampoCombo("Estado", cmbEstado)
        );

        Button btnAsignar = new Button("Asignar");
        btnAsignar.setStyle("-fx-background-color: " + COLOR_BOTON + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 8 20;");
        btnAsignar.setOnAction(e -> registrarVenta());

        panel.getChildren().addAll(titulo, fila1, fila2, btnAsignar);
        return panel;
    }

    private VBox crearTabla() {
        VBox panel = new VBox(15);
        panel.setStyle("-fx-background-color: " + COLOR_PANEL + "; -fx-background-radius: 10;");
        panel.setPadding(new Insets(20));

        Label titulo = new Label("MEMBRESÍAS REGISTRADAS");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        titulo.setTextFill(Color.web(COLOR_SUBTITULO));

        TableColumn<Membresia, String> colTipo = columna("PLAN", "tipo", 150);
        TableColumn<Membresia, LocalDate> colInicio = new TableColumn<>("INICIO");
        colInicio.setCellValueFactory(new PropertyValueFactory<>("fechaInicio"));
        colInicio.setPrefWidth(120);

        TableColumn<Membresia, LocalDate> colFin = new TableColumn<>("VENCIMIENTO");
        colFin.setCellValueFactory(new PropertyValueFactory<>("fechaFin"));
        colFin.setPrefWidth(120);

        TableColumn<Membresia, String> colEstado = columna("ESTADO", "estado", 100);

        TableColumn<Membresia, Void> colRenovar = new TableColumn<>("ACCIÓN");
        colRenovar.setPrefWidth(100);
        colRenovar.setCellFactory(col -> new TableCell<>() {
            final Button btn = new Button("Renovar");
            {
                btn.setStyle("-fx-background-color: #374151; -fx-text-fill: white; -fx-background-radius: 5; -fx-padding: 4 10;");
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

        tabla.getColumns().clear();
        tabla.getColumns().add(colTipo);
        tabla.getColumns().add(colInicio);
        tabla.getColumns().add(colFin);
        tabla.getColumns().add(colEstado);
        tabla.getColumns().add(colRenovar);
        tabla.setItems(listaMembresias);
        tabla.setStyle("-fx-background-color: " + COLOR_CAMPO + ";");
        tabla.setPrefHeight(250);

        panel.getChildren().addAll(titulo, tabla);
        return panel;
    }

    // --- MÉTODOS DE APOYO Y ESTILO ---
    
    public String estiloInput() {
        return "-fx-background-color: " + COLOR_CAMPO + "; -fx-text-fill: white; -fx-background-radius: 6; -fx-padding: 8;";
    }

    public VBox crearCampoCombo(String etiqueta, ComboBox<String> combo) {
        VBox box = new VBox(4);
        HBox.setHgrow(box, Priority.ALWAYS);
        Label lbl = new Label(etiqueta);
        lbl.setTextFill(Color.web(COLOR_TEXTO));
        box.getChildren().addAll(lbl, combo);
        return box;
    }

    // Este es el método que te faltaba y el Main pedía:
    public VBox crearCampo(String etiqueta, TextField txt) {
        VBox box = new VBox(4);
        HBox.setHgrow(box, Priority.ALWAYS);
        Label lbl = new Label(etiqueta);
        lbl.setTextFill(Color.web(COLOR_TEXTO));
        txt.setStyle(estiloInput());
        box.getChildren().addAll(lbl, txt);
        return box;
    }

    private VBox crearCampoFecha(String etiqueta, DatePicker dp) {
        VBox box = new VBox(4);
        HBox.setHgrow(box, Priority.ALWAYS);
        Label lbl = new Label(etiqueta);
        lbl.setTextFill(Color.web(COLOR_TEXTO));
        box.getChildren().addAll(lbl, dp);
        return box;
    }

    private TableColumn<Membresia, String> columna(String titulo, String propiedad, double ancho) {
        TableColumn<Membresia, String> col = new TableColumn<>(titulo);
        col.setCellValueFactory(new PropertyValueFactory<>(propiedad));
        col.setPrefWidth(ancho);
        return col;
    }

    // --- MÉTODOS QUE EL MAIN BUSCA (PUENTE) ---

    public void actualizarInfo() {
        cargarDatosDesdeBD();
    }

    public void calcularTotal() {
        // Implementar lógica de cálculo si es necesario
    }

    public void registrarVenta() {
        asignarMembresia();
    }

    // --- LÓGICA DE NEGOCIO ---

    private void asignarMembresia() {
        String tipo = cmbTipo.getValue();
        String duracionTexto = cmbDuracion.getValue();
        LocalDate fechaInicio = dpFechaInicio.getValue();
        String estado = cmbEstado.getValue();

        int duracionMeses = switch (duracionTexto) {
            case "3 meses" -> 3;
            case "6 meses" -> 6;
            case "12 meses" -> 12;
            default -> 1;
        };

        LocalDate fechaFin = fechaInicio.plusMonths(duracionMeses);
        double precio = switch (tipo) {
            case "Pro — $2,500/mes" -> 2500;
            case "Elite — $4,200/mes" -> 4200;
            default -> 1800;
        };

        Membresia m = new Membresia(tipo, duracionMeses, precio, fechaInicio, null);
        m.setFechaFin(fechaFin);
        m.setEstado(estado);

        try {
            membresiaDAO.guardar(m);
            listaMembresias.add(m);
            limpiar();
        } catch (Exception e) {
            mostrarAlerta("Error de Conexión", "No se pudo guardar: " + e.getMessage());
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
            System.err.println("Error al renovar: " + e.getMessage());
        }
    }

    private void limpiar() {
        cmbTipo.setValue("Básico — $1,800/mes");
        cmbDuracion.setValue("1 mes");
        dpFechaInicio.setValue(LocalDate.now());
        cmbEstado.setValue("Activa");
    }

    private void mostrarAlerta(String titulo, String contenido) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
}