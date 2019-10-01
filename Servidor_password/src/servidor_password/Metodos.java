package servidor_password;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import static servidor_password.Servidor_password.perfiles;


public class Metodos {
    public static String LeerBloqueados(String Nombre){
        String texto= "";
        String bloqueados= "";
        File file= new File("UsuariosLogin.txt");
        if(file.exists()){
            FileReader fr = null;
            BufferedReader br = null;
      try {
         fr = new FileReader (file);
         br = new BufferedReader(fr);
         String linea;
         while((linea=br.readLine())!=null){texto= texto + linea;}
         
	 StringTokenizer tokens=new StringTokenizer(texto, ":;");
         while(tokens.hasMoreTokens()){
            String nombre=tokens.nextToken();
            String contraseña=tokens.nextToken();
            String Bloqueados=tokens.nextToken();
            if(Bloqueados.equals("-"))
            Bloqueados= "";
            if(Nombre.equals(nombre))
            bloqueados= Bloqueados;
        }
      }catch(Exception e){ e.printStackTrace(); }finally{ try{ if( null != fr ){   fr.close(); } }catch (Exception e2){ e2.printStackTrace(); }}
        }else{
            System.out.println("No se encontró el archivo especificado");
        }
        return bloqueados;
    }
    
   public static String LeerNombres(){
        String texto= "";
        String Nombres= "";
        File file= new File("UsuariosLogin.txt");
        if(file.exists()){
            FileReader fr = null;
            BufferedReader br = null;
      try {
         fr = new FileReader (file);
         br = new BufferedReader(fr);
         String linea;
         while((linea=br.readLine())!=null){texto= texto + linea;}
	 StringTokenizer tokens=new StringTokenizer(texto, ":;");
        while(tokens.hasMoreTokens()){
            String nombre=tokens.nextToken();
            String contraseña=tokens.nextToken();
            String Bloqueados=tokens.nextToken();
            Nombres= Nombres + nombre + "]";
        }
      }catch(Exception e){ e.printStackTrace(); }finally{ try{ if( null != fr ){   fr.close(); } }catch (Exception e2){ e2.printStackTrace(); }}
        }else{
            try {
                file.createNewFile();
            } catch (IOException ex) {
                
            }
        }
        return Nombres;
    }
   
   public static String LeerArchivo(){
        String texto= "";
        File file= new File("UsuariosLogin.txt");
        if(file.exists()){
            FileReader fr = null;
            BufferedReader br = null;
      try {
         fr = new FileReader (file);
         br = new BufferedReader(fr);
         String linea;
         while((linea=br.readLine())!=null){texto= texto + linea;}
      }catch(Exception e){ e.printStackTrace(); }finally{ try{ if( null != fr ){   fr.close(); } }catch (Exception e2){ e2.printStackTrace(); }}
        }else{
            try {
                file.createNewFile();
            } catch (IOException ex) {
                
            }
        }
        return texto;
    }
   
   public static ArrayList<Usuarios> LeerUsuarios(){
        ArrayList<Usuarios> lista= new ArrayList<>();
        String texto= "";
        File file= new File("UsuariosLogin.txt");
        if(file.exists()){
            FileReader fr = null;
            BufferedReader br = null;
      try {
         fr = new FileReader (file);
         br = new BufferedReader(fr);
         String linea;
         while((linea=br.readLine())!=null){texto= texto + linea;}
         StringTokenizer tokens=new StringTokenizer(texto, ":;");
        while(tokens.hasMoreTokens()){
            String nombre=tokens.nextToken();
            String contraseña=tokens.nextToken();
            String Bloqueados=tokens.nextToken();
            lista.add(new Usuarios(nombre, contraseña, null, Bloqueados));
        }
      }catch(Exception e){ e.printStackTrace(); }finally{ try{ if( null != fr ){   fr.close(); } }catch (Exception e2){ e2.printStackTrace(); }}
        }else{
            try {
                file.createNewFile();
            } catch (IOException ex) {
                
            }
        }
        return lista;
    }
    
    public static void EscribirUsuario(){
        String nombres=null;
        nombres = LeerNombres();
        String cadena= "";
        BufferedWriter bw = null;
        FileWriter fw = null;
        try{
        File file = new File("UsuariosLogin.txt");
        
        if (!file.exists()) {
            file.createNewFile();
        }
        fw = new FileWriter(file.getAbsoluteFile(), true);
        bw = new BufferedWriter(fw);
        for(Usuarios usuario : perfiles){
            if(!nombres.contains(usuario.getNombre())){
                if(usuario.getBloqueados().isEmpty()){
                    cadena= cadena + usuario.getNombre() + ":" + usuario.getContraseña() + ":" + "-" + ";";
                }else{
                cadena= cadena + usuario.getNombre() + ":" + usuario.getContraseña() + ":" + usuario.getBloqueados() + ";";
                }
            }
        }
        bw.write(cadena);
    } catch (IOException e) {
        e.printStackTrace();
    } finally {try {
            if (bw != null)
                bw.close();
            if (fw != null)
                fw.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    }
    
    public static void EscribirBloqueo(String Nombre, String Bloqueo){
        
        String texto= "";
        String perfilestxt=null;
        
        perfilestxt = LeerArchivo();
        
        
        if(Bloqueo.isEmpty())
            Bloqueo= "-";
        ArrayList<Usuarios> usuarios= new ArrayList<>();
        StringTokenizer tokens=new StringTokenizer(perfilestxt, ":;");
        while(tokens.hasMoreTokens()){
            String nombre=tokens.nextToken();
            String contraseña=tokens.nextToken();
            String Bloqueados=tokens.nextToken();
            
            usuarios.add(new Usuarios(nombre, contraseña, null, Bloqueados));
        }
        BufferedWriter bw = null;
        FileWriter fw = null;
        try{
        File file = new File("UsuariosLogin.txt");
       
        if (!file.exists()) {
            file.createNewFile();
        }
        fw = new FileWriter(file.getAbsoluteFile());
        bw = new BufferedWriter(fw);
        for(Usuarios usuario : usuarios){
            if(usuario.getNombre().equals(Nombre)){
                texto= texto + usuario.getNombre() + ":" + usuario.getContraseña() + ":" + Bloqueo + ";";
            }else{
            if(usuario.getBloqueados().isEmpty()){
                texto= texto + usuario.getNombre() + ":" + usuario.getContraseña() + ":-" + ";";
            }else{
           texto= texto + usuario.getNombre() + ":" + usuario.getContraseña() + ":" + usuario.getBloqueados() + ";";
            }
            }
        }
        bw.write(texto);
    } catch (IOException e) {
        e.printStackTrace();
    } finally {try {
            if (bw != null)
                bw.close();
            if (fw != null)
                fw.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    }
    
}