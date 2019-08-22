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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
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

    /**
     * 显示当前的已经部署的流程文件 并且可以部署文件
     * @param model 显示页面
     * @return ftl文件名称
     */
    @RequestMapping(value = "/process-list")
    public String processList(ModelMap model){
        List<ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery().list();
        model.addAttribute("processDefinitionList", processDefinitions);
        return "process-list";
    }

    /**
     * 部署流程文件
     * @param file 需要部署的文件
     * @param redirectAttributes 用于显示闪回信息
     * @return
     */
    @RequestMapping(value = "/deploy", method = RequestMethod.POST)
    public String deploy(@RequestParam(value="file")MultipartFile file, RedirectAttributes redirectAttributes){
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
            redirectAttributes.addFlashAttribute("message", "任务成功部署");
        }catch (Exception e){
            System.out.println("error on deploy process");
            redirectAttributes.addFlashAttribute("level", "danger");
            redirectAttributes.addFlashAttribute("message", "任务部署失败");
        }
        return "redirect:process-list";
    }

    /**
     * 读取流程文件或者流程图的显示
     * @param processDefinitionId 流程定义的id
     * @param resourceName 资源名称
     * @param response 存放资源信息
     * @throws Exception
     */
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

    /**
     * 根据部署流程id删除该部署
     * TODO: 目前尚存在一些问题，即不能使用GET进行删除
     * @param deploymentId 部署流程的id
     * @param redirectAttributes 用于显示闪现消息
     * @return 重定向到显示流程列表
     */
    @RequestMapping(value = "/delete-deployment")
    public String deleteProcessDefinition(@RequestParam("deploymentId") String deploymentId
            , RedirectAttributes redirectAttributes){
        repositoryService.deleteDeployment(deploymentId, true);

        redirectAttributes.addFlashAttribute("level", "info");
        redirectAttributes.addFlashAttribute("message", "流程删除成功");

        return "redirect:process-list";
    }
}
