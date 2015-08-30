package teamvoy.com.pinbox;

/**
 * Created by Lubomyr Shershun on 30.08.2015.
 * l.sherhsun@gmail.com
 */
public class VersionModel {
    public String name;

    public static final String[] data = {"Cupcake", "Donut", "Eclair",
            "Froyo", "Gingerbread", "Honeycomb",
            "Icecream Sandwich", "Jelly Bean", "Kitkat", "Lollipop"};

    VersionModel(String name){
        this.name=name;
    }
}
