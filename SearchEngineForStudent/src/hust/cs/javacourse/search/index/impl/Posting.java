package hust.cs.javacourse.search.index.impl;

import hust.cs.javacourse.search.index.AbstractPosting;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Comparator;
import java.util.List;
/**
 * AbstractPosting的具体实现类
 */
public class Posting extends AbstractPosting {
    /**
     * 缺省构造函数
     */
    public Posting(){

    }
    public Posting(int docId, int freq, List<Integer> positions){
        super(docId,freq,positions);
    }
    /**
     * 判断二个Posting内容是否相同
     * @param obj ：要比较的另外一个Posting
     * @return 如果内容相等返回true，否则返回false
     */
    public boolean equals(Object obj){
        if(this == obj || (obj instanceof Posting) && this.docId == ((Posting) obj).docId && this.freq == ((Posting)obj).freq){
            for(int pos : this.positions)
                if(!((Posting) obj).positions.contains(pos))
                    return false;
            for(int pos : ((Posting) obj).positions)
                if(!(this.positions.contains(pos)))
                    return false;
            return true;
        }
        return false;
    }
    /**
     * 返回Posting的字符串表示
     * @return 字符串
     */
    public String toString(){
        StringBuffer sb = new StringBuffer("\"docID\": " + this.docId + ", \"freq\": " + this.freq + ", \"positions\": [");
        for(int pos : this.positions){
            sb.append(pos);
            sb.append(' ');
        }
        return sb.deleteCharAt(sb.length() - 1).append(']').toString();
    }
    /**
     * 返回包含单词的文档id
     * @return ：文档id
     */
    public int getDocId(){
        return this.docId;
    }
    /**
     * 设置包含单词的文档id
     * @param docId：包含单词的文档id
     */
    public void setDocId(int docId){
        this.docId = docId;
    }
    /**
     * 返回单词在文档里出现的次数
     * @return ：出现次数
     */
    public int getFreq(){
        return this.freq;
    }
    /**
     * 设置单词在文档里出现的次数
     * @param freq:单词在文档里出现的次数
     */
    public void setFreq(int freq){
        this.freq = freq;
    }
    /**
     * 返回单词在文档里出现的位置列表
     * @return ：位置列表
     */
    public List<Integer> getPositions(){
        return this.positions;
    }
    /**
     * 设置单词在文档里出现的位置列表
     * @param positions：单词在文档里出现的位置列表
     */
    public void setPositions(List<Integer> positions){
        this.positions = positions;
    }
    /**
     * 比较二个Posting对象的大小（根据docId）
     * @param o： 另一个Posting对象
     * @return ：二个Posting对象的docId的差值
     */
    @Override
    public int compareTo(AbstractPosting o){
        return this.docId - ((Posting)o).docId;
    }
    /**
     * 对内部positions从小到大排序
     */
    public void sort(){
        this.positions.sort(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1 - o2;
            }
        });
    }
    /**
     * 写到二进制文件
     * @param out :输出流对象
     */
    public void writeObject(ObjectOutputStream out){
        try {
            out.writeInt(this.docId);
            out.writeInt(this.freq);
            out.writeInt(this.positions.size());
            for(int pos : this.positions)
                    out.writeInt(pos);
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * 从二进制文件读
     * @param in ：输入流对象
     */
    public void readObject(ObjectInputStream in){
        try {
            this.docId = in.readInt();
            this.freq = in.readInt();
            int size = in.readInt();
            for(int i = 0;i < size;i++)
                this.positions.add(in.readInt());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
