package utils;

import org.activiti.engine.form.FormData;
import org.activiti.engine.form.FormProperty;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Common {
    /**
     * 获取文件的扩展名
     * @param filename 文件名
     * @return 返回文件的扩展名 不包括.
     */
    public static String getExtension(String filename){
        int index = filename.lastIndexOf('.');
        return filename.substring(index + 1);
    }

    /**
     * 根据formData和request填充成activiti中FormService所需要的数据
     * @param formData 主要用于获取键
     * @param request 主要用于获取值
     * @return 返回键值对
     */
    public static Map<String, String> getFormValue(FormData formData, HttpServletRequest request){
        Map<String, String> formValues = new HashMap<>();
        String formKey = formData.getFormKey();

        //外置表单
        if (StringUtils.isNotBlank(formKey)){
            Map<String, String[]> parameterMap = request.getParameterMap();
            Set<Map.Entry<String, String[]>> entrySet = parameterMap.entrySet();
            for (Map.Entry<String, String[]> entry : entrySet) {
                String key = entry.getKey();
                formValues.put(key, entry.getValue()[0]);
            }
        }
        //动态表单
        else{
            //从请求中获取表单的值
            List<FormProperty> formProperties = formData.getFormProperties();
            for (FormProperty formProperty: formProperties){
                if (formProperty.isWritable()){
                    String value = request.getParameter(formProperty.getId());
                    formValues.put(formProperty.getId(), value);
                }
            }
        }
        return formValues;
    }
}
