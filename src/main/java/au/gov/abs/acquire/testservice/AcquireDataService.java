package au.gov.abs.acquire.testservice;

//import javax.naming.InitialContext;
import java.util.ArrayList;
import java.util.HashMap;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.jboss.resteasy.annotations.providers.jaxb.json.Mapped;

@Path("/acquiredataservice")
public class AcquireDataService {

	static ArrayList<Collector> collectors = new ArrayList<Collector>();
	static ArrayList<AddressDTO> addresses = new ArrayList<AddressDTO>();
	static HashMap<String, Response> responses = new HashMap<String, Response>();

	private static synchronized void init() {
		if (addresses.size() == 0) {
			for (int i = 0; i < 100; ++i) {
				String is = String.valueOf(i);
				addresses.add(new AddressDTO(is, is + " Example Street Adelaide SA 5000", -35.221478, 149.105231 + (double) i / 10000.0));
			}

			for (int i = 1001; i < 1011; ++i) {
				String s = String.valueOf(i);
				collectors.add(new Collector(s, s, s));
			}
		}
	}

	@GET
	@Path("/requestaddress/{username}/{password}")
	@Produces("application/json")
	@Mapped
	public AddressDTO requestaddress(@PathParam("username") String username, @PathParam("password") String password) throws ClassNotFoundException {

		init();

		String collector_id = checkLogin(username, password);
		if (collector_id == null) {
			return new AddressDTO();
		}

		for (AddressDTO check : addresses) {
			if (check.getStatus().equals("A") && check.getCollectorId().equals(collector_id)) {
				return check;
			}
		}

		for (AddressDTO check : addresses) {
			if (check.getStatus().equals("N")) {
				check.setStatus("A");
				check.setCollectorId(collector_id);
				return check;
			}
		}

		return new AddressDTO();
	}

	private String checkLogin(String username, String password) {
		for (Collector c : collectors) {
			if (c.getUsername().equals(username) && c.getPassword().equals(password))
				return c.getId();
		}
		return null;
	}

	@GET
	@Path("/uploadresponse/{address_id}/{photo}/{q1}/{q2}/{q3}")
	public void uploadresponse(@PathParam("address_id") String address_id, @PathParam("photo") String photo, @PathParam("q1") String q1,
			@PathParam("q2") String q2, @PathParam("q3") String q3) throws ClassNotFoundException {

		init();

		Response resp = new Response();
		resp.setQ1(q1);
		resp.setQ2(q2);
		resp.setQ3(q3);
		resp.setAddressId(address_id);
		resp.setPhoto(photo.getBytes());

		responses.put(address_id, resp);

		for (AddressDTO check : addresses) {
			if (check.getId().equals(address_id)) {
				check.setStatus("S");
			}
		}
	}

	@GET
	@Path("/uploadresponseperson/{address_id}/{name}/{age}/{work}")
	public void uploadresponseperson(@PathParam("address_id") String address_id, @PathParam("name") String name, @PathParam("age") String age,
			@PathParam("work") String work) throws ClassNotFoundException {

		init();

		Person p = new Person();
		p.setAge(age);
		p.setName(name);
		p.setJob(work);

		Response resp = responses.get(address_id);
		resp.getPeople().add(p);
	}

	@GET
	@Path("/alldata")
	@Produces("text/plain")
	public String alldata() throws ClassNotFoundException {

		init();

		String alladdresses = "";

		for (AddressDTO addr : addresses) {
			alladdresses += "id: " + addr.getId() + "\n";
			alladdresses += "address_text: " + addr.getAddressText() + "\n";
			alladdresses += "latitude: " + addr.getLatitude() + "\n";
			alladdresses += "longtitude: " + addr.getLongitude() + "\n";
			alladdresses += "status: " + addr.getStatus() + "\n";
			alladdresses += "collector_id: " + addr.getCollectorId() + "\n";

			if (responses.containsKey(addr.getId())) {
				Response resp = responses.get(addr.getId());
				alladdresses += "\taddress_id: " + resp.getAddressId() + "\n";
				// alladdresses += "\tphoto: " + resp.getPhoto() + "\n";
				alladdresses += "\tq1: " + resp.getQ1() + "\n";
				alladdresses += "\tq2: " + resp.getQ2() + "\n";
				alladdresses += "\tq3: " + resp.getQ3() + "\n";

				for (Person p : resp.getPeople()) {
					alladdresses += "\t\tname: " + p.getName() + "\n";
					alladdresses += "\t\tage: " + p.getAge() + "\n";
					alladdresses += "\t\twork: " + p.getJob() + "\n";
				}
			}

		}

		return alladdresses;
	}
}
