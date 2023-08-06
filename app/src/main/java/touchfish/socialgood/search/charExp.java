package touchfish.socialgood.search;

import java.util.ArrayList;

import touchfish.socialgood.ItemModel;

public class charExp extends Exp {
    char character;

    public  charExp(String word){
        this.character = word.charAt(0);

    }

    public String show() {
        String a = String.valueOf(character);
        return a;
    }

    @Override
    public Object identify(ArrayList<ItemModel> itemlist) {
        return character;
    }


}
