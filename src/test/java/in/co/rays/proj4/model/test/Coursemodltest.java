package in.co.rays.proj4.model.test;

import java.util.Iterator;
import java.util.List;

import in.co.rays.proj4.bean.CourseBean;
import in.co.rays.proj4.model.CourseModel;

public class Coursemodltest {
	public void test() {
		CourseBean bean;
		CourseModel model= new CourseModel();
		try {
		List l=	model.list();
		Iterator it=l.iterator();
		while(it.hasNext()) {
		bean=(CourseBean)it.next();
		System.out.println(bean.getName());
		}
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
	}
	
public static void main(String[] args) {
	 Coursemodltest ct=new Coursemodltest();
	 ct.test();
}
}
