package com.tarun.los.operation;

import static com.tarun.los.Utils.Utility.scanner;
import static com.tarun.los.Utils.Utility.serialCounter;

import java.util.ArrayList;

import com.tarun.los.Utils.CommonConstants;
import com.tarun.los.Utils.LoanConstants;
import com.tarun.los.Utils.StageConstants;
import com.tarun.los.Utils.Utility;
import com.tarun.los.customer.Customer;
import com.tarun.los.customer.LoanDetails;
import com.tarun.los.customer.PersonalInformation;

public class LOSProcess implements StageConstants,CommonConstants {
	   //private Customer customer[]=new Customer[100];
    private ArrayList<Customer> customers=new ArrayList<>();  //<> (Generic)Restrict type of element to Customer   
	
    public void approval(Customer customer) {
    	customer.setStage(APPROVAL);
    	int score = customer.getLoandetails().getScore();
    	System.out.println(" Id "+customer.getId());
    	System.out.println(" Name is "+customer.getPersonal().getFirstName()+" "+customer.getPersonal().getLastName());
    	System.out.println("Score is "+customer.getLoandetails().getScore());
    	System.out.println("Loan "+customer.getLoandetails().getType()+" "+customer.getLoandetails().getAmount()+" Duration "+customer.getLoandetails().getDuration());
        double approveAmount=customer.getLoandetails().getAmount()*(score/100);
        System.out.println("Loan Approve Amount is "+approveAmount);
        System.out.println("Do u want this Loan or Not");
        char choice= scanner.next().toUpperCase().charAt(0);
        if(choice==NO) {
        	customer.setStage(REJECT);
        	customer.setRemarks("Customer Denied the Approved Amount "+approveAmount);
        	return;
        }
        else {
        	showEMI(customer);
        }
        
    }
    private void showEMI(Customer customer) {
    	//System.out.println("EMI is ");
    	if(customer.getLoandetails().getType().equalsIgnoreCase(LoanConstants.HOME_LOAN)) {
    		customer.getLoandetails().setRoi(LoanConstants.HOME_LOAN_ROI);
    	}
    	if(customer.getLoandetails().getType().equalsIgnoreCase(LoanConstants.AUTO_LOAN)) {
    		customer.getLoandetails().setRoi(LoanConstants.AUTO_LOAN_ROI);
    	}
    	if(customer.getLoandetails().getType().equalsIgnoreCase(LoanConstants.PERSONAL_LOAN)) {
    		customer.getLoandetails().setRoi(LoanConstants.PERSONAL_LOAN_ROI);
    	}
        double perMonthPrinciple=customer.getLoandetails().getAmount()/customer.getLoandetails().getDuration();  
        double interest=perMonthPrinciple*customer.getLoandetails().getRoi();
        double totalEmi=perMonthPrinciple+interest;
        System.out.println("Your EMI is "+totalEmi);
    }
    
    
    public void  qde(Customer customer) {
		customer.setStage(QDE);
		System.out.println("Application Number "+customer.getId());
		System.out.println("Name "+customer.getPersonal().getFirstName()+" "+customer.getPersonal().getLastName());
        System.out.println("You Applied for a"+customer.getLoandetails().getType()+" Duration "+customer.getLoandetails().getDuration()+" Amount "+customer.getLoandetails().getAmount());
        System.out.println("Enter the Pan Card Number");
       String panCard = scanner.next();
       System.out.println("Enter the VoterId Number");
       String voterid = scanner.next();
       System.out.println("Enter the Income");
       double income = scanner.nextDouble();
       System.out.println("Enter the Liablity");
       double liablity = scanner.nextDouble();
       System.out.println("Enter the Phone Number");
       String phone = scanner.next();
       System.out.println("Enter the Email");
       String email=scanner.next();
       customer.getPersonal().setPanCard(panCard);
       customer.getPersonal().setVoterId(voterid);
       customer.getPersonal().setPhone(phone);
       customer.getPersonal().setEmail(email);
       customer.setIncome(income);
       customer.setLiablity(liablity);
	}
	
