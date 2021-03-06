/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sb;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import sb.identificacion.IdentificacionController;
import sb.registro.RegistroController;

/**
 *
 * @author L745
 */
public class FXMLDocumentController implements Initializable {
    //admin,registro,identificacion y reporte del dia
    @FXML
    private Label usuario_label,password_label;
    @FXML
    private PasswordField pfpass;
    @FXML
    private TextField usuario;
    @FXML public ImageView admin;
    
    
    
    @FXML public void admin() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/sb/login/Login.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));  
            stage.show();
        }catch(Exception e) {
            e.printStackTrace();
        }
        
    }
    
    @FXML
    private void cliente() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/sb/registro/Registro.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));  
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    RegistroController.lector.stopCapture();
                }
            });
            stage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void reporte() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/sb/reporte/Reporte.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));  
            stage.show();
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void verificacion() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/sb/identificacion/Identificacion.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    IdentificacionController.lector.stopCapture();
                }
            });
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
//        usuario_label.setTextFill(Color.web("#0076a3"));
    }    
    
}
