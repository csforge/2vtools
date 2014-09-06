package to._2v.tools.dao.demo;

import to._2v.tools.ObjectPool;

public class GlobalFactory {
	
	public static DemoDAO getDemoDAO() {
		return (DemoDAO) ObjectPool.getInstance("DemoDAO", DemoDAOImpl.class);
	}
}
