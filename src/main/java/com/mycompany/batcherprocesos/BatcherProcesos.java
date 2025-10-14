
package com.mycompany.batcherprocesos;

import AppConfig.Job;
import JobScheduler.JobScheduler;
import java.util.ArrayList;
import java.util.List;


public class BatcherProcesos {

    public static void main(String[] args) {
        
        JobScheduler scheduler = new JobScheduler();
        
        Job j1 = new Job("Compilar Proyecto", 2, 4, 2048, 2000);
        Job j2 = new Job("Analizar Datos", 3, 6, 4096, 3000);
        Job j3 = new Job("Backup", 1, 2, 1024, 1000);
        
        scheduler.addToReady(j1);
        scheduler.addToReady(j2);
        scheduler.addToReady(j3);
        
        scheduler.addToRunning(j1);
        
        scheduler.addToWaiting(j3);
        
        scheduler.printStatus();
    }
}
