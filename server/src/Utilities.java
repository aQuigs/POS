import java.util.Random;

public class Utilities {
	
	public String GeneratePassword(){
		String Characters = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz1234567890";
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		while (sb.length() < 9){
            int index = (int) (random.nextFloat() * Characters.length());
            sb.append(Characters.charAt(index));
		}
		
		return sb.toString();
		
	}
}
