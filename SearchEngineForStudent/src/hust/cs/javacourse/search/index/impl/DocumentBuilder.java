package hust.cs.javacourse.search.index.impl;

import hust.cs.javacourse.search.index.AbstractDocument;
import hust.cs.javacourse.search.index.AbstractDocumentBuilder;
import hust.cs.javacourse.search.index.AbstractTermTuple;
import hust.cs.javacourse.search.parse.AbstractTermTupleFilter;
import hust.cs.javacourse.search.parse.AbstractTermTupleStream;
import hust.cs.javacourse.search.parse.impl.LengthTermTupleFilter;
import hust.cs.javacourse.search.parse.impl.PatternTermTupleFilter;
import hust.cs.javacourse.search.parse.impl.StopWordTermTupleFilter;
import hust.cs.javacourse.search.parse.impl.TermTupleScanner;

import javax.print.Doc;
import java.io.*;
/**
 * AbstractDocumentBuilder的具体实现类
 */
public class DocumentBuilder extends AbstractDocumentBuilder {
    /**
     * 缺省构造函数
     */
    public DocumentBuilder (){

    }
    /**
     * <pre>
     * 由解析文本文档得到的TermTupleStream,构造Document对象.
     * @param docId             : 文档id
     * @param docPath           : 文档绝对路径
     * @param termTupleStream   : 文档对应的TermTupleStream
     * @return ：Document对象
     * </pre>
     */
    public  AbstractDocument build(int docId, String docPath, AbstractTermTupleStream termTupleStream){
        AbstractDocument document = new Document(docId,docPath);
        for(AbstractTermTuple att = termTupleStream.next();att != null;att = termTupleStream.next()){
            document.getTuples().add(att);
        }
        return document;
    }
    /**
     * <pre>
     * 由给定的File,构造Document对象.
     * 该方法利用输入参数file构造出AbstractTermTupleStream子类对象后,内部调用
     *      AbstractDocument build(int docId, String docPath, AbstractTermTupleStream termTupleStream)
     * @param docId     : 文档id
     * @param docPath   : 文档绝对路径
     * @param file      : 文档对应File对象
     * @return          : Document对象
     * </pre>
     */
    public AbstractDocument build(int docId, String docPath, File file){
        AbstractTermTupleStream atts;
        try {
            atts = new TermTupleScanner(new BufferedReader(new InputStreamReader(new FileInputStream(file))));
            atts = new StopWordTermTupleFilter(atts); //再加上停用词过滤器
            atts = new PatternTermTupleFilter(atts); //再加上正则表达式过滤器
            atts = new LengthTermTupleFilter(atts); //再加上单词长度过滤器
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return this.build(docId,docPath,atts);
    }
}
