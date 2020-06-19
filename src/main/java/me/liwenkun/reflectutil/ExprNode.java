package me.liwenkun.reflectutil;

public abstract class ExprNode {

    private ExprNode next;

    public abstract Object evaluate(Object instance, Object... params) throws EvaluateException;

    public abstract int getParamCount();

    ExprNode next() {
        return next;
    }

    void setNext(ExprNode next) {
        this.next = next;
    }
}
