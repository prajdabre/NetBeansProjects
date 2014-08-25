
public class SearchObject {
	private String name;
	private String price;
	private String imgurl;
	private String type;
	
	void setName(String Name) {
		this.name = Name;
	}
	void setPrice(String Price) {
		this.price = Price;
	}
	void setImgurl(String Imgurl) {
		this.imgurl = Imgurl;
	}
	void setType(String Type) {
		this.type = Type;
	}
	String getName() {
		return this.name;
	}
	String getPrice() {
		return this.price;
	}
	String getImgurl() {
		return this.imgurl;
	}
	String getType() {
		return this.type;
	}
	public SearchObject(String Name, String Price, String Imgurl, String Type) {
		this.name = Name;
		this.price = Price;
		this.imgurl = Imgurl;
		this.type = Type;
	}
}
