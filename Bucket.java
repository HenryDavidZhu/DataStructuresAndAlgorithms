public class Bucket {
	// Each Entry contains a key-value pair
	String key;
	Integer value;
	Bucket next; // Used for chaining
	
	public Bucket(String key, Integer value) {
		this.key = key;
		this.value = value;
	}
}