package to._2v.tools.util;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
/**
 * PaginationSQL 類 用于生成不同數據庫的分頁SQL<br>
 * 假設MYSQL數據庫中 有一張用戶表(USER) 根据条件查询出来的記錄總數爲 50, 分頁單位爲每頁10筆記錄<br>
 *                 <br>
 *  1)查出第2頁的分頁SQL,按照表原始順序<br>
 *      PaginationSQL ps = new PaginationSQL(2,10,50);<br>
 *      ps.getPaginationSQLUseSingleModelForMySQL("select * from tablename where name like '%A2%'");<br>
 *  2)查出第2頁的分頁SQL,按照給定的欄位排序<br>
 *      PaginationSQL ps = new PaginationSQL(2,10,50);<br>
 *      ps.getPaginationSQLUseSingleModelForMySQL("select * from tablename where name like '%A2%'","name");<br> 
 *  3)查出第2頁的分頁SQL,按照給定的欄位排序,并指定排序類型,現指定爲降序類型<br>
 *      PaginationSQL ps = new PaginationSQL(2,10,50);<br>
 *      ps.getPaginationSQLUseSingleModelForMySQL("select * from tablename where name like '%A2%'","name",PaginationSQL.DESC);<br> 
 *  SQLServer 和 Oracle的用法与上述相同<br>
 *  另外 subSet 用于讓使用者裝入由分頁SQL查詢出來的記錄LIST. 若用戶不設置則爲空!
 *           
 * @author angel
 *
 */
public class PaginationSQL {
	
	public static final String ASC = "ASC";
	public static final String DESC = "DESC";
    /**
     * 當前頁碼
     */ 
	private int currPage;
	/**
	 * 總頁數
	 */
	private int allPages;
	/**
	 * 總記錄數
	 */
	private int allCol;
	int backPage;
	int nextPage;
	/**
	 * 分頁單位
	 */
	int iPage;
    /**
     * 可存放查出的子List
     */
	private List subSet = new ArrayList();
	/**
	 * 記錄 select from 及 where 語句
	 */
	private Hashtable sentence = new Hashtable();
	/**
	 * PaginationSQL 構造函數 默認當前頁碼爲 1
	 * @param iPage: 分頁單位 
	 * @param allCol: 記錄總數
	 */
	public PaginationSQL(int iPage,int allCol){ 
		this(1,iPage,allCol);
	}
	/**
	 * PaginationSQL 構造函數
	 * @param currPage: 當前頁碼
	 * @param iPage: 分頁單位
	 * @param allCol: 記錄總數
	 */
	public PaginationSQL(int currPage,int iPage,int allCol){ 
		this.currPage = currPage;
		this.backPage = this.currPage - 1;
		this.nextPage = this.currPage + 1;
		this.iPage = iPage;
		this.allCol = allCol;
		this.allPages = this.getAllPages(iPage,this.allCol);
	}
	/**
	 * 
	 * @param sql: 非嵌套SQL语句
	 * @return
	 */
	public String getPaginationSQLUseSingleModelForMySQL(String sql){
		int index = (this.currPage - 1)*this.iPage;
		String SQL = sql+" LIMIT "+index+","+this.iPage;
		return SQL;
	}
	/**
	 * 
	 * @param sql: 非嵌套SQL语句
	 * @param id: 非嵌套SQL语句
	 * @return
	 */
	public String getPaginationSQLUseSingleModelForMySQL(String sql,String id){

		int index = (this.currPage - 1)*this.iPage;
		String SQL = sql+" ORDER BY "+id+" LIMIT "+index+","+this.iPage;
		return SQL;
	}
	/**
	 * 
	 * @param sql: 非嵌套SQL语句
	 * @param id: 非嵌套SQL语句
	 * @param sortType: ASC/DESC
	 * @return
	 */
	public String getPaginationSQLUseSingleModelForMySQL(String sql,String id,String sortType){
		if(!sortType.equalsIgnoreCase(PaginationSQL.ASC)&&!sortType.equalsIgnoreCase(PaginationSQL.DESC))
				return null;
	    int index = (this.currPage - 1)*this.iPage;
		String SQL = sql+" ORDER BY "+id+" "+sortType+" LIMIT "+index+","+this.iPage;
		return SQL;
		
	}

