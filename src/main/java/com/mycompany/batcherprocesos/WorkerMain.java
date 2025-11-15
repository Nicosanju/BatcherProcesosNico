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

        String jobId =(args[0]) ;
        String jobName = args[1];
        long duration = Long.parseLong(args[2]);

        long tiempo = 0;
        long heartbeat = 1000;

        System.out.println("[START] " + jobName);
        while (tiempo < duration) {
            Thread.sleep(heartbeat);
            tiempo += heartbeat;
            System.out.println("[HB] " + jobName + " tiempoEspera " + tiempo/1000 + " s");
        }
        

        System.out.println("[END] Job " + jobName);
    }
}
