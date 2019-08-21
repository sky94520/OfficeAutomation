# OA系统相关说明
## 1. 库
> 1. SpringBoot Web库
> 2. Activiti 工作流库
> 3. freemarker 模板
> 4. rapid-core 扩展freemarker实现了模板继承
## 2. 热更新
> 默认并未实现热更新，可参考此帖子：https://blog.csdn.net/babyyaoyao/article/details/80832417
> maven已经添加依赖，需要修改idea
## 3. 闪现消息
> 在Spring MVC中，被@RequestMapping注解的方法，可以接收
> RedirectAttributes ra参数，通过ra.addFlashAttribute(key,value)
> 可以向模板文件中注册名称为key，值为value的对象。