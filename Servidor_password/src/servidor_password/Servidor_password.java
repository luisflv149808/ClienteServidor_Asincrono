package servidor_password;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.JOptionPane;
import servidor_password.Usuarios;

public class Servidor_password {

   public static ArrayList<Usuarios> perfiles= new ArrayList<>();
   
    public static void main(String[] args){
        System.err.println("The chat server is running...");
        ExecutorService pool= Executors.newFixedThreadPool(500);
        try(ServerSocket listener= new ServerSocket(59001)){
            while(true){
                pool.execute(new Handler(listener.accept()));
            }
        }catch (Exception e){}
    }
    
    private static class Handler implements Runnable{
        private String usuario;
        private String nombre;
        private String contraseña;
        private Socket socket;
        private Scanner in;
        private PrintWriter out;
        
        public Handler(Socket socket){
            this.socket= socket;
        }
        
        public void run(){
            try{
                in= new Scanner(socket.getInputStream());
                out= new PrintWriter(socket.getOutputStream(), true);
                
                while (true) {                    
                    out.println("SUBMITNAME");
                    usuario= in.nextLine();
                    nombre= usuario.substring(0,usuario.indexOf(";"));
                    contraseña= usuario.substring(usuario.indexOf(";")+1);
                    if(usuario==null || usuario.toLowerCase().startsWith("quit") || usuario.length()<4){
                        continue;
                    }
                    synchronized(perfiles){
                        if(!perfiles.contains(usuario)){
                            if(Metodos.LeerArchivo().contains(nombre)){
                                if(Metodos.LeerArchivo().contains(contraseña)){
                                out.println("NAMEACCEPTED " + usuario);
                            for(Usuarios usuarios : perfiles){
                                usuarios.getEscritor().println("MESSAGE " + nombre + "  has joined");
                            }
                            perfiles.add(new Usuarios(nombre, contraseña, out, Metodos.LeerBloqueados(nombre)));
                            
                            Metodos.EscribirUsuario();
                            break;
                            }
                            }else{
                                out.println("NAMEACCEPTED " + usuario);
                            for(Usuarios usuarios : perfiles){
                                usuarios.getEscritor().println("MESSAGE " + nombre + "  has joined");
                            }
                            perfiles.add(new Usuarios(nombre, contraseña, out, Metodos.LeerBloqueados(nombre)));
                           
                            Metodos.EscribirUsuario();
                            break;
                            }
                        }
                    }
                }
                
                while (true) {                    
                    String input= in.nextLine();
                    String bloqueados= "";
                    for(Usuarios usuarios : perfiles){
                        if(usuarios.getNombre().equals(nombre))
                            bloqueados= usuarios.getBloqueados();
                    }
                    if(input.startsWith("/")){
                    if(input.toLowerCase().startsWith("/quit")){
                        return;
                    }else if(input.toLowerCase().startsWith("/bloquear")){
                        String bloqueado= input.substring(10);
                        if(bloqueado.equals(nombre)){
                            for(Usuarios usuarios : perfiles){
                                if(usuarios.getNombre().equals(nombre))
                                    usuarios.getEscritor().println("No puedes bloquearte a ti mismo");
                            }
                            continue;
                        }
                        for(Usuarios usuarios : perfiles){
                            if(usuarios.getNombre().equals(bloqueado)){
                                System.out.println(nombre + " bloqueó a " + bloqueado);
                                if(usuarios.getBloqueados().isEmpty()){
                                    usuarios.setBloqueados(nombre + "]");
                                }else{
                                    usuarios.setBloqueados(usuarios.getBloqueados() + nombre + "↨");
                                }
                                Metodos.EscribirBloqueo(usuarios.getNombre(), usuarios.getBloqueados());
                            }
                        }
                    }else if(input.toLowerCase().startsWith("/desbloquear")){
                        String desbloquear= input.substring(13);
                        for(Usuarios usuarios : perfiles){
                            if(usuarios.getNombre().equals(desbloquear)){
                                if(usuarios.getBloqueados().contains(nombre)){
                                    System.out.println(nombre + " desbloqueó a " + desbloquear);
                                    usuarios.setBloqueados(usuarios.getBloqueados().replace(nombre + "]", ""));
                                    Metodos.EscribirBloqueo(usuarios.getNombre(), usuarios.getBloqueados());
                                }
                            }
                        }
                    }else{
                        try{
                        int separador= input.substring(1).indexOf(" ");
                        String destinatario =  input.substring(1, separador + 1);
                        String mensaje =  input.substring(1).substring(separador + 1);
                        
                        if(destinatario.equals(nombre)){
                            for(Usuarios usuarios : perfiles){
                                if(usuarios.getNombre().equals(nombre))
                                    usuarios.getEscritor().println("No puedes enviarte a ti mismo");
                            }
                            continue;
                        }
                        
                        for(Usuarios usuarios : perfiles){
                            if(!bloqueados.contains(usuarios.getNombre())){
                            if(usuarios.getNombre().equals(destinatario)){
                                System.out.println("[" + usuarios.getNombre() + "] - [" + destinatario + "]");
                                usuarios.getEscritor().println("MESSAGE " + nombre + " --- " + destinatario + " : " + mensaje);
                            }
                            if(usuarios.getNombre().equals(nombre)){
                                System.out.println("[" + usuarios.getNombre() + "] - [" + destinatario + "]");
                                usuarios.getEscritor().println("MESSAGE " + nombre + " --- " + destinatario + ": " + mensaje);
                            }
                            }
                        }
                        }catch (Exception e){
                        }
                    }
                }else{
                        for(Usuarios usuarios : perfiles){
                            if(!bloqueados.contains(usuarios.getNombre())){
                             usuarios.getEscritor().println("MESSAGE " + nombre +" : " + input);
                            }
                        }
                    }
                }
                
            } catch (Exception e){
                
            } finally {
                if(out!=null && nombre!=null){
                    System.err.println(nombre + " is leaving");
                    perfiles.remove(nombre);
                    for(Usuarios usuarios : perfiles){
                        usuarios.getEscritor().println("MESSAGE " + nombre + " has left");
                    }
                }
                try{ socket.close(); } catch (IOException e){}
            }
        }
    }
    
    
    
   
    
}
