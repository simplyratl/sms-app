import java.util.ArrayList;
import java.util.List;

public class User {

	private int years;
	private String fullName;
	private String number;
	private String gender;
	private List<String> contacts;

	public User(int years, String fullName, String number, String gender) {
		super();
		this.years = years;
		this.fullName = fullName;
		this.number = number;
		this.gender = gender;
		this.contacts = new ArrayList<String>();
	}

	public int getYears() {
		return years;
	}

	public void setYears(int years) {
		this.years = years;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public List<String> getContacts() {
		return contacts;
	}

	public void setContacts(List<String> contacts) {
		this.contacts = contacts;
	}

	public boolean addContact(String name) {
		if(contacts.contains(name)) {
			return false;
		} else {
			contacts.add(name);
			return true;
		}
	}
	
	
	@Override
	public String toString() {
		String allContacts = "";

		for (int i = 0; i < contacts.size(); i++) {
			allContacts += "-" + contacts.get(i);
		}

		return fullName + allContacts;
	}
}