    public void moveToNextStage(Customer customer) {
    	while(true) {
    	if(customer.getStage()==SOURCING) {
    		   System.out.println("Sourcing Want to Move to the Next Stage Y/N");
        	   char choice=scanner.next().toUpperCase().charAt(0);
        	   if(choice==YES) {
        		   qde(customer);
        	   }
        	   else {
        		   return;
        	   }
    	}
    	else
    	if(customer.getStage()==QDE) {
    	   System.out.println("QDE Want to Move to the Next Stage Y/N");
    	   char choice=scanner.next().toUpperCase().charAt(0);
    	   if(choice==YES) {
    		   dedupe(customer);
    	   }
    	   else  {
    		   return;
    	   }
    	}
    	else
    	if(customer.getStage()==DEDUPE) {
     	   System.out.println("Dedupe Want to Move to the Next Stage Y/N");
     	  char choice=scanner.next().toUpperCase().charAt(0);
     	   if(choice==YES) {
     		   scoring(customer);
     	   }
     	   else {
     		   return;
     	   }
     	}
    	if(customer.getStage()==SCORING) {
 		   System.out.println("Scoring Want to Move to the Next Stage Y/N");
     	   char choice=scanner.next().toUpperCase().charAt(0);
     	   if(choice==YES) {
     		   approval(customer);
     	   }
     	   else {
     		   return;
     	   }
 	}
    	}
    }
    
    public void scoring (Customer customer) {
    	customer.setStage(SCORING);
    	int score=0;
    	double totalIncome=customer.getIncome()-customer.getLiablity();
    	if(customer.getPersonal().getAge()>=21 && customer.getPersonal().getAge()<=35) {
           score+=50;
    	}
    	if(totalIncome>=200000) {
    		score+=50;
    	}
    	customer.getLoandetails().setScore(score);
    }
    
    
    
    public void dedupe(Customer customer) {
    //	System.out.println("Inside Dedupe");
    	customer.setStage(DEDUPE);
    	boolean isNegativeFound=false;
       for(Customer negativeCustomer : DB.getNegativeCustomers()) {
    	   int negativeScore=isNegative(customer,negativeCustomer);
    	   if(negativeScore>0) {
    		   System.out.println("Customer Record found in Dedupe and Score is "+negativeScore);
    	       isNegativeFound=true;
    	       break;
    	   }
       }
      if(isNegativeFound) {
    	  System.out.println("Do you want to Proceed this Loan "+customer.getId());
    	  char choice=scanner.next().toUpperCase().charAt(0);
    	  if(choice==NO) {
    		  customer.setRemarks("Loan is Rejected,Due to High score in Dedupe Check");
    		  customer.setStage(REJECT);
    		  return ;
    	  }
      }  
    }
    private int isNegative(Customer customer, Customer negative) {
    	int percentageMatch=0;
    	if(customer.getPersonal().getPhone().equals(negative.getPersonal().getPhone())){
    	  percentageMatch+=20;	
    	}
    	if(customer.getPersonal().getEmail().equals(negative.getPersonal().getEmail())){
      	  percentageMatch+=20;	
      	}
    	if(customer.getPersonal().getVoterId().equals(negative.getPersonal().getVoterId())){
      	  percentageMatch+=20;	
      	}
    	if(customer.getPersonal().getPanCard().equals(negative.getPersonal().getPanCard())){
      	  percentageMatch+=20;	
      	}
    	if(customer.getPersonal().getAge()==negative.getPersonal().getAge() && customer.getPersonal().getFirstName().equalsIgnoreCase(negative.getPersonal().getFirstName())){
      	  percentageMatch+=20;	
      	}
    	return percentageMatch;
    
    }
    
    
    public void sourcing() {
    	   Customer customer=new Customer();
    	   customer.setId(serialCounter);
    	   customer.setStage(SOURCING);
    	   System.out.println("Enter the First Name");
    	   String firstName=scanner.next();
    	   System.out.println("Enter the Last Name");
    	   String lastName=scanner.next();
    	   System.out.println("Enter the Age");
    	   int age=scanner.nextInt();
    	   System.out.println("Enter the Loan Type HL , AL , PL ");
           String type = scanner.next();
           System.out.println("Enter the Amount");
           double amount=scanner.nextDouble();
           System.out.println("Duration of Loan");
           int duration=scanner.nextInt();
           PersonalInformation pd=new PersonalInformation();
           pd.setFirstName(firstName);
           pd.setLastName(lastName);
           pd.setAge(age);
           customer.setPersonal(pd);
           LoanDetails loandetails=new LoanDetails();
           loandetails.setType(type);
           loandetails.setAmount(amount);
           loandetails.setDuration(duration);
           customer.setLoandetails(loandetails);
           customers.add(customer);
           serialCounter++;
           System.out.println("Sourcing Done...");
       }
       public void checkStage(int applicationNumber) {
    	   boolean isStageFound=false;
    	   if(customers!=null && customers.size()>0) {
       for(Customer customer : customers) {
    	   if(customer.getId()==applicationNumber) {
    		   System.out.println("You are on "+Utility.getStageName(customer.getStage()));
    		   isStageFound=true;
    		   moveToNextStage(customer);
    		   break;
    	   }
       }	  
     }
        if(!isStageFound) {
    	   System.out.println("Invalid Application Number");
       }
}}
