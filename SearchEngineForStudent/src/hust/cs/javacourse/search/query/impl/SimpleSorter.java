package hust.cs.javacourse.search.query.impl;

import hust.cs.javacourse.search.index.AbstractPosting;
import hust.cs.javacourse.search.query.AbstractHit;
import hust.cs.javacourse.search.query.Sort;

import java.util.Comparator;
import java.util.List;

/**
 * 实现了Sort接口的排序类。
 */
public class SimpleSorter implements Sort {
    /**
     * 对命中结果集合根据文档得分排序
     * @param hits ：命中结果集合
     */
    public void sort(List<AbstractHit> hits){
        hits.sort(new Comparator<AbstractHit>() {
            @Override
            public int compare(AbstractHit o1, AbstractHit o2) {
                return (int)(o2.getScore() - o1.getScore());
            }
        });
    }

    /**
     * 计算命中文档的得分, 作为命中结果排序的依据.
     *      在该类中，直接以文档的命中Term数作为得分。
     * @param hit ：命中文档
     * @return ：命中文档的得分
     */
    public double score(AbstractHit hit){
        double score = 0;
        for(AbstractPosting ap : hit.getTermPostingMapping().values())
            score += ap.getFreq();
        return score;
    }
}
