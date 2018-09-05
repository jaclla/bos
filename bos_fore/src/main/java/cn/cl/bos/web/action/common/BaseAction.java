package cn.cl.bos.web.action.common;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import org.springframework.data.domain.Page;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;


public abstract class BaseAction<T> extends ActionSupport implements ModelDriven<T> {
    //模型驱动
    protected T model;

    @Override
    public T getModel() {
        return model;
    }

    //构造器
    public BaseAction() {
        //BaseAction<Area>
        Type genericSuperclass = this.getClass().getGenericSuperclass();
        ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
        Class<T> modelClass = (Class<T>) parameterizedType.getActualTypeArguments()[0];
        try {
           model = modelClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("构建失败");
        }

    }

    //属性驱动
    protected int rows;

    protected int page;

    public void setRows(int rows) {
        this.rows = rows;
    }

    public void setPage(int page) {
        this.page = page;
    }

    protected void pushPageDataToValueStack(Page<T> pageData) {
        //返回数据到客户端
        Map<String, Object> result = new HashMap<>();
        result.put("total", pageData.getTotalElements());
        result.put("rows", pageData.getContent());
        //将map转换成json 使用struts2-json-plugin插件
        ActionContext.getContext().getValueStack().push(result);
    }
}
