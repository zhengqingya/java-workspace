package com.zhengqing.demo.modules.common.validator.textformat;


import com.zhengqing.demo.modules.common.exception.MyException;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p> 参数校验验证器 </p>
 *
 * @author : zhengqing
 * @description :
 * @date : 2019/12/11 11:01
 */
public class TextFormatValidatorClass implements ConstraintValidator<TextFormat, Object> {

    private boolean notChinese;
    private String[] contains;
    private String[] notContains;
    private String startWith;
    private String endsWith;
    private String message;

    @Override
    public void initialize(TextFormat textFormat) {
        this.notChinese = textFormat.notChinese();
        this.contains = textFormat.contains();
        this.notContains = textFormat.notContains();
        this.startWith = textFormat.startWith();
        this.endsWith = textFormat.endsWith();
        this.message = textFormat.message();
    }

    @Override
    public boolean isValid(Object type, ConstraintValidatorContext context) {
        String target = (String) type;
        if (type instanceof String) {
            if (notChinese) {
                Pattern pattern = Pattern.compile("[\u4e00-\u9fa5]");
                Matcher m = pattern.matcher(target);
                if (m.find()) {
                    throw new MyException(message);
                }
            }

            for (int j = 0; j < contains.length; j++) {
                if (!target.contains(contains[j])) {
                    throw new MyException(message);
                }
            }

            for (int j = 0; j < notContains.length; j++) {
                if (target.contains(notContains[j])) {
                    throw new MyException(message);
                }
            }

            if (!StringUtils.isEmpty(startWith)) {
                if (!target.startsWith(startWith)) {
                    throw new MyException(message);
                }
            }

            if (!StringUtils.isEmpty(target)) {
                if (!target.endsWith(endsWith)) {
                    throw new MyException(message);
                }
            }
        } else if (type instanceof Integer) {

        }
        return true;
    }

}
