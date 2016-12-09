/* This file was generated by SableCC (http://www.sablecc.org/). */

package node;

import analysis.*;

 
public final class TRBracket extends Token
{
    public TRBracket()
    {
        super.setText(")");
    }

    public TRBracket(int line, int pos)
    {
        super.setText(")");
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone()
    {
      return new TRBracket(getLine(), getPos());
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseTRBracket(this);
    }

    @Override
    public void setText(  String text)
    {
        throw new RuntimeException("Cannot change TRBracket text.");
    }
}
