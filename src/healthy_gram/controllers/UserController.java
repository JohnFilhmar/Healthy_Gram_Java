package healthy_gram.controllers;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import healthy_gram.DatabaseModel;

public class UserController {

	private String username;
	private String passcode;
    private static DatabaseModel dbm = new DatabaseModel();

    public static String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = md.digest(password.getBytes());
        StringBuilder hashedPassword = new StringBuilder();

        for (byte b : hashBytes) {
            String hex = Integer.toHexString(0xFF & b);
            if (hex.length() == 1) {
                hashedPassword.append('0');
            }
            hashedPassword.append(hex);
        }
        return hashedPassword.toString();
    }
    public static boolean comparePasswords(String inputPassword, String hashedPassword) throws NoSuchAlgorithmException {
        String hashedInput = hashPassword(inputPassword);
        return hashedInput.equals(hashedPassword);
    }
    
    public Boolean verifyUser(String usernameInput, String passwordInput) {
		Object[][] users = dbm.raw("SELECT `name` AS username, `passcode` AS password FROM `preparation` UNION ALL SELECT `name` AS username, `passcode` AS password FROM `cashier`");
    	for(Object[] row : users) {
        	String userName = (String) row[0];
        	String passCode = (String) row[1];
    		if(usernameInput.equals(userName)) {
    			try {
    				if(comparePasswords(passwordInput, passCode)) {
    					return true;
    				}
    			} catch(Exception e) {
    				e.printStackTrace();
    				return false;
    			}
    		}
    	}
    	return false;
    }
    
    public Boolean newUser(String usernameInput, String passwordInput, String contactNumber, String sex, boolean userType) throws NoSuchAlgorithmException {
	    try {
	    	HashMap<String, Object> insertValues = new HashMap<>();
		    	insertValues.put("name", usernameInput);
			    insertValues.put("passcode", hashPassword(passwordInput));
			    insertValues.put("Contact_no", contactNumber);
			    insertValues.put("Sex", sex);
			if(userType) {
				dbm.table("cashier");
				dbm.insert(insertValues);
			} else {
				dbm.table("preparation");
				dbm.insert(insertValues);
			}
			return true;
	    } catch(Exception e) {
	    	System.out.println(e.getMessage());
	    	return false;
	    }
    }
    
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPasscode() {
		return passcode;
	}
	public void setPasscode(String passcode) {
		this.passcode = passcode;
	}
	
}
