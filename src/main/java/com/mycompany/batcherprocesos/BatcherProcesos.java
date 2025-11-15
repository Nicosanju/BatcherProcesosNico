package com.mycompany.batcherprocesos;

import AppConfig.Job;
import JobScheduler.JobScheduler;
import java.io.IOException;
import java.util.List;
import jobs.JobLoader;

public class BatcherProcesos {

    public static void main(String[] args) throws Exception {
        JobScheduler scheduler = new JobScheduler();
        scheduler.printStatus();
        scheduler.scheduleJobsFCFS();
        scheduler.printStatus();

        /*Job j1 = new Job("Compilar Proyecto", 2, 4, 2048, 2000);
        Job j2 = new Job("Analizar Datos", 3, 6, 4096, 3000);
        Job j3 = new Job("Backup", 1, 2, 1024, 1000);

        scheduler.addToReady(j1);
        scheduler.addToReady(j2);
        scheduler.addToReady(j3);

        scheduler.addToRunning(j1);
        
        scheduler.addToWaiting(j3);*/
        try {

            List<Job> jobs = JobLoader.loadJobs("/jobs.yaml");

            for (Job job : jobs) {
                job.setState(Job.JobState.READY);
                scheduler.addToReady(job);
                scheduler.addJob(job);
                System.out.println(job.getName()+ ": (Prioridad " + job.getPriority() +"," + job.getCpuCores()+
                " cores," + job.getMemMb() +" MB,"+ job.getDurationMs() + " ms" + ")." );                
            }
            
            scheduler.printStatus();

            scheduler.scheduleJobsFCFS();

            scheduler.printStatus();

        } catch (IOException e) {
            System.err.println("Error al leer el archivo" + e.getMessage());

        }
    }
}
