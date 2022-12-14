package sk.upjs.ics.votuj;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.mysql.cj.xdevapi.Schema.CreateCollectionOptions;

public class Password {

	//zdroj: ocs.spring.io/spring-security/site/docs/current/api/org/springframework/security/crypto/bcrypt/BCrypt.html
	//pouzite: .gensalt(), .hashpw(), .matches()

	public String createPassword(String passwordTemp) {
		BCrypt bc = new BCrypt();
		String salt = bc.gensalt();
		String hashed = bc.hashpw(passwordTemp, salt);
		return hashed;
	}

	public boolean isCorrect(String dbsPasswd, String userPasswd) {
		BCryptPasswordEncoder c = new BCryptPasswordEncoder();
		return c.matches(userPasswd, dbsPasswd);
	}
	
	/*
	public static void main(String[] args) {
		Password p = new Password();
		System.out.println(p.createPassword("heslo01"));
	}
	*/
	
}
