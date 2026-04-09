package com.gym.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class InscripcionUI {

    // ── Colores del tema oscuro ──────────────────────────────
    private static final String COLOR_FONDO     = "#1a1a2e"; // fondo general
    private static final String COLOR_PANEL     = "#16213e"; // fondo de paneles
    private static final String COLOR_CAMPO     = "#0f3460"; // fondo de inputs
    private static final String COLOR_BOTON     = "#7c3aed"; // color botón principal
    private static final String COLOR_TEXTO     = "#ffffff"; // texto blanco
    private static final String COLOR_SUBTITULO = "#a78bfa"; // morado claro para títulos

    // Campos del formulario
    private TextField txtNombre    = new TextField(); // nombre completo
    private TextField txtTelefono  = new TextField(); // teléfono
    private TextField txtCorreo    = new TextField(); // correo electrónico
    private DatePicker dpFechaInicio = new DatePicker(java.time.LocalDate.now()); // fecha inicio
    private TextField txtReferido  = new TextField(); // referido por (opcional)
    private CheckBox chkTerminos   = new CheckBox("Acepto los términos y condiciones y la política de uso del gimnasio.");

    // Plan seleccionado por defecto
    private String planSeleccionado = "Básico";

    // Botones de selección de plan
    private Button btnBasico = new Button();
    private Button btnPro    = new Button();
    private Button btnElite  = new Button();

    // Método principal que devuelve toda la vista de Inscripción
    public VBox getVista() {
        VBox root = new VBox(20); // contenedor vertical
        root.setStyle("-fx-background-color: " + COLOR_FONDO + ";");
        root.setPadding(new Insets(20));

        root.getChildren().addAll(
            crearSelectorPlanes(), // sección de planes
            crearFormulario()      // formulario de datos
        );

        return root;
    }

    // Crea la sección donde el usuario elige su plan
    private VBox crearSelectorPlanes() {
        VBox panel = new VBox(15);
        panel.setStyle("-fx-background-color: " + COLOR_PANEL + "; -fx-background-radius: 10;");
        panel.setPadding(new Insets(20));

        // Texto pequeño encima del título
        Label subtitulo = new Label("ÚNETE HOY");
        subtitulo.setFont(Font.font("Arial", FontWeight.BOLD, 11));
        subtitulo.setTextFill(Color.web(COLOR_SUBTITULO));

        // Título principal
        Label titulo = new Label("Elige tu plan y empieza");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        titulo.setTextFill(Color.web(COLOR_TEXTO));

        // ── Botones de planes ──
        // Cada botón muestra el nombre y precio del plan
        configurarBotonPlan(btnBasico, "Básico", "$1,800", false);
        configurarBotonPlan(btnPro, "Pro", "$2,500", true); // true = más popular
        configurarBotonPlan(btnElite, "Elite", "$4,200", false);

        // Al hacer click selecciona ese plan y resalta el botón
        btnBasico.setOnAction(e -> seleccionarPlan("Básico"));
        btnPro.setOnAction(e -> seleccionarPlan("Pro"));
        btnElite.setOnAction(e -> seleccionarPlan("Elite"));

        // Fila horizontal con los tres botones de plan
        HBox planesBox = new HBox(15, btnBasico, btnPro, btnElite);
        planesBox.setAlignment(Pos.CENTER);

        // Resaltar el plan Básico por defecto al iniciar
        seleccionarPlan("Básico");

        panel.getChildren().addAll(subtitulo, titulo, planesBox);
        return panel;
    }

    // Configura visualmente un botón de plan
    private void configurarBotonPlan(Button btn, String nombre, String precio, boolean popular) {
        // Si es popular agrega una etiqueta encima
        String etiqueta = popular ? "Más popular\n" : "";
        btn.setText(etiqueta + nombre + "\n" + precio);
        btn.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        btn.setTextFill(Color.web(COLOR_TEXTO));
        btn.setPrefWidth(160);
        btn.setPrefHeight(70);
        btn.setStyle(
            "-fx-background-color: " + COLOR_CAMPO + ";" +
            "-fx-background-radius: 10;" +
            "-fx-text-fill: white;"
        );
    }

    // Resalta el plan seleccionado y quita el resaltado de los otros
    private void seleccionarPlan(String plan) {
        planSeleccionado = plan;

        // Estilo normal para todos los botones
        String estiloNormal =
            "-fx-background-color: " + COLOR_CAMPO + ";" +
            "-fx-background-radius: 10;" +
            "-fx-text-fill: white;";

        // Estilo resaltado para el plan seleccionado
        String estiloActivo =
            "-fx-background-color: " + COLOR_BOTON + ";" +
            "-fx-background-radius: 10;" +
            "-fx-text-fill: white;" +
            "-fx-border-color: white;" +
            "-fx-border-radius: 10;" +
            "-fx-border-width: 2;";

        // Aplica el estilo correspondiente a cada botón
        btnBasico.setStyle(plan.equals("Básico") ? estiloActivo : estiloNormal);
        btnPro.setStyle(plan.equals("Pro")    ? estiloActivo : estiloNormal);
        btnElite.setStyle(plan.equals("Elite") ? estiloActivo : estiloNormal);
    }

    // Crea el formulario de datos del nuevo inscrito
    private VBox crearFormulario() {
        VBox panel = new VBox(15);
        panel.setStyle("-fx-background-color: " + COLOR_PANEL + "; -fx-background-radius: 10;");
        panel.setPadding(new Insets(20));

        // ── Fila 1: Nombre completo y Teléfono ──
        HBox fila1 = new HBox(15);
        txtNombre.setPromptText("Tu nombre");
        txtTelefono.setPromptText("+1 809");
        fila1.getChildren().addAll(
            crearCampo("Nombre completo", txtNombre),
            crearCampo("Teléfono", txtTelefono)
        );

        // ── Fila 2: Correo electrónico ──
        HBox fila2 = new HBox(15);
        txtCorreo.setPromptText("correo@email.com");
        fila2.getChildren().add(crearCampo("Correo electrónico", txtCorreo));

        // ── Fila 3: Fecha de inicio y Referido ──
        HBox fila3 = new HBox(15);
        dpFechaInicio.setStyle(estiloInput());
        dpFechaInicio.setMaxWidth(Double.MAX_VALUE);
        txtReferido.setPromptText("Opcional");
        fila3.getChildren().addAll(
            crearCampoFecha("Fecha de inicio", dpFechaInicio),
            crearCampo("Referido por", txtReferido)
        );

        // ── Checkbox de términos y condiciones ──
        chkTerminos.setTextFill(Color.web(COLOR_TEXTO));
        chkTerminos.setStyle("-fx-font-size: 12;");

        // ── Botón principal de inscripción ──
        Button btnInscribir = new Button("Inscribirme ahora");
        btnInscribir.setMaxWidth(Double.MAX_VALUE); // ocupa todo el ancho
        btnInscribir.setStyle(
            "-fx-background-color: " + COLOR_BOTON + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-font-size: 14;" +
            "-fx-background-radius: 8;" +
            "-fx-padding: 12;"
        );
        // Al hacer click valida y procesa la inscripción
        btnInscribir.setOnAction(e -> inscribir());

        // Enlace de inicio de sesión para usuarios existentes
        Label lblLogin = new Label("¿Ya tienes cuenta? Inicia sesión");
        lblLogin.setTextFill(Color.web(COLOR_SUBTITULO));
        lblLogin.setAlignment(Pos.CENTER);

        panel.getChildren().addAll(fila1, fila2, fila3, chkTerminos, btnInscribir, lblLogin);
        return panel;
    }

    // Crea un campo de texto con su etiqueta
    private VBox crearCampo(String etiqueta, TextField campo) {
        VBox box = new VBox(4); // 4px entre etiqueta y campo
        HBox.setHgrow(box, Priority.ALWAYS); // ocupa todo el espacio horizontal
        Label lbl = new Label(etiqueta);
        lbl.setTextFill(Color.web(COLOR_TEXTO));
        campo.setStyle(estiloInput());
        campo.setMaxWidth(Double.MAX_VALUE);
        box.getChildren().addAll(lbl, campo);
        return box;
    }

    // Crea un campo de fecha con su etiqueta
    private VBox crearCampoFecha(String etiqueta, DatePicker dp) {
        VBox box = new VBox(4);
        HBox.setHgrow(box, Priority.ALWAYS);
        Label lbl = new Label(etiqueta);
        lbl.setTextFill(Color.web(COLOR_TEXTO));
        dp.setMaxWidth(Double.MAX_VALUE);
        box.getChildren().addAll(lbl, dp);
        return box;
    }

    // Estilo CSS reutilizable para inputs
    private String estiloInput() {
        return "-fx-background-color: " + COLOR_CAMPO + ";" +
               "-fx-text-fill: white;" +
               "-fx-prompt-text-fill: #6b7280;" +
               "-fx-background-radius: 6;" +
               "-fx-padding: 8;";
    }

    // Lógica de validación e inscripción
    private void inscribir() {
        String nombre   = txtNombre.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String correo   = txtCorreo.getText().trim();

        // Validación: campos obligatorios no pueden estar vacíos
        if (nombre.isEmpty() || telefono.isEmpty() || correo.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Campos obligatorios");
            alert.setContentText("Nombre, teléfono y correo son obligatorios.");
            alert.showAndWait();
            return;
        }

        // Validación: el usuario debe aceptar los términos
        if (!chkTerminos.isSelected()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Términos y condiciones");
            alert.setContentText("Debes aceptar los términos y condiciones.");
            alert.showAndWait();
            return;
        }

        // Si todo está bien muestra mensaje de éxito
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Inscripción exitosa");
        alert.setContentText("¡" + nombre + " inscrito en plan " + planSeleccionado + " exitosamente!");
        alert.showAndWait();

        // Limpia el formulario después de inscribir
        limpiar();
    }

    // Limpia todos los campos del formulario
    private void limpiar() {
        txtNombre.clear();
        txtTelefono.clear();
        txtCorreo.clear();
        txtReferido.clear();
        dpFechaInicio.setValue(java.time.LocalDate.now());
        chkTerminos.setSelected(false);
        seleccionarPlan("Básico"); // regresa al plan por defecto
    }
}