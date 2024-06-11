package hust.cs.javacourse.search.index.impl;

import hust.cs.javacourse.search.index.AbstractTerm;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * AbstractTerm的具体实现类
 */
public class Term extends AbstractTerm {
    /**
     * 缺省构造函数
     */
    public Term(){

    }
    public Term(String content){
        super(content);
    }
    /**
     * 判断二个Term内容是否相同
     * @param obj ：要比较的另外一个Term
     * @return 如果内容相等返回true，否则返回false
     */
    public boolean equals(Object obj){
        if(this == obj)
            return true;
        if(!(obj instanceof Term))
            return false;
        if(this.content == null && ((Term)obj).content != null || this.content != null && ((Term)obj).content == null)
            return false;
        return this.content == ((Term)obj).content || this.content.equals(((Term) obj).content);
    }
    /**
     * 返回Term的字符串表示
     * @return 字符串
     */
    @Override
    public String toString() {
        return this.content;
    }
    /**
     * 返回Term内容
     * @return Term内容
     */
    public String getContent(){
        return this.content;
    }
    /**
     * 设置Term内容
     * @param content：Term的内容
     */
    public void setContent(String content){
        this.content = content;
    }
    /**
     * 比较二个Term大小（按字典序）
     * @param o： 要比较的Term对象
     * @return ： 返回二个Term对象的字典序差值
     */
    public int compareTo(AbstractTerm o){
        return this.content.compareTo(((Term)o).content);
    }
    /**
     * 写到二进制文件
     * @param out :输出流对象
     */
    public void writeObject(ObjectOutputStream out){
        try {
            out.writeInt(this.content.length());
            for(int i = 0;i < this.content.length();i++)
                out.writeChar(this.content.charAt(i));
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
        StringBuffer sb = new StringBuffer();
        try {
            int length = in.readInt();
            for(int j = 0;j < length;j++)
                sb.append(in.readChar());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.content = sb.toString();
    }
}
