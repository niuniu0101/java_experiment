package hust.cs.javacourse.search.parse.impl;

import hust.cs.javacourse.search.index.AbstractTermTuple;
import hust.cs.javacourse.search.parse.AbstractTermTupleFilter;
import hust.cs.javacourse.search.parse.AbstractTermTupleStream;

import static hust.cs.javacourse.search.util.Config.*;

/**
 * AbstractTermTupleFilter的具体实现子类。以单词的长度为标准过滤单词，单词长度在TERM_FILTER_MINLENGTH与TERM_FILTER_MAXLENGTH之间。
 */
public class LengthTermTupleFilter extends AbstractTermTupleFilter {

    /**
     * 构造函数
     *
     * @param input ：Filter的输入，类型为AbstractTermTupleStream
     */
    public LengthTermTupleFilter(AbstractTermTupleStream input) {
        super(input);
    }
    /**
     * 获得下一个三元组
     * @return : 下一个三元组；如果到了流的末尾，返回null
     */
    @Override
    public AbstractTermTuple next() {
        AbstractTermTuple att = this.input.next();
        while(att != null){
            if(!this.ban(att.term.getContent()))
                return att;
            att = this.input.next();
        }
        return null;
    }
    /**
     * 判断一个单词是否被禁止
     * @param str : 单词
     * @return true 如果被禁止了
     */
    public boolean ban(String str){
        return str.length() < TERM_FILTER_MINLENGTH || str.length() > TERM_FILTER_MAXLENGTH;
    }
}
