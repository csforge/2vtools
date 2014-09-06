package to._2v.tools.commons.sort;

import java.util.Collections;
import java.util.List;

/**
 * List sorting
 * 
 * @since 1.4
 * @author robert
 * @version 1.00, 2007.01.11
 */
public final class ListSorting {

	/**
	 * 
	 * @param list
	 *            --a list<list> for sorting
	 * @param sortingIndex
	 *            --排序字段下标组
	 * @param sortCondition
	 *            --排序原则(ASC(默认)/DESC)
	 */
	public static final String condition="DESC";
	
	/**
	 * 排序--ListSorting.sort()
	 * @param list<list<object>>
	 * @exception ClassNotFoundException if the class cannot be located
	 */
	public static void sort(List list) {
		int[] index = { 0 };// 默认的排序字段下标组
		if (list != null) {
			List l =(List) list.get(0);
			if (l.size() >= 1) {
				Collections.sort(list, new ListComparator(index,
						"ASC"));
			}
		}

	}
	
	/**
	 * 排序--ListSorting.sort()
	 * @param list<list<object>>
	 * @param sortCondition --ASC/DESC
	 * @exception ClassNotFoundException if the class cannot be located
	 */
	public static void sort(List list, String sortCondition) {
		int[] index = { 0 };// 默认的排序字段下标组
		if (list != null) {
			List l = (List)list.get(0);
			if (l.size() >= 1) {
				Collections.sort(list, new ListComparator(index,
						sortCondition));
			}
		}

	}

	/**
	 * 排序--ListSorting.sort()
	 * @param list<list<object>>
	 * @param sortingIndex --the number group of sorting indexs
	 * @param sortCondition --ASC/DESC
	 * @exception ClassNotFoundException if the class cannot be located
	 */
	public static void sort(List list, int[] sortingIndex,
			String sortCondition) {
		int[] index = { 0 };// 默认的排序字段下标组
		if (list != null) {
			List l = (List)list.get(0);
			if (l.size() >= 1 && sortingIndex.length >= 1) {
				index = sortingIndex;
				Collections.sort(list, new ListComparator(index,
						sortCondition));
			}
		}

	}
	
	/**
	 * for juging the object is equal this or not
	 */
	public  boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		return this.getClass() == obj.getClass();
	}

}
