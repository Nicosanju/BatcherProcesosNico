/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package JobScheduler;

import AppConfig.Job;
import java.time.Instant;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nico
 */
public class JobScheduler {

    private int quantumMs;
    private List<Job> allJobs;
    private Queue<Job> readyQueue;
    private Queue<Job> waitingQueue;
    private Map<String, Job> runningJobs;
    private int totalCPUCores = 4;
    private int totalMemMb = 2048;
    private int usedCPUCores = 0;
    private int usedMemMb = 0;

    public JobScheduler() {
        allJobs = new ArrayList<>();
        this.readyQueue = new LinkedList<>();
        this.waitingQueue = new LinkedList<>();
        this.runningJobs = new HashMap<>();
    }

    public void scheduleJobsFCFS() {

        Iterator<Job> it = readyQueue.iterator();

        while (it.hasNext()) {
            Job job = it.next();

            // No dejamos arrancar más de un job a la vez si tu CPU es de 1 core
            try {
                startWorker(job);   // ⬅️ AQUÍ SE LANZA EL PROCESO REAL
            } catch (Exception ex) {
                Logger.getLogger(JobScheduler.class.getName()).log(Level.SEVERE, null, ex);
            }
        it.remove();
        }
    }

    public void scheduleJobsRR() {

        if (readyQueue.isEmpty()) {
            System.out.println("No hay jobs en ready");
            return;
        }
        Job currentJob = readyQueue.peek();
        System.out.println("Despachando job (RR):" + currentJob.getName() + " por " + getQuantumMs() + " ms");
    }

    public void addJob(Job job) {

        job.setState(Job.JobState.NEW);
        allJobs.add(job);
    }

    public void addToWaiting(Job job) {

        getWaitingQueue().add(job);
        job.setState(Job.JobState.WAITING);
    }

    public void addToReady(Job job) {
        getReadyQueue().add(job);
        job.setState(Job.JobState.READY);
    }

    public void addToRunning(Job job) {
        getRunningJobs().put(String.valueOf( job.getId()), job);
        job.setState(Job.JobState.RUNNING);
    }

    /*public Job getNextReady(){
    
        return readyQueue.poll();//Saca el job y devuelve el primer elemento
    
}*/
    public void printStatus() {
        System.out.println("=== ESTADO DEL SCHEDULER ===");
        System.out.println("READY: " + readyQueue.size());
        System.out.println("WAITING: " + waitingQueue.size());
        System.out.println("RUNNING: " + runningJobs.size());
        System.out.println("Quantum: " + getQuantumMs() + "ms");
        System.out.println("=============================");
    }

    public void startJobs() {

        for (Job job : readyQueue) {
            if (job.getCpuCores() <= totalCPUCores && job.getMemMb() <= totalMemMb) {
                try {
                    startWorker(job);
                } catch (Exception ex) {
                    Logger.getLogger(JobScheduler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        System.out.println("El proceso se inicio correctamente");
    }
    
    private void startWorker(Job job) throws Exception {
        String cp = System.getProperty("java.class.path");
        ProcessBuilder pb = new ProcessBuilder("java",
                "-cp",cp,"com.mycompany.batcherprocesos.WorkerMain",
                String.valueOf(job.getId()) ,String.valueOf(job.getDurationMs()),
                String.valueOf(job.getCpuCores()),String.valueOf(job.getMemMb())                        
        );
            
        
           Process  hijo = pb.start();
         
        
        job.setState(Job.JobState.RUNNING);
        job.setStartTime(Instant.now());
        
        runningJobs.put(String.valueOf(job.getId()),job);
        
        System.out.println("Proceso lanzado. PID: "+ hijo.pid() + ": "+ job.getName());
        
         // Hilo para leer stdout y heartbeats
        Thread outReader = new Thread(new WorkerOutputReader(hijo, job));
        outReader.start();
    }

    public Queue<Job> getReadyQueue() {
        return readyQueue;
    }

    public Queue<Job> getWaitingQueue() {
        return waitingQueue;
    }

    public Map<String, Job> getRunningJobs() {
        return runningJobs;
    }

    /**
     * @return the allJobs
     */
    public List<Job> getAllJobs() {
        return allJobs;
    }

    /**
     * @param allJobs the allJobs to set
     */
    public void setAllJobs(List<Job> allJobs) {
        this.allJobs = allJobs;
    }

    public int getJobCount() {

        return allJobs.size();

    }

    /**
     * @return the quantumMs
     */
    public int getQuantumMs() {
        return quantumMs;
    }

    /**
     * @param quantumMs the quantumMs to set
     */
    public void setQuantumMs(int quantumMs) {
        this.quantumMs = quantumMs;
    }

}
