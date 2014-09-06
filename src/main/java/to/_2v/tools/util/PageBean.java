package to._2v.tools.util;

public class PageBean {

	private int current;
	private int length;
	private int next;
	private int back;
	private int first;
	private int last;
	private int range;
	private int size;// count
	private int fromIndex;
	private int toIndex;
	private static int defaultRange = 10;

	public PageBean(int current, int size, int range) {
		this.current = current;
		this.size = size;
		this.range = range;

		compute();
	}
	
	public PageBean(int current, int size) {
		this.current = current;
		this.size = size;
		this.range = defaultRange;

		compute();
	}
	
	public void setCurrent(int current) {
		this.current = current;
		compute();
	}
	
	private void compute() {
		length = size / range;
		if (size % range > 0)
			length++;
		next = current + 1;
		if (next >= length)
			next = length - 1;
		back = current - 1;
		if (back < 0)
			back = 0;
		first = 0;
		last = length - 1;
		fromIndex = current * range;
		toIndex = fromIndex + range - 1;
		if (toIndex >= size)
			toIndex = size - 1;

	}
	
	public String toString(){
		return new StringBuffer("current=").append(current).append(",size=").append(size).append(",range=").append(range)
		.append(",length=").append(length).append(",back=").append(back).append(",next=").append(next)
		.append(",first=").append(first).append(",last=").append(last)
		.append(",fromIndex=").append(fromIndex).append(",toIndex=").append(toIndex).toString();
	}

	public static void setDefaultRange(int defaultRange) {
		PageBean.defaultRange = defaultRange;
	}
	
	public int getFirst() {
		return first;
	}

	public int getLast() {
		return last;
	}

	public int getCurrent() {
		return current;
	}

	public int getLength() {
		return length;
	}

	public int getNext() {
		return next;
	}

	public int getBack() {
		return back;
	}

	public int getRange() {
		return range;
	}

	public int getSize() {
		return size;
	}

	public int getFromIndex() {
		return fromIndex;
	}

	public int getToIndex() {
		return toIndex;
	}
}
