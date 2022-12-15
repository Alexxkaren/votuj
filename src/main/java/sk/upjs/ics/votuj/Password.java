package sk.upjs.ics.votuj;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.codec.Hex;

public class Password {
	
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
}
