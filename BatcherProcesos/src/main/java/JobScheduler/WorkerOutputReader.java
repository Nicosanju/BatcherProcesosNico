/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package JobScheduler;

import AppConfig.Job;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 *
 * @author Nico
 */
public class WorkerOutputReader  {

    private Process process;
    private Job job;

    public WorkerOutputReader(Process process, Job job) {
        this.process = process;
        this.job = job;
    }

    public void readOutput() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.startsWith("[HB]")) {
                    System.out.println("Heartbeat recibido de " + job.getName() + ": " + linea);
                }
                if (linea.startsWith("[END]")) {
                    System.out.println("Job completado: " + job.getName());
                }
            }
        } catch (Exception e) {
            System.out.println("Error leyendo el stdout del proceso");
        }
    }
}


