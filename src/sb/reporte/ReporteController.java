package sb.reporte;



import com.digitalpersona.onetouch.DPFPTemplate;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import sb.SQL.SQLConnection;
import sb.clases.Cliente;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javafx.scene.control.Alert;
//import sb.identificacion.IdentificacionController;
//import sb.registro.RegistroController;


//Tengo que combinar la clase sensor con esta clase para poder hacer las cosas bien y no me salga un nullpoint
public class ReporteController implements Initializable {

    Connection connection;
    Calendar calendario = Calendar.getInstance();
    Date date;
    //public RegistroController regCon = new RegistroController();
//    public IdentificacionController idenCon = new IdentificacionController();
    ObservableList<Cliente> lista = FXCollections.observableArrayList();
    
    @FXML private TableView<Cliente> table_clientes;
    @FXML private TableColumn<Cliente,String> colHora, colNombre, colFecha;
    @FXML public TextField buscar;
    
    public void borrarHistorial(){
        colHora.setCellValueFactory(null);
        colNombre.setCellValueFactory(null);
        colFecha.setCellValueFactory(null);
        table_clientes.setItems(null);
        lista.clear();
    }
    
    public void buscarHistorial(){
        if (buscar.getText().equals("")){
            new Alert(Alert.AlertType.INFORMATION, "Casilla vacia!").show();
        }else{
            try {
                //Mostrar en una tabla
                String b = buscar.getText();
                FileReader fr = new FileReader(b+".txt");
                BufferedReader br = new BufferedReader(fr);
                String lectura;
                while((lectura=br.readLine()) != null){
                    String[] palabras = lectura.split(" ");
                    System.out.println(palabras[palabras.length-1]);
                    String con = "";
                    if (palabras.length==4){
                        con+=palabras[1]+" "+palabras[2];
                        lista.add(new Cliente(palabras[0], con, palabras[palabras.length-1]));
                    }else{
                        lista.add(new Cliente(palabras[0], palabras[1], palabras[palabras.length-1]));
                    }
                }
                colHora.setCellValueFactory(new PropertyValueFactory<>("hora"));
                colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
                colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
                table_clientes.setItems(lista);
            } catch (IOException ex) {
                System.out.println("Error! : "+ ex);
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb){
        
        DateFormat ff = new SimpleDateFormat("dd-MM-YYYY");
        //System.out.println("fecha: "+ff.format(new Date()));
        try {     
            this.connection = new SQLConnection("root", "", "sb").getConnection();
            System.out.println("Conexion Lista");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Conexión no realizada"+e.getMessage());
        }
        if(connection != null){
            try {
                //Mostrar en una tabla
                String fecha = String.valueOf(ff.format(new Date()));
                FileReader fr = new FileReader("reporte"+fecha+".txt");
                BufferedReader br = new BufferedReader(fr);
                String lectura;
                while((lectura = br.readLine())!=null){
                    System.out.println(lectura);
                    String[] palabras = lectura.split(" ");
                    System.out.println(palabras);
                    String con = "";
                    if (palabras.length==4){
                        con+=palabras[1]+" "+palabras[2];
                        lista.add(new Cliente(palabras[0], con, palabras[palabras.length-1]));
                    }else{
                        lista.add(new Cliente(palabras[0], palabras[1], palabras[palabras.length-1]));
                    }
                    //lista.add(new Cliente(palabras[0], palabras[1], palabras[palabras.length-1]));
                }
                colHora.setCellValueFactory(new PropertyValueFactory<>("hora"));
                colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
                colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
                table_clientes.setItems(lista);
            } catch (IOException ex) {
                System.out.println("Error! : "+ ex);
            }
        }
    }    

    private DPFPTemplate getTemplate() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

 
    
    
}