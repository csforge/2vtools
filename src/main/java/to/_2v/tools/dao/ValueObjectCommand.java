package to._2v.tools.dao;

import java.sql.ResultSet;
import java.util.Map;


public interface ValueObjectCommand {

	/**
	 * implements it when operate update("insert","update","delete")
	 * @param vo
	 * @return
	 */
	public Object[] execute(Object vo);

	/**
	 * implements it when operate find,search("select")
	 * @param rs
	 * @return
	 */
	public Object execute(ResultSet rs);
	
	public void execute(ResultSet rs, Map map);

}
