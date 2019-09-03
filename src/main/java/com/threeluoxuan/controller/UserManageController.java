/**
 * by dxy
 * 存在的问题：
 * 用户组管理存在的问题：1.用户修改的模态框太丑2.关闭模态框时无法清楚数据，需要在重新打开时清楚上一次填充的数据，影响用户体验3.人员修改成功后，弹出的信息在模态框下面，看不太清楚4.modifyGroups函数中有问题未解决
 *
 */
package com.threeluoxuan.controller;


import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.identity.UserQuery;
import org.activiti.engine.impl.util.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Controller
public class UserManageController {

    @Resource
    private IdentityService identityService;

    /**
     * 展示所有用户信息
     * @return
     */
    @RequestMapping(value = "/user-manage")
    public ModelAndView userManage(){
        ModelAndView view = new ModelAndView("user-manage");
        List<User> userList = identityService.createUserQuery().list();
        view.addObject("userList", userList);
        return view;
    }

    /**
     * 添加一个新用户
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "/add-user",method = RequestMethod.POST)
    @ResponseBody
    public  void addUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //接受前端数据
        String id = request.getParameter("id");
        String firstName = request.getParameter("first_name");
        String lastName = request.getParameter("last_name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        //建立新的User并保存到数据库
        User user = identityService.newUser(id);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(password);
        try {
            identityService.saveUser(user);

            response.setContentType("application/json;charset=utf-8;");
            JSONObject returnValue = new JSONObject();
            returnValue.put("success",true);
            returnValue.put("message","用户添加成功");
            response.getWriter().print(returnValue);
        }
        catch (Exception e){
            e.printStackTrace();
            response.setContentType("application/json;charset=utf-8;");
            JSONObject returnValue = new JSONObject();
            returnValue.put("success",false);
            returnValue.put("message","用户添加失败");
            response.getWriter().print(returnValue);
        }
    }

    /**
     * 展示用户组的信息
     * @return
     */
    @RequestMapping(value = "/group-manage")
    public ModelAndView groupManage(){
        ModelAndView view = new ModelAndView("group-manage");
        List<Group> groupList = identityService.createGroupQuery().list();
        view.addObject("groupList", groupList);
        return view;
    }

    /**
     * 添加用户组
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "/add-group")
    public void addGroup(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");
        String name = request.getParameter("name");
        String type = request.getParameter("type");
        Group group = identityService.newGroup(id);
        group.setName(name);
        group.setType(type);
        try {
            identityService.saveGroup(group);
            response.setContentType("application/json;charset=utf-8;");
            JSONObject returnValue = new JSONObject();
            returnValue.put("success",true);
            returnValue.put("message","用户组添加成功");
            response.getWriter().print(returnValue);
        } catch (Exception e) {
            e.printStackTrace();
            response.setContentType("application/json;charset=utf-8;");
            JSONObject returnValue = new JSONObject();
            returnValue.put("success",false);
            returnValue.put("message","用户组添加失败");
            response.getWriter().print(returnValue);
        }
    }

    /**
     * 利用得到的用户组ID，取得该用户组中用户的信息，返回给前端
     */
    @RequestMapping(value = "/get-member")
    public void getMember(HttpServletRequest request, HttpServletResponse response) throws IOException{
        String id =request.getParameter("id");
        try {
            //由用户组ID查询用户组内成员信息
            List<User> usersInGroup = identityService.createUserQuery().memberOfGroup(id).list();
            //下面语句还真挺重要，没有的话前端不显示汉字
            response.setContentType("application/json;charset=utf-8;");
            JSONObject membersInfo = new JSONObject();
            for(User user:usersInGroup){
                JSONObject userInfo = new JSONObject();
                userInfo.put("firstName",user.getFirstName());
                userInfo.put("lastName",user.getLastName());
                userInfo.put("email",user.getEmail());
                userInfo.put("password",user.getPassword());
                membersInfo.put(user.getId(),userInfo);
            }
            response.getWriter().print(membersInfo);
        } catch (Exception e){
            e.printStackTrace();
            response.setContentType("application/json;charset=utf-8;");
            JSONObject returnValue = new JSONObject();
            returnValue.put("success",false);
            returnValue.put("message","获取用户组信息失败");
            response.getWriter().print(returnValue);
        }
    }

