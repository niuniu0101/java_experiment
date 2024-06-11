package hust.cs.javacourse.search.query.impl;

import hust.cs.javacourse.search.index.AbstractPosting;
import hust.cs.javacourse.search.index.AbstractTerm;
import hust.cs.javacourse.search.index.AbstractTermTuple;
import hust.cs.javacourse.search.query.AbstractHit;

import java.util.List;
import java.util.Map;

/**
 * AbstractHit的具体实现子类。
 */
public class Hit extends AbstractHit {
    /**
     * 缺省构造函数
     */
    public Hit(){}

    /**
     * 构造器
     * @param docId：文档的id
     * @param docPath：文档的绝对路径
     */
    public Hit(int docId,String docPath){
        super(docId,docPath);
    }

    /**
     * 构造器
     * @param docId：文档的id
     * @param docPath：文档的绝对路径
     * @param termPostingMapping：文档被命中部分的映射
     */
    public Hit(int docId, String docPath, Map<AbstractTerm, AbstractPosting> termPostingMapping){
        super(docId,docPath,termPostingMapping);
    }

    /**
     * 获取docId
     * @return 成员docId
     */
    @Override
    public int getDocId() {
        return this.docId;
    }
    /**
     * 获取docPath
     * @return 成员docPath
     */
    @Override
    public String getDocPath() {
        return this.docPath;
    }
    /**
     * 获取content
     * @return 成员content
     */
    @Override
    public String getContent() {
        return this.content;
    }

    /**
     * 设置文档内容
     * @param content ：文档内容
     */
    @Override
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 获取分数score
     * @return 成员score
     */
    @Override
    public double getScore() {
        return this.score;
    }

    /**
     * 设置文档得分
     * @param score ：文档得分
     */
    @Override
    public void setScore(double score) {
        this.score = score;
    }

    /**
     * 获取Term到Posting的映射
     * @return 成员termPostingMapping
     */
    @Override
    public Map<AbstractTerm, AbstractPosting> getTermPostingMapping() {
        return this.termPostingMapping;
    }

    /**
     * 以良好的形式展示Hit的内容
     * @return Hit的字符串表示
     */
    @Override
    public String toString() {
        String retContent = this.content;
        StringBuffer sb1 = new StringBuffer("docId: " + this.docId + "\ndocPath: " + this.docPath + "\ncontent:\n"),sb2 = new StringBuffer("\nTermPostingMapping:\n");
        for(AbstractTerm at : this.termPostingMapping.keySet()){
            if(this.termPostingMapping.get(at).getDocId() == this.docId){
                sb2.append(at.getContent() + " ---> {" + this.termPostingMapping.get(at).toString() + "}\n");
                String regex = at.getContent();
//                若要通过测试集，就不能设置高亮。否则 toString() 检测不通过。
                retContent = retContent.replaceAll(regex,"\033[32m" + regex + "\033[0m");
            }
        }
        return sb1.append(retContent).append(sb2).append("score: " + this.score).toString();
    }

    /**
     * 以分数为标准，对两个AbstractHit对象进行对比
     * @param o the object to be compared.
     * @return
     */
    public int compareTo(AbstractHit o){
        return (int)(Math.round(this.score - ((Hit)o).score));
    }
}
