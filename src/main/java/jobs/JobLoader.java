/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jobs;

import AppConfig.Job;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.yaml.snakeyaml.Yaml;


public class JobLoader {
    
    public static List<Job> loadJobs(String path) throws IOException{
    
        List<Job> jobsList = new ArrayList<>();
        Yaml yaml = new Yaml();
        
        try(InputStream input = JobLoader.class.getResourceAsStream(path)){
            if(input ==null){
                    throw new RuntimeException("No se encontro el YAML" + path);
            
            }
        
        }
    
    
    }
    
}
