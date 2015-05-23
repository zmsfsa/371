package main.java;



import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Main {/*
	public static void main(String[] args) {
		WorkSql work = new WorkSql();
		System.out.println("inserting...");
		for (int i = 1; i < 10; i++) {
			work.addUser(new User("log" + i, "pwd" + i, "name" + i, "lname" + i));
			Calendar date = Calendar.getInstance();
			date.set(2015, 11, i);
			work.addEvent(new Event("event" + i, date));
		}
		
		System.out.println("making good includes");
		if(work.makeInclude("log1", "event1") < 0)
			System.out.println("shit");
		if(work.makeInclude("log2", "event2") < 0)
			System.out.println("shit");
		System.out.println("making bad includes");
		if(work.makeInclude("log100", "event2") < 0)
			System.out.println("with unexisting login don't even try, bitch");
		if(work.makeInclude("log2", "event100") < 0)
			System.out.println("with unexisting event don't even try, bitch");
		
		
		
		System.out.println("deleting user log1");
		
		Event u = work.getEventByName("event5");
		List<Event> l = new LinkedList(Arrays.asList(u));
		
		if(work.deleteEvent(l) < 0)
			System.out.println("mistake");
	}*/
}
