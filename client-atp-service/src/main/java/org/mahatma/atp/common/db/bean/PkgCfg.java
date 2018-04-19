package org.mahatma.atp.common.db.bean;


/**
 * Created by JiYunfei on 17-10-10.
 */
public class PkgCfg {
    private Long id;
    private String name;
    private String properties;
    private Long pkgId;
    private Boolean pkgCfgDefault;

    public Long getPkgId() {
        return pkgId;
    }

    public void setPkgId(Long pkgId) {
        this.pkgId = pkgId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }

    public Boolean isPkgCfgDefault() {
        return pkgCfgDefault;
    }

    public void setPkgCfgDefault(Boolean pkgCfgDefault) {
        this.pkgCfgDefault = pkgCfgDefault;
    }
}
