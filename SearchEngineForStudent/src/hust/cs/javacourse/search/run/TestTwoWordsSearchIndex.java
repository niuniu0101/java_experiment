package hust.cs.javacourse.search.run;

import hust.cs.javacourse.search.index.impl.Term;
import hust.cs.javacourse.search.query.AbstractHit;
import hust.cs.javacourse.search.query.AbstractIndexSearcher;
import hust.cs.javacourse.search.query.Sort;
import hust.cs.javacourse.search.query.impl.IndexSearcher;
import hust.cs.javacourse.search.query.impl.SimpleSorter;
import hust.cs.javacourse.search.util.Config;
import hust.cs.javacourse.search.util.StringSplitter;

import java.util.List;
import java.util.Scanner;

/**
 * 测试两个单词的搜索
 */
public class TestTwoWordsSearchIndex {
    /**
     *  索引构建程序入口
     * @param args : 命令行参数
     */
    public static void main(String[] args){
        Sort simpleSorter = new SimpleSorter();
        String indexFile = Config.INDEX_DIR + "index.dat";
        AbstractIndexSearcher searcher = new IndexSearcher();
        searcher.open(indexFile);
        Scanner input=new Scanner(System.in);
        String word=input.nextLine();
        StringSplitter splitter=new StringSplitter();
        splitter.setSplitRegex(Config.STRING_SPLITTER_REGEX);
        while(!word.equals("a")) {
            List<String> term= splitter.splitByRegex(word);
            AbstractHit[] hits;
            if(term.size()==1) {
                hits = searcher.search(new Term(word), simpleSorter);
            }
            else if(term.size()==3&&term.get(1).equals("OR")){
                hits = searcher.search(new Term(term.get(0)),new Term(term.get(2)),simpleSorter, AbstractIndexSearcher.LogicalCombination.OR);
            }
            else if(term.size()==3&&term.get(1).equals("AND")){
                hits = searcher.search(new Term(term.get(0)), new Term(term.get(2)), simpleSorter, AbstractIndexSearcher.LogicalCombination.AND);
            }
            else{
                hits = searcher.search(new Term(term.get(0)), new Term(term.get(1)), simpleSorter,AbstractIndexSearcher.LogicalCombination.AND);
            }
            if(hits.length == 0)
                System.out.println("No hits found for this search. ");
            else for (AbstractHit hit : hits) {
                System.out.println(hit);
            }
            word=input.next();
        }

    }
}
