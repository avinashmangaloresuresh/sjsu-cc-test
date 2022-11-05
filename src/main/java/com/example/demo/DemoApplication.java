package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;




@SpringBootApplication
public class DemoApplication {
  public static void main(String[] args) {
    SpringApplication.run(DemoApplication.class, args);
  }
}

// Add the controller.
@RestController
class HelloWorldController {
  @GetMapping("/")
  public String hello() {
//    return "hello world!";
	// Instantiates a client
	    Datastore datastore = DatastoreOptions.getDefaultInstance().getService();

	    // The kind for the new entity
	    String kind = "Task";
	    // The name/ID for the new entity
	    String name = "sampletask1";
	    // The Cloud Datastore key for the new entity
	    Key taskKey = datastore.newKeyFactory().setKind(kind).newKey(name);

	    // Prepares the new entity
	    Entity task = Entity.newBuilder(taskKey).set("description", "Buy milk").build();

	    // Saves the entity
	    datastore.put(task);

//	    System.out.printf("Saved %s: %s%n", task.getKey().getName(), task.getString("description"));

	    // Retrieve entity
	    Entity retrieved = datastore.get(taskKey);

	    return "Retrieved "+taskKey.getName()+": "+ retrieved.getString("description");

  }
}
