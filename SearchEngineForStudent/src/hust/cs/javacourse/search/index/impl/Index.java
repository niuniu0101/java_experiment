package hust.cs.javacourse.search.index.impl;

import hust.cs.javacourse.search.index.*;

import java.io.*;
import java.util.Set;

/**
 * AbstractIndex的具体实现类
 */
public class Index extends AbstractIndex {
    /**
     * 返回索引的字符串表示
     *
     * @return 索引的字符串表示
     */
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer().append("docId to docPath mapping:\n");
        for(int docId : this.docIdToDocPathMapping.keySet())
            sb.append(docId).append(" ---> ").append(this.docIdToDocPathMapping.get(docId)).append('\n');
        sb.append("Totally ").append(this.docIdToDocPathMapping.size()).append(" mappings.\n").append("term to postingList mapping:\n");
        for(AbstractTerm t : this.termToPostingListMapping.keySet())
            sb.append(t.toString()).append(" ---> ").append(this.termToPostingListMapping.get(t).toString()).append('\n');
        sb.append("Totally ").append(this.termToPostingListMapping.size()).append(" mappings.\n");
        return sb.toString();
    }

    /**
     * 添加文档到索引，更新索引内部的HashMap
     *
     * @param document ：文档的AbstractDocument子类型表示
     */
    @Override
    public void addDocument(AbstractDocument document) {
        this.docIdToDocPathMapping.put(document.getDocId(),document.getDocPath());
        for(AbstractTermTuple att : document.getTuples()){
            AbstractTerm t = att.term;
            if(!this.termToPostingListMapping.containsKey(t))
                this.termToPostingListMapping.put(t,new PostingList());
            AbstractPostingList apl = this.termToPostingListMapping.get(t);
            AbstractPosting ap;
            int index;
            if((index = apl.indexOf(document.getDocId())) == -1){
                ap = new Posting();
                ap.setDocId(document.getDocId());
                apl.add(ap);
            }
            else{
                ap = apl.get(index);
                ap.setDocId(document.getDocId());
            }
            ap.setFreq(ap.getFreq() + 1);
            ap.getPositions().add(att.curPos);
        }
        this.optimize();
    }

    /**
     * <pre>
     * 从索引文件里加载已经构建好的索引.内部调用FileSerializable接口方法readObject即可
     * @param file ：索引文件
     * </pre>
     */
    @Override
    public void load(File file) {
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
            this.readObject(in);
            in.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * <pre>
     * 将在内存里构建好的索引写入到文件. 内部调用FileSerializable接口方法writeObject即可
     * @param file ：写入的目标索引文件
     * </pre>
     */
    @Override
    public void save(File file) {
        try {
            ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(file));
            this.writeObject(output);
            output.flush();
            output.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 返回指定单词的PostingList
     *
     * @param term : 指定的单词
     * @return ：指定单词的PostingList;如果索引字典没有该单词，则返回null
     */
    @Override
    public AbstractPostingList search(AbstractTerm term) {
        if(this.termToPostingListMapping.containsKey(term))
            return this.termToPostingListMapping.get(term);
        return null;
    }

    /**
     * 返回索引的字典.字典为索引里所有单词的并集
     *
     * @return ：索引中Term列表
     */
    @Override
    public Set<AbstractTerm> getDictionary() {
        return this.termToPostingListMapping.keySet();
    }

    /**
     * <pre>
     * 对索引进行优化，包括：
     *      对索引里每个单词的PostingList按docId从小到大排序
     *      同时对每个Posting里的positions从小到大排序
     * 在内存中把索引构建完后执行该方法
     * </pre>
     */
    @Override
    public void optimize() {
        for(AbstractTerm t : this.termToPostingListMapping.keySet()){
            AbstractPostingList l = this.termToPostingListMapping.get(t);
            l.sort();
            AbstractPosting p;
            for(int i = 0;(p = l.get(i)) != null;i++)
                p.sort();
        }
    }

    /**
     * 根据docId获得对应文档的完全路径名
     *
     * @param docId ：文档id
     * @return : 对应文档的完全路径名
     */
    @Override
    public String getDocName(int docId) {
        return this.docIdToDocPathMapping.get(docId);
    }

    /**
     * 写到二进制文件
     *
     * @param out :输出流对象
     */
    @Override
    public void writeObject(ObjectOutputStream out) {
        try {
            out.writeInt(this.docIdToDocPathMapping.size());
            for(int i : this.docIdToDocPathMapping.keySet()){
                out.writeInt(i);
                out.writeInt(this.docIdToDocPathMapping.get(i).length());
                out.writeChars(this.docIdToDocPathMapping.get(i));
            }
            out.writeInt(this.termToPostingListMapping.size());
            for(AbstractTerm t : this.termToPostingListMapping.keySet()){
                t.writeObject(out);
                this.termToPostingListMapping.get(t).writeObject(out);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 从二进制文件读
     *
     * @param in ：输入流对象
     */
    @Override
    public void readObject(ObjectInputStream in) {
        try {
            int size = in.readInt();
            for(int i = 0;i < size;i++){
                int docId,stringLength;
                StringBuffer sb = new StringBuffer();
                docId = in.readInt();
                stringLength = in.readInt();
                for(int j = 0;j < stringLength;j++)
                    sb.append(in.readChar());
                this.docIdToDocPathMapping.put(docId,sb.toString());
            }
            size = in.readInt();
            for(int i = 0;i < size;i++){
                AbstractTerm t = new Term();
                AbstractPostingList l = new PostingList();
                t.readObject(in);
                l.readObject(in);
                this.termToPostingListMapping.put(t,l);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
