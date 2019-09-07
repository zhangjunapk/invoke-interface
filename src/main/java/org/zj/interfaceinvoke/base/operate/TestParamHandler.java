package org.zj.interfaceinvoke.base.operate;

import com.alibaba.fastjson.JSONObject;
import org.zj.interfaceinvoke.base.bean.RequestParam;
import org.zj.interfaceinvoke.base.util.AESUtil;
import org.zj.interfaceinvoke.business.test.bean.RequestHlzApply;

import java.util.UUID;

/**
 * @ProjectName: interface-invoke
 * @ClassName: TestParamHandler
 * @Description: 测试的参数处理类
 * @Author: ZhangJun
 * @Date: 2019/9/7 19:39
 */
public class TestParamHandler implements IParamHandle{
    @Override
    public void handleParam(RequestParam requestParam) {
        for(Object jsonParam:requestParam.getJsonParam()){
            if(jsonParam instanceof RequestHlzApply){
                RequestHlzApply jsonParam1 = (RequestHlzApply) jsonParam;
                jsonParam1.setReqBody(JSONObject.toJSONString(jsonParam1.getUserInfo()));
                jsonParam1.getReqBodyBean().setTransId(UUID.randomUUID().toString());
                System.out.println("我来处理了--->");
                String aesKey ="5211C13B90005152F295915A1A05E5DF9B4D2CB2575A09C12D257D061062A322";
                String reqBody = AESUtil.encrypt(jsonParam1.getReqBody(),aesKey);
                jsonParam1.setReqBody(reqBody);
                requestParam.setHandledJsonParam(JSONObject.toJSONString(jsonParam1));
            }
        }
    }
}
