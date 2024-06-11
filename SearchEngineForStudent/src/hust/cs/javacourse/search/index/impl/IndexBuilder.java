package hust.cs.javacourse.search.index.impl;

import hust.cs.javacourse.search.index.AbstractDocument;
import hust.cs.javacourse.search.index.AbstractDocumentBuilder;
import hust.cs.javacourse.search.index.AbstractIndex;
import hust.cs.javacourse.search.index.AbstractIndexBuilder;
import hust.cs.javacourse.search.util.Config;
import hust.cs.javacourse.search.util.FileUtil;

import java.io.File;
import java.io.IOException;
/**
 * AbstractIndexBuilder的具体实现类
 */
public class IndexBuilder extends AbstractIndexBuilder {
    /**
     * 构建索引必须解析文档构建Document对象，因此包含AbstractDocumentBuilder的子类对象
     */
    public IndexBuilder(AbstractDocumentBuilder docBuilder){
        super(docBuilder);
    }
    /**
     * <pre>
     * 构建指定目录下的所有文本文件的倒排索引.
     *      需要遍历和解析目录下的每个文本文件, 得到对应的Document对象，再依次加入到索引，并将索引保存到文件.
     * @param rootDirectory ：指定目录
     * @return ：构建好的索引
     * </pre>
     */
    public AbstractIndex buildIndex(String rootDirectory){
        AbstractIndex index = new Index();
        for(String path : FileUtil.list(rootDirectory)){
            AbstractDocument document = this.docBuilder.build(docId++,path,new File(path));
            index.addDocument(document);
        }
        File file = new File(Config.INDEX_DIR.substring(0,Config.INDEX_DIR.length() - 1));
        if(!file.exists())
            file.mkdir();
        file = new File(Config.INDEX_DIR + "index.dat");
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        index.save(file);
        return index;
    }
}
