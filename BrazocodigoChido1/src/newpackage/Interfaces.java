package newpackage;
// se importan las librias para la conexión con Arduino

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
// Se importan las librerías para crear la interfaz grafica
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileDescriptor;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.LinkedList;
import javax.swing.JButton;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Interfaces extends JFrame {

    //Se declaran los botones para controlar cada movimiento del brazo
    private JButton derecha, izquierda, cerrar, abrir, abajoCodo, abajoHombro,
            arribaCodo, arribaHombro, generar, automatico, borrar;

    //  Se decalran variables OuputStream que serviran 
    //Para enviar los caracteres en el serial
    private OutputStream ouput;
    // Variable de tipo serial que servira para guardar la dirección del puerto
    SerialPort serialport;
    // Variable con el nombre del puerto 
    private String puerto;
    // variables donde se guardaran la velocidad de conexión y el tiempo 
    // En el que hará la conexión
    private int time, dataRate;
    // variables booleanas que sirven para validar la conexión y validar 
    // Las acciones que realizara el brazo
    private boolean con, gem;
    // Variable y lista enlazada que serviran para aguardar las secuencias 
    //predefinidas por el usuario
    String sec;
    LinkedList<Lista> lista = new LinkedList<>();

    public Interfaces() {
        // Se realiza la llamada al construtor super de la clase JFrame
        super("Control");
        // Se le quita el Layout por default al JFrame
        setLayout(null);
        // Se inicializan las variables booleanas como false la variable
        // sec como vacio la variable puerto con el puerto que nos indica
        // El IDE de arduino time se declara con segundos 
        // El dataRate con la velocida de conexión del puerto 
        //y por ultimo la variable ouput como null
        gem = false;
        sec = "";
        puerto = "COM4";
        time = 2000;
        con = false;
        dataRate = 9600;
        ouput = null;
        // Se inicializan los botones de la interfaz con el texto de la 
        // Accion que realizará cada boton
        borrar = new JButton("Borrar secuencia");
        derecha = new JButton("Derecha");
        generar = new JButton("Generar secuencia");
        izquierda = new JButton("Izquierda");
        arribaHombro = new JButton("Subir Hombro");
        arribaCodo = new JButton("Subir Codo");
        abajoCodo = new JButton("Bajar codo");
        abajoHombro = new JButton("Bajar Hombro");
        cerrar = new JButton("Cerrar pinzas");
        abrir = new JButton("Abrir pinzas");
        automatico = new JButton("Automatico");
        // Se les asigna un color para cada acción que realizara 
        borrar.setBackground(Color.red);
        derecha.setBackground(Color.BLUE);
        izquierda.setBackground(Color.BLUE);
        arribaHombro.setBackground(Color.red);
        arribaCodo.setBackground(Color.GRAY);
        abajoCodo.setBackground(Color.GRAY);
        abajoHombro.setBackground(Color.red);
        cerrar.setBackground(Color.cyan);
        abrir.setBackground(Color.CYAN);
        automatico.setBackground(Color.pink);
        generar.setBackground(Color.MAGENTA);
        // Se les asigna una ubicacion y tamaño dentro del JFrame
        derecha.setBounds(250, 150, 150, 50);
        izquierda.setBounds(50, 150, 150, 50);
        arribaHombro.setBounds(50, 75, 150, 50);
        arribaCodo.setBounds(250, 75, 150, 50);
        abajoHombro.setBounds(50, 225, 150, 50);
        abajoCodo.setBounds(250, 225, 150, 50);
        abrir.setBounds(50, 300, 150, 50);
        cerrar.setBounds(250, 300, 150, 50);
        automatico.setBounds(50, 15, 150, 50);
        generar.setBounds(250, 15, 150, 50);
        borrar.setBounds(175, 400, 150, 50);
        // Se añande los botoens en el JFRame
        add(derecha);
        add(izquierda);
        add(arribaCodo);
        add(arribaHombro);
        add(abajoCodo);
        add(abajoHombro);
        add(abrir);
        add(cerrar);
        add(automatico);
        add(generar);
        add(borrar);
        // Llamda al método encargado de realizar la conexión con arduino
        Conection();

        //Evento para el boton para subir Hombro
        arribaHombro.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Sí la variable gem es verdadera se añadira el caracter
                //A la lista que servira para armar una secuencia para 
                //después ser ejecutada
                // Y se enviara el caracter por el serial para relizar 
                // La accion en el brazo
                // Se enviara el caracter 1 para subir el hombro
                if (gem == true) {
                    sec = "1";
                    enviarDatos("1");
                    lista.add(new Lista(sec));
                } else {
                    // en caso contrario se movera el hombro hacia arriba 
                    //10 grados
                    enviarDatos("1");
                }
            }
        });
        // Evento para bajar el hombro
        abajoHombro.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Sí la variable gem es verdadera se añadira el caracter
                //A la lista que servira para armar una secuencia para 
                //después ser ejecutada
                // Y se enviara el caracter por el serial para relizar 
                // La accion en el brazo
                // Se enviara el caracter 2 parar mover el hombro hacia abajo
                if (gem == true) {
                    sec = "2";
                    lista.add(new Lista(sec));
                    enviarDatos("2");
                } else {
                // En caso contrario se movera el Hombro hacia abajo 10 grados
                    enviarDatos("2");
                }

            }
        });
        // Evento girar Izquierda
        izquierda.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Sí la variable gem es verdadera se añadira el caracter
                //A la lista que servira para armar una secuencia para 
                //después ser ejecutada
                // Y se enviara el caracter por el serial para relizar 
                // La accion en el brazo
                // Se enviara el caracter 4 para mover hacia la Izquierda
                if (gem == true) {
                    sec = "4";
                    lista.add(new Lista(sec));
                    enviarDatos("4");
                } else {
                    enviarDatos("4");
                    // En caso contrario se movera en forma continua hacia 
                    //la izquierda
                }
            }
        });
        // Evento girar Derecha
        derecha.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Sí la variable gem es verdadera se añadira el caracter
                //A la lista que servira para armar una secuencia para 
                //después ser ejecutada
                // Y se enviara el caracter por el serial para relizar 
                // La accion en el brazo
                // Se enviara el caracter 3 para mover a la derecha
                if (gem == true) {
                    sec = "3";
                    lista.add(new Lista(sec));
                    enviarDatos("3");
                } else {
                    // En caso contrario se movera en forma continua hacia 
                    //la derecha
                    enviarDatos("3");
                }
            }
        });
        // Evento subir codo
        arribaCodo.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                //Sí la variable gem es verdadera se añadira el caracter
                //A la lista que servira para armar una secuencia para 
                //después ser ejecutada
                // Y se enviara el caracter por el serial para relizar 
                // La accion en el brazo
                // Se enviara el caracter 5 para subir el codo
                if (gem == true) {
                    sec = "5";
                    lista.add(new Lista(sec));
                    enviarDatos("5");
                } else {
                // En caso contrario se movera hacia arriba el codo 
                // 10 grados
                    enviarDatos("5");
                }
            }
        });
        // Evento Abajo codo
        abajoCodo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Sí la variable gem es verdadera se añadira el caracter
                //A la lista que servira para armar una secuencia para 
                //después ser ejecutada
                // Y se enviara el caracter por el serial para relizar 
                // La accion en el brazo
                // Se enviara el caracter 6 para bajar el codo
                if (gem == true) {
                    sec = "6";
                    lista.add(new Lista(sec));
                    enviarDatos("6");
                } else {
                    // En caso contrario se movera hacia abajo el codo 
                    // 10 grados
                    enviarDatos("6");
                }
            }
        });
        // Evento para abrir las Pinzas
        abrir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Sí la variable gem es verdadera se añadira el caracter
                //A la lista que servira para armar una secuencia para 
                //después ser ejecutada
                // Y se enviara el caracter por el serial para relizar 
                // La accion en el brazo
                // Se enviara el caracter 7 para abrir las pinzas
                if (gem == true) {
                    sec = "7";
                    lista.add(new Lista(sec));
                    enviarDatos("7");
                } else {
                    // En caso contrario se abriran las pinzas 
                    enviarDatos("7");
                }
            }
        });
        //Evento para cerrar las pinzas
        cerrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Sí la variable gem es verdadera se añadira el caracter
                //A la lista que servira para armar una secuencia para 
                //después ser ejecutada
                // Y se enviara el caracter por el serial para relizar 
                // La accion en el brazo
                // Se enviara el caracter 8 para cerrar las pinzas
                if (gem == true) {
                    sec = "8";
                    lista.add(new Lista(sec));
                    enviarDatos("8");
                } else {
                    // En caso contrario se cerraran las pinzas 
                    enviarDatos("8");
                }
            }
        });
        // Evento para iniciar secuencia en automatico
        automatico.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Una vez tarminando de ingresar la secuencia 
                // Se valida si la variale gem es true envia 
                // El caracter 9 para posicionar el brazo en 90° por cada servo
                if (gem == true) {
                    enviarDatos("9");
                // Se empieza a recorrer la lista con la se cuencia guradada
                // Para enviar caracter por caracter por el serial
                    for (int i = 0; i < lista.size(); i++) {
                        System.out.println(lista.get(i).secuencia);
                        enviarDatos(lista.get(i).secuencia);

                    }

                } else {
                // En caso contraio sólo se posicionara en °90 cada servo
                   enviarDatos("9");
                }
            }
        });
        // Evento Generar secuencia
        generar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            // Vuelve true la variable Gem para poder ingresar la secuencia 
            // desada y manda un mensaje de alerta
                gem = true;
                JOptionPane.showMessageDialog(null, "Presione los botones para "
                                                + " generar secuencia");
            }
        });
        // Evento borrar secuencia
        borrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Vuelve false la variable gem y limpia la lista
                // ya sea para guardar una nueva secuencia o para modo manual
                gem = false;
                lista.clear();
            }
        });
    }

    // Método para Realizar la Conexión con arduino regresa true si la
    // Conexión es exitosa
    public boolean Conection() {
        // Variable que contendrá el puerto que se leera del sistema
        CommPortIdentifier puertoId = null;
        // Se crea un Enumeration y se obtienen todos los puerto del sistema
        Enumeration puertoEnum = CommPortIdentifier.getPortIdentifiers();
        // Se recorre el Enumeration mientras haya elementos
        while (puertoEnum.hasMoreElements()) {
            // obtiene el siguiente elemnto de la lista de Enumeration
            CommPortIdentifier ActualPuerto = (CommPortIdentifier) puertoEnum
                    .nextElement();
            //se valida que el puerto que declaramos se encuentre en la lista
            // Si se encuentra se rompre el ciclo
            //Se asigna el peurto que se encontro
            if (puerto.equals(ActualPuerto.getName())) {
                puertoId = ActualPuerto;
                break;
            }
        }
        // Si el puerto es null se muestra mensaje que no se pudo conectar
        if (puertoId == null) {
            mostrarError("No se puede conectar ");
            System.exit(ERROR);
        }
        
        try {
            // se abre el puerto  con los parametro obtenido e ingresados
            serialport = (SerialPort) puertoId.open(this.getClass().getName(),
                            time);
            //Se asginan parametros puerto serie
            serialport.setSerialPortParams(dataRate, SerialPort.DATABITS_8, 
                                SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
            //se inicializa la variable ouput con el método .getOutputStream()
            ouput = serialport.getOutputStream();
        } catch (Exception e) {
            //En caso de error se mostrara mensaje de error
            mostrarError(e.getMessage());
            System.exit(ERROR);
        }
        return con;
    }

        // Método de enviar datos
    private void enviarDatos(String Datos) {
        // Se recibe el dato que se quiere enviar se abre modulo try cath para
        // controlar las excepciones
        try {
            // Se mandan datos por el serial
            ouput.write(Datos.getBytes());
        } catch (Exception e) {
            // En caso de error se manda mensaje de error
            mostrarError("Error");
            System.exit(ERROR);
        }
    }
    // Método para mostrar mensaje de error
    public void mostrarError(String err) {
        //Se recibe un String con el mensaje deseado y se muestra mediante un
        //JOptionPane
        JOptionPane.showMessageDialog(this, err, "ERROR", 
                JOptionPane.ERROR_MESSAGE);
    }

        // Método main de la clase 
    public static void main(String[] args) {
        //Se instancia objeta de la clase
        Interfaces obj = new Interfaces();
        // propeidad para terminas el programa
        obj.setDefaultCloseOperation(EXIT_ON_CLOSE);
        // Tamaño de la vantana
        obj.setSize(450, 600);
        // Localizacion de donde se abrira la ventana
        obj.setLocation(450, 250);
        // Se le quita la propiedad para que se dinamica la ventana
        obj.setResizable(false);
        // se hace visible
        obj.setVisible(true);
    }
}
