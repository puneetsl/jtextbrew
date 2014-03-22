package test;

import info.puneetsingh.fsm.TextBrew;
public class Main {
	public static void main(String[] args) {
		System.out.println(TextBrew.compare("BRYN MAWR PIZZA II", "BR MA PI II"));
		System.out.println(TextBrew.compare("clmbs blvd", "columbus boulevard"));
		System.out.println(TextBrew.compare("hosp", "hospital"));
		System.out.println(TextBrew.compare("lole", "hospital"));
		System.out.println(TextBrew.compare("ko", "no"));
		System.out.println(TextBrew.compare("hojpithol", "hospital"));
		System.out.println(TextBrew.compare("docprofil", "hospital"));
		System.out.println(TextBrew.compare("puneetsl", "puneet singh ludu"));
		System.out.println(TextBrew.compare("puneet singh ludu","puneetsl"));
		System.out.println(TextBrew.compareAndGiveBestScore("puneet singh ludu","puneetsl"));
		System.out.println("----------------------------------------");
		System.out.println(TextBrew.compareUsingQuadratic("BRYN MAWR PIZZA II", "BR MA PI II",2));
		System.out.println(TextBrew.compareUsingQuadratic("clmbs blvd", "columbus boulevard",1.5));
		System.out.println(TextBrew.compareUsingQuadratic("hosp", "hospital",3));
		System.out.println(TextBrew.compareUsingQuadratic("lole", "hospital",4));
		System.out.println(TextBrew.compareUsingQuadratic("ko", "no",2));
		System.out.println(TextBrew.compareUsingQuadratic("hojpithol", "hospital",2));
		System.out.println(TextBrew.compareUsingQuadratic("docprofil", "hospital",2.1));
		System.out.println(TextBrew.compareUsingQuadratic("puneetsl", "puneet singh ludu",1.2));
		System.out.println(TextBrew.compareUsingQuadratic("puneet singh ludu","puneetsl",1.4));
		System.out.println(TextBrew.compareUsingQuadratic("puneet singh ludu","puneetsl",1.5));
		System.out.println("----------------------------------------");
		TextBrew.setCosts(0, 0.5, 10, 1);
		System.out.println(TextBrew.compare("BRYN MAWR PIZZA II", "BR MA PI II"));
		System.out.println(TextBrew.compare("clmbs blvd", "columbus boulevard"));
		System.out.println(TextBrew.compare("hosp", "hospital"));
		System.out.println(TextBrew.compare("", "hospital"));
		System.out.println(TextBrew.compare("ko", "no"));
		System.out.println(TextBrew.compare("hojpithol", "hospital"));
		System.out.println(TextBrew.compare("docprofil", "hospital"));
		System.out.println(TextBrew.compare("puneetsl", "puneet singh ludu"));
		System.out.println(TextBrew.compare("puneet singh ludu","puneetsl"));
		System.out.println(TextBrew.compareAndGiveBestScore("puneet singh ludu","puneetsl"));
	}
}
