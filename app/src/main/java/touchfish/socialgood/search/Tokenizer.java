package touchfish.socialgood.search;

import java.util.Scanner;

/**
 * Welcome to Task 1.
 * In this task your job is to implement the next() method. Just some rules:
 * 1. You may NOT modify any other classes in this task 1 package.
 * 2. You may not modify any of the methods or fields (that already exist) within this class.
 * 3. You may create additional fields or methods to finish you implementation within this class.
 * <p>
 * Tokenization, within the context of this lab, is the process of splitting a string into
 * small units called, 'Tokens', to be passed onto the Parser.
 */
public class Tokenizer {
    private String buffer;          // String to be transformed into tokens each time next() is called.
    private Token currentToken;     // The current token. The next token is extracted when next() is called.

    /**
     * Tokenizer class constructor
     * The constructor extracts the first token and save it to currentToken
     * **** please do not modify this part ****
     */
    public Tokenizer(String text) {
        buffer = text;          // save input text (string)
        next();                 // extracts the first token.
    }

    public  String[] getNumber(String buffer,String num){
        String[] result=null;
        num = num+ buffer.substring(0,1);
        buffer = buffer.substring(1);
        if (buffer.length()!=0 && Character.isDigit(buffer.charAt(0))){
            result = getNumber(buffer,num);
        }else {
            result = new String[]{num, buffer};
        }
        return result;
    }

    /**
     * This function will find and extract a next token from {@code _buffer} and
     * save the token to {@code currentToken}.
     */
    public void next() {
        buffer = buffer.trim();     // remove whitespace

        if (buffer.isEmpty()) {
            currentToken = null;    // if there's no string left, set currentToken null and return
            return;
        }

        /*
        To help you, we have already written the first few steps in the tokenization process.
        The rest will follow a similar format.
         */
        char firstChar = buffer.charAt(0);

        if (Character.isDigit(firstChar)){
            String[] result = getNumber(buffer,"");
            currentToken = new Token(result[0], Token.Type.INT);
        }else if ((int)firstChar>=65 && (int)firstChar<91){
            String letter = String.valueOf(firstChar);
            currentToken = new Token(letter, Token.Type.ALPHABET);
        }else if ( (int)firstChar>96 && (int)firstChar<=122){
            String letter = String.valueOf(firstChar);
            currentToken = new Token(letter, Token.Type.ALPHABET);
        }
        else {
            String other = String.valueOf(firstChar);
            currentToken = new Token(other, Token.Type.OTHER);
        }



        // ########## YOUR CODE ENDS HERE ##########
        // Remove the extracted token from buffer
        int tokenLen = currentToken.getToken().length();
        buffer = buffer.substring(tokenLen);
    }

    /**
     * Returns the current token extracted by {@code next()}
     * **** please do not modify this part ****
     *
     * @return type: Token
     */
    public Token current() {
        return currentToken;
    }

    /**
     * Check whether tokenizer still has tokens left
     * **** please do not modify this part ****
     *
     * @return type: boolean
     */
    public boolean hasNext() {
        return currentToken != null;
    }
}
