package to._2v.tools.commons.sort;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 该类用于为MultilistSort.Sort 方法中的 Collections.sort(List,new
 * CompareSomething(HashMap param1,int param1));指定 比较器
 * 
 * @author shining
 * 
 */
public class BeanComparator implements Comparator {

	private HashMap reflectedBeans;

	private int compositorType;

	private static final int SORT_ASCENDING = 1;

	/**
	 * 
	 * @param reflectedBeans:
	 *            存储反射过的List
	 * @param compositorType:指定升序还是降序
	 */
	public BeanComparator(HashMap reflectedBeans, int compositorType) {

		this.reflectedBeans = reflectedBeans;
		this.compositorType = compositorType;
	}

	/**
	 * 
	 * @param object1:从待排序List中传来的Bean的引用
	 * @param object2:从待排序List中传来的Bean的引用,它与object2作比较
	 */
	public int compare(Object object1, Object object2) {

		List tempList1 = (List) reflectedBeans.get(object1.toString());
		List tempList2 = (List) reflectedBeans.get(object2.toString());

		int result = 0;

		for (int i = 0; i < tempList1.size(); i++) {

			try {
				if (tempList1.get(i) != null && tempList2.get(i) != null) // 如果两个属性均不为空
					result = this.compareTo(tempList1.get(i), tempList2.get(i));
				else
					// 如果其中一个属性为空
					result = this.compareNull(tempList1.get(i), tempList2
							.get(i));
				if (result > 0 || result < 0)
					break;
			} catch (Exception ex) {

				ex.printStackTrace();

			}

		}
		return compositorType == SORT_ASCENDING ? result : -result;
	}

	public boolean equals(Object collator) {

		if (this == collator) {
			return true;
		}
		if (collator == null) {
			return false;
		}

		return this.getClass() == collator.getClass();
	}

	public int compareTo(Object object1, Object object2) {

		if (object1 instanceof String) {

			return ((String) object1).compareTo((String) object2);
		}

		if (object1 instanceof Integer) {

			return ((Integer) object1).compareTo((Integer) object2);
		}
		if (object1 instanceof Long) {

			return ((Long) object1).compareTo((Long) object2);
		}
		if (object1 instanceof Short) {

			return ((Short) object1).compareTo((Short) object2);
		}
		if (object1 instanceof Double) {

			return ((Double) object1).compareTo((Double) object2);
		}
		if (object1 instanceof Float) {

			return ((Float) object1).compareTo((Float) object2);
		}
		if (object1 instanceof Boolean) {

			if(((Boolean) object1).booleanValue()==false&&((Boolean) object2).booleanValue()==true)return -1;
			if(((Boolean) object1).booleanValue()==true&&((Boolean) object2).booleanValue()==false)return 1;
			return 0;
				
		}
		if (object1 instanceof BigDecimal) {

			return ((BigDecimal) object1).compareTo((BigDecimal) object2);
		}
		if (object1 instanceof BigInteger) {

			return ((BigInteger) object1).compareTo((BigInteger) object2);
		}
		if (object1 instanceof Byte) {

			return ((Byte) object1).compareTo((Byte) object2);
		}
		if (object1 instanceof Date) {

			return ((Date) object1).compareTo((Date) object2);
		}

		if (object1 instanceof Calendar) {

			return ((Calendar) object1).getTime().compareTo(((Calendar) object2).getTime());
		}

		return 0;
	}

	public int compareNull(Object object1, Object object2) {

		if (object1 == null && object2 != null)
			return -1; // 在默认升序的情况下,第一个是NULL,第二个不是NULL,则第一个比第一个小,则返回-1
		else if (object1 != null && object2 == null)
			return 1; // 在默认升序的情况下,第一个不是NULL,第二个是NULL,则第一个比第一个大,则返回1,需转换位置
		else
			return 0; // 两个都为NULL,则相等,返回0
	}

}

