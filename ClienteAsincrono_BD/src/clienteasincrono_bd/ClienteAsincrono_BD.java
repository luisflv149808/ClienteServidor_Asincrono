package clienteasincrono_bd;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ClienteAsincrono_BD {

    String serverAdress;
    Scanner in;
    PrintWriter out;
    JFrame frame= new JFrame("Chat");
    JTextField textField= new JTextField(50);
    JTextArea messageArea= new JTextArea(16, 50);
    
    public ClienteAsincrono_BD(String serverAdress){
        this.serverAdress= serverAdress;
        textField.setEditable(false);
        messageArea.setEditable(false);
        
        frame.getContentPane().add(textField, BorderLayout.SOUTH);
        frame.getContentPane().add(new JScrollPane(messageArea), BorderLayout.CENTER);
        frame.pack();
        
        textField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                out.println(textField.getText().trim());
                textField.setText("");
            }
        });
    }
    
    private String getName(){
        return JOptionPane.showInputDialog(frame, "Nombre de pantalla: ", "Selecciona nombre de pantalla", JOptionPane.PLAIN_MESSAGE);
    }
    
    private void run(){
        try{
            Socket socket= new Socket(serverAdress, 59001);
            in= new Scanner(socket.getInputStream());
            out= new PrintWriter(socket.getOutputStream(), true);
            
            while (in.hasNextLine()) {                
                String line= in.nextLine();
                if(line.startsWith("SUBMITNAME")){
                    out.println(getName());
                } else if(line.startsWith("NAMEACCEPTED")){
                    this.frame.setTitle("Chat - " + line.substring(13));
                    textField.setEditable(true);
                } else if(line.startsWith("MESSAGE")){
                    messageArea.append(line.substring(8) + "\n");
                }
            }
        }catch(IOException ex){
            System.out.println("Error al crear el socket " + ex);
            System.exit(1);
            
        }
            finally { 
            frame.setVisible(false);
            frame.dispose();
        }
    }    
    
    public static void main(String[] args) throws Exception{
        if(args.length != 1){
            System.err.println("La dirección IP es incorrecta");
            return;
        }
        ClienteAsincrono_BD cliente= new ClienteAsincrono_BD(args[0]);
        cliente.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        cliente.frame.setVisible(true);
        cliente.run();
    }
    
}