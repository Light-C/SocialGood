package touchfish.socialgood.search;

import java.util.ArrayList;


public class SplitExp extends Exp{
    Exp parse;
    Exp word;
    Exp number;
    Exp character;
//    ArrayList result = new ArrayList<>();
    Boolean numberFirst;

    public SplitExp( Exp parse,Exp word,Exp number,Exp character,Boolean numberFirse){
        this.parse = parse;
        this.word = word;
        this.number = number;
        this.character = character;
        this.numberFirst = numberFirse;
    }



    @Override
    public String show() {
        if (word!=null && parse!= null){
            return "(" +word.show()+" wordSplitParse "+parse.show()+ ")";
        }else if (word!=null && number!= null){
            return "(" +word.show()+" wordSplitCharacter "+number.show()+ ")";
        } else if (number!=null && word!=null) {
            return "(" +number.show()+" numberSplitWord "+word.show()+ ")";
        }else if (character!=null && number!=null){
            return "(" +character.show()+" characterSplitNumber "+number.show()+ ")";
        }else if (character!=null && parse!=null){
            return "(" +character.show()+" characterSplitParse "+parse.show()+ ")";
        }

        return  number.show();
    }



    public Object identify(ArrayList itemlist) {

        if (word==null && parse==null && number!=null && character==null){
            Object p =number.identify(itemlist);
            itemlist.add(p);
//            return itemlist;
        }

        if (word!=null && parse!=null){
            Object p =word.identify(itemlist);
            itemlist.add(p);
            itemlist.add(parse.identify(itemlist));
        }
        if (number!=null && word!=null && numberFirst==false){
            Object p = word.identify(itemlist);
//            System.out.println(p.getClass().getTypeName().equals("java.lang.Character"));
//            System.out.println(p.getClass().getTypeName().equals("java.lang.Double"));
            itemlist.add(p);
            itemlist.add(number.identify(itemlist));
        }

        if (number!=null && word!=null && numberFirst==true){
            Object p = number.identify(itemlist);
//            System.out.println(p.getClass().getTypeName().equals("java.lang.Character"));
//            System.out.println(p.getClass().getTypeName().equals("java.lang.Double"));
            itemlist.add(p);
            itemlist.add(word.identify(itemlist));
        }


        if (character!=null && parse!=null){
            Object p = character.identify(itemlist);
//            System.out.println(p.getClass().getTypeName().equals("java.lang.Character"));
//            System.out.println(p.getClass().getTypeName().equals("java.lang.Double"));
            itemlist.add(p);
            itemlist.add(parse.identify(itemlist));

        }

        return itemlist;
    }
}
