package hust.cs.javacourse.search.index.impl;

import hust.cs.javacourse.search.index.AbstractTermTuple;

/**
 * AbstractTermTuple的具体实现类
 */
public class TermTuple extends AbstractTermTuple {
    /**
     * 缺省构造函数
     */
    public TermTuple(){
        this.term = new Term();
    }

    /**
     * 构造器
     * @param term：三元组所包含的Term
     */
    public TermTuple(Term term){
        this.term = term;
    }
    /**
     * 判断二个三元组内容是否相同
     * @param obj ：要比较的另外一个三元组
     * @return 如果内容相等（三个属性内容都相等）返回true，否则返回false
     */
    public boolean equals(Object obj){
        if(this == obj)
            return true;
        if(!(obj instanceof TermTuple))
            return false;
        if(this.term == null && ((TermTuple)obj).term != null || this.term != null && ((TermTuple)obj).term == null)
            return false;
        return this.curPos == ((TermTuple)obj).curPos && (this.term == ((TermTuple)obj).term || this.term.equals(((TermTuple)obj).term));
    }
    /**
     * 获得三元组的字符串表示
     * @return ： 三元组的字符串表示
     */
    public String toString(){
        return "term \"" + this.term + "\" appeared in position " + this.curPos;
    }
}
