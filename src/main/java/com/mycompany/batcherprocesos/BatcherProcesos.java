
package com.mycompany.batcherprocesos;

import AppConfig.Job;
import JobScheduler.JobScheduler;
import java.io.IOException;
import java.util.List;
import jobs.JobLoader;


public class BatcherProcesos {

    public static void main(String[] args)throws Exception {
        String cp = System.getProperty("java.class.path");
        JobScheduler scheduler = new JobScheduler();
        ProcessBuilder pb = new ProcessBuilder("java","-cp",cp," com.mycompany.batcherprocesos.WorkerMain");
        pb.inheritIO();
        pb.start();
        
        /*Job j1 = new Job("Compilar Proyecto", 2, 4, 2048, 2000);
        Job j2 = new Job("Analizar Datos", 3, 6, 4096, 3000);
        Job j3 = new Job("Backup", 1, 2, 1024, 1000);

        scheduler.addToReady(j1);
        scheduler.addToReady(j2);
        scheduler.addToReady(j3);

        scheduler.addToRunning(j1);
        
        scheduler.addToWaiting(j3);*/
        
        
        try{
            
            List<Job> jobs = JobLoader.loadJobs("/jobs.yaml");
            
            System.out.println("Jobs leidos: " +jobs.size());
            
            
            for(Job job : jobs){
                
                job.setState(Job.JobState.READY);
                if(job.getState()== Job.JobState.READY){    
                scheduler.addToReady(job);
                scheduler.addJob(job);
                System.out.println(job.getName()+ ": (Prioridad " + job.getPriority() +"," + job.getCpuCores()+" cores," + job.getMemMb() +" MB,"+ job.getDurationMs() + " ms" + ")." );                scheduler.setQuantumMs(1500);  // por ejemplo, 250 ms
                scheduler.scheduleJobsRR();
                
                
                
                }
                else{
                scheduler.addToRunning(job);
                System.out.println(job.getName()+ ": (Prioridad " + job.getPriority() +"," + job.getCpuCores()+" cores," + job.getMemMb() +" MB,"+ job.getDurationMs() + " ms" + ")." );                scheduler.setQuantumMs(1500);  // por ejemplo, 250 ms
                }
                }
            scheduler.printStatus();
            System.out.println(" Los archivos guardados en la lista fueron "+ scheduler.getJobCount()+ " jobs");
            
            scheduler.scheduleJobsFCFS();
            
            scheduler.printStatus();
            System.out.println("Jobs guardados en la lista: " + scheduler.getJobCount());
            
        }catch(IOException e){
            System.err.println("Error al leer el archivo"+ e.getMessage());
            
            
    }
}
}
