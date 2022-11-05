package com.example.demo;

import java.awt.PageAttributes.MediaType;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.KeyFactory;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.google.cloud.datastore.StructuredQuery;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;
import com.google.cloud.datastore.Entity.Builder;




@SpringBootApplication
public class DemoApplication {
  public static void main(String[] args) {
    SpringApplication.run(DemoApplication.class, args);
  }
}

// Add the controller.
@RestController
class HelloWorldController {
	String baseUrl = "http://localhost:8080";
  @GetMapping("/")
  public String hello() {
//    return "hello world!";
	// Instantiates a client
	    Datastore datastore = DatastoreOptions.getDefaultInstance().getService();

	    // The kind for the new entity
	    String kind = "Car";
	    // The name/ID for the new entity
	    // The Cloud Datastore key for the new entity
//	    Key carKey = datastore.newKeyFactory().setKind(kind).newKey(name);//.newKey(name);
	    KeyFactory keyFactory = datastore.newKeyFactory().setKind(kind);
	    Key carKey = datastore.allocateId(keyFactory.newKey());

	    // Prepares the new entity
	    Entity car = Entity.newBuilder(carKey)
	    		.set("id", 123)
	    		.set("model", "Tesla")
	    		.set("price", 523.12)
	    		.build();
	    // Saves the entity
	    datastore.put(car);

//	    System.out.printf("Saved %s: %s%n", task.getKey().getName(), task.getString("description"));

	    // Retrieve entity
	    Entity retrieved = datastore.get(carKey);

	    return "Retrieved "+carKey.getName()+": "+ retrieved.getString("model");

  }
  
  
  
  
  
  
  @GetMapping("/car/{id}")
//@ResponseBody
public ResponseEntity<Object> getFooByIdUsingQueryParam(@PathVariable long id) {
	  System.out.println("Mapping 1");
	  Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
//	  Query<Entity> query =
//			    Query.newEntityQueryBuilder()
//			        .setKind("Car")
//			        .setFilter(PropertyFilter.eq("id", id+""))
//			        .build();
//
//	  
	  Query<Entity> query =
			    Query.newEntityQueryBuilder()
			        .setKind("Car")
			        .setFilter(PropertyFilter.eq("id", id))
			        .build();
	  
	  QueryResults<Entity> results = datastore.run(query);
	  Car responseObj = new Car();
	  if(!results.hasNext()) {
		  return new ResponseEntity<Object>("", HttpStatus.NOT_FOUND);
	  }
	  while (results.hasNext()) {
	    Entity currentEntity = results.next();
//	    responseEntity = currentEntity;
	    responseObj.setId(currentEntity.getLong("id"));
	    responseObj.setModel(currentEntity.getString("model"));
	    responseObj.setPrice(currentEntity.getLong("price"));
	    System.out.println(currentEntity.getLong("id"));
	  }
	  
	  return new ResponseEntity<Object>(responseObj, HttpStatus.OK);
//    return "ID: " + id;
}
  
  
  
  
  
  
  
  @GetMapping("/car")
//  @ResponseBody
  public ResponseEntity<Object> getFooByIdUsingQueryParam() {
	  System.out.println("Mapping 2");
	  Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
//	  Query<Entity> query =
//			    Query.newEntityQueryBuilder()
//			        .setKind("Car")
//			        .setFilter(PropertyFilter.eq("id", id+""))
//			        .build();
//
//	  
	  Query<Entity> query =
			    Query.newEntityQueryBuilder()
			        .setKind("Car")
			        .build();
	  
	  QueryResults<Entity> results = datastore.run(query);
	  ArrayList<Car> responseObjValue = new ArrayList<Car>();
	  HashMap<String, ArrayList<Car>> responseObj = new HashMap<String, ArrayList<Car>>();
	  
	  if(!results.hasNext()) {
		  return new ResponseEntity<Object>("", HttpStatus.NOT_FOUND);
	  }
	  
	  
	  while (results.hasNext()) {
	    Entity currentEntity = results.next();
	    Car responseElement = new Car();
	    
	    responseElement.setId(currentEntity.getLong("id"));
	    responseElement.setModel(currentEntity.getString("model"));
	    responseElement.setPrice(currentEntity.getLong("price"));
	    
	    responseObjValue.add(responseElement);
	    
	    System.out.println(currentEntity.getLong("id"));
	  }
	  
	  responseObj.put("carList", responseObjValue);
	  return new ResponseEntity<Object>(responseObj, HttpStatus.OK);
//      return "ID: " + id;
  }
  
  
  
  
  
 
   @PostMapping(
      value = "/car")
  public ResponseEntity<Object> postBody(@RequestBody Car car) {
	   System.out.println("Mapping 3");
	   Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
//      Person persistedPerson = personService.save(person);
//      return ResponseEntity
//          .created(URI
//                   .create(String.format("/car/%s", car.getModel())))
//          .body(car);
	   
	   
	   
	   Query<Entity> query =
			    Query.newEntityQueryBuilder()
			        .setKind("Car")
			        .setFilter(PropertyFilter.eq("id", car.getId()))
			        .build();
	  
	  QueryResults<Entity> results = datastore.run(query);
	  Car responseObj = new Car();
	  if(results.hasNext()) {
		  return new ResponseEntity<Object>("", HttpStatus.CONFLICT);
	  }
	  
	  
	   
	   
	   String kind = "Car";
	    // The name/ID for the new entity
	    // The Cloud Datastore key for the new entity
//	    Key carKey = datastore.newKeyFactory().setKind(kind).newKey(name);//.newKey(name);
	    KeyFactory keyFactory = datastore.newKeyFactory().setKind(kind);
	    Key carKey = datastore.allocateId(keyFactory.newKey());
	    
	    
	   Entity carEntity = Entity.newBuilder(carKey)
	    		.set("id", car.getId())
	    		.set("model", car.getModel())
	    		.set("price", car.getPrice())
	    		.build();
	   System.out.println("Price: " + car.getPrice());
	    // Saves the entity
	    datastore.put(carEntity);
	    
	    HttpHeaders headers = new HttpHeaders();
	    headers.add("Content-Type", "application/json; charset=UTF-8");
	    headers.add("Location", baseUrl+"/car/"+car.getId());
	   
	    return new ResponseEntity<Object>("", headers, HttpStatus.CREATED);
  }
  
  
  
