package hust.cs.javacourse.search.index.impl;

import hust.cs.javacourse.search.index.AbstractPosting;
import hust.cs.javacourse.search.index.AbstractPostingList;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Comparator;
import java.util.List;

/**
 * AbstractPostingList的具体实现类
 */
public class PostingList extends AbstractPostingList {
    /**
     * 添加Posting,要求不能有内容重复的posting
     * @param posting：Posting对象
     */
    public void add(AbstractPosting posting){
        for(int i = 0;i < this.list.size();i++){
            if(this.list.get(i).getDocId() < posting.getDocId())
                continue;
            // 不处理重复的posting
            if(this.list.get(i).getDocId() == posting.getDocId())
                return;
            this.list.add(i,posting);
            return;
        }
        // 如果一直到尾部都没找到插入的位置，则插入到尾部。
        this.list.add(posting);
    }
    /**
     * 获得PosingList的字符串表示
     * @return ： PosingList的字符串表示
     */
    @Override
    public String toString(){
        if(this.list.isEmpty())
            return "";
        StringBuffer sb = new StringBuffer();
        for(int i = 0;i < this.list.size() - 1;i++)
            sb.append('{').append(this.list.get(i).toString()).append("} -> ");
        sb.append('{').append(this.list.getLast().toString()).append('}');
        return sb.toString();
    }
    /**
     * 添加Posting列表,,要求不能有内容重复的posting
     * @param postings：Posting列表
     */
    public void add(List<AbstractPosting> postings){
        for(AbstractPosting p : postings)
            this.add(p);
    }
    /**
     * 返回指定下标位置的Posting
     * @param index ：下标
     * @return ： 指定下标位置的Posting
     */
    public AbstractPosting get(int index){
        return (index < 0 || index >= this.list.size()) ? null : this.list.get(index);
    }
    /**
     * 返回指定Posting对象的下标
     * @param posting：指定的Posting对象
     * @return ：如果找到返回对应下标；否则返回-1
     */
    public int indexOf(AbstractPosting posting){
        for(int i = 0;i < this.list.size();i++){
            if(this.list.get(i).equals(posting))
                return i;
        }
        return -1;
    }
    /**
     * 返回指定文档id的Posting对象的下标
     * @param docId ：文档id
     * @return ：如果找到返回对应下标；否则返回-1
     */
    public int indexOf(int docId){
        for(int i = 0;i < this.list.size();i++){
            if(this.list.get(i).getDocId() == docId)
                    return i;
        }
        return -1;
    }
    /**
     * 是否包含指定Posting对象
     * @param posting： 指定的Posting对象
     * @return : 如果包含返回true，否则返回false
     */
    public boolean contains(AbstractPosting posting){
        return this.indexOf(posting) != -1;
    }
    /**
     * 删除指定下标的Posting对象
     * @param index：指定的下标
     */
    public void remove(int index){
        if(index >=0 && index < this.list.size())
            this.list.remove(index);
    }
    /**
     * 删除指定的Posting对象
     * @param posting ：定的Posting对象
     */
    public void remove(AbstractPosting posting){
        this.remove(this.indexOf(posting));
    }
    /**
     * 返回PostingList的大小，即包含的Posting的个数
     * @return ：PostingList的大小
     */
    public int size(){
        return this.list.size();
    }
    /**
     * 清除PostingList
     */
    public void clear(){
        this.list.clear();
    }
    /**
     * PostingList是否为空
     * @return 为空返回true;否则返回false
     */
    public boolean isEmpty(){
        return this.list.isEmpty();
    }
    /**
     * 根据文档id的大小对PostingList进行从小到大的排序
     */
    public  void sort(){
        this.list.sort(new Comparator<AbstractPosting>() {
            @Override
            public int compare(AbstractPosting o1, AbstractPosting o2) {
                return o1.getDocId() - o2.getDocId();
            }
        });
    }
    /**
     * 写到二进制文件
     * @param out :输出流对象
     */
    @Override
    public void writeObject(ObjectOutputStream out) {
        try {
            out.writeInt(this.list.size());
            for(AbstractPosting p : this.list)
                p.writeObject(out);
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * 从二进制文件读
     * @param in ：输入流对象
     */
    @Override
    public void readObject(ObjectInputStream in) {
        try {
            int size = in.readInt();
            Posting p = null;
            for(int i = 0;i < size;i++){
                p = new Posting();
                p.readObject(in);
                this.list.add(p);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
