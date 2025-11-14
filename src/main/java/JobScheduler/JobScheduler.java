/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package JobScheduler;

import AppConfig.Job;
import java.time.Instant;
import java.util.*;


/**
 *
 * @author Nico
 */
public class JobScheduler {
    private int quantumMs;
    private List<Job> allJobs; 
    private Queue<Job> readyQueue;
    private Queue<Job> waitingQueue;
    private Map<String,Job> runningJobs;
    private int totalCPUCores= 4;
    private int totalMemMb = 2048;
    private int usedCPUCores= 0;
    private int usedMemMb= 0;
    
public JobScheduler() {
        allJobs = new ArrayList<>();
        this.readyQueue = new LinkedList<>();
        this.waitingQueue = new LinkedList<>();
        this.runningJobs = new HashMap<>();
    }

public void scheduleJobsFCFS(){
    
    Iterator<Job> it = readyQueue.iterator();
    
    while(it.hasNext()){
        Job job = it.next();
        
        if(job.getCpuCores() <=(totalCPUCores-usedCPUCores)&& 
            job.getMemMb()<=(totalMemMb-usedMemMb) && job.getState() == Job.JobState.READY){
            addToRunning(job);
            usedCPUCores+= job.getCpuCores();
            usedMemMb += job.getMemMb();
            job.setStartTime(Instant.now());
            
            
            it.remove();
            System.out.println("Job ejecutándose "+ job.getName());
        }else{
            System.out.println("job esperando recursos " + job.getName());
            addToWaiting(job);
            it.remove();
        }
    }
}
public void scheduleJobsRR(){
    
        if(readyQueue.isEmpty()){
            System.out.println("No hay jobs en ready");
            return;
        }
        Job currentJob = readyQueue.peek();
        System.out.println("Despachando job (RR):" + currentJob.getName()+ " por "+ getQuantumMs() + " ms");
      }






public void addJob(Job job){

job.setState(Job.JobState.NEW);
allJobs.add(job);
}
public void addToWaiting(Job job){

    getWaitingQueue().add(job);
    job.setState(Job.JobState.WAITING);
}
public void addToReady(Job job){
        getReadyQueue().add(job);
    job.setState(Job.JobState.READY);
}
public void addToRunning(Job job){
        getRunningJobs().put(job.getId(), job);
    job.setState(Job.JobState.RUNNING);
}
/*public Job getNextReady(){
    
        return readyQueue.poll();//Saca el job y devuelve el primer elemento
    
}*/
public void printStatus(){
        System.out.println("=== ESTADO DEL SCHEDULER ===");
        System.out.println("READY: " + readyQueue.size());
        System.out.println("WAITING: " + waitingQueue.size());
        System.out.println("RUNNING: " + runningJobs.size());
        System.out.println("Quantum: "+getQuantumMs()+ "ms");
        System.out.println("=============================");
    }


   
    public Queue<Job> getReadyQueue() {
        return readyQueue;
    }

    
    public Queue<Job> getWaitingQueue() {
        return waitingQueue;
    }

    
    public Map<String,Job> getRunningJobs() {
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
    public int getJobCount(){
    
    
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