   @PutMapping("/car/{id}")
		  public ResponseEntity<Object> putBody(@PathVariable long id, @RequestBody Car car) {
	   System.out.println("Mapping 4");
			   Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
//		      Person persistedPerson = personService.save(person);
//		      return ResponseEntity
//		          .created(URI
//		                   .create(String.format("/car/%s", car.getModel())))
//		          .body(car);
			   
			   
			   
			   
			   Query<Entity> query =
					    Query.newEntityQueryBuilder()
					        .setKind("Car")
					        .setFilter(PropertyFilter.eq("id", id))
					        .build();
			  
			  QueryResults<Entity> results = datastore.run(query);
			  System.out.println("model: "+car.getModel());
			  System.out.println("price: "+car.getPrice());
			  
			  
			  if(!results.hasNext()) {
				  return new ResponseEntity<Object>("", HttpStatus.NOT_FOUND);
			  }
			  
			  
			  while (results.hasNext()) {									// Runs only once; should run only once
				  Entity currentCar = results.next();
//				  Key carKey = datastore.newKeyFactory().setKind("Car").newKey(currentCar.getKey());
//			    Entity carEntity
			    Builder builder = Entity.newBuilder(currentCar.getKey());
			    
			    System.out.println("SETTING ID: "+currentCar.getLong("id"));
			    builder = builder.set("id", currentCar.getLong("id"));
			    
			    if(car.getModel()!=null) {
			    	System.out.println("GOT: "+car.getModel());
			    	builder = builder.set("model", car.getModel());
				   }
			    else {
			    	builder = builder.set("model", currentCar.getString("model"));
			    }
			    if(car.getPrice()!=0) {
			    	System.out.println("GOT: "+car.getPrice());
			    	builder = builder.set("price", car.getPrice());
				   }
			    else {
			    	builder = builder.set("price", currentCar.getLong("price"));
			    }
			    
				   Entity carEntity = builder.build();
				   System.out.println("Model: "+car.getModel());
				   System.out.println("Price: " + car.getPrice());
				    // Saves the entity
				    datastore.put(carEntity);
			    	
			  }
			    
			   
			   
//			   Entity carEntity = Entity.newBuilder(carKey);
//			   if(car.getId()) {
//				   
//			   }
//			    		.set("id", car.getId())
//			    		.set("model", car.getModel())
//			    		.set("price", car.getPrice())
//			    		.build();
//			   System.out.println("Price: " + car.getPrice());
//			    // Saves the entity
//			    datastore.put(carEntity);
//			    
			    
			   
//			   return car.getModel();
			  return new ResponseEntity<Object>("", HttpStatus.OK);
		  }
  
   
   @DeleteMapping("/car/{id}")
// @ResponseBody
 public ResponseEntity<Object> deleteFoo(@PathVariable long id) {
	   System.out.println("Mapping 5");
	  Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
//	  Query<Entity> query =
//			    Query.newEntityQueryBuilder()
//			        .setKind("Car")
//			        .setFilter(PropertyFilter.eq("id", id+""))
//			        .build();
//
//	  
	  Query<Entity> query =
			    Query.newEntityQueryBuilder()
			        .setKind("Car")
			        .setFilter(PropertyFilter.eq("id", id))
			        .build();
	  
	  QueryResults<Entity> results = datastore.run(query);
	  
	  if(!results.hasNext()) {
		  return new ResponseEntity<Object>("", HttpStatus.NOT_FOUND);
	  }
	  
	  
	  while (results.hasNext()) {									// Runs only once; should run only once
		  System.out.println("I'm in");
		  Entity currentCar = results.next();
//		  Key carKey = datastore.newKeyFactory().setKind("Car").newKey(currentCar.getKey());
//	    Entity carEntity
		  datastore.delete(currentCar.getKey());
	  }
	    
  
	   
//	   return car.getModel();
	  return new ResponseEntity<Object>("", HttpStatus.OK);
 }
   
  
  
}
