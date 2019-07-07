package com.tarun.los.start;

import com.tarun.los.Utils.Utility;
import com.tarun.los.operation.LOSProcess;


public class Application {

	public static void main(String[] args) {
		LOSProcess process=new LOSProcess();
		while(true) {
		System.out.println("Do u have Application Number or Not(Enter 0) Press -1 to Exit");
        int applicationNumber=Utility.scanner.nextInt();
        if(applicationNumber==-1) {
        	System.out.println("Thanks For Using..");
        	System.exit(0);
        			
        }
        if(applicationNumber==0) {
        	//New number
        	process.sourcing();
        }
        else {
        	//Existing Customer
        	process.checkStage(applicationNumber);
        }
		}
		
	}

}
