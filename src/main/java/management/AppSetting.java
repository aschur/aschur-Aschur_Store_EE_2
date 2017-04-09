package management;

public class AppSetting {

	private String name;
	private long value;
	
	public AppSetting() {}
	
	public AppSetting(String name, Long value) {
		this.name = name;
		this.value = value;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getValue() {
		return value;
	}
	public void setValue(long value) {
		this.value = value;
	}
	
	
	
}
