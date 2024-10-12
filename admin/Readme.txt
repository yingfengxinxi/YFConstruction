
crm_pro
├── admin         -- 系统管理模块和用户管理模块
├── authorization -- 登录鉴权模块
├── core          -- 通用的代码和工具类
├── crm           -- 客户管理模块（租户相关）
├── gateway       -- 网关模块
├── oa            -- OA模块（审批相关）
└── work          -- 项目管理模块（任务分配相关）
└── workflow          -- 工作流模块
-----------------------以上原框架自带---------------------------
├── build           -- 智慧工地功能模块（核心业务）
├── dataagg     -- 数据聚合模块
├── datarecept   -- 数据接收模块
├── message   -- 消息发送模块
├── xxl-job-admin   -- 定时任务模块


### 前置环境
- Jdk1.8
- Maven3.5.0+   
- Mysql5.7.20 
- Redis(版本不限)
- Nacos（1.2.1)

### 安装说明
#### 1. 导入初始化sql,目前项目下gateway模块使用的独立数据库，其他模块使用同一数据库
- 初始化gateway模块数据库：新建数据库 wk_nacos，参见脚本：DBsql/wk_nacos.sql
- 初始化其余模块数据库：新建数据库 wk_build_demo，参见脚本：DBsql/wk_build_demo.sql
- 初始化定时任务模块数据库：新建数据库 xxl_job ，参见脚本：DBsql/xxl_job.sql
#### 2.在各个模块下resource目录配置数据库以及redis帐号信息（默认使用的是application-dev.yml配置文件）
#### 3.项目需在项目根目录执行mvn install
#### 4.项目启动 
先启动nacos、redis等基础服务
其中项目基础模块：gateway，authorization，admin必须启动，其他模块可按需启动。
启动完成后，在浏览器中访问：http://localhost:8443/即可登录系统

### 其他说明
新增服务时需在wk_nacos.config_info_route表中新增相应网关路由
