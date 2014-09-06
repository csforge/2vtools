package to._2v.tools.commons.sort;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * MultilistSort 是一个多重排序工具类,它是基于List-Bean模式的,也就是List里面的每一个元素是一个javabean.<br>
 * 用户通过传入待排序的List和bean中待排序的属性字符串数组来进行排序.属性字符串数组的排序权重是按照下标先后次序确定的.<br>
 * sort方法可指定是升序还是降序.<br>
 * 静态常量 SORT_ASCENDING 表示按升序排序 静态常量 REVERSE_ORDER 表示按降序排序<br>
 * 例如待排序的List 里面有4个bean,它们的关系如下<br>
 *  List list = new ArrayList();<br> list.add(new
 * Bean("ab",40,25.0));<br> list.add(new Bean("ac",50,35.0));<br> list.add(new
 * Bean("ad",30,10.0)); <br>list.add(new Bean("ac",10,12.0));<br>
 * 
 * 它们的属性比重 String attributes[] = {"stringID","intID","doubleID"};<br>
 * 
 * 利用语句 MultilistSort.sort(list,attributes,MultilistSort.SORT_ASCENDING); 进行排序<br>
 * 
 * 它们排序后的结果为<br> ab 40 25.0 <br>ac 10 12.0 <br>ac 50 35.0<br> ad 30 10.0<br>
 * 
 * 另外要注意:应保证list里面的bean全是合法的,即bean不能为空且输入的属性权重字符串数组也是bean里面可以找到的,否则排序不成功返回false.
 * 
 * @author shining
 * @version 1.1 01/18/07
 * @since 1.4
 */
public class BeanSorting {
    
	/**
	 * 表示按升序排序
	 */
	public static final int SORT_ASCENDING = 1;
	/**
	 * 表示按降序排序
	 */
	public static final int REVERSE_ORDER = -1;

	/**
	 * 
	 * @param beanSet:待排序List
	 * @param attributes:待排序List中Bean的属性权重字符串数组
	 * @return 排序成功则返回true,失败则返回false
	 * @throws Exception
	 */
	public static boolean sort(List beanSet, String attributes[]){
		return sort(beanSet,attributes,BeanSorting.SORT_ASCENDING);
	}
	/**
	 * 
	 * @param beanSet:待排序List
	 * @param attributes:待排序List中Bean的属性权重字符串数组
	 * @param compositorType:选择升序还是降序
	 * @return 排序成功则返回true,失败则返回false
	 */
	public static boolean sort(List beanSet, String attributes[],
			int compositorType) {
      
		// 常规检查
		if (beanSet == null || attributes == null || beanSet.isEmpty()
				|| attributes.length == 0)
			return false;

		if (compositorType != BeanSorting.SORT_ASCENDING && compositorType != BeanSorting.REVERSE_ORDER)
			return false;

		// 利用反射保存每一个Bean需要排序的属性
		HashMap reflectedBeans = new HashMap(); // 用Hash-List方式保存反射过的对象属性,key=beanSet里面每一个Bean的地址
		

		try {

			for (int i = 0; i < beanSet.size(); i++) {

				List listTemp = new ArrayList();
				Class classTemp = beanSet.get(i).getClass(); // 如果beanSet存在为NULL的元素则为抛出异常

				for (int j = 0; j < attributes.length; j++) { // 即使beanSet的Bean不为空,但如果没有与输入的属性权重字符串数组匹配的话也会抛出异常

					StringBuffer sb = new StringBuffer(attributes[j]);
					sb.replace(0, 1, attributes[j].substring(0, 1)
							.toUpperCase()); // 第一个字母变为大写

					Method methodTemp = classTemp.getDeclaredMethod("get"
							+ sb.toString(), new Class[] {});
					listTemp.add(methodTemp.invoke(beanSet.get(i),
							new Object[] {}));
				}

				reflectedBeans.put(beanSet.get(i).toString(), listTemp); // 存储bean的地址
			}

			Collections.sort(beanSet, new BeanComparator(reflectedBeans,
					compositorType));
			return true;

		} catch (Exception e) {

			e.printStackTrace();
			return false;
		}

	}

}
