
package juego.en.java;

import control.Teclado;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;


public class juego extends Canvas implements Runnable {
    
    private static final long serialVersionUID= 1L;
    
    private static final int ANCHO = 800;
    private static final int ALTO  = 600;
    
    private static volatile boolean enFuncionamiento = false;
    
    private static final String NOMBRE = "Juego";
    
    private static int aps = 0;
    private static int fps = 0;
    
    private static JFrame ventana;
    private static Thread thread;
    private static Teclado  teclado;
    
    private juego(){
        setPreferredSize(new Dimension(ANCHO, ALTO));
        
        teclado = new Teclado();
        addKeyListener(teclado);
        
        ventana = new JFrame(NOMBRE);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setResizable(false);
        ventana.setLayout(new BorderLayout());
        ventana.add(this, BorderLayout.CENTER);
        ventana.pack();
        ventana.setLocationRelativeTo(null);
        ventana.setVisible(true);
        
        
    }
    
    public static void main(String [] args){
        juego juego = new juego();
        juego.iniciar();
    }
    
    private synchronized void iniciar(){
        enFuncionamiento = true;
        
        thread = new Thread(this,"Graficos");
        thread.start();
    }
    
    
    
    private synchronized void detener(){
        enFuncionamiento = false;
        
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    private void actualizar(){ 
        teclado.actualizar();
        
        if(teclado.arriba){ 
            System.out.println("arriba"); 
        }
        if(teclado.abajo){
            System.out.println("abajo"); 
        }
        if(teclado.izquierda){ 
            System.out.println("izquierda"); 
        }
        if(teclado.derecha)  {
            System.out.println("derecha"); 
        }
        
        
        aps++;
    }
    
    
    
    private void mostrar(){
        fps++;
    } 
    
    
    

   
    public void run() {
        final int NS_POR_SEGUNDO = 1000000000;
        final byte APS_OBJETIVO = 60;
        final double NS_POR_ACTUALIZACION = NS_POR_SEGUNDO / APS_OBJETIVO;
        
        long referenciaActualizacion = System.nanoTime();
        long referenciaContador = System.nanoTime();
        
        double tiempoTranscurrido;
        double delta = 0;
        
        //foco de la pantalla
        requestFocus();
        
        
        while (enFuncionamiento){
            final long inicioBucle = System.nanoTime();
            
            tiempoTranscurrido = inicioBucle - referenciaActualizacion;
            referenciaActualizacion = inicioBucle;
            
            delta += tiempoTranscurrido / NS_POR_ACTUALIZACION;
            
            while(delta >= 1){
                actualizar();
                delta --;
            }
            
            
         
            mostrar();
            
            if(System.nanoTime() - referenciaContador > NS_POR_SEGUNDO){
                ventana.setTitle(NOMBRE + "|| APS: " + aps + "|| FPS: "+ fps);
                aps = 0;
                fps = 0;
                referenciaContador = System.nanoTime();
            
            } 
        }
        System.out.println("El thread 2 se esta ejecutando");
        
    }
    
}

