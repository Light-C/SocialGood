package touchfish.socialgood;

import java.io.Serializable;

public class ActivityClass implements Serializable {
    public String publisher;
    public String title;
    public int credit;
    public String start;
    public String end;
    public String description;
    public int likes;
    public int dislikes;
    public String pictureName;

    public ActivityClass(){

    }

    public ActivityClass(String publisher, String title, int credit, String description, String start, String end, String pictureName){
        this.publisher = publisher;
        this.title = title;
        this.credit = credit;
        this.description = description;
        this.start = start;
        this.end = end;
        this.pictureName = pictureName;
    }


    public void setLikes(int likes) {
        this.likes = likes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }

    public int getLikes() {
        return likes;
    }

    public int getDislikes() {
        return dislikes;
    }

    @Override
    public String toString() {
        return "{" +
                "publisher=" + publisher +
                ", title=" + title +
                ", start=" + start +
                ", end=" + end +
                '}';
    }

}
