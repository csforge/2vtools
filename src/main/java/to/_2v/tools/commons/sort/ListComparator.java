package to._2v.tools.commons.sort;

import java.sql.Date;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;

/**
 * 依据排序規则的比较器
 * @author robert
 * @version 1.01 2007.01.12 10:10:00
 * @since in J2SDK v1.4
 */
public final class ListComparator implements Comparator {

	private int[] index = { 0 };

	//排序依据条件
	private String orderCondition = "ASC";

	/**
	 * 排序器
	 * @param index sorting indexs group
	 * @param orderCondition string of ASC or DESC
	 */
	public ListComparator(int[] index, String orderCondition) {
		if (index != null && index.length >= 1 && orderCondition != null) {
			this.index = index;
			this.orderCondition = orderCondition;
		}
	}

	/**
	 * 比较2个List对象
	 * @since 1.4
	 * @exception out of bounds exception
	 * 自动启动排序比较器
	 */
	public int compare(Object obja, Object objb) {

		List list1=(List)obja;
		List list2=(List)objb;
		int result = -2;
		if (list1.size() == list2.size()) {
			for (int i = 0; i < index.length; i++) {
				Object obj1 = list1.get(index[i]);
				Object obj2 = list2.get(index[i]);

				if (obj1 == null || obj2 == null) {
					if (obj1 == null && obj2 != null) {
						result = -1;
						break;
					} else if (obj1 != null && obj2 == null) {
						result = 1;
						break;
					} else {
						result = 0;
						break;
					}
				}

				if (index.length <= list1.size()) {
					result = sortObject(obj1, obj2);
					if (result != 0) {
						// 当且发现了相同对象时才继续比较
						break;
					}
				}
			}

			if (orderCondition.equals("DESC")) {
				result = -result;
			}
		}
		return result;
	}

	/**
	 * 比较对象的大小
	 * @param obj1 object for compare
	 * @param obj2 object for compare
	 * @return int
	 * @exception classCastException
	 * 比较 由compare启动
	 */
	private int sortObject(Object obj1, Object obj2) {
		int result = -1;
		try {
			// String
			if (obj1 instanceof String && obj2 instanceof String) {
				result = obj1.toString().compareTo(obj2.toString());
			}
			// Date
			if (obj1 instanceof Date && obj2 instanceof Date) {
				result = ((Date) obj1).compareTo((Date) obj2);
			}
			// Calendar
			if (obj1 instanceof Calendar && obj2 instanceof Calendar) {
				long i=((Calendar)obj1).getTimeInMillis()-((Calendar)obj2).getTimeInMillis();
				result =i>0?1:(i==0?0:-1 );
			}
			//Number
			if (obj1 instanceof Number && obj2 instanceof Number) {
				double i=((Number)obj1).doubleValue()-((Number)obj2).doubleValue();
				result =i>0?1:(i==0?0:-1 );
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	/**
	 * 比较2个对象是否相同
	 */
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		return this.getClass() == obj.getClass();
	}


}
