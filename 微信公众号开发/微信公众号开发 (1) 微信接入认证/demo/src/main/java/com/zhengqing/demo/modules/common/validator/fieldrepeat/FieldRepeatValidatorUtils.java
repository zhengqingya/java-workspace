package com.zhengqing.demo.modules.common.validator.fieldrepeat;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.zhengqing.demo.modules.common.exception.MyException;
import com.zhengqing.demo.utils.BeanUtil;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 *  <p> 数据库字段内容重复判断处理工具类 </p>
 *
 * @author：  zhengqing <br/>
 * @date：  2019/9/10$ 9:28$ <br/>
 * @version：  <br/>
 */
public class FieldRepeatValidatorUtils {

    /**
     * 实体类id字段
     */
    private static String id;
    /**
     * 实体类id字段值
     */
    private static Integer idValue;
    /**
     * 校验字段
     */
    private static String field;
    /**
     * 校验字段2
     */
    private static String fieldTwo;
    /**
     * 校验字段值 - 字符串、数字、对象...
     */
    private static Object fieldValue;
    /**
     * 校验字段值2
     */
    private static Object fieldTwoValue;
    /**
     * 校验字段 - 对应数据库字段
     */
    private static String db_field;
    /**
     * 校验字段2
     */
    private static String db_field_two;
    /**
     * 实体类对象值
     */
    private static Object object;

    /**
     * 校验数据
     *
     * @param field：校验字段
     * @param object：对象数据
     * @param message：回调到前端提示消息
     * @return: boolean
     */
    public static boolean fieldRepeat(String id, String field, String fieldTwo, String twoValue, Object object, String message) {
        FieldRepeatValidatorUtils.id = id;
        FieldRepeatValidatorUtils.field = field;
        FieldRepeatValidatorUtils.fieldTwo = fieldTwo;
        FieldRepeatValidatorUtils.object = object;
        getFieldValue();

        // ⑦ 校验字段内容是否重复
        // 工厂模式 + ar动态语法
        Model entity = (Model) object;
        List list = new ArrayList();
        if (StringUtils.isBlank(fieldTwo)) {
            list = entity.selectList(new EntityWrapper().eq(db_field, fieldValue));
        } else {
            if (StringUtils.isNotBlank(twoValue)) {
                fieldTwoValue = twoValue;
            }
            list = entity.selectList(new EntityWrapper().eq(db_field, fieldValue).eq(db_field_two, fieldTwoValue));
        }
        // 如果数据重复返回false -> 再返回自定义错误消息到前端
        if (idValue == null) {
            if (!CollectionUtils.isEmpty(list)) {
                throw new MyException(message);
            }
        } else {
            if (!CollectionUtils.isEmpty(list)) {
                // fieldValueNew：前端输入字段值
                Object fieldValueNew = fieldValue;
                FieldRepeatValidatorUtils.object = entity.selectById(idValue);

                // 获取该id所在对象的校验字段值 - 旧数据
                getFieldValue();

                // 获取list中指定字段属性值 - 这里只获取主键id
                List<Integer> idList = BeanUtil.getFieldList(list, id, false);
                boolean isContainsIdValue = idList.contains(idValue);
                if (!fieldValueNew.equals(fieldValue) || list.size() > 1 || !isContainsIdValue) {
                    throw new MyException(message);
                }
            }
        }
        return true;
    }

    /**
     * 获取id、校验字段值
     */
    public static void getFieldValue() {
        // ① 获取所有的字段
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field f : fields) {
            // ② 设置对象中成员 属性private为可读
            f.setAccessible(true);
            // ③ 判断字段注解是否存在
            if (f.isAnnotationPresent(ApiModelProperty.class)) {
                // ④ 如果存在则获取该注解对应的字段,并判断是否与我们要校验的字段一致
                if (f.getName().equals(field)) {
                    try {
                        // ⑤ 如果一致则获取其属性值
                        fieldValue = f.get(object);
                        // ⑥ 获取该校验字段对应的数据库字段属性  目的： 给 mybatis-plus 做ar查询使用
                        TableField annotation = f.getAnnotation(TableField.class);
                        db_field = annotation.value();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }

                // 如果校验字段2的值不为空则查其值
                if (StringUtils.isNotBlank(fieldTwo)) {
                    if (f.getName().equals(fieldTwo)) {
                        try {
                            // ⑤ 如果一致则获取其属性值
                            fieldTwoValue = f.get(object);
                            // ⑥ 获取该校验字段对应的数据库字段属性  目的： 给 mybatis-plus 做ar查询使用
                            TableField annotation = f.getAnnotation(TableField.class);
                            db_field_two = annotation.value();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }

                // ⑦ 获取id值 -> 作用：判断是插入还是更新操作
                if (id.equals(f.getName())) {
                    try {
                        idValue = (Integer) f.get(object);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
