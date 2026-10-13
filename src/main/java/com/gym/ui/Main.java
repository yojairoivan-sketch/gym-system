package com.gym.ui;

import com.gym.model.Usuario;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class Main extends Application {

    private static final String COLOR_FONDO  = "#1a1a2e";
    private static final String COLOR_NAVBAR = "#0f3460";
    private static final String COLOR_BOTON  = "#7c3aed";

    private static final String ESTILO_BOTON_BASE =
            "-fx-text-fill: white; -fx-font-size: 13; -fx-padding: 6 12; -fx-background-radius: 6;";

    @Override
    public void start(Stage stage) {
        mostrarLogin(stage);
        stage.show();
    }

    // Muestra la pantalla de login; al autenticarse carga el sistema según el rol
    private void mostrarLogin(Stage stage) {
        LoginUI loginUI = new LoginUI(usuario -> cargarSistema(stage, usuario));

        stage.setScene(new Scene(loginUI.getVista(), 400, 350));
        stage.setTitle("Login - GymPro");
    }

    private void cargarSistema(Stage stage, Usuario usuario) {

        HBox navbar = new HBox(8);
        navbar.setStyle("-fx-background-color: " + COLOR_NAVBAR + ";");
        navbar.setPadding(new Insets(12, 20, 12, 20));

        Label logo = new Label("💪 GymPro");
        logo.setFont(Font.font("Impact", FontWeight.BOLD, 18));
        logo.setTextFill(Color.WHITE);

        Button btnInscripcion  = crearBotonNav("Inscripción");
        Button btnClientes     = crearBotonNav("Clientes");
        Button btnMembresias   = crearBotonNav("Membresías");
        Button btnProductos    = crearBotonNav("Productos");
        Button btnVentas       = crearBotonNav("Ventas");
        Button btnPagos        = crearBotonNav("Pagos");
        Button btnAsistencia   = crearBotonNav("Asistencia");
        Button btnEntrenadores = crearBotonNav("Entrenadores");

        Button btnLogout = new Button("Cerrar sesión");
        btnLogout.setStyle(
                "-fx-background-color: #4c1987;" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-padding: 6 14;" +
                "-fx-background-radius: 8;"
        );

        // Control de acceso por rol
        HBox centro = new HBox(10);
        centro.setAlignment(Pos.CENTER);

        switch (usuario.getRol()) {
            case ENTRENADOR:
                centro.getChildren().addAll(btnClientes, btnAsistencia);
                break;
            case RECEPCIONISTA:
                centro.getChildren().addAll(btnInscripcion, btnClientes, btnMembresias,
                        btnVentas, btnPagos, btnAsistencia);
                break;
            case ADMINISTRADOR:
                centro.getChildren().addAll(btnInscripcion, btnClientes, btnMembresias,
                        btnProductos, btnVentas, btnPagos, btnAsistencia, btnEntrenadores);
                break;
        }

        HBox izquierda = new HBox(logo);
        HBox derecha = new HBox(btnLogout);
        derecha.setAlignment(Pos.CENTER_RIGHT);

        // Espaciadores para centrar el menú
        Region espacioIzq = new Region();
        Region espacioDer = new Region();
        HBox.setHgrow(espacioIzq, Priority.ALWAYS);
        HBox.setHgrow(espacioDer, Priority.ALWAYS);

        navbar.getChildren().addAll(izquierda, espacioIzq, centro, espacioDer, derecha);

        ScrollPane scroll = new ScrollPane();
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: " + COLOR_FONDO + ";");

        Button[] todosBotones = {
                btnInscripcion, btnClientes, btnMembresias, btnProductos,
                btnVentas, btnPagos, btnAsistencia, btnEntrenadores
        };

        // Vista inicial según rol
        if (usuario.getRol() == Usuario.Rol.ENTRENADOR) {
            cambiarVista(scroll, btnAsistencia, todosBotones, new AsistenciaUI().getVista());
        } else {
            cambiarVista(scroll, btnInscripcion, todosBotones, new InscripcionUI().getVista());
        }

        // Cada navegación crea la pantalla de nuevo: así muestra datos actualizados
        // de la base y no se duplican columnas ni opciones de los formularios
        btnInscripcion.setOnAction(e ->
                cambiarVista(scroll, btnInscripcion, todosBotones, new InscripcionUI().getVista()));
        btnClientes.setOnAction(e ->
                cambiarVista(scroll, btnClientes, todosBotones, new ClienteUI().getVista()));
        btnMembresias.setOnAction(e ->
                cambiarVista(scroll, btnMembresias, todosBotones, new MembresiaUI().getVista()));
        btnProductos.setOnAction(e ->
                cambiarVista(scroll, btnProductos, todosBotones, new ProductoUI().getVista()));
        btnVentas.setOnAction(e ->
                cambiarVista(scroll, btnVentas, todosBotones, new VentaUI().getVista()));
        btnPagos.setOnAction(e ->
                cambiarVista(scroll, btnPagos, todosBotones, new PagoUI().getVista()));
        btnAsistencia.setOnAction(e ->
                cambiarVista(scroll, btnAsistencia, todosBotones, new AsistenciaUI().getVista()));
        btnEntrenadores.setOnAction(e ->
                cambiarVista(scroll, btnEntrenadores, todosBotones, new EntrenadorUI().getVista()));

        btnLogout.setOnAction(e -> mostrarLogin(stage));

        VBox root = new VBox(navbar, scroll);
        VBox.setVgrow(scroll, Priority.ALWAYS);

        Scene scene = new Scene(root, 1050, 700);
        scene.getStylesheets().add(getClass().getResource("/com/gym/ui/CSS/app.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("GymPro - Sistema de Gestión");
    }

    private void cambiarVista(ScrollPane scroll, Button activo, Button[] todos, Pane vista) {
        scroll.setContent(vista);
        resaltarBoton(activo, todos);
    }

    private Button crearBotonNav(String texto) {
        Button btn = new Button(texto);
        btn.setStyle("-fx-background-color: transparent; " + ESTILO_BOTON_BASE);
        return btn;
    }

    private void resaltarBoton(Button activo, Button[] todos) {
        for (Button btn : todos) {
            String fondo = (btn == activo) ? COLOR_BOTON : "transparent";
            btn.setStyle("-fx-background-color: " + fondo + "; " + ESTILO_BOTON_BASE);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
