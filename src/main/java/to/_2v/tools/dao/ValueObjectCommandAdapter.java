package to._2v.tools.dao;

import java.sql.ResultSet;
import java.util.Map;


public class ValueObjectCommandAdapter implements ValueObjectCommand {

	/**
	 * implements it when operate update("insert","update","delete")
	 * @param vo
	 * @return
	 */
	public Object[] execute(Object vo) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * implements it when operate find,search("select")
	 * @param rs
	 * @return
	 */
	public Object execute(ResultSet rs) {
		// TODO Auto-generated method stub
		return null;
	}

	public void execute(ResultSet rs, Map map) {
		// TODO Auto-generated method stub
		
	}

}
