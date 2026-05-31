/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AppConfig;

import java.time.Instant;
import java.util.UUID;

/**
 *
 * @author Nico
 */
public class Job {
    private String id ;
    private static int nextId = 1;
    private String name;
    private int priority;
    private int cpuCores;
    private int memMb;
    private long durationMs;
    private JobState state;
    private Instant arrivalTime;
    private Instant startTime;
    private Instant endTime;
    

    
    public enum JobState{
        
        NEW,
        READY,
        WAITING,
        RUNNING,
        DONE,
        FAILED

}
 public Job(String name, int priority, int cpuCores, int memMb, long durationMs) {
        this.id = "Job-" + nextId++; 
        this.name = name;
        this.priority = priority;
        this.cpuCores = cpuCores;
        this.memMb = memMb;
        this.durationMs = durationMs;
        this.state = JobState.NEW;
        this.arrivalTime = Instant.now();
    }    

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the priority
     */
    public int getPriority() {
        return priority;
    }

    /**
     * @param priority the priority to set
     */
    public void setPriority(int priority) {
        if(priority < 0 || priority> 4 ){
            throw new IllegalArgumentException("Prioridad debe estar entre 0 y 4");
        
        }
        this.priority = priority;
    }

    /**
     * @return the cpuCores
     */
    public int getCpuCores() {
        return cpuCores;
    }

    /**
     * @param cpuCores the cpuCores to set
     */
    public void setCpuCores(int cpuCores) {
        this.cpuCores = cpuCores;
    }

    /**
     * @return the memMb
     */
    public int getMemMb() {
        return memMb;
    }

    /**
     * @param memMb the memMb to set
     */
    public void setMemMb(int memMb) {
        this.memMb = memMb;
    }

    /**
     * @return the durationMs
     */
    public long getDurationMs() {
        return durationMs;
    }

    /**
     * @param durationMs the durationMs to set
     */
    public void setDurationMs(long durationMs) {
        this.durationMs = durationMs;
    }

    /**
     * @return the state
     */
    public JobState getState() {
        return state;
    }

    /**
     * @param state the state to set
     */
    public void setState(JobState state) {
        this.state= state;
    }

    /**
     * @return the arrivalTime
     */
    public Instant getArrivalTime() {
        return arrivalTime;
    }

    /**
     * @param arrivalTime the arrivalTime to set
     */
    public void setArrivalTime(Instant arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    /**
     * @return the endTime
     */
    public Instant getEndTime() {
        return endTime;
    }

    /**
     * @param endTime the endTime to set
     */
    public void setEndTime(Instant endTime) {
        this.endTime= endTime;
    }
    /**
     * @return the startTime
     */
    public Instant getStartTime() {
        return startTime;
    }

    /**
     * @param startTime the startTime to set
     */
    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

   
}
