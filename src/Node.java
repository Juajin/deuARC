
public class Node {
	private Node nextColumn;
	private String content;
	private String index;
	private String address;
	static int counter;
	Node(String index, String address, String content) {
		this.index = index;
		this.address = address;
		this.content = content;
	}

	public void add(String index, String address, String content) {
		if (this.address != null || this.index != null || this.content != null) {
			Node tempNode = new Node(index, address, content);
			nextColumn = tempNode;
			counter++;
		}
		else
		{
			this.address=address;
			this.index=index;
			this.content=content;
			nextColumn=new Node(null,null,null);
		}
	}

	public Node getNextColumn() {
		return nextColumn;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public static int getCounter() {
		return counter;
	}

	public static void setCounter(int counter) {
		Node.counter = counter;
	}

}
