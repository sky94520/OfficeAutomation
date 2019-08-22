package com.threeluoxuan.service;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import utils.Common;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipInputStream;

@Service
public class DeploymentService {
    @Resource
    private RepositoryService repositoryService;

    /**
     * 获取流程定义列表
     * @return 流程定义数组
     */
    public List<ProcessDefinition> getProcessDefinitionList(){
        List<ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery().list();
        return processDefinitions;
    }

    /**
     * 删除已经部署好的流程
     * @param deploymentId 流程的ID
     * @param cascade TODO:是否级联
     */
    public void deleteDeployment(String deploymentId, boolean cascade){
        repositoryService.deleteDeployment(deploymentId, cascade);
    }

    /**
     * 部署文件
     * @param file 文件
     * @return 是否部署成功
     */
    public boolean deploy(MultipartFile file){
        //获取上传的文件名
        String filename = file.getOriginalFilename();
        boolean ret = false;
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
            ret = true;
        }catch (Exception e){
            System.out.println("error on deploy process");
        }
        return ret;
    }

    /**
     * 获取流程对应的资源，并返回InputStream
     * @param processDefinitionId 流程定义的ID
     * @param resourceName 资源名
     * @return InputStream对象
     */
    public InputStream readResource(String processDefinitionId, String resourceName){
        ProcessDefinitionQuery query = repositoryService.createProcessDefinitionQuery();
        ProcessDefinition processDefinition = query.processDefinitionId(processDefinitionId).singleResult();
        //通过接口读取资源流
        InputStream resourceAsStream =  repositoryService.getResourceAsStream(processDefinition.getDeploymentId(), resourceName);

        return resourceAsStream;
    }
}
