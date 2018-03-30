package com.enterprise.demo.controller;

import com.enterprise.demo.dataobject.vo.ResultVO;
import com.enterprise.demo.utils.CodeGenerator;
import com.enterprise.demo.utils.ResultVOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Map;

@Controller
@RequestMapping("/code")
public class CodeGenerateController {

    //    @Value("${server.context-path}")
    private String path;

    @GetMapping("/index")
    public ModelAndView codeIndex(Map<String, Object> map) {
        map.put("base_path", path);
        return new ModelAndView("view/index", map);
    }

    @GetMapping("/generate")
    public ModelAndView codeGenerate(Map<String, Object> map) {
        map.put("base_path", path);
        return new ModelAndView("view/generate", map);
    }

    @PostMapping("/generate")
    @ResponseBody
    public ResultVO codeCreate(@RequestBody Map<String, Object> map) {
        String tableName = map.get("table_name_").toString();
        String modelName = map.get("model_name_").toString();
        Boolean requireSql = (Boolean) map.get("require_sql_");
        map.remove("table_name_");
        map.remove("model_name_");
        map.remove("require_sql_");
        String sqlName = "";
        try {
            sqlName = CodeGenerator.genModel(modelName, tableName, map, requireSql);
            CodeGenerator.genRepository(modelName);
            CodeGenerator.genService(modelName);
            CodeGenerator.genController(modelName);
        } catch (Exception e) {
            return ResultVOUtils.returnFail();
        }
        return ResultVOUtils.returnSuccess("sql_name", sqlName);
    }

    @GetMapping("/download_sql/{sqlName}")
    public void sqlDownload(HttpServletResponse res, @PathVariable String sqlName) {
        String fileName = CodeGenerator.PROJECT_PATH + CodeGenerator.SQL_FILE_PATH + sqlName + ".sql";

        res.setHeader("content-type", "application/octet-stream");
        res.setContentType("application/octet-stream");
        res.setHeader("Content-Disposition", "attachment;filename=" + sqlName + ".sql");

        byte[] buff = new byte[1024];
        BufferedInputStream bis = null;
        OutputStream os = null;
        try {
            os = res.getOutputStream();
            bis = new BufferedInputStream(new FileInputStream(new File(fileName)));
            int i = bis.read(buff);
            while (i != -1) {
                os.write(buff, 0, buff.length);
                os.flush();
                i = bis.read(buff);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
