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
    private Queue<Job> readyQueue;
    private Queue<Job> waitingQueue;
    private Map<String,Job> runningJobs;
public JobScheduler() {
        
        this.readyQueue = new LinkedList<>();
        this.waitingQueue = new LinkedList<>();
        this.runningJobs = new HashMap<>();
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
public Job getNextReady(){
    
        return readyQueue.poll();//Saca el job y devuelve el primer elemento
    
}
public void printStatus(){
        System.out.println("=== ESTADO DEL SCHEDULER ===");
        System.out.println("READY: " + readyQueue.size());
        System.out.println("WAITING: " + waitingQueue.size());
        System.out.println("RUNNING: " + runningJobs.size());
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


}

