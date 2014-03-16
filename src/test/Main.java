package test;

import info.puneetsingh.fsm.TextBrew;
public class Main {
	public static void main(String[] args) {
		System.out.println(TextBrew.compare("BRYN MAWR PIZZA II", "BR MA PI II"));
		System.out.println(TextBrew.compare("clmbs blvd", "columbus boulevard"));
		System.out.println(TextBrew.compare("hosp", "hospital"));
		System.out.println(TextBrew.compare("", "hospital"));
		System.out.println(TextBrew.compare("ko", "no"));
		System.out.println(TextBrew.compare("hojpithol", "hospital"));
	}
}
