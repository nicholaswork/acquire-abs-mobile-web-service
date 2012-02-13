package au.gov.abs.acquire.testservice;

import java.util.ArrayList;

public class Response {
	String addressId;
	String q1;
	String q2;
	String q3;
	ArrayList<Person> people;
	byte[] photo;
	
	public String getAddressId() {
		return addressId;
	}

	public void setAddressId(String addressId) {
		this.addressId = addressId;
	}

	public String getQ1() {
		return q1;
	}

	public void setQ1(String q1) {
		this.q1 = q1;
	}

	public String getQ2() {
		return q2;
	}

	public void setQ2(String q2) {
		this.q2 = q2;
	}

	public String getQ3() {
		return q3;
	}

	public void setQ3(String q3) {
		this.q3 = q3;
	}

	public ArrayList<Person> getPeople() {
		if (people == null)
			people = new ArrayList<Person>();
		return people;
	}

	public void setPeople(ArrayList<Person> people) {
		if (people == null)
			people = new ArrayList<Person>();
		this.people = people;
	}

	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}
}
