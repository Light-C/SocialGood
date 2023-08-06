package touchfish.socialgood;

import java.io.Serializable;
import java.util.List;

public class Publisher implements Serializable {
    public String email;
    public String password;
    public String username;

    public List<ActivityClass> activities;
    public List<ItemModel> goods;

    public Publisher() {

    }

    public Publisher(String email, String password, String username) {
        this.email = email;
        this.password = password;
        this.username = username;
    }

    public List getActivities(){

        //get ids from PublisherDetails

        //add to list

        return null;
    }

    public List getGoods(){

        //get ids from PublisherDetails

        //add to list

        return null;
    }

    public void publishActivity(){
        //new Activity

        //add to ActivitiesData

        //add to PublisherDetails
    }

    public void publishGood(){

    }

}