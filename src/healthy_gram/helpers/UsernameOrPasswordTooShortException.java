package healthy_gram.helpers;

public class UsernameOrPasswordTooShortException extends Exception {
	private static final long serialVersionUID = 1L;

	public UsernameOrPasswordTooShortException(String message) {
		super(message);
	}
}
