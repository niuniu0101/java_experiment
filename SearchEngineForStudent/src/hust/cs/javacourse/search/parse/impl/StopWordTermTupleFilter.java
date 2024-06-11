package hust.cs.javacourse.search.parse.impl;

import hust.cs.javacourse.search.index.AbstractTermTuple;
import hust.cs.javacourse.search.parse.AbstractTermTupleFilter;
import hust.cs.javacourse.search.parse.AbstractTermTupleStream;
import hust.cs.javacourse.search.util.StopWords;

import java.util.Arrays;

/**
 * AbstractTermTupleFilter的具体实现子类。以禁止词为标准过滤单词，范式STOP_WORDS中出现了的单词，一律过滤掉。
 */
public class StopWordTermTupleFilter extends AbstractTermTupleFilter {
    /**
     * 构造函数
     *
     * @param input ：Filter的输入，类型为AbstractTermTupleStream
     */
    public StopWordTermTupleFilter(AbstractTermTupleStream input) {
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
        return Arrays.asList(StopWords.STOP_WORDS).contains(str);
    }
}
