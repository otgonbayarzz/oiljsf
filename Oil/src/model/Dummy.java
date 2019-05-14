package model;

public class Dummy {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		User user = new User();
		Object o = user;
		user = (User) o;
		String className = o.getClass().getName().replace("model.", "");
		System.out.println("class name is: " + className);
		
	}

}
