package to._2v.tools.util;

import java.util.ArrayList;
import java.util.List;
/**
 * Pagination類別是一個具有分頁功能的bean, 它通過接收當前頁數,分頁單位和待分頁的List,計算出分頁總數和分頁後<br>
 * 每一頁的紀錄並把這些紀錄存在一個子List裏面.<br>
 * 
 * 示例1:對紀錄總數爲11的List進行分頁,當前頁爲1,分頁單位爲5.則用以下寫法.<br>
 *  
 *   Pagination pagination = new Pagination(1,5,list);<br>
 *   List subList = pagination.getSubSet();<br>
 *  
 *   subList得到的是下標從0到4的5條紀錄.<br>
 *   
 * 示例2:對紀錄總數爲11的List進行分頁,當前頁爲1,分頁單位爲5,空構造函數寫法<br>
 * 
 *   Pagination pagination = new Pagination();<br>
 *   List subList = pagination.getSubSetByParm(1,5,list);<br>
 *  
 *   subList得到的是下標從0到4的5條紀錄.<br>
 * @version 1.1 03/12/07   
 * @author angel
 * @since 1.4
 */
public class Pagination {
    /**
     * START_SUFFIX_FORM_ZERO : 表示待分頁的List下標是從0開始
     */
	public static final int START_SUFFIX_FORM_ZERO = 0;
	/**
	 * START_SUFFIX_FORM_ONE : 表示待分頁的List下標是從1開始
	 */
	public static final int START_SUFFIX_FORM_ONE = 1;
	
	private int currPage;
	private int allPages;
	private int allCol;
	int backPage;
	int nextPage;
	private List subSet = new ArrayList();
	
	/**
	 * Pagination 的構造參數,不帶參數
	 *
	 */
	public Pagination(){
		
	}
	/**
	 * Pagination 的構造參數,當不指定待排序List下標的起始值,默認從0開始,當不指定當前頁時,當前頁默認爲第一頁
	 * @param iPage: 當前頁碼
	 * @param set: 待分頁的List
	 */
	public Pagination(int iPage,List set){ 
		this(1,iPage,set,START_SUFFIX_FORM_ZERO);
	}
	/**
	 * Pagination 的構造參數,當不指定待排序List下標的起始值,默認從0開始.
	 * @param currPage: 當前頁碼
	 * @param iPage: 分頁單位
	 * @param set: 待分頁的List
	 */
	public Pagination(int currPage,int iPage,List set){ 
		this(currPage,iPage,set,START_SUFFIX_FORM_ZERO);
	}
	/**
	 * Pagination 的構造參數,可指定List數組下標是從0還是從1開始.
	 * @param currPage: 當前頁碼
	 * @param iPage: 分頁單位
	 * @param set: 待分頁的List
	 * @param startSuffixForList: 確定待分頁的List是從0還是1開始,接收參數0或1.
	 */
	public Pagination(int currPage,int iPage,List set,int startSuffixForList){
		
		this.currPage = currPage;
		this.allPages = this.getAllPages(iPage,set.size());
		this.allCol = set.size();
        this.subSet = this.getSubSetByParm(currPage, iPage, set, startSuffixForList);
        this.nextPage = this.currPage + 1;
        this.backPage = this.currPage - 1;
	}
	/**
	 * 通過當前頁碼,分頁單位和待分頁的set得到分頁後該頁的紀錄集合
	 * @param currPage: 當前頁碼
	 * @param iPage: 分頁單位
	 * @param set: 待分頁的List
	 * @return 返回分類後該頁的紀錄集合
	 */
	public List getSubSetByParm(int currPage,int iPage,List set){
		return this.getSubSetByParm(currPage, iPage, set, START_SUFFIX_FORM_ZERO);
	}
	/**
	 * 通過當前頁碼,分頁單位,待分頁的set和該set的下標起始值得到分頁後該頁的紀錄集合
	 * @param currPage: 當前頁碼
	 * @param iPage: 分頁單位
	 * @param set: 待分頁的List
	 * @param startSuffixForList
	 * @return 返回分類後該頁的紀錄集合
	 */
	public List getSubSetByParm(int currPage,int iPage,List set,int startSuffixForList){
		int startSuffix = 0;
        if(startSuffixForList==START_SUFFIX_FORM_ONE){
			
			startSuffix = getStartSuffixFor1(currPage,iPage);
			for(int i=startSuffix,j=0;i<startSuffix+iPage;i++,j++){
					if(i<=set.size())
				       subSet.set(j, set.get(i));
					else break;
			}
		}else if(startSuffixForList==START_SUFFIX_FORM_ZERO){
			
			startSuffix = getStartSuffixFor0(currPage-1,iPage); 
			
			for(int i=startSuffix,j=0; i<startSuffix+iPage; i++,j++){	
					if(i<set.size())
					   subSet.add(j, set.get(i));
					else break;
				
			}
			
		}
        return subSet;
	}
	
	/**
	 * 
	 * @param iPage: 分頁單位
	 * @param allCol: 待分頁List的總紀錄數
	 * @return 返回分頁數
	 */
	public int getAllPages(int iPage,int allCol){ //根据分页单位和总记录,返回分页数
	     
		return (int)Math.ceil((allCol+iPage-1)/iPage);
	}
	
	private int getStartSuffixFor1(int currPage,int iPage){ //通过当前页和分页单位找到起始下标
		
		return (currPage-1)*iPage+1;
	}
	private int getStartSuffixFor0(int currPage,int iPage){ //通过当前页和分页单位找到起始下标
		return currPage*iPage;
	}	
	
	
    /**
     * 得到分頁總數
     * @return 返回分頁總數
     */
	public int getAllPages() {
		return allPages;
	}
    /**
     * 得到當前頁
     * @return 返回當前頁
     */
	public int getCurrPage() {
		return currPage;
	}
    /**
     * 設定分頁總數
     * @param allPages:分頁總數
     */
	public void setAllPages(int allPages) {
		this.allPages = allPages;
	}
    /**
     * 
     * @param currPage:當前頁
     */
	public void setCurrPage(int currPage) {
		this.currPage = currPage;
	}
    /**
     * 得到總紀錄數
     * @return 返回總紀錄數
     */
	public int getAllCol() {
		return allCol;
	}
    /**
     * 設置總紀錄數
     * @param allCol: 總紀錄數
     */
	public void setAllCol(int allCol) {
		this.allCol = allCol;
	}
    /**
     * 得到分頁後子紀錄的集合
     * @return 分頁後子紀錄的集合
     */
	public List getSubSet() {
		return subSet;
	}
    /**
     * 設置子紀錄集合
     * @param subSet 子紀錄集合
     */
	public void setSubSet(List subSet) {
		this.subSet = subSet;
	}
	/**
	 * 返回上一頁的頁碼
	 * @return 上一頁的頁碼
	 */
	public int getBackPage() {
		return this.backPage;
	}
	/**
	 * 返回下一頁的頁碼
	 * @return 下一頁的頁碼
	 */
	public int getNextPage() {
		return this.nextPage;
	}
	/**
	 * 設置上一頁的頁碼
	 * @param backPage 上一頁的頁碼
	 */
	public void setBackPage(int backPage) {
		this.backPage = backPage;
	}
	/**
	 * 設置下一頁的頁碼
	 * @param nextPage 下一頁的頁碼
	 */
	public void setNextPage(int nextPage) {
		this.nextPage = nextPage;
	}
	
}
