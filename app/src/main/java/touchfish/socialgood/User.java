package touchfish.socialgood;

import android.app.Activity;
import android.util.Log;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class User implements Serializable {
    public String email;
    public String password;
    public String username;
    public String tag;
    public int credit;
    public List<Activity> follow;
    public Map<String, Integer> history;
    public String profilePictureName;


    public User() {

    }

    public User(String email, String password, String username) {
        this.email = email;
        this.password = password;
        this.username = username;
    }

    public void register(){
        credit = 0;
        String content = email+"|"+username+"｜"+credit+"||";
        try {
            // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
            FileWriter writer = new FileWriter("assets/UserDetails.csv", true);
            writer.write(content);
            Log.d("writer content",content);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("writer content","error");
        }
    }

    public int getCredit(){
        return credit;
    }

    public Map getHistory(){

        return null;
    }

    public void attendActivity(ActivityClass activity){

        //add to history

        //add credit

        //add to UserDetails

    }

    public void exchangeGood(int number,ItemModel good){

        //add to history

        //minus credit

        //add to UserDetails

    }

    public void followActivity(ActivityClass activity){

        //add to follow

        //add to UserDetails

    }

    public void likeActivity(ActivityClass activity){
        //add to ActivitiesData

    }

    public void dislikeActivity(ActivityClass activity){
        //add to ActivitiesData

    }

}
