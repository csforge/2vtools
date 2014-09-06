package to._2v.tools.dao;

import java.util.List;

import to._2v.tools.ValueObject;

public abstract class AbstractCacheDAO extends AbstractDAO{

	public static String poolKey;

	protected int delete0(ValueObject vo){
		CachePool.clean(poolKey);
		return delete(vo);
	}

	protected int insert0(ValueObject vo){
		CachePool.clean(poolKey);
		return create(vo);
	}

	protected int update0(ValueObject vo){
		CachePool.clean(poolKey);
		return update(vo);
	}

	protected List search0(ValueObject vo) {
		if (CachePool.get(poolKey) == null)
			CachePool.put(poolKey, search(vo));
		return (List) CachePool.get(poolKey);
	}
	
	public List search0(ValueObject vo, int fromIndex, int toIndex){
		return search0(vo).subList(fromIndex, toIndex);
	}
	
	public abstract void setPoolKey(String poolKey);
}
