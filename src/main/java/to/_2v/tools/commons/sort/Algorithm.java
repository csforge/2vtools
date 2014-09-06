package to._2v.tools.commons.sort;

public class Algorithm {
	
	public static void bubbleort(int []_avs,int _left,int _right){
		int r = _right;//if(_right==undefined) r = _avs.length-1;
		int l = _left;//if(_left==undefined) l = 0;
		for(int i=r;i>l;i--)
			for(int j=l;j<i;j++)
				if(_avs[j]>_avs[j+1])
					swap(_avs,j,j+1);
	}
	public static void shellsort(int []_avs,int _left,int _right){
		int r = _right;//if(_right==undefined) r = _avs.length-1;
		int l = _left;//if(_left==undefined) l = 0;
		int len = r-l+1;
		for(int gap=len/2;gap>0;gap=gap/2)
			for(int i=gap;i<len;i++)
				for(int j=i-gap+l;j>=l && _avs[j]>_avs[j+gap];j-=gap)
					swap(_avs,j,j+gap);
	}
	public static void qsort(int []_avs,int _left,int _right){
		if(_left>=_right) return;
		swap(_avs, _left, (_left+_right)/2);
		int last = _left;
		for(int i=_left+1;i<=_right;i++)
			if(_avs[i]<_avs[_left])
				swap(_avs,++last,i);
		swap(_avs,_left,last);
		qsort(_avs,_left,last-1);
		qsort(_avs,last+1,_right);
	}
	public static void swap(int []_avs,int _i,int _j){
		int tmp;
		tmp = _avs[_i];
		_avs[_i] = _avs[_j];
		_avs[_j] = tmp;
	}
}
