/**
 * Copyright (c) 2019-YEARALL rights Reserved
 */
package com.qlj.flow.util;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ConstPool;
import javassist.bytecode.FieldInfo;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.BooleanMemberValue;
import javassist.bytecode.annotation.ClassMemberValue;
import javassist.bytecode.annotation.IntegerMemberValue;
import javassist.bytecode.annotation.LongMemberValue;
import javassist.bytecode.annotation.MemberValue;
import javassist.bytecode.annotation.StringMemberValue;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 *
 * @author qiulujin
 * @version :  com.wj.rule2.util.NewClassUtil.java,  v  0.1  2020/5/9  18:09  qiulujin  Exp  $$
 */
public class ClassUtil {

	/**
	 * 日志对象
	 */
	private static final Logger logger= LoggerFactory.getLogger(ClassUtil.class);

	/**
	 * classPool
	 */
	public static final ClassPool pool = ClassPool.getDefault();

	/**
	 * 基础参数类型的map
	 */
	public static final Map<String, String> paramTypeClass=new HashMap<>();

	/**
	 * 默认的类型前缀
	 */
	public static final String DEFAULT_CLASS_PREFIX="java.lang.";

	/**
	 * 应对类更改的情况，记录更改版本，然后根据版本加载class
	 */
	public static final Map<String, Integer> CLASS_VERSION_MAP=new HashMap<>();

	/**
	 * 静态初始化
	 */
	static{
		paramTypeClass.put("BigDecimal","java.math.BigDecimal");
		try {
			pool.insertClassPath(new File(ClassUtil.class.getResource("/").getFile()).toString());
		} catch (NotFoundException e) {
			logger.warn("动态classPool初始化失败！",e);
		}
	}
	/**
	 *  注意： 多次调用此方法，如果fields 和 fieldTypes 不完全一致，则会自动生成新class，
	 *      新class格式为className后面加上 版本号， 比如 com.wj.test 新版本将会是com.wj.test1
	 *      多次调用版本号将会自动增加
	 * @param className
	 * @param fields
	 * @param fieldTypes
	 * @return
	 */
	public static Class getClass(String className, String[] fields, String[] fieldTypes){
		return getClass(className,fields,fieldTypes,null);
	}

	/**
	 *  注意： 多次调用此方法，如果fields 和 fieldTypes 不完全一致，则会自动生成新class，
	 *      新class格式为className后面加上 版本号， 比如 com.wj.test 新版本将会是com.wj.test1
	 *      多次调用版本号将会自动增加
	 * @param className
	 * @param fields
	 * @param fieldTypes
	 * @param annotation
	 * @return
	 */
	public static Class getClass(String className, String[] fields, String[] fieldTypes, Class<?> annotation){
		//如果只有一个注解，表示所有字段上都需要添加此注解，生成跟字段数量相同的注解数组，进行注解设置
		Class<?>[] annotationClasses = new Class[fields.length];
		Arrays.fill(annotationClasses,annotation);
		return getClass(className,fields,fieldTypes,annotationClasses,null);
	}

	/**
	 *  注意： 多次调用此方法，如果fields 和 fieldTypes 不完全一致，则会自动生成新class，
	 *      新class格式为className后面加上 版本号， 比如 com.wj.test 新版本将会是com.wj.test1
	 *      多次调用版本号将会自动增加
	 * @param className
	 * @param fields
	 * @param fieldTypes
	 * @param annotations
	 * @param annotationParams
	 * @return
	 */
	public static Class getClass(String className, String[] fields, String[] fieldTypes, Class[] annotations,
                                 List<Map<String, Object>> annotationParams){
		try{
			//获取最新版本的class
			Integer version = CLASS_VERSION_MAP.get(className);
			if(null!=version){
				className=className+version;
			}

			//尝试直接从内存中加载类，如果有则直接返回
			CtClass ctClass = pool.get(className);
			boolean b = checkClassChange(ctClass, fields, fieldTypes);
			System.out.println("class changed ? : "+b);
			//如果要创建新版本，则版本号加一
			if(b){
				if(null==version){
					version=1;
				}else {
					version=version+1;
				}
				className=className+version;
				//记录最新版本的版本号
				CLASS_VERSION_MAP.put(className,version);
			}else{
				return Class.forName(className);
			}
		}catch (NotFoundException | ClassNotFoundException e){
			logger.debug("未获取到指定类:"+className+" ,需要重新动态生成。");
		}
		try{
			CtClass ctClass = pool.makeClass(className);

			final String finalClassName=className;
			Arrays.stream(fields).forEach(LambdaUtil.lambdaWithIndex((filedName, index)->{
				String fileType=fieldTypes[index];
				Class annotation=null;
				Map<String, Object> annotationParam=null;
				if(null!=annotations&&index<annotations.length){
					annotation=annotations[index];
				}
				if(null!=annotationParams&&(index<annotationParams.size())){
					annotationParam=annotationParams.get(index);
				}

				setField(finalClassName, ctClass, filedName, fileType,annotation,annotationParam);
			}));

			return ctClass.toClass();
		}catch (CannotCompileException e){
			logger.debug("指定类:"+className+" 动态生成异常。",e);
		}
		return null;
	}

	/**
	 * 检查class定义是否有改动
	 *  仅检查字段和字段类型是否有改动
	 * @param className  类全路径
	 * @param fields
	 * @param fieldTypes
	 * @return
	 */
	public static boolean checkClassChange(String className, String[] fields, String[] fieldTypes){
		try{
			CtClass ctClass = pool.get(className);
			return checkClassChange(ctClass,fields,fieldTypes);
		}catch (NotFoundException e){
			return true;
		}
	}


	/**
	 * 获取指定包下的所有类，仅支持当前项目内的class，不支持jar内的class
	 * 不支持多层目录，仅支持当前包下的class，子包内的不会返回
	 * @param packageName
	 * @return
	 */
	public static List<Class<?>> getClassListByPackageName(String packageName){
		try{
			return getAllClassByPackageName(packageName);
		}catch (IOException e){
			e.printStackTrace();
			logger.debug("获取包下的所有类异常:"+packageName,e);
		}
		return new ArrayList<>();
	}


	/**
	 * 获取指定包下的所有class
	 *   不会获取子包内的class
	 *   不支持获取jar包内的class
	 * @param packageName
	 * @throws IOException
	 */
	private static List<Class<?>> getAllClassByPackageName(String packageName) throws IOException {
		String packagePath= StringUtils.replace(packageName,".","/");
		Enumeration<URL> resources = Thread.currentThread().getContextClassLoader().getResources(packagePath);

		List<Class<?>> classList=new ArrayList<>();

		while (resources.hasMoreElements()){
			URL url = resources.nextElement();
			String protocol = url.getProtocol();
			if(!StringUtils.equalsIgnoreCase("file",protocol)){
				continue;
			}
			String filePath = URLDecoder.decode(url.getFile(),"utf-8");
			File folder = new File(filePath);
			if(!folder.isDirectory()){
				//非文件夹  暂时过滤
				continue;
			}
			List<Class<?>> collect = Arrays.stream(Objects.requireNonNull(folder.listFiles()))
					.filter(file -> StringUtils.endsWith(file.getName(), ".class"))
					.map(file -> {
						String className = packageName + "." + StringUtils.replace(file.getName(), ".class", "");
						try {
							return Class.forName(className);
						} catch (ClassNotFoundException e) {
							logger.error("加载" + className + " 失败!");
						}
						return null;
					}).filter(Objects::nonNull).collect(Collectors.toList());
			if(!CollectionUtils.isEmpty(collect)){
				classList.addAll(collect);
			}

		}

		return classList;
	}

	/**
	 * 检查class定义是否有改动
	 *  仅检查字段和字段类型是否有改动
	 * @param ctClass
	 * @param fields
	 * @param fieldTypes
	 * @return
	 */
	private static boolean checkClassChange(CtClass ctClass, String[] fields, String[] fieldTypes){
		try{
			CtField[] declaredFields = ctClass.getDeclaredFields();
			Map<String, String> fieldMap=new HashMap<>();
			//属性数量不一致，则表示class定义发生了改变
			if(fields.length!=declaredFields.length){
				return true;
			}
			Arrays.stream(fields).forEach(LambdaUtil.lambdaWithIndex((field, index)->{
				String fieldType=getFiledType(fieldTypes[index]);
				fieldMap.put(field,fieldType);
			}));
			for(int i=0;i<declaredFields.length;i++){
				CtField field=declaredFields[i];
				String fieldType=field.getType().getName();
				String fieldName = field.getName();
				String newFieldType = fieldMap.get(fieldName);
				//在新的定义中获取不到fieldName对应的类型定义,说明class定义发生了 改变
				if(StringUtils.isBlank(newFieldType)){
					return true;
				}
				//新的field类型跟老的不一致，说明class定义发生了改变
				if(!StringUtils.equalsIgnoreCase(fieldType,newFieldType)){
					return true;
				}
			}
			return false;
		}catch (NotFoundException e){
			return true;
		}
	}

	/**
	 * 设置字段
	 * @param className
	 * @param ctClass
	 * @param filedName
	 * @param fileType
	 */
	private static void setField(String className, CtClass ctClass, String filedName, String fileType,
                                 Class annotation, Map<String, Object> annotationParam) {
		String fileTypeName = getFiledType(fileType);
		try {
			//定义filed
			CtClass fieldClazz = pool.get(fileTypeName);
			CtField ctField = new CtField(fieldClazz,filedName,ctClass);
			ctField.setModifiers(Modifier.PRIVATE);

			//设置字段注解
			setAnnotation(ctClass, annotation, ctField,annotationParam);

			//添加字段
			ctClass.addField(ctField);

			//设置getter和setter
			CtMethod setter = CtNewMethod.setter("set" + StringUtils.capitalize(filedName), ctField);
			CtMethod getter = CtNewMethod.getter("set" + StringUtils.capitalize(filedName), ctField);
			ctClass.addMethod(setter);
			ctClass.addMethod(getter);

		} catch (NotFoundException | CannotCompileException e) {
			StringBuilder message=new StringBuilder();
			message.append("生成class:").append(className);
			message.append(", 生成指定field:").append(filedName);
			message.append(",参数类型:").append(fileTypeName);
			message.append("异常!");
			logger.warn(message.toString(),e);
		}
	}

	/**
	 * 获得参数真正的class类型
	 * @param fileType
	 * @return
	 */
	private static String getFiledType(String fileType) {
		//带 . 的类型则直接使用
		String fileTypeName=fileType;
		// 不带.的类型看一下是否在paramTypeClass的map中，如果在则使用指定的类型
		if(StringUtils.indexOf(fileType,".")<0){
			fileTypeName = paramTypeClass.get(fileType);
		}
		//如果都不在，则默认type为java.lang包下的基础类型
		if(StringUtils.isBlank(fileTypeName)){
			fileTypeName=DEFAULT_CLASS_PREFIX+fileType;
		}
		return fileTypeName;
	}

	/**
	 * 设置字段注解
	 * @param ctClass
	 * @param annotationType
	 * @param ctField
	 */
	private static void setAnnotation(CtClass ctClass, Class annotationType, CtField ctField, Map<String, Object> annotationParam) {
		if(null==annotationType){
			return;
		}
		ConstPool constPool = ctClass.getClassFile().getConstPool();
		AnnotationsAttribute annotationsAttribute = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
		Annotation annotation=new Annotation(annotationType.getName(),constPool);
		setAnnotationParam(annotationParam, constPool, annotation);
		annotationsAttribute.addAnnotation(annotation);

		FieldInfo fieldInfo = ctField.getFieldInfo();
		fieldInfo.addAttribute(annotationsAttribute);
	}

	/**
	 *
	 * @param annotationParam
	 * @param constPool
	 * @param annotation
	 */
	private static void setAnnotationParam(Map<String, Object> annotationParam, ConstPool constPool, Annotation annotation) {
		if(null==annotationParam){
			return;
		}
		for(String key:annotationParam.keySet()){
			Object o = annotationParam.get(key);
			MemberValue value = getMemberValue(constPool, o);
			annotation.addMemberValue(key,value);
		}
	}

	/**
	 * 获得注解value
	 * @param constPool
	 * @param o
	 * @return
	 */
	private static MemberValue getMemberValue(ConstPool constPool, Object o) {
		String simpleName = o.getClass().getSimpleName();
		if(StringUtils.equalsIgnoreCase(simpleName,"String")){
			StringMemberValue v = new StringMemberValue(constPool);
			v.setValue(StringEscapeUtils.escapeHtml4(o.toString()));
			return v;
		}
		if(StringUtils.equalsIgnoreCase(simpleName,"Integer")){
			IntegerMemberValue v = new IntegerMemberValue(constPool);
			v.setValue(Integer.parseInt(o.toString()));
			return v;
		}
		if(StringUtils.equalsIgnoreCase(simpleName,"Long")){
			LongMemberValue v = new LongMemberValue(constPool);
			v.setValue(Long.parseLong(o.toString()));
			return v;
		}
		if(StringUtils.equalsIgnoreCase(simpleName,"Boolean")){
			BooleanMemberValue v = new BooleanMemberValue(constPool);
			v.setValue(Boolean.parseBoolean(o.toString()));
			return v;
		}
		if(StringUtils.equalsIgnoreCase(simpleName,"Class")){
			ClassMemberValue v = new ClassMemberValue(constPool);
			v.setValue(((Class)o).getName());
			return v;
		}

		return null;
	}

	/**
	 * 获取对象的所有属性信息
	 * @param object
	 * @return
	 */
	public static List<Field> getFieldList(Object object){
		Class<?> clazz = object.getClass();
		List<Field> fieldList=new ArrayList<>();
		while (clazz != null) {
			fieldList.addAll(Arrays.asList(clazz.getDeclaredFields()));
			clazz = clazz.getSuperclass();
		}
		return fieldList;
	}
}
