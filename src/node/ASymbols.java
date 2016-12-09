/* This file was generated by SableCC (http://www.sablecc.org/). */

package node;

import java.util.*;
import analysis.*;

 
public final class ASymbols extends PSymbols
{
    private final LinkedList<TSymbol> _inputletters_ = new LinkedList<TSymbol>();
    private final LinkedList<TSymbol> _stackletters_ = new LinkedList<TSymbol>();
    private final LinkedList<TIdentifier> _states_ = new LinkedList<TIdentifier>();
    private TIdentifier _startState_;
    private TSymbol _bottom_;

    public ASymbols()
    {
        // Constructor
    }

    public ASymbols(
          List<TSymbol> _inputletters_,
          List<TSymbol> _stackletters_,
          List<TIdentifier> _states_,
          TIdentifier _startState_,
          TSymbol _bottom_)
    {
        // Constructor
        setInputletters(_inputletters_);

        setStackletters(_stackletters_);

        setStates(_states_);

        setStartState(_startState_);

        setBottom(_bottom_);

    }

    @Override
    public Object clone()
    {
        return new ASymbols(
            cloneList(this._inputletters_),
            cloneList(this._stackletters_),
            cloneList(this._states_),
            cloneNode(this._startState_),
            cloneNode(this._bottom_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseASymbols(this);
    }

    public LinkedList<TSymbol> getInputletters()
    {
        return this._inputletters_;
    }

    public void setInputletters(List<TSymbol> list)
    {
        this._inputletters_.clear();
        this._inputletters_.addAll(list);
        for(TSymbol e : list)
        {
            if(e.parent() != null)
            {
                e.parent().removeChild(e);
            }

            e.parent(this);
        }
    }

    public LinkedList<TSymbol> getStackletters()
    {
        return this._stackletters_;
    }

    public void setStackletters(List<TSymbol> list)
    {
        this._stackletters_.clear();
        this._stackletters_.addAll(list);
        for(TSymbol e : list)
        {
            if(e.parent() != null)
            {
                e.parent().removeChild(e);
            }

            e.parent(this);
        }
    }

    public LinkedList<TIdentifier> getStates()
    {
        return this._states_;
    }

    public void setStates(List<TIdentifier> list)
    {
        this._states_.clear();
        this._states_.addAll(list);
        for(TIdentifier e : list)
        {
            if(e.parent() != null)
            {
                e.parent().removeChild(e);
            }

            e.parent(this);
        }
    }

    public TIdentifier getStartState()
    {
        return this._startState_;
    }

    public void setStartState(TIdentifier node)
    {
        if(this._startState_ != null)
        {
            this._startState_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._startState_ = node;
    }

    public TSymbol getBottom()
    {
        return this._bottom_;
    }

    public void setBottom(TSymbol node)
    {
        if(this._bottom_ != null)
        {
            this._bottom_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._bottom_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._inputletters_)
            + toString(this._stackletters_)
            + toString(this._states_)
            + toString(this._startState_)
            + toString(this._bottom_);
    }

    @Override
    void removeChild(  Node child)
    {
        // Remove child
        if(this._inputletters_.remove(child))
        {
            return;
        }

        if(this._stackletters_.remove(child))
        {
            return;
        }

        if(this._states_.remove(child))
        {
            return;
        }

        if(this._startState_ == child)
        {
            this._startState_ = null;
            return;
        }

        if(this._bottom_ == child)
        {
            this._bottom_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(  Node oldChild,   Node newChild)
    {
        // Replace child
        for(ListIterator<TSymbol> i = this._inputletters_.listIterator(); i.hasNext();)
        {
            if(i.next() == oldChild)
            {
                if(newChild != null)
                {
                    i.set((TSymbol) newChild);
                    newChild.parent(this);
                    oldChild.parent(null);
                    return;
                }

                i.remove();
                oldChild.parent(null);
                return;
            }
        }

        for(ListIterator<TSymbol> i = this._stackletters_.listIterator(); i.hasNext();)
        {
            if(i.next() == oldChild)
            {
                if(newChild != null)
                {
                    i.set((TSymbol) newChild);
                    newChild.parent(this);
                    oldChild.parent(null);
                    return;
                }

                i.remove();
                oldChild.parent(null);
                return;
            }
        }

        for(ListIterator<TIdentifier> i = this._states_.listIterator(); i.hasNext();)
        {
            if(i.next() == oldChild)
            {
                if(newChild != null)
                {
                    i.set((TIdentifier) newChild);
                    newChild.parent(this);
                    oldChild.parent(null);
                    return;
                }

                i.remove();
                oldChild.parent(null);
                return;
            }
        }

        if(this._startState_ == oldChild)
        {
            setStartState((TIdentifier) newChild);
            return;
        }

        if(this._bottom_ == oldChild)
        {
            setBottom((TSymbol) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}
