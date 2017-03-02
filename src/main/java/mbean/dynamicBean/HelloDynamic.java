package mbean.dynamicBean;


import java.lang.reflect.Constructor;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.DynamicMBean;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanConstructorInfo;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.ReflectionException;

public class HelloDynamic implements DynamicMBean {
	// 管理控件（MBean）属性
	private String name;

	// 动态创建MBean需要的变量
	private String className = this.getClass().getName();
	private String description = "Simple implementation of a dynamic MBean.";

	private MBeanAttributeInfo[] attributes;
	private MBeanConstructorInfo[] constructors;
	private MBeanOperationInfo[] operations;
	private MBeanInfo mBeanInfo;
	private MBeanNotificationInfo[] notifications;

	public HelloDynamic() {
		init();
		buildDynamicMBean();
	}

	private void init() {
		className = this.getClass().getName();
		description = "Simple implementation of a dynamic MBean.";
		attributes = new MBeanAttributeInfo[1];
		constructors = new MBeanConstructorInfo[1];
		operations = new MBeanOperationInfo[1];
		notifications = new MBeanNotificationInfo[0];
	}

	private void buildDynamicMBean() {
		// constructors
		Constructor<?>[] ctors = this.getClass().getConstructors();
		constructors[0] = new MBeanConstructorInfo(
				"HelloDynamic(): Constructs a HelloDynamic object", ctors[0]);
		// attributes
		attributes[0] = new MBeanAttributeInfo("name", "java.lang.String",
				"Name: name string", true, true, false);
		// methods
		MBeanParameterInfo[] params = null;
		operations[0] = new MBeanOperationInfo("print",
				"print(): print the name", params, "void",
				MBeanOperationInfo.INFO);
		// MBeanInfo
		mBeanInfo = new MBeanInfo(this.className, description, attributes,
				constructors, operations, notifications);
	}

	@Override
	public Object getAttribute(String attribute)
			throws AttributeNotFoundException, MBeanException,
			ReflectionException {
		if (attribute == null) {
			return null;
		}
		if ("name".equals(attribute)) {
			return name;
		}
		return null;
	}

	@Override
	public AttributeList getAttributes(String[] attributes) {
		if (attributes == null) {
			return null;
		}
		AttributeList reslist = new AttributeList();

		for (String attr : attributes) {
			try {
				Object value = getAttribute(attr);
				reslist.add(new Attribute(attr, value));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return reslist;
	}

	@Override
	public MBeanInfo getMBeanInfo() {
		return mBeanInfo;
	}

	@Override
	public Object invoke(String actionName, Object[] params, String[] signature)
			throws MBeanException, ReflectionException {
		if (actionName.equals("print")) {
			print();
		} else if("dynamicPrint".equals(actionName)) {
			dynamicPrint();
		}
		return null;
	}

	@Override
	public void setAttribute(Attribute attribute)
			throws AttributeNotFoundException, InvalidAttributeValueException,
			MBeanException, ReflectionException {
		if (attribute == null) {
			return;
		}

		String attrname = attribute.getName();
		Object attrvalue = attribute.getValue();

		if ("name".equals(attrname)) {
			if (attrvalue == null) {
				name = null;
			} else
				try {
					if (Class.forName("java.lang.String").isAssignableFrom(
							attrvalue.getClass())) {
						name = (String) attrvalue;
					}
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
		}

	}

	@Override
	public AttributeList setAttributes(AttributeList attributes) {
		if (attributes == null) {
			return null;
		}
		AttributeList reslist = new AttributeList();
		for (Object obj : attributes) {
			Attribute attr = (Attribute) obj;
			try {
				setAttribute(attr);
				String attrname = attr.getName();
				Object attrvalue = attr.getValue();
				reslist.add(new Attribute(attrname, attrvalue));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return reslist;
	}

	private void print() {
		System.out.println("Hello " + name + ", This is helloDynamic");
		// add method dynamic at runtime
		operations = new MBeanOperationInfo[2];
		buildDynamicMBean();
		MBeanParameterInfo[] parameters = null;
		operations[1] = new MBeanOperationInfo("dynamicPrint",
				"dynamicPrint: Runtime generated by print method", parameters,
				"void", MBeanOperationInfo.INFO);
	}

	private void dynamicPrint() {
		System.out.println("This is a runtime generated method!");
	}
}
