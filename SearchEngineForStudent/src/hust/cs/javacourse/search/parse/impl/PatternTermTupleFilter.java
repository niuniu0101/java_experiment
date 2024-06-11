package hust.cs.javacourse.search.parse.impl;

import hust.cs.javacourse.search.index.AbstractTermTuple;
import hust.cs.javacourse.search.parse.AbstractTermTupleFilter;
import hust.cs.javacourse.search.parse.AbstractTermTupleStream;

import static hust.cs.javacourse.search.util.Config.*;

/**
 * AbstractTermTupleFilter的具体实现子类。以正则表达式为标准过滤单词，过滤掉含有非字母字符的单词。
 */
public class PatternTermTupleFilter extends AbstractTermTupleFilter {
    /**
     * 构造函数
     *
     * @param input ：Filter的输入，类型为AbstractTermTupleStream
     */
    public PatternTermTupleFilter(AbstractTermTupleStream input) {
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
        return !str.matches(TERM_FILTER_PATTERN);
    }
}
