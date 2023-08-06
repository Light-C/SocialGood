package touchfish.socialgood;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import java.util.ArrayList;

import touchfish.socialgood.search.Exp;
import touchfish.socialgood.search.ItemModel;
import touchfish.socialgood.search.Parser;
import touchfish.socialgood.search.Tokenizer;

public class ParserTest {
    private static Tokenizer tokenizer;
    private static final String SIMPLE_CASE1 = "a";
    private static final String SIMPLE_CASE2 = "2";
    private static final String MID_CASE = "ldj345f";
    private static final String MID_CASE1 = "23ldjf";
    private static final String MID_CASE2 = "ldjf23";
    private static final String COMPLEX_CASE1 = "(09sad7ur9824(*&(*&hjoiu90";

    ArrayList<ItemModel> test= new ArrayList();
    String[] items = {"happy","money","mark","mc","kfc","tired","fat"};
    double[] prices = {1000,2000,3000,111,7888,2323,8882};
    public ArrayList generateData(){
        for (int i =0 ;i<items.length;i++){
            ItemModel a = new ItemModel(items[i],prices[i] );
            test.add(a);
        }
        return test;
    }

    @Test(timeout=1000)
    public void testSimpleCase1(){
        tokenizer = new Tokenizer(SIMPLE_CASE1);
        try{
            Exp exp = new Parser(tokenizer).parseExp();
            assertEquals("incorrect display format", "a", exp.show());
            ArrayList result = new  ArrayList();
            assertEquals("incorrect identify value",'a', exp.identify(result));
        }catch (Exception e){
            fail(e.getMessage());
        }
    }

    @Test(timeout=1000)
    public void testMID_CASE(){
        tokenizer = new Tokenizer(MID_CASE1);
        try{
            Exp exp = new Parser(tokenizer).parseExp();
            assertEquals("incorrect display format", "((l characterSplitParse (d characterSplitParse (j characterSplitParse f))) wordSplitCharacter 23.0)", exp.show());
            ArrayList result = new  ArrayList();
            assertEquals("[23.0, l, d, j, f, (this Collection), (this Collection), (this Collection)]", exp.identify(result).toString());
        }catch (Exception e){
            fail(e.getMessage());
        }
    }

    @Test(timeout=1000)
    public void testCOMPLEX_CASE1(){
        tokenizer = new Tokenizer(COMPLEX_CASE1);
        try{
            Exp exp = new Parser(tokenizer).parseExp();
            assertEquals("incorrect display format", "(( characterSplitParse ((s characterSplitParse (a characterSplitParse (d characterSplitParse ((u characterSplitParse (r characterSplitParse ((( characterSplitParse (* characterSplitParse (& characterSplitParse (( characterSplitParse (* characterSplitParse (& characterSplitParse (h characterSplitParse (j characterSplitParse (o characterSplitParse (i characterSplitParse (u characterSplitParse 90.0))))))))))) wordSplitCharacter 9824.0))) wordSplitCharacter 7.0)))) wordSplitCharacter 9.0))", exp.show());
            ArrayList result = new  ArrayList();
            assertEquals("[(, 9.0, s, a, d, 7.0, u, r, 9824.0, (, *, &, (, *, &, h, j, o, i, u, 90.0, (this Collection), (this Collection), (this Collection), (this Collection), (this Collection), (this Collection), (this Collection), (this Collection), (this Collection), (this Collection), (this Collection), (this Collection), (this Collection), (this Collection), (this Collection), (this Collection), (this Collection), (this Collection), (this Collection), (this Collection)]", exp.identify(result).toString());
        }catch (Exception e){
            fail(e.getMessage());
        }
    }

    @Test(timeout=1000)
    public void testMID_CASE2(){
        tokenizer = new Tokenizer(MID_CASE2);
        try{
            Exp exp = new Parser(tokenizer).parseExp();
            assertEquals("incorrect display format", "(l characterSplitParse (d characterSplitParse (j characterSplitParse (f characterSplitParse 23.0))))", exp.show());
            ArrayList result = new  ArrayList();
            assertEquals("[l, d, j, f, 23.0, (this Collection), (this Collection), (this Collection), (this Collection)]", exp.identify(result).toString());
        }catch (Exception e){
            fail(e.getMessage());
        }
    }


    @Test(timeout=1000)
    public void COMPLEX_CASE1(){
        tokenizer = new Tokenizer(MID_CASE);
        try{
            Exp exp = new Parser(tokenizer).parseExp();
            assertEquals("incorrect display format", "(l characterSplitParse (d characterSplitParse (j characterSplitParse (f wordSplitCharacter 345.0))))", exp.show());
            ArrayList result = new  ArrayList();
            assertEquals("incorrect identify value","[l, d, j, 345.0, f, (this Collection), (this Collection), (this Collection)]", exp.identify(result).toString());
        }catch (Exception e){
            fail(e.getMessage());
        }
    }

    @Test(timeout=1000)
    public void testSimpleCase2(){
        tokenizer = new Tokenizer(SIMPLE_CASE2);
        try{
            Exp exp = new Parser(tokenizer).parseExp();
            assertEquals("incorrect display format", "2.0", exp.show());
            ArrayList result = new  ArrayList();
            assertEquals("incorrect identify value","[2.0]", exp.identify(result).toString());
        }catch (Exception e){
            fail(e.getMessage());
        }
    }

}
