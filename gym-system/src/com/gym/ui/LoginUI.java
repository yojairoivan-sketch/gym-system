package com.gym.ui;

import com.gym.dao.UsuarioDAO;
import com.gym.model.Usuario;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.function.Consumer;

public class LoginUI {

    private TextField txtUsuario = new TextField();
    private PasswordField txtPassword = new PasswordField();
    private Label lblMensaje = new Label();
    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private Consumer<Usuario> onLoginSuccess;

    public LoginUI(Consumer<Usuario> onLoginSuccess) {
        this.onLoginSuccess = onLoginSuccess;
    }

    public VBox getVista() {

        // ── Fondo general ──
        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.getStylesheets().add(
            getClass().getResource("/com/gym/ui/CSS/login.css").toExternalForm()
        );
        root.getStyleClass().add("login-root");

        // ── Panel central ──
        VBox panel = new VBox(16);
        panel.setAlignment(Pos.CENTER);
        panel.setMaxWidth(340);
        panel.getStyleClass().add("login-panel");

        // ── Emoji / icono ──
        Label emoji = new Label("💪");
        emoji.setStyle("-fx-font-size: 42px;");
        emoji.setAlignment(Pos.CENTER);

        // ── Titulo principal ──
        Label titulo = new Label("GYMPRO");
        titulo.getStyleClass().add("login-titulo");

        // ── Subtitulo ──
        Label subtitulo = new Label("Sistema de Gestion GymPro");
        subtitulo.getStyleClass().add("login-subtitulo");

        // ── Campo usuario ──
        txtUsuario.setPromptText("user");
        txtUsuario.setMaxWidth(Double.MAX_VALUE);
        txtUsuario.getStyleClass().add("login-input");

        // ── Campo contraseña ──
        txtPassword.setPromptText("••••••••");
        txtPassword.setMaxWidth(Double.MAX_VALUE);
        txtPassword.getStyleClass().add("login-input");

        // ── Boton ──
        Button btnLogin = new Button("Iniciar sesion");
        btnLogin.setMaxWidth(Double.MAX_VALUE);
        btnLogin.getStyleClass().add("login-boton");
        btnLogin.setOnAction(e -> procesarLogin());

        // ── Mensaje de error/exito ──
        lblMensaje.setMaxWidth(Double.MAX_VALUE);
        lblMensaje.setAlignment(Pos.CENTER);


        panel.getChildren().addAll(
            emoji, titulo, subtitulo,
            txtUsuario, txtPassword,
            btnLogin, lblMensaje
        );

        // Centra el panel en el root
        VBox.setVgrow(panel, Priority.NEVER);
        root.getChildren().add(panel);
        root.setMinSize(Double.MAX_VALUE, Double.MAX_VALUE);

        return root;
    }

    private void procesarLogin() {
        String user = txtUsuario.getText().trim();
        String pass = txtPassword.getText().trim();

        if (user.isEmpty() || pass.isEmpty()) {
            mostrarMensaje("Llene todos los campos", false);
            return;
        }

        try {
            Usuario u = usuarioDAO.buscarPorUsername(user);
            if (u != null && u.verificarPassword(pass)) {
                mostrarMensaje("¡Bienvenido, " + u.getUsername() + "!", true);
                onLoginSuccess.accept(u);
            } else {
                mostrarMensaje("Usuario o clave incorrecta", false);
            }
        } catch (Exception e) {
            mostrarMensaje("Error al conectar o datos invalidos", false);
            e.printStackTrace();
        }
    }

    private void mostrarMensaje(String msg, boolean exito) {
        lblMensaje.setText(msg);
        lblMensaje.getStyleClass().removeAll("login-error", "login-success");
        lblMensaje.getStyleClass().add(exito ? "login-success" : "login-error");
    }
}