package org.mahatma.atp.servlet;

import org.helium.database.Database;
import org.helium.framework.annotations.FieldSetter;
import org.helium.framework.annotations.ServiceSetter;
import org.helium.framework.annotations.ServletImplementation;
import org.helium.http.servlet.HttpMappings;
import org.helium.http.servlet.HttpServlet;
import org.helium.http.servlet.HttpServletContext;
import org.mahatma.atp.common.db.bean.Pkg;
import org.mahatma.atp.common.db.dao.PkgCfgDao;
import org.mahatma.atp.common.db.dao.PkgDao;
import org.mahatma.atp.common.db.dao.TaskResultDetailDao;
import org.mahatma.atp.common.db.dao.TcDao;
import org.mahatma.atp.common.db.impl.PkgCfgDaoImpl;
import org.mahatma.atp.common.db.impl.PkgDaoImpl;
import org.mahatma.atp.common.db.impl.TaskResultDetailDaoImpl;
import org.mahatma.atp.common.db.impl.TcDaoImpl;
import org.mahatma.atp.common.util.FormatUtil;
import org.mahatma.atp.conf.util.RunShellUtil;
import org.mahatma.atp.plan.RunPlan;
import org.mahatma.atp.util.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 对数据库的判断只有获取记录用于判断和删除pkg，pkgcfg，tc
 * <p>
 * Created by JiYunfei on 17-9-11.
 */
@ServletImplementation(id = "atp:DeletePackageServlet")
@HttpMappings(contextPath = "/autoTest/apps", urlPattern = "/delete")
public class DeletePackageServlet extends HttpServlet {
    private static Logger LOGGER = LoggerFactory.getLogger(DeletePackageServlet.class);

    @FieldSetter("URCS_ATPDB")
    private Database atpDB;
    @ServiceSetter
    private RunPlan runPlan;

    @Override
    public void process(HttpServletContext ctx) throws Exception {
        LOGGER.info("delete start!");
        HttpServletRequest request = ctx.getRequest();
        HttpServletResponse response = ctx.getResponse();
        PkgDao pkgDao = new PkgDaoImpl(atpDB);
        TcDao tcDao = new TcDaoImpl(atpDB);
        PkgCfgDao pkgCfgDao = new PkgCfgDaoImpl(atpDB);
        TaskResultDetailDao taskResultDetailDao = new TaskResultDetailDaoImpl(atpDB);

        Long pkgId = Long.parseLong(request.getParameter("id"));
        if (pkgId == null || pkgId < 1) {
            HttpUtil.sendResponse("400", "BAD_REQUEST", "", response);
            return;
        } else if (runPlan.pkgInPlan(pkgId)) {
            HttpUtil.sendResponse("444", "PKG_IN_PLAN", "", response);
            return;
        } else if (!pkgDao.isExisted(pkgId)) {
            HttpUtil.sendResponse("201", "ID_NOT_IN_DB", "", response);
            return;
        } else {
            Pkg pkg = pkgDao.get(pkgId);
            if (pkg != null) {
                try {
                    pkgCfgDao.deleteByPkgId(pkgId);
                    List<Long> tcIds = tcDao.listByPkgId(pkgId);
                    for (Long tcId : tcIds) {
                        LOGGER.info("delete tc id : {}", tcId);
                        taskResultDetailDao.deleteByTcId(tcId);
                    }
                    tcDao.deleteByPkgId(pkgId);
                    pkgDao.delete(pkgId);
                    RunShellUtil.runShellAndThrows(FormatUtil.generateDeletePackageCommand(pkg));
                    HttpUtil.sendResponse("200", "SUCCESS", "", response);
                    return;
                } catch (Exception e) {
                    HttpUtil.sendResponse("500", "SERVER_ERROR", "", response);
                    return;
                }
            } else {
                HttpUtil.sendResponse("500", "SERVER_ERROR", "", response);
                return;
            }
        }
    }

}
