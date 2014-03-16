package test;

import info.puneetsingh.fsm.TextBrew;
public class Main {
	public static void main(String[] args) {
		System.out.println(TextBrew.compare("BRYN MAWR PIZZA II", "TASTY PLACE"));
		System.out.println(TextBrew.compare("clmbs blvd", "columbus boulevard"));
		System.out.println(TextBrew.compare("hosp", "hospital"));
	}
}
