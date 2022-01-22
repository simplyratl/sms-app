
public class User {
	
	private int years;
	private String fullName;
	private String number;
	private String gender;
	
	public User(int years, String fullName, String number, String gender) {
		super();
		this.years = years;
		this.fullName = fullName;
		this.number = number;
		this.gender = gender;
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

	@Override
	public String toString() {
		return "Ime i Prezime: " + fullName + ", Godine: " + years + ", Broj Telefona: " + number + ", Pol: " + gender;
	}
	
	
	
	
}
