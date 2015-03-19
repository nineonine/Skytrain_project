package model;

import android.os.Parcel;
import android.os.Parcelable;

public class Station implements Parcelable {
	
	//HERE IS OUR STATION MODEL
	//we decided to keep everything ins String
	
	private String name;
	public String zone;
	private String description;
	private String location;
	public String latX;
	public String longY;
	//we should also store the R.drawable id of the station's picture
	private String image;

	//Standard Constructor
	public Station(String name, String zone, String description,
			String location, String latX, String longY) {
		super();
		this.name = name;
		this.zone = zone;
		this.description = description;
		this.location = location;
		this.latX = latX;
		this.longY = longY;
	}

	// used to inflate the POJO once it has
	// reached its destination activity
	private Station(Parcel in) {

		String[] data = new String[7];
		in.readStringArray(data);
		this.name = data[0];
		this.zone = data[1];
		this.description = data[2];
		this.location = data[3];
		this.latX = data[4];
		this.longY = data[5];
		this.image = data[6];
	}
	
	//getter and setter
	
	public String getImage(){
		return image;
	}
	public void setImage(String image){
		this.image = image;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {

		dest.writeStringArray(new String[] { this.name, this.zone,
				this.description, this.location, this.latX,
				this.longY, this.image });

	}

	// More boilerplate
	// Failure to add this results in the following exception
	// "android.os.BadParcelableException: Parcelable protocol
	// requires a Parcelable.Creator object called CREATOR on class"
	public static final Parcelable.Creator<Station> CREATOR =
			new Parcelable.Creator<Station>() {
		public Station createFromParcel(Parcel in) {
			return new Station(in);
		}

		public Station[] newArray(int size) {
			return new Station[size];
		}
	};

	
	//GETTERS AND SETTERS
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getLatX() {
		return latX;
	}

	public void setLatX(String latX) {
		this.latX = latX;
	}

	public String getLongY() {
		return longY;
	}

	public void setLongY(String longY) {
		this.longY = longY;
	}

}