    /**
     * 修改用户组成员时获取组中用户信息
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/modify-get-members")
    @ResponseBody
    public void modifyGetMembers(HttpServletRequest request,HttpServletResponse response) throws IOException {
        String id =request.getParameter("id");
        try {
            //由用户组ID查询用户组内成员信息
            List<User> usersInGroup = identityService.createUserQuery().memberOfGroup(id).list();
            ArrayList<String> exitIdGroup = new ArrayList<>();
            JSONObject existMembersInfo = new JSONObject();
            for(User user:usersInGroup){
                exitIdGroup.add(user.getId());
                existMembersInfo.put(user.getId(),user.getId());
            }
            //查询未在用户组中的用户ID
            List<User> userList = identityService.createUserQuery().list();
            JSONObject noExistMembersInfo = new JSONObject();
            for(User user:userList){
                if(!exitIdGroup.contains(user.getId())){
                    noExistMembersInfo.put(user.getId(),user.getId());
                }
            }
            //将两个JSON文件合并
            JSONObject membersInfo = new JSONObject();
            membersInfo.put("exit",existMembersInfo);
            membersInfo.put("noExit",noExistMembersInfo);
            response.getWriter().print(membersInfo);
        }catch (Exception e){
            e.printStackTrace();
            response.setContentType("application/json;charset=utf-8;");
            JSONObject returnValue = new JSONObject();
            returnValue.put("success",false);
            returnValue.put("message","获取用户组信息失败");
            response.getWriter().print(returnValue);
        }

    }

    /**
     * 修改用户组中成员
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/modify-members")
    @ResponseBody
    public void modifyMembers(HttpServletRequest request,HttpServletResponse response) throws IOException {
        String groupId = request.getParameter("groupId");
        String[] newIdArray = request.getParameter("idList").split(" ");
        List<User> exitUserList = null;
        //获取群组中已存在的用户
        try{
            exitUserList = identityService.createUserQuery().memberOfGroup(groupId).list();
        }catch (Exception e){
            e.printStackTrace();
            response.setContentType("application/json;charset=utf-8;");
            JSONObject returnValue = new JSONObject();
            returnValue.put("success",false);
            returnValue.put("message","修改用户组信息失败");
            response.getWriter().print(returnValue);
        }
        //一个为null的ArrayList不能添加元素
        ArrayList<String> exitIdList = new ArrayList<>();
        for(User user:exitUserList){
            exitIdList.add(user.getId());
        }
        try{
            //将不是该用户组的用户移出用户组
            for(String id:exitIdList){
                if(Arrays.asList(newIdArray).contains(id)) continue;
                identityService.deleteMembership(id,groupId);
            }
            //将新增的用户加入用户组
            for(String id:newIdArray){
                if(exitIdList.contains(id)) continue;
                identityService.createMembership(id,groupId);
            }
            response.setContentType("application/json;charset=utf-8;");
            JSONObject returnValue = new JSONObject();
            returnValue.put("success",true);
            returnValue.put("message","修改用户组成员成功");
            response.getWriter().print(returnValue);
        }catch (Exception e){
            e.printStackTrace();
            response.setContentType("application/json;charset=utf-8;");
            JSONObject returnValue = new JSONObject();
            returnValue.put("success",false);
            returnValue.put("message","修改用户组信息失败");
            response.getWriter().print(returnValue);
        }

        response.setContentType("application/json;charset=utf-8;");
        JSONObject returnValue = new JSONObject();
        returnValue.put("success",true);
        returnValue.put("message","用户组修改成功");
        response.getWriter().print(returnValue);
    }

    /**
     * 查看用户所属用户组时获取用户组信息
     * @param request
     * @param response
     */
    @RequestMapping("/get-group")
    @ResponseBody
    public void getGroup(HttpServletRequest request,HttpServletResponse response){
        String id = request.getParameter("id");
        try{
            List<Group> groupContainsID = identityService.createGroupQuery().groupMember(id).list();
            response.setContentType("application/json;charset=utf-8;");
            JSONObject groupsInfo = new JSONObject();
            for(Group group : groupContainsID){
                JSONObject groupInfo = new JSONObject();
                groupInfo.put("name",group.getName());
                groupInfo.put("type",group.getType());
                groupsInfo.put(group.getId(),groupInfo);
            }
            response.getWriter().print(groupsInfo);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 修改用户所属用户组时，获取用户所属用户组的信息
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/modify-get-groups")
    @ResponseBody
    public void modifyGetGroups(HttpServletRequest request,HttpServletResponse response) throws IOException {
        String id =request.getParameter("id");
        try {
            //由用户ID查询用户所属用户组
            List<Group> groupContainsUser = identityService.createGroupQuery().groupMember(id).list();
            ArrayList<String> exitIdGroup = new ArrayList<>();
            JSONObject existGroupsInfo = new JSONObject();
            for(Group group:groupContainsUser){
                exitIdGroup.add(group.getId());
                existGroupsInfo.put(group.getId(),group.getName());
            }
            //查询不包括这个用户的用户组
            List<Group> groupList = identityService.createGroupQuery().list();
            JSONObject noExistGroupsInfo = new JSONObject();
            for(Group group:groupList){
                if(!exitIdGroup.contains(group.getId())){
                    noExistGroupsInfo.put(group.getId(),group.getName());
                }
            }
            //将两个JSON文件合并
            JSONObject groupsInfo = new JSONObject();
            groupsInfo.put("exit",existGroupsInfo);
            groupsInfo.put("noExit",noExistGroupsInfo);
            response.setContentType("application/json;charset=utf-8;");
            response.getWriter().print(groupsInfo);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 修改用户所属的用户组
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/modify-groups")
    @ResponseBody
    public void modifyGroups(HttpServletRequest request,HttpServletResponse response) throws IOException {
        String userId = request.getParameter("userId");
        String[] newNameArray = request.getParameter("nameList").split(" ");
        List<Group> exitGroupList = new ArrayList<>();
        //获取用户所在的用户组
        try {
            exitGroupList = identityService.createGroupQuery().groupMember(userId).list();
        } catch (Exception e) {
            e.printStackTrace();
            response.setContentType("application/json;charset=utf-8;");
            JSONObject returnValue = new JSONObject();
            returnValue.put("success", false);
            returnValue.put("message", "修改用户组信息失败");
            response.getWriter().print(returnValue);
        }
        ArrayList<String> exitNameList = new ArrayList<>();
        if(exitGroupList.size() != 0) {
            for (Group group : exitGroupList) {
                exitNameList.add(group.getName());
            }
        }
        try {
            //将该用户移出自己不属于的用户组
            if(exitNameList.size() != 0) {
                for (String name : exitNameList) {
                    if (Arrays.asList(newNameArray).contains(name)) continue;
                    Group group = identityService.createGroupQuery().groupName(name).singleResult();
                    identityService.deleteMembership(userId, group.getId());
                }
            }
            //将用户添加入新所属的用户组
            for (String name : newNameArray) {
                if (exitNameList.contains(name)) continue;
                Group group = identityService.createGroupQuery().groupName(name).singleResult();
                identityService.createMembership(userId, group.getId());
            }
            response.setContentType("application/json;charset=utf-8;");
            JSONObject returnValue = new JSONObject();
            returnValue.put("success", true);
            returnValue.put("message", "修改用户组成员成功");
            response.getWriter().print(returnValue);
        } catch (Exception e) {
            //会报错，原因未知
            //e.printStackTrace();
            response.setContentType("application/json;charset=utf-8;");
            JSONObject returnValue = new JSONObject();
            returnValue.put("success", false);
            returnValue.put("message", "修改用户组信息失败");
            response.getWriter().print(returnValue);
        }
    }

}
