
public class Memory {
	private int size;
	public String[] data;
	Memory(int size){
		data=new String[size];
	}
	public String getData(int i) {
		return data[i];
	}
	public void setData(String[] data) {
		this.data = data;
	}
	
	
}
