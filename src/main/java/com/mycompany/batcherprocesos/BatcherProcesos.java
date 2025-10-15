
package com.mycompany.batcherprocesos;

import AppConfig.Job;
import JobScheduler.JobScheduler;
import java.util.ArrayList;
import java.util.List;
import jobs.JobLoader;


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
        
        
        try{
            
            List<Job> jobs = JobLoader.loadJobs("/jobs.yaml");
            
            System.out.println("Jobs cargados: " +jobs.size());
            
            
            for(Job job : jobs){
            
                scheduler.addToReady(job);
                System.out.printf(" - %s (Prioridad %d, %d cores, %d MB, %d ms)%n",
                job.getName(), job.getPriority(), job.getCpuCores(), job.getMemMb(), job.getDurationMs());           
            }
            scheduler.printStatus();
            
        }catch(Exception e){
            System.err.println("Error al leer el archivo"+ e.getMessage());
            e.printStackTrace();
            
    }
}
}
