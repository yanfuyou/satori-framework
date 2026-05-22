package com.satori.sso.mp;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.satori.model.po.OperationInputPO;
import com.satori.model.po.OperationPO;
import com.satori.sso.handler.LoginHandler;
import com.satori.sso.model.LoginModel;
import org.apache.ibatis.reflection.MetaObject;

/**
 * @author yanfuyou
 * @date 2025/05/28 20:27:55
 * @description
 */
public class SatoriMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        if (needFill(metaObject)) {
            LoginModel.UserInfo currentUser = LoginHandler.getCurrentUser();
            this.strictInsertFill(metaObject, "creatorId", Long.class, currentUser.getUserId());
            metaObject.setValue("modifierId", null);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        if (needFill(metaObject)) {
            LoginModel.UserInfo currentUser = LoginHandler.getCurrentUser();
            this.strictUpdateFill(metaObject, "modifierId", Long.class, currentUser.getUserId());
        }
    }

    private boolean needFill(MetaObject metaObject) {
        Object obj = metaObject.getOriginalObject();
        return obj instanceof OperationPO || obj instanceof OperationInputPO;
    }
}
