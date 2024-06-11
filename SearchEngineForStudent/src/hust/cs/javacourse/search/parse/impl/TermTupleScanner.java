package hust.cs.javacourse.search.parse.impl;

import hust.cs.javacourse.search.index.AbstractTermTuple;
import hust.cs.javacourse.search.index.impl.Term;
import hust.cs.javacourse.search.index.impl.TermTuple;
import hust.cs.javacourse.search.parse.AbstractTermTupleScanner;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static hust.cs.javacourse.search.util.Config.STRING_SPLITTER_REGEX;

/**
 * AbstractTermTupleScanner的具体实现子类。
 */
public class TermTupleScanner extends AbstractTermTupleScanner {
    /**
     * 用于暂存从input中读入的单词
     */
    List<String> buffer = new ArrayList<>();
    int curPos;
    /**
     * 构造函数
     * @param input：指定输入流对象，应该关联到一个文本文件
     */
    public  TermTupleScanner(BufferedReader input){
        super(input);
    }
    /**
     * 获得下一个三元组
     * @return : 下一个三元组；如果到了流的末尾，返回null
     */
    public AbstractTermTuple next(){
        AbstractTermTuple tt = new TermTuple();
        while(this.buffer.isEmpty()){
            String line;
            try {
                line = input.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if(line == null)
                return null;
            else if(line.isBlank())
                continue;
            else
                this.buffer.addAll(Arrays.asList(line.trim().toLowerCase().split(STRING_SPLITTER_REGEX)));
            break;
        }
        tt.term.setContent(buffer.getFirst());

        buffer.removeFirst();
        tt.curPos = this.curPos++;
        return tt;
    }
}
