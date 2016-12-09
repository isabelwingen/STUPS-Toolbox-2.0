/* This file was generated by SableCC (http://www.sablecc.org/). */

package parser;

import node.*;

@SuppressWarnings("serial") 
public class ParserException extends Exception
{
    Token token;
    String realMsg;

    public ParserException(  Token token, String  message)
    {
        super(message);
        this.token = token;
    }

    public ParserException(  Token token, String prefix, String  message)
    {
        super(prefix+message);
        this.realMsg = message;
        this.token = token;
    }

    public Token getToken()
    {
        return this.token;
    }

    public String getRealMsg()
    {
        return this.realMsg;
    }
}
