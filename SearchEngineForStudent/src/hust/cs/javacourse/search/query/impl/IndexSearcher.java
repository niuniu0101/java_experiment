package hust.cs.javacourse.search.query.impl;

import hust.cs.javacourse.search.index.AbstractPosting;
import hust.cs.javacourse.search.index.AbstractPostingList;
import hust.cs.javacourse.search.index.AbstractTerm;
import hust.cs.javacourse.search.query.AbstractHit;
import hust.cs.javacourse.search.query.AbstractIndexSearcher;
import hust.cs.javacourse.search.query.Sort;

import java.io.File;
import java.util.*;

/**
 * 是AbstractIndexSearcher的具体实现子类。
 */
public class IndexSearcher extends AbstractIndexSearcher {
    /**
     * 从指定索引文件打开索引，加载到index对象里. 一定要先打开索引，才能执行search方法
     * @param indexFile ：指定索引文件
     */
    public void open(String indexFile){
        this.index.load(new File(indexFile));
        this.index.optimize();
    }
    /**
     * 根据单个检索词进行搜索
     * @param queryTerm ：检索词
     * @param sorter ：排序器
     * @return ：命中结果数组
     */
    public AbstractHit[] search(AbstractTerm queryTerm, Sort sorter) {
        AbstractPostingList apl = this.index.search(queryTerm);
        if (apl != null) {
            List<AbstractHit> list = new ArrayList<>();
            for (int i = 0; i < apl.size(); i++) {
                AbstractPosting p = apl.get(i);
                Map<AbstractTerm,AbstractPosting> map= new HashMap<>();
                map.put(queryTerm,p);
                AbstractHit hit = new Hit(p.getDocId(), this.index.getDocName(p.getDocId()), map);
                list.add(hit);
                hit.setScore(sorter.score(hit));
            }
            sorter.sort(list);
            return list.toArray(new AbstractHit[0]);
        }
        return new AbstractHit[0];
    }
    /**
     *
     * 根据二个检索词进行搜索
     * @param queryTerm1 ：第1个检索词
     * @param queryTerm2 ：第2个检索词
     * @param sorter ：    排序器
     * @param combine ：   多个检索词的逻辑组合方式
     * @return ：命中结果数组
     */
    public AbstractHit[] search(AbstractTerm queryTerm1, AbstractTerm queryTerm2, Sort sorter, LogicalCombination combine){
        if(queryTerm1.equals(queryTerm2))
            return search(queryTerm1,sorter);
        AbstractPostingList apl1 = this.index.search(queryTerm1), apl2 = this.index.search(queryTerm2);
        List<AbstractHit> list = new ArrayList<>();
        if(combine == LogicalCombination.AND){
            if(apl1 == null || apl2 == null)
                return new AbstractHit[0];
            int i = 0,j = 0;
            while(i < apl1.size() && j < apl2.size()){
                if(apl1.get(i).getDocId() < apl2.get(j).getDocId())
                    i++;
                else if(apl1.get(i).getDocId() > apl2.get(j).getDocId())
                    j++;
                else{
                    Map<AbstractTerm,AbstractPosting> map = new HashMap<>();
                    map.put(queryTerm1,apl1.get(i));
                    map.put(queryTerm2,apl2.get(j));
                    AbstractHit hit = new Hit(apl1.get(i).getDocId(), this.index.getDocName(apl1.get(i).getDocId()), map);
                    list.add(hit);
                    hit.setScore(sorter.score(hit));
                    i++;j++;
                }
            }
            sorter.sort(list);
            return list.toArray(new AbstractHit[0]);
        }
        else{
            if(apl1 == null && apl2 == null)
                return new AbstractHit[0];
            if(apl1 == null)
                return search(queryTerm2,sorter);
            else if(apl2 == null)
                return search(queryTerm1,sorter);
            else{
                AbstractHit hit;
                int i = 0,j = 0;
                while(i < apl1.size() && j < apl2.size()){
                    Map<AbstractTerm,AbstractPosting> map = new HashMap<>();
                    if(apl1.get(i).getDocId() < apl2.get(j).getDocId()){
                        map.put(queryTerm1,apl1.get(i));
                        hit = new Hit(apl1.get(i).getDocId(), this.index.getDocName(apl1.get(i).getDocId()), map);
                        i++;
                    }
                    else if(apl1.get(i).getDocId() > apl2.get(j).getDocId()){
                        map.put(queryTerm2,apl2.get(j));
                        hit = new Hit(apl2.get(j).getDocId(), this.index.getDocName(apl2.get(j).getDocId()), map);
                        j++;
                    }
                    else{
                        map.put(queryTerm1,apl1.get(i));
                        map.put(queryTerm2,apl2.get(j));
                        hit = new Hit(apl1.get(i).getDocId(), this.index.getDocName(apl1.get(i).getDocId()), map);
                        i++;j++;
                    }
                    list.add(hit);
                    hit.setScore(sorter.score(hit));
                }
                if(i < apl1.size()){
                    while(i < apl1.size()){
                        Map<AbstractTerm,AbstractPosting> map = new HashMap<>();
                        map.put(queryTerm1,apl1.get(i));
                        hit = new Hit(apl1.get(i).getDocId(), this.index.getDocName(apl1.get(i).getDocId()), map);
                        i++;
                        list.add(hit);
                        hit.setScore(sorter.score(hit));
                    }
                }
                else{
                    while(j < apl2.size()){
                        Map<AbstractTerm,AbstractPosting> map = new HashMap<>();
                        map.put(queryTerm2,apl2.get(j));
                        hit = new Hit(apl2.get(j).getDocId(), this.index.getDocName(apl2.get(j).getDocId()), map);
                        j++;
                        list.add(hit);
                        hit.setScore(sorter.score(hit));
                    }
                }
                return list.toArray(new AbstractHit[0]);
            }
        }
    }
    /**
     *
     * 根据二个检索词进行搜索，并且要求位置相邻
     * @param queryTerm1 ：第1个检索词
     * @param queryTerm2 ：第2个检索词
     * @param sorter ：    排序器
     * @return ：命中结果数组
     */
}
