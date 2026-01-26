package com.vsu.iscr.utils;



import com.vsu.iscr.core.vo.ResultVo;

/**
 * Description: 返回数据工具类
 */
public class ResultVoUtil {

    /**
     * 私有化工具类 防止被实例化
     * j
     */
    private ResultVoUtil() {
    }

    /**
     * 成功
     *
     * @param object 需要返回的数据
     * @return data
     */
    public static ResultVo success(Object object) {
        ResultVo result = new ResultVo();
        result.setCode(0);
        result.setMessage("操作成功");
        result.setData(object);
        return result;
    }

    /**
     * 成功
     *
     * @param object 需要返回的数据
     * @return data
     */
    public static ResultVo success(Object object, Integer code) {
        ResultVo result = new ResultVo();
        result.setCode(code);
        result.setMessage("ok");
        result.setData(object);
        return result;
    }

    /**
     * 成功
     *
     * @param total  分页返回总页数
     * @param object 返回的数据
     * @return
     */
    public static ResultVo success(Long total, Object object) {
        ResultVo result = new ResultVo();
        result.setCode(0);
        result.setMessage("ok");
        result.setCount(total);
        result.setData(object);
        return result;
    }

    /**
     * 成功
     *
     * @return 返回空
     */
    public static ResultVo success() {
        ResultVo result = new ResultVo();
        result.setCode(0);
        result.setMessage("ok");
        return result;
    }

    /**
     * 成功
     *
     * @param msg 消息提示
     * @return 返回
     */
    public static ResultVo success(String msg) {
        ResultVo result = new ResultVo();
        result.setCode(0);
        result.setMessage(msg);
        return result;
    }



    /**
     * 错误
     *
     * @param code 状态码
     * @param msg  消息
     * @return ResultBean
     */
    public static ResultVo error(Integer code, String msg) {
        ResultVo result = new ResultVo();
        result.setCode(code);
        result.setMessage(msg);
        return result;
    }

    /**
     * 错误
     *
     * @param code 状态码
     * @param msg  消息
     * @param data 数据
     * @return ResultBean
     */
    public static ResultVo error(Integer code, String msg, Object data) {
        ResultVo result = new ResultVo();
        result.setCode(code);
        result.setMessage(msg);
        result.setData(data);
        return result;
    }

    /**
     * 错误
     *
     * @param msg 错误信息
     * @return ResultBean
     */
    public static ResultVo error(String msg) {
        return error(-1, msg);
    }


    /**
     * 响应返回结果
     *
     * @param rows 影响行数
     * @return 操作结果
     */
    public static ResultVo toAjax(int rows) {
        return rows > 0 ? success("操作成功") : error("操作失败");
    }

}
