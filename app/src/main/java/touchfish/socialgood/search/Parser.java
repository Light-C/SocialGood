package touchfish.socialgood.search;


import java.util.ArrayList;

/**
 * This parse is used to retrieve characters from the search box. The formula is as follows:
 *
 * <parseExp> ::= <wordExp>splitExp<parseExp>|<wordExp>|<numExp>|<wordExp>splitExp<numExp>|<numExp>splitExp<wordExp>
 * <wordExp> ::= <characterExp>|<characterExp>splitExp<paesrExp>
 * <characterExp> ::= charExp
 * <numExp> ::= IntExp
 * Whatever information the user enters through this formula, the program can process.
 * Currently only the numbers and characters entered by the user are retrieved,
 * and more characters can be retrieved if needed.
 */

public class Parser {


    public static class IllegalProductionException extends IllegalArgumentException {
        public IllegalProductionException(String errorMessage) {
            super(errorMessage);
        }
    }
    Tokenizer tokenizer;
    ArrayList<ItemModel1> test;

    public Parser(Tokenizer tokenizer) {
        this.tokenizer = tokenizer;

    }


    public static Tokenizer valueTokenizer(Tokenizer tt){
        String words = "";
        while (tt.hasNext()){
            if (tt.current().getType()!= Token.Type.OTHER){
                words = words+tt.current().getToken();
            }
            tt.next();
        }
        Tokenizer t =new  Tokenizer(words);
        System.out.println(words);
    return t;
    }

    public Exp parseExp(){

        if (tokenizer.current().getType()== Token.Type.INT){
            Exp number  = numExp();
            SplitExp number1 = new SplitExp(null,null,number,null,false);
            tokenizer.next();
            if (!tokenizer.hasNext()){
                return number1;
            }
            Exp word = wordExp();
            Exp numSplitWord = new  SplitExp(null,word,number,null,true);
            return numSplitWord;
        }else{
            Exp word = wordExp();
            if (tokenizer.hasNext()){
                tokenizer.next();
            }else {
                return word;
            }

            if (tokenizer.current().getType() == Token.Type.INT){
                Exp number = numExp();
                Exp wordSplitNum = new SplitExp(null,word,number,null,false);
                return wordSplitNum;
            }else {
                Exp parse = parseExp();
                Exp wordSplitExp = new SplitExp(parse,word,null,null,false);
                return wordSplitExp;
            }
        }
    }

    public Exp wordExp(){
        Exp character = characterExp();
        tokenizer.next();
        if (!tokenizer.hasNext()){
            return character;
        }
            Exp parse = parseExp();
            tokenizer.next();
            Exp charSplitParse = new SplitExp(parse,null,null,character,false);
            return charSplitParse;

    }


    public Exp numExp(){
        return new IntExp(Double.valueOf(tokenizer.current().getToken()));
    }


    public Exp characterExp(){
        return new charExp(tokenizer.current().getToken());
    }


}
