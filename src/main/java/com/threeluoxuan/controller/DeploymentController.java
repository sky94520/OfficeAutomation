package com.threeluoxuan.controller;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import utils.Common;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipInputStream;

/**
 * @author renxiaoniu
 * 显示当前已经部署的流程 和部署流程文件
 */
@Controller
public class DeploymentController {
    @Resource
    private RepositoryService repositoryService;
    //流程定义列表
    @RequestMapping(value = "/process-list")
    public String processList(ModelMap model){
        List<ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery().list();
        model.addAttribute("processDefinitionList", processDefinitions);
        return "process-list";
    }
    @RequestMapping(value = "/deploy", method = RequestMethod.POST)
    public String deploy(@RequestParam(value="file")MultipartFile file){
        //获取上传的文件名
        String filename = file.getOriginalFilename();
        try{
            InputStream fileInputStream = file.getInputStream();
            String extension = Common.getExtension(filename);
            //根据扩展名进行部署
            DeploymentBuilder builder = repositoryService.createDeployment();
            if (extension.equals("zip") || extension.equals("bar")){
                ZipInputStream zip = new ZipInputStream(fileInputStream);
                builder.addZipInputStream(zip);
            }
            else {
                builder.addInputStream(filename, fileInputStream);
            }
            builder.deploy();
        }catch (Exception e){
            System.out.println("error on deploy process");
        }
        return "redirect:process-list";
    }
    @RequestMapping(value = "/read-resource")
    public void readResource(@RequestParam("pdid") String processDefinitionId, @RequestParam("resourceName") String resourceName,
                             HttpServletResponse response) throws Exception{
        ProcessDefinitionQuery query = repositoryService.createProcessDefinitionQuery();
        ProcessDefinition processDefinition = query.processDefinitionId(processDefinitionId).singleResult();
        //通过接口读取资源流
        InputStream resourceAsStream =  repositoryService.getResourceAsStream(processDefinition.getDeploymentId(), resourceName);
        //输出资源
        byte[] bytes = new byte[1024];
        int len = -1;
        while ((len = resourceAsStream.read(bytes, 0, 1024)) != -1){
            response.getOutputStream().write(bytes, 0, len);
        }
    }
    @RequestMapping(value = "/delete-deployment")
    public String deleteProcessDefinition(@RequestParam("deploymentId") String deploymentId){
        repositoryService.deleteDeployment(deploymentId, true);
        return "redirect:process-list";
    }
}
