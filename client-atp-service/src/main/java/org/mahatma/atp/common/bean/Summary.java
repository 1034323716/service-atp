package org.mahatma.atp.common.bean;

import com.feinno.superpojo.annotation.Entity;
import com.feinno.superpojo.annotation.Field;
import com.feinno.superpojo.annotation.NodeType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lyfx on 17-10-10.
 */
@Entity(name = "summary")
public class Summary extends SuperClass{

    /**
     * 类型
     * 0,test case
     * 1,test case set
     */
    @Field(id= 1,name = "type",type = NodeType.ATTR)
    private int type = 0;

    /**
     * 目前只用于用例集Id,运行时,获得该id去用例集查找执行
     */
    @Field(id =2,name = "id",type = NodeType.ATTR)
    private long id;

    /**
     * 当type为1时，该参数应为空
     */
    @Field(id = 3,name = "package")
    private List<PkgCfgArgs> pkgCfgArgsList = new ArrayList<>();

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<PkgCfgArgs> getPkgCfgArgsList() {
        return pkgCfgArgsList;
    }

    public void setPkgCfgArgsList(List<PkgCfgArgs> pkgCfgArgsList) {
        this.pkgCfgArgsList = pkgCfgArgsList;
    }
}
