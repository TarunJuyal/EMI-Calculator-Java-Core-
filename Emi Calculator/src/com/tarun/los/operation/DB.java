package com.tarun.los.operation;

import java.util.ArrayList;

import com.tarun.los.customer.Customer;
import com.tarun.los.customer.PersonalInformation;

public interface DB {
   public static ArrayList<Customer> getNegativeCustomers() {
	   ArrayList<Customer> negativeCustomers = new ArrayList<>();
	   Customer customer=new Customer();
	   customer.setId(1010);
	   PersonalInformation pd=new PersonalInformation();
	   pd.setFirstName("Tim");
	   pd.setLastName("Jackson");
	   pd.setPhone("2222");
	   pd.setPanCard("BW1000");
	   pd.setVoterId("A111");
	   pd.setEmail("tim@gmail.com");
	   customer.setPersonal(pd);
	   negativeCustomers.add(customer);
	   customer=new Customer();
	   customer.setId(1011);
	   pd=new PersonalInformation();
	   pd.setFirstName("Tom");
	   pd.setLastName("Dahl");
	   pd.setPhone("3333");
	   pd.setPanCard("BW2000");
	   pd.setVoterId("A222");
	   pd.setEmail("tom@gmail.com");
	   customer.setPersonal(pd);
	   negativeCustomers.add(customer);
	   return negativeCustomers;
   }
}
