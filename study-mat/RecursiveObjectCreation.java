package test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class RecursiveObjectCreation {
	public static void main(String[] args) throws Exception {
		createObj(Employee.class);

	}

	public static <T> T createObj(Class<T> clazz) throws Exception {
		Field[] declaredFields = clazz.getDeclaredFields();
		T obj = clazz.newInstance();
		for (Field field : declaredFields) {
			String input = field.getName();
			Method declaredMethod = null;
			try {
				declaredMethod = clazz.getDeclaredMethod(
						"set" + input.substring(0, 1).toUpperCase()
								+ input.substring(1), field.getType());
			} catch (java.lang.NoSuchMethodException e) {
				// if no setter found skip this field
				break;

			}

			if (field.getType().equals(String.class)) {
				declaredMethod.invoke(obj, input);
			} else if (field.getType().equals(Integer.class)
					|| field.getType().equals(Integer.TYPE)) {

				declaredMethod.invoke(obj, 1);
			} else if (field.getType().equals(Integer.class)
					|| field.getType().equals(Integer.TYPE)) {

				declaredMethod.invoke(obj, 1);
			} else if (field.getType().equals(Boolean.class)
					|| field.getType().equals(Boolean.TYPE)) {

				declaredMethod.invoke(obj, true);
			} else if (field.getType().equals(Long.class)
					|| field.getType().equals(Long.TYPE)) {

				declaredMethod.invoke(obj, 1l);
			} else if (field.getType().equals(Float.class)
					|| field.getType().equals(Float.TYPE)) {

				declaredMethod.invoke(obj, 1f);
			} else if (field.getType().equals(Double.class)
					|| field.getType().equals(Double.TYPE)) {

				declaredMethod.invoke(obj, 1d);
			} else if (field.getType().equals(Character.class)
					|| field.getType().equals(Character.TYPE)) {

				declaredMethod.invoke(obj, 'a');
			} else {
				declaredMethod.invoke(obj, createObj(field.getType()));
			}
		}
		System.out.println(obj);
		return obj;
	}

}
