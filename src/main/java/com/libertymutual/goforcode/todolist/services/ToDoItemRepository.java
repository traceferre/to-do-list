// ToDoItemRepository.java
package com.libertymutual.goforcode.todolist.services;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import com.libertymutual.goforcode.todolist.models.ToDoItem;

@Service
public class ToDoItemRepository {

    private Integer nextId = 1;

    /**
     * Get all the items from the file. 
     * @return A list of the items. If no items exist, returns an empty list.
     */
    public List<ToDoItem> getAll() 
    {    	
        try (FileReader reader = new FileReader("todolist.csv");
        	 BufferedReader br = new BufferedReader(reader)) {
        	
        	ArrayList<ToDoItem> todolist = new ArrayList<ToDoItem>();
        	Iterable<CSVRecord> record = CSVFormat.DEFAULT.parse(br);
        	
        	for (CSVRecord individualRecord : record)
        	{
        		ToDoItem tdItem = new ToDoItem();
        		tdItem.setText(individualRecord.get(0));
        		tdItem.setId(Integer.valueOf(individualRecord.get(1)));
        		tdItem.setComplete(Boolean.valueOf((individualRecord.get(2))));
        		todolist.add(tdItem);
        	}
        	this.nextId = todolist.size()+1;
        	return todolist;
        	
        } catch (FileNotFoundException e) {
        	System.err.println("Could not find file");
			return Collections.emptyList();
		} catch (IOException e) {
			System.err.println("Could not read file");
			return Collections.emptyList();
		}        
    }

    /**
     * Assigns a new id to the ToDoItem and saves it to the file.
     * @param item The to-do item to save to the file.
     */
    public void create(ToDoItem item) 
    {    	
    	item.setId(this.nextId);		
    	
    	try (FileWriter writer = new FileWriter("todolist.csv", true);
           	 BufferedWriter buff = new BufferedWriter(writer);
    		 CSVPrinter printer = new CSVPrinter(buff, CSVFormat.DEFAULT)) {
    		
    		ArrayList<String> todolist = new ArrayList<String>();
    		String complete = String.valueOf(!item.getIsIncomplete());
    		
    			
    		todolist.add(item.getText());
    		todolist.add(this.nextId.toString());    		
    		todolist.add(complete);    		
    		printer.printRecord(todolist);
    		
    		this.nextId++;
    		           	
        } catch (FileNotFoundException e) {
           	System.err.println("Could not find file");
   		} catch (IOException e) {
   			System.err.println("Could not read file");
   		}            	
    }

    /**
     * Gets a specific ToDoItem by its id.
     * @param id The id of the ToDoItem.
     * @return The ToDoItem with the specified id or null if none is found.
     */
    public ToDoItem getById(int id) {
    	List<ToDoItem> todolist = new ArrayList<ToDoItem>();
    	todolist = this.getAll();
    	for (ToDoItem item : todolist)
    	{
    		if (item.getId() == id) return item;
    	}
    	return null;
    }

    /**
     * Updates the given to-do item in the file.
     * @param item The item to update.
     */
    public void update(ToDoItem item) {
    		
    	List<ToDoItem> todolist = new ArrayList<ToDoItem>();
    	todolist = this.getAll();
    	    	
    	this.nextId = 1;
    	
    	try (FileWriter writer = new FileWriter("todolist.csv");
   	         BufferedWriter buff = new BufferedWriter(writer);
   	         CSVPrinter printer = new CSVPrinter(buff, CSVFormat.DEFAULT)) {
   			
   	       	 ArrayList<String[]> templist = new ArrayList<String[]>();
   	       			
   	       	 for (ToDoItem oneItem : todolist) 
   	       	 {
   	       		 if (oneItem.getId() == item.getId()) oneItem.setComplete(true);
   	       		 
   	       		 templist.add(new String[] {oneItem.getText(),
   	       								    Integer.toString(oneItem.getId()),
   	       								    Boolean.toString(oneItem.isComplete())});
   	       	 }
   	       	 printer.printRecords(templist);
   	       	 this.nextId++;  		       		
   	       		           	
   	     } catch (FileNotFoundException e) {
   	              	System.err.println("Could not find file");
   	     } catch (IOException e) {
   	      			System.err.println("Could not read file");
   	     }   	
    }
}