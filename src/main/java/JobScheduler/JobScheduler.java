/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package JobScheduler;

import AppConfig.Job;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

        while (!readyQueue.isEmpty()) {

            Job job = readyQueue.poll();

            try {
                startWorker(job);
            } catch (Exception ex) {
                Logger.getLogger(JobScheduler.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
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
        getRunningJobs().put(String.valueOf(job.getId()), job);
        job.setState(Job.JobState.RUNNING);
    }

    
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
                "-cp", cp, "com.mycompany.batcherprocesos.WorkerMain",
                job.getId(), job.getName(), String.valueOf(job.getDurationMs()),
                String.valueOf(job.getCpuCores()), String.valueOf(job.getMemMb())
        );

        Process hijo = pb.start();

        job.setStartTime(Instant.now());

        addToRunning(job);
        
        printScheduler();  

        System.out.println("Proceso lanzado. PID: " + hijo.pid() + ": " + job.getName());

        try (BufferedReader reader
                = new BufferedReader(new InputStreamReader(hijo.getInputStream()))) {

            String line;

            while ((line = reader.readLine()) != null) {

                System.out.println("Worker [" + job.getId() + "]: " + line);

                if (line.startsWith("[END]")) {
                    completeJob(job, 0);
                    moveWaitingToReady();
                }

                if (line.startsWith("[HB]")) {
                    System.out.println("Heartbeat recibido de " + job.getName());
                }
            }
        }

        
    }

    private void releaseResources(Job job) {

        usedCPUCores -= job.getCpuCores();
        usedMemMb -= job.getMemMb();

        System.out.println("Recursos liberados para job " + job.getName()
                + ". CPU libre: " + (totalCPUCores - usedCPUCores)
                + ", Memoria libre: " + (totalMemMb - usedMemMb) + " MB");
    }

    private void completeJob(Job job, int exitCode) {

        releaseResources(job);
        job.setEndTime(Instant.now());

        if (exitCode == 0) {
            job.setState(Job.JobState.DONE);
        } else {
            job.setState(Job.JobState.FAILED);
        }

        runningJobs.remove(String.valueOf(job.getId()));

        System.out.println("Job " + job.getName() + " finalizado. Estado: " + job.getState() + "  " + job.getEndTime());

        printScheduler();
        //De aqui para abajo hacemos la revisión en ready
    }

    private void moveWaitingToReady() {
        Iterator<Job> it = waitingQueue.iterator();

        while (it.hasNext()) {
            Job job = it.next();

            // Verifica si hay recursos suficientes
            if (job.getCpuCores() <= (totalCPUCores - usedCPUCores)
                    && job.getMemMb() <= (totalMemMb - usedMemMb)) {

                // Reservar recursos
                usedCPUCores += job.getCpuCores();
                usedMemMb += job.getMemMb();

                // Pasar a READY
                addToReady(job);

                // Quitar de WAITING
                it.remove();

                System.out.println("Job " + job.getName() + " movido de WAITING a READY");
            }
        }
    }

    public void printScheduler() {

        System.out.println("=================================");
        System.out.println("READY:");

        for (Job job : readyQueue) {
            System.out.println(" - " + job.getName()+ ". Ms:"+ quantumMs);
        }

        System.out.println("\nWAITING:");

        for (Job job : waitingQueue) {
            System.out.println(" - " + job.getName() + ". Ms:"+ quantumMs);
        }

        System.out.println("\nRUNNING:");

        for (Job job : runningJobs.values()) {
            System.out.println(" - " + job.getName()+ ". Ms:"+ quantumMs);
        }

        System.out.println("\nDONE:");

        for (Job job : allJobs) {
            if (job.getState() == Job.JobState.DONE) {
                System.out.println(" - " + job.getName()+ ". Ms:"+ quantumMs);
            }
        }

        System.out.println("\nFAILED:");

        for (Job job : allJobs) {
            if (job.getState() == Job.JobState.FAILED) {
                System.out.println(" - " + job.getName()+ ". Ms:"+ quantumMs);
            }
        }

        System.out.println("=================================");
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
