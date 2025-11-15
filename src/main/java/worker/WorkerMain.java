/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package worker;

/**
 *
 * @author Nico
 */
public class WorkerMain {
    public static void main(String[]args)throws Exception{
    
    String jobId = args[0];
    long duration = Long.parseLong(args[1]);
    int cores = Integer.parseInt(args[2]);
    int memMb = Integer.parseInt(args[3]);
    
        System.out.println("Worker iniciado para job " + jobId +" con duracion de " + duration + " ms");
    
        Thread.sleep(duration);
        
        System.out.println("Worker finalizado para el job " + jobId);
    }
}