   /**
    * 
    * @param sql: 非嵌套SQL语句
    * @param id: 該表的排序欄位
    * @return
    */
   public String getPaginationSQLUseSingleModelForSQLServer(String sql , String id){
	   
	   Hashtable sentence = this.setSentenceForSQL(sql);
		String SQL = "";
	   if(this.currPage==1){
		   if(sql.indexOf("where")!=-1)
			SQL = "SELECT TOP("+this.iPage+") "+sentence.get("select")+" FROM "+sentence.get("from")+" WHERE "+sentence.get("where")+" ORDER BY "+id;
		   else
		    SQL = "SELECT TOP("+this.iPage+") "+sentence.get("select")+" FROM "+sentence.get("from")+""+" ORDER BY "+id; 
	   }else{
		   int index = (this.currPage - 1)*this.iPage;
		   if(sql.indexOf("where")!=-1){
			   SQL = " SELECT TOP ("+this.iPage+") "+sentence.get("select")+
               " FROM "+sentence.get("from")+
               " WHERE ("+id+">"+
                  " (SELECT  MAX("+id+")"+
                    " FROM (SELECT     TOP ("+index+") "+id+
                    "  FROM "+sentence.get("from")+
                    " WHERE "+sentence.get("where")+
                    " ORDER BY "+id+") AS T)) "+
                  " AND "+sentence.get("where")+
             " ORDER BY "+id;    
		   }else{
			   
			   SQL = " SELECT TOP ("+this.iPage+") "+sentence.get("select")+
               " FROM "+sentence.get("from")+
               " WHERE ("+id+">"+
                  " (SELECT  MAX("+id+")"+
                    " FROM (SELECT     TOP ("+index+") "+id+
                    "  FROM "+sentence.get("from")+
                    " ORDER BY "+id+") AS T)) "+
                   " ORDER BY "+id;    
		   }
  
	   }
	   
	   return SQL;

   }
   /**
    * 
    * @param sql: 非嵌套SQL语句 
    * @return
    */
   public String getPaginationSQLUseSingleModelForOracle(String sql){
	   
	   Hashtable sentence = this.setSentenceForSQL(sql);
	   String SQL = "";
		String selectValue2 = sentence.get("select").toString();
		int beginPointer = (this.currPage-1)*this.iPage+1;
		int endPointer = this.currPage*this.iPage;
		if(sentence.get("select").toString().trim().equals("*"))
		      selectValue2 = sentence.get("from").toString().trim()+"."+sentence.get("select").toString().trim();
		
		System.out.println(selectValue2);
		SQL = "select * from ("+
		      "select "+selectValue2+", rownum as my_rownum from ("+
		      "select "+sentence.get("select").toString()+" from "+sentence.get("from").toString()+" ) "+
		      sentence.get("from").toString()+" where rownum <="+endPointer+"  AND "+sentence.get("where").toString()+") where my_rownum>="+beginPointer;
		return SQL;
	   
   }
   /**
    * 
    * @param sql: 非嵌套SQL语句 
    * @param id: 該表的排序欄位
    * @return
    */
   public String getPaginationSQLUseSingleModelForOracle(String sql,String id){
	   Hashtable sentence = this.setSentenceForSQL(sql);
	   String SQL = "";
		String selectValue2 = sentence.get("select").toString();
		int beginPointer = (this.currPage-1)*this.iPage+1;
		int endPointer = this.currPage*this.iPage;
		if(sentence.get("select").toString().trim().equals("*"))
		      selectValue2 = sentence.get("from").toString().trim()+"."+sentence.get("select").toString().trim();
		SQL = "select * from ("+
		      "select "+selectValue2+", rownum as my_rownum from ("+
		      "select "+sentence.get("select").toString()+" from "+sentence.get("from").toString()+" ) "+
		      sentence.get("from").toString()+" where rownum <="+endPointer+"  AND "+sentence.get("where").toString()+" ORDER BY "+id+") where my_rownum>="+beginPointer;
		return SQL;
	   
   }
   /**
    * 
    * @param sql: 非嵌套SQL语句 
    * @param id: 該表的排序欄位
    * @param sortType: ASC/DESC
    * @return
    */
   public String getPaginationSQLUseSingleModelForOracle(String sql,String id,String sortType){
	   if(!sortType.equalsIgnoreCase(PaginationSQL.ASC)&&!sortType.equalsIgnoreCase(PaginationSQL.DESC))
			return null;
	   
	   Hashtable sentence = this.setSentenceForSQL(sql);
	   String SQL = "";
		String selectValue2 = sentence.get("select").toString();
		int beginPointer = (this.currPage-1)*this.iPage+1;
		int endPointer = this.currPage*this.iPage;
		if(sentence.get("select").toString().trim().equals("*"))
		      selectValue2 = sentence.get("from").toString().trim()+"."+sentence.get("select").toString().trim();
		SQL = "select * from ("+
		      "select "+selectValue2+", rownum as my_rownum from ("+
		      "select "+sentence.get("select").toString()+" from "+sentence.get("from").toString()+" ) "+
		      sentence.get("from").toString()+" where rownum <="+endPointer+"  AND "+sentence.get("where").toString()+" ORDER BY "+id+" "+sortType+") where my_rownum>="+beginPointer;
		return SQL;
   }
	public Hashtable setSentenceForSQL(String singleSQL){
		String select = "";
		String from = "";
		 String where =  "";
		 String singleSQL2 = singleSQL.trim();
		int selectBeginIndex = 6;
		int selectEndIndex = singleSQL2.indexOf("from");
		int fromBeginIndex = selectEndIndex+4;
		int fromEndIndex =  singleSQL2.indexOf("where");
		
		int whereBeginIdex = 0;
		 select = singleSQL2.substring(selectBeginIndex, selectEndIndex);
		if(fromEndIndex!=-1)  { //如果存在where
			 from = singleSQL2.substring(fromBeginIndex, fromEndIndex);
			 whereBeginIdex = fromEndIndex+5;
			 where =  singleSQL2.substring(whereBeginIdex);
			
		}else{
			from = singleSQL2.substring(fromBeginIndex);
		}
		sentence.put("select", select);
		sentence.put("from", from);
		sentence.put("where", where);
		
		return this.sentence;
	}
	
