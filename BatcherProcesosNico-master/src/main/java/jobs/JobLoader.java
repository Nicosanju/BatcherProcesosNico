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
import java.util.Map;
import org.yaml.snakeyaml.Yaml;

 
public class JobLoader {
    
    public static List<Job> loadJobs(String path) throws IOException{
    
        List<Job> jobsList = new ArrayList<>();
        Yaml yaml = new Yaml();
        
        
        //Busca un recurso dentro del classpath del proyecto y devuelve con un InputStream todo lo que hay dentro
        try(InputStream input = JobLoader.class.getResourceAsStream(path)){
            if(input ==null){
                    System.out.println("No se encontro el YAML" + path);
            }
            System.out.println("Cargando YAML desde: " + path);
            
            //Carga los jobs definidos en un archivo YAML y los convierte en objetos
            Map<String,Object> data = yaml.load(input);
            //Extrae la lista de jobs en data,devuelve un object que representa la lista de jobs del YAML
            //Hace un casting al List por cada job en YAML,y lo represtenta como un mapa clave(String),valor(Object)
            List<Map<String,Object>>jobsYaml = (List<Map<String,Object>>) data.get("jobs");
            //Recorremos el list  jobsYaml y le asignamos el correspondiente valor clave/objeto a cada elemento
            for(Map<String,Object> jobsMap : jobsYaml){
                String id = (String) jobsMap.get("id");
                String name = (String) jobsMap.get("name");
                int priority = (int) jobsMap.get("priority");
                
                Map<String,Object> resources = (Map<String,Object>) jobsMap.get("resources");
                    int cpuCores= (int) resources.get("cpuCores");
                    String memString= (String) resources.get("memMb");
                
                Map<String,Object> workload = (Map<String,Object>) jobsMap.get("workload");
                    long durationMs = ((Number) workload.get("durationMs")).longValue();
            
                    
                    if(id ==null|| id.isEmpty()){throw new IllegalArgumentException("id no puede estar vacío");}
                    if(name == null || name.isEmpty()){throw new IllegalArgumentException("el nombre no puede estar vacío");}
                    if( priority < 0 ||priority > 4){throw new IllegalArgumentException("la prioridad tiene que estar entre 0 y 4");}
                    if(cpuCores < 1){throw new IllegalArgumentException("El numero de nucleos tiene que ser mayor que cero");}
                    if(!memString.matches("\\d+(MB|GB)")){throw new IllegalArgumentException("memory debe ser un numero seguido de MB o GB");}
                    if(durationMs <=0){throw new IllegalArgumentException("El numero de ms tiene que ser mayor que cero");}
            //Instanciamos la clase job, y le asignamos los valores del map,correspondiendose a los del constructor job    
                Job job = new Job(name,priority,cpuCores,parseMemory(memString),durationMs);
            //Mientras haya jobs los añadimos a la lista
                jobsList.add(job);
            }
        }
        catch(Exception e ){
            e.printStackTrace();
        
        }
        return jobsList;
            
    
    
    }
    
    private static int parseMemory(String memoryStr){
    
        if(memoryStr.endsWith("GB")){
            return Integer.parseInt(memoryStr.replace("GB", ""))*1024;
            
        }else{
        
            return Integer.parseInt(memoryStr.replace("MB", ""));
        }
    
    }
   
    
}

