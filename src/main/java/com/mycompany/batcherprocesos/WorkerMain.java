/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.batcherprocesos;

/**
 *
 * @author Nico
 */
public class WorkerMain {

    public static void main(String[] args) throws Exception {

        int jobId =Integer.parseInt(args[0]) ;
        long duration = Long.parseLong(args[1]);

        long tiempo = 0;
        long heartbeat = 1000;

        System.out.println("[START]" + jobId);
        while (tiempo < duration) {
            Thread.sleep(heartbeat);
            tiempo += heartbeat;
            System.out.println("[HB] " + jobId + " tiempoEspera " + tiempo + " s");
        }
        

        System.out.println("[END] Job " + jobId);
    }
}