	public int getAllPages(int iPage,int allCol){ //根据分页单位和总记录,返回分页数
	     
		return (int)Math.ceil((allCol+iPage-1)/iPage);
	}
	
	public int getAllCol() {
		return allCol;
	}
	public int getAllPages() {
		return allPages;
	}
	public int getBackPage() {
		return backPage;
	}
	public int getCurrPage() {
		return currPage;
	}

	public int getNextPage() {
		return nextPage;
	}
	/**
	 * subSet 用于讓使用者裝入由分頁SQL查詢出來的記錄LIST. 若用戶不設置則爲空!
	 * @return
	 */
	public List getSubSet() {
		return subSet;
	}
	public void setAllCol(int allCol) {
		this.allCol = allCol;
	}
	public void setAllPages(int allPages) {
		this.allPages = allPages;
	}
	public void setBackPage(int backPage) {
		this.backPage = backPage;
	}
	public void setCurrPage(int currPage) {
		this.currPage = currPage;
	}

	public void setNextPage(int nextPage) {
		this.nextPage = nextPage;
	}
	public void setSubSet(List subSet) {
		this.subSet = subSet;
	}
	public int getIPage() {
		return iPage;
	}
	public void setIPage(int page) {
		iPage = page;
	}
	public Hashtable getSentence() {
		return sentence;
	}
	public void setSentence(Hashtable sentence) {
		this.sentence = sentence;
	}
	
}
