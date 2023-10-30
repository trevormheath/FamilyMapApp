package JsonObjects;

public class LocArray {
    private Location[] data;

    public LocArray(Location[] locList){
        this.data = locList;
    }

    public Location[] getData() {
        return data;
    }

    public void setData(Location[] locList) {
        this.data = locList;
    }
    public Location getLocAt(int n){
        return data[n];
    }
}
