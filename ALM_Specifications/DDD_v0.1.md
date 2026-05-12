# 企业级开源需求/设计/缺陷管理工具 - 数据库设计文档 (DDD)

---

## 文档信息
| 属性 | 值 |
|------|------|
| 文档版本 | v0.1 |
| 文档类型 | DDD (Database Design Document) |
| 创建日期 | 2026-05-11 |
| 关联TD | TD_v0.1.md |

---

## 一、数据库概述

### 1.1 数据库选型
- **数据库类型**: PostgreSQL 16
- **字符集**: UTF-8 (Unicode)
- **排序规则**: en_US.UTF-8

### 1.2 数据库命名规范
| 对象类型 | 命名规则 | 示例 |
|----------|----------|------|
| 数据库 | 项目名 | alm_db |
| 表 | 小写复数 | requirements |
| 字段 | 小写下划线 | req_id |
| 索引 | idx_表名_字段名 | idx_requirements_project_id |

---

## 二、核心表设计

### 2.1 用户与权限

#### users（用户表）
| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| user_id | VARCHAR(32) | PRIMARY KEY | 用户ID（UUID） |
| username | VARCHAR(50) | NOT NULL, UNIQUE | 用户名 |
| email | VARCHAR(100) | NOT NULL, UNIQUE | 邮箱 |
| password | VARCHAR(255) | NOT NULL | 密码（加密） |
| real_name | VARCHAR(50) | | 真实姓名 |
| avatar | VARCHAR(255) | | 头像URL |
| status | VARCHAR(20) | DEFAULT 'ACTIVE' | 状态 |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 创建时间 |
| updated_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 更新时间 |

#### roles（角色表）
| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| role_id | VARCHAR(32) | PRIMARY KEY | 角色ID |
| name | VARCHAR(50) | NOT NULL, UNIQUE | 角色名称 |
| description | TEXT | | 角色描述 |
| permissions | TEXT | | 权限列表(JSON) |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 创建时间 |

#### user_roles（用户角色关系表）
| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| user_id | VARCHAR(32) | FOREIGN KEY | 用户ID |
| role_id | VARCHAR(32) | FOREIGN KEY | 角色ID |
| PRIMARY KEY | (user_id, role_id) | | 联合主键 |

### 2.2 项目管理

#### projects（项目表）
| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| project_id | VARCHAR(32) | PRIMARY KEY | 项目ID |
| name | VARCHAR(100) | NOT NULL | 项目名称 |
| description | TEXT | | 项目描述 |
| status | VARCHAR(20) | DEFAULT 'ACTIVE' | 状态 |
| created_by | VARCHAR(32) | FOREIGN KEY | 创建人ID |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 创建时间 |
| updated_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 更新时间 |

#### project_members（项目成员表）
| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| project_id | VARCHAR(32) | FOREIGN KEY | 项目ID |
| user_id | VARCHAR(32) | FOREIGN KEY | 用户ID |
| role | VARCHAR(32) | | 项目内角色 |
| joined_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 加入时间 |
| PRIMARY KEY | (project_id, user_id) | | 联合主键 |

### 2.3 需求管理

#### requirements（需求表）
| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| req_id | VARCHAR(32) | PRIMARY KEY | 需求ID |
| title | VARCHAR(255) | NOT NULL | 需求标题 |
| content | TEXT | | 需求描述 |
| type | VARCHAR(32) | NOT NULL | 类型(USER/PRD/SYS) |
| priority | VARCHAR(16) | DEFAULT 'MEDIUM' | 优先级 |
| status | VARCHAR(32) | DEFAULT 'DRAFT' | 状态 |
| project_id | VARCHAR(32) | FOREIGN KEY | 项目ID |
| version | VARCHAR(16) | DEFAULT '1.0' | 版本号 |
| created_by | VARCHAR(32) | FOREIGN KEY | 创建人ID |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 创建时间 |
| updated_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 更新时间 |

### 2.4 设计管理

#### designs（设计表）
| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| design_id | VARCHAR(32) | PRIMARY KEY | 设计ID |
| title | VARCHAR(255) | NOT NULL | 设计标题 |
| content | TEXT | | 设计内容 |
| type | VARCHAR(32) | | 类型(ARCHITECTURE/MODULE/DETAIL) |
| project_id | VARCHAR(32) | FOREIGN KEY | 项目ID |
| version | VARCHAR(16) | DEFAULT '1.0' | 版本号 |
| created_by | VARCHAR(32) | FOREIGN KEY | 创建人ID |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 创建时间 |

### 2.5 测试管理

#### test_cases（测试用例表）
| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| case_id | VARCHAR(32) | PRIMARY KEY | 用例ID |
| title | VARCHAR(255) | NOT NULL | 用例标题 |
| steps | TEXT | | 测试步骤 |
| expected | TEXT | | 预期结果 |
| status | VARCHAR(32) | DEFAULT 'DRAFT' | 状态 |
| project_id | VARCHAR(32) | FOREIGN KEY | 项目ID |
| created_by | VARCHAR(32) | FOREIGN KEY | 创建人ID |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 创建时间 |

### 2.6 任务管理

#### tasks（任务表）
| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| task_id | VARCHAR(32) | PRIMARY KEY | 任务ID |
| type | VARCHAR(32) | NOT NULL | 类型(TASK/BUG/SUBTASK/EPIC) |
| title | VARCHAR(255) | NOT NULL | 标题 |
| description | TEXT | | 描述 |
| status | VARCHAR(32) | DEFAULT 'TODO' | 状态 |
| priority | VARCHAR(16) | DEFAULT 'MEDIUM' | 优先级 |
| assignee_id | VARCHAR(32) | FOREIGN KEY | 经办人ID |
| sprint_id | VARCHAR(32) | FOREIGN KEY | 迭代ID |
| project_id | VARCHAR(32) | FOREIGN KEY | 项目ID |
| created_by | VARCHAR(32) | FOREIGN KEY | 创建人ID |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 创建时间 |
| updated_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 更新时间 |

#### sprints（迭代表）
| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| sprint_id | VARCHAR(32) | PRIMARY KEY | 迭代ID |
| name | VARCHAR(100) | NOT NULL | 迭代名称 |
| start_date | DATE | NOT NULL | 开始日期 |
| end_date | DATE | NOT NULL | 结束日期 |
| status | VARCHAR(32) | DEFAULT 'PLANNING' | 状态 |
| project_id | VARCHAR(32) | FOREIGN KEY | 项目ID |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 创建时间 |

---

## 三、追溯关系表

### 3.1 traceability_req_design（需求-设计关联）
| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| id | BIGSERIAL | PRIMARY KEY | 自增主键 |
| req_id | VARCHAR(32) | FOREIGN KEY | 需求ID |
| design_id | VARCHAR(32) | FOREIGN KEY | 设计ID |
| relation_type | VARCHAR(32) | | 关系类型 |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 创建时间 |

### 3.2 traceability_design_test（设计-测试关联）
| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| id | BIGSERIAL | PRIMARY KEY | 自增主键 |
| design_id | VARCHAR(32) | FOREIGN KEY | 设计ID |
| case_id | VARCHAR(32) | FOREIGN KEY | 测试用例ID |
| relation_type | VARCHAR(32) | | 关系类型 |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 创建时间 |

### 3.3 traceability_req_test（需求-测试关联）
| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| id | BIGSERIAL | PRIMARY KEY | 自增主键 |
| req_id | VARCHAR(32) | FOREIGN KEY | 需求ID |
| case_id | VARCHAR(32) | FOREIGN KEY | 测试用例ID |
| relation_type | VARCHAR(32) | | 关系类型 |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 创建时间 |

### 3.4 traceability_item_task（对象-任务关联）
| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| id | BIGSERIAL | PRIMARY KEY | 自增主键 |
| item_type | VARCHAR(32) | NOT NULL | 对象类型(REQ/DESIGN/TEST) |
| item_id | VARCHAR(32) | NOT NULL | 对象ID |
| task_id | VARCHAR(32) | FOREIGN KEY | 任务ID |
| relation_type | VARCHAR(32) | | 关系类型 |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 创建时间 |

---

## 四、ASPICE 4.0相关表

### 4.1 aspice_process_domain（ASPICE过程域表）
| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| domain_id | VARCHAR(32) | PRIMARY KEY | 过程域ID（如SWE.1、SUP.8） |
| name | VARCHAR(100) | NOT NULL | 过程域名称 |
| description | TEXT | | 过程域描述 |
| category | VARCHAR(32) | | 类别（SWE/SUP/SAM/ENG） |
| capability_levels | VARCHAR(255) | | 能力等级(CL1-CL5) |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 创建时间 |

### 4.2 aspice_work_product（ASPICE工作产品表）
| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| product_id | VARCHAR(32) | PRIMARY KEY | 工作产品ID |
| domain_id | VARCHAR(32) | FOREIGN KEY | 所属过程域ID |
| name | VARCHAR(100) | NOT NULL | 工作产品名称 |
| description | TEXT | | 工作产品描述 |
| required | BOOLEAN | DEFAULT true | 是否必需 |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 创建时间 |

### 4.3 aspice_assessment（ASPICE评估表）
| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| assessment_id | VARCHAR(32) | PRIMARY KEY | 评估ID |
| project_id | VARCHAR(32) | FOREIGN KEY | 项目ID |
| domain_id | VARCHAR(32) | FOREIGN KEY | 过程域ID |
| capability_level | VARCHAR(16) | | 评估能力等级(CL1-CL5) |
| status | VARCHAR(32) | DEFAULT 'PLANNED' | 评估状态 |
| findings | TEXT | | 评估发现 |
| recommendations | TEXT | | 改进建议 |
| assessed_at | TIMESTAMP | | 评估时间 |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 创建时间 |

### 4.4 aspice_metric（ASPICE度量数据表）
| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| metric_id | VARCHAR(32) | PRIMARY KEY | 度量ID |
| project_id | VARCHAR(32) | FOREIGN KEY | 项目ID |
| domain_id | VARCHAR(32) | FOREIGN KEY | 过程域ID |
| metric_name | VARCHAR(100) | NOT NULL | 度量名称 |
| metric_value | VARCHAR(255) | | 度量值 |
| collected_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 收集时间 |

### 4.5 aspice_project_domain（项目-过程域关联表）
| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| id | BIGSERIAL | PRIMARY KEY | 自增主键 |
| project_id | VARCHAR(32) | FOREIGN KEY | 项目ID |
| domain_id | VARCHAR(32) | FOREIGN KEY | 过程域ID |
| target_level | VARCHAR(16) | | 目标能力等级 |
| current_level | VARCHAR(16) | | 当前能力等级 |
| PRIMARY KEY | (project_id, domain_id) | | 联合主键 |

---

## 五、审批流相关表

### 5.1 approval_template（审批流程模板表）
| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| template_id | VARCHAR(32) | PRIMARY KEY | 模板ID |
| name | VARCHAR(100) | NOT NULL | 模板名称 |
| description | TEXT | | 模板描述 |
| workflow_json | TEXT | NOT NULL | 流程定义JSON |
| is_active | BOOLEAN | DEFAULT true | 是否启用 |
| created_by | VARCHAR(32) | FOREIGN KEY | 创建人ID |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 创建时间 |
| updated_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 更新时间 |

### 5.2 approval_flow（审批流程实例表）
| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| flow_id | VARCHAR(32) | PRIMARY KEY | 流程实例ID |
| template_id | VARCHAR(32) | FOREIGN KEY | 关联模板ID |
| document_id | VARCHAR(32) | NOT NULL | 关联文档ID |
| document_type | VARCHAR(32) | NOT NULL | 文档类型 |
| status | VARCHAR(32) | DEFAULT 'PENDING' | 流程状态 |
| applicant_id | VARCHAR(32) | FOREIGN KEY | 申请人ID |
| title | VARCHAR(255) | NOT NULL | 申请标题 |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 创建时间 |
| updated_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 更新时间 |

### 5.3 approval_node（审批节点表）
| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| node_id | VARCHAR(32) | PRIMARY KEY | 节点ID |
| flow_id | VARCHAR(32) | FOREIGN KEY | 关联流程ID |
| node_type | VARCHAR(32) | NOT NULL | 节点类型(SERIAL/PARALLEL) |
| node_order | INT | NOT NULL | 节点顺序 |
| status | VARCHAR(32) | DEFAULT 'PENDING' | 节点状态 |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 创建时间 |

### 5.4 approval_task（审批任务表）
| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| task_id | VARCHAR(32) | PRIMARY KEY | 任务ID |
| node_id | VARCHAR(32) | FOREIGN KEY | 关联节点ID |
| assignee_id | VARCHAR(32) | FOREIGN KEY | 审批人ID |
| status | VARCHAR(32) | DEFAULT 'PENDING' | 任务状态 |
| comment | TEXT | | 审批意见 |
| added_by | VARCHAR(32) | FOREIGN KEY | 加签人ID |
| added_type | VARCHAR(16) | | 加签类型(BEFORE/AFTER) |
| transferred_from | VARCHAR(32) | FOREIGN KEY | 转签来源人ID |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 创建时间 |
| completed_at | TIMESTAMP | | 完成时间 |

### 5.5 approval_delegation（审批委托表）
| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| delegation_id | VARCHAR(32) | PRIMARY KEY | 委托ID |
| delegator_id | VARCHAR(32) | FOREIGN KEY | 委托人ID |
| delegatee_id | VARCHAR(32) | FOREIGN KEY | 被委托人ID |
| start_time | TIMESTAMP | NOT NULL | 委托开始时间 |
| end_time | TIMESTAMP | NOT NULL | 委托结束时间 |
| is_active | BOOLEAN | DEFAULT true | 是否有效 |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 创建时间 |

---

## 六、组织架构同步相关表

### 6.1 organization（组织架构表）
| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| org_id | VARCHAR(32) | PRIMARY KEY | 组织ID |
| name | VARCHAR(100) | NOT NULL | 组织名称 |
| parent_id | VARCHAR(32) | FOREIGN KEY | 上级组织ID |
| type | VARCHAR(32) | NOT NULL | 组织类型(COMPANY/DEPARTMENT/TEAM) |
| external_id | VARCHAR(100) | | 外部系统ID |
| sync_source | VARCHAR(50) | | 同步来源 |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 创建时间 |
| updated_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 更新时间 |

### 6.2 sync_config（同步配置表）
| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| config_id | VARCHAR(32) | PRIMARY KEY | 配置ID |
| name | VARCHAR(100) | NOT NULL | 配置名称 |
| type | VARCHAR(32) | NOT NULL | 同步类型(LDAP/SCIM/API) |
| config_json | TEXT | NOT NULL | 配置参数JSON |
| sync_frequency | INT | DEFAULT 60 | 同步频率(分钟) |
| sync_mode | VARCHAR(16) | DEFAULT 'INCREMENTAL' | 同步模式 |
| conflict_strategy | VARCHAR(16) | DEFAULT 'SKIP' | 冲突策略 |
| is_active | BOOLEAN | DEFAULT true | 是否启用 |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 创建时间 |
| updated_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 更新时间 |

### 6.3 sync_task（同步任务表）
| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| task_id | VARCHAR(32) | PRIMARY KEY | 任务ID |
| config_id | VARCHAR(32) | FOREIGN KEY | 关联配置ID |
| status | VARCHAR(32) | DEFAULT 'PENDING' | 任务状态 |
| sync_type | VARCHAR(32) | NOT NULL | 同步类型(ORG/ROLE/USER) |
| start_time | TIMESTAMP | | 开始时间 |
| end_time | TIMESTAMP | | 结束时间 |
| sync_count | INT | DEFAULT 0 | 同步数量 |
| error_message | TEXT | | 错误信息 |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 创建时间 |

### 6.4 sync_log（同步日志表）
| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| log_id | VARCHAR(32) | PRIMARY KEY | 日志ID |
| task_id | VARCHAR(32) | FOREIGN KEY | 关联任务ID |
| operation_type | VARCHAR(32) | NOT NULL | 操作类型 |
| target_type | VARCHAR(32) | NOT NULL | 目标类型 |
| target_id | VARCHAR(32) | | 目标ID |
| target_name | VARCHAR(255) | | 目标名称 |
| external_id | VARCHAR(100) | | 外部系统ID |
| message | TEXT | | 日志消息 |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 创建时间 |

---

## 七、AI模型配置表

### 7.1 ai_model（AI模型配置表）
| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| model_id | VARCHAR(32) | PRIMARY KEY | 模型ID |
| name | VARCHAR(100) | NOT NULL | 模型名称 |
| provider | VARCHAR(50) | NOT NULL | 模型提供商 |
| api_base_url | VARCHAR(255) | NOT NULL | API基础URL |
| api_key | VARCHAR(255) | NOT NULL | API密钥（加密） |
| model_name | VARCHAR(100) | NOT NULL | 模型名称 |
| max_tokens | INT | DEFAULT 4096 | 最大token数 |
| temperature | DECIMAL(3,2) | DEFAULT 0.7 | 温度参数 |
| is_active | BOOLEAN | DEFAULT true | 是否启用 |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 创建时间 |
| updated_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 更新时间 |

### 7.2 ai_generation_log（AI生成日志表）
| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| log_id | VARCHAR(32) | PRIMARY KEY | 日志ID |
| model_id | VARCHAR(32) | FOREIGN KEY | 使用的模型ID |
| generation_type | VARCHAR(32) | NOT NULL | 生成类型 |
| input_text | TEXT | NOT NULL | 输入文本 |
| output_text | TEXT | | 输出文本 |
| token_usage | INT | | token使用量 |
| duration_ms | INT | | 耗时(毫秒) |
| success | BOOLEAN | NOT NULL | 是否成功 |
| error_message | TEXT | | 错误信息 |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 创建时间 |

---

## 八、国际化相关表

### 8.1 language（语言表）
| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| lang_code | VARCHAR(10) | PRIMARY KEY | 语言代码(zh-CN/en-US) |
| name | VARCHAR(50) | NOT NULL | 语言名称 |
| native_name | VARCHAR(50) | NOT NULL | 本地语言名称 |
| is_active | BOOLEAN | DEFAULT true | 是否启用 |

### 8.2 user_language（用户语言偏好表）
| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| user_id | VARCHAR(32) | PRIMARY KEY | 用户ID |
| lang_code | VARCHAR(10) | FOREIGN KEY | 语言代码 |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 创建时间 |
| updated_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 更新时间 |

---

## 九、开源网盘集成表

### 9.1 storage_config（存储配置表）
| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| config_id | VARCHAR(32) | PRIMARY KEY | 配置ID |
| name | VARCHAR(100) | NOT NULL | 配置名称 |
| type | VARCHAR(32) | NOT NULL | 存储类型 |
| endpoint_url | VARCHAR(255) | NOT NULL | 服务端点URL |
| username | VARCHAR(100) | | 用户名 |
| password | VARCHAR(255) | | 密码（加密存储） |
| api_key | VARCHAR(255) | | API密钥 |
| base_path | VARCHAR(255) | | 基础存储路径 |
| is_default | BOOLEAN | DEFAULT false | 是否默认配置 |
| is_active | BOOLEAN | DEFAULT true | 是否启用 |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 创建时间 |
| updated_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 更新时间 |

### 9.2 file_storage（文件存储表）
| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| file_id | VARCHAR(32) | PRIMARY KEY | 文件ID |
| config_id | VARCHAR(32) | FOREIGN KEY | 关联存储配置ID |
| original_name | VARCHAR(255) | NOT NULL | 原始文件名 |
| storage_path | VARCHAR(500) | NOT NULL | 存储路径 |
| file_size | BIGINT | | 文件大小（字节） |
| file_type | VARCHAR(100) | | 文件类型（MIME类型） |
| hash_md5 | VARCHAR(32) | | MD5哈希值 |
| status | VARCHAR(32) | DEFAULT 'UPLOADED' | 文件状态 |
| iso_file_number | VARCHAR(100) | | ISO9001文件编号 |
| controlled | BOOLEAN | DEFAULT false | 是否受控文件 |
| distribution_status | VARCHAR(32) | DEFAULT 'PENDING' | 分发状态 |
| obsoleted_by | VARCHAR(32) | FOREIGN KEY | 替代文件ID |
| archived_at | TIMESTAMP | | 归档时间 |
| archived_by | VARCHAR(32) | FOREIGN KEY | 归档人ID |
| created_by | VARCHAR(32) | FOREIGN KEY | 上传人ID |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 创建时间 |
| updated_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 更新时间 |

### 9.3 file_permission（文件权限表）
| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| permission_id | VARCHAR(32) | PRIMARY KEY | 权限ID |
| file_id | VARCHAR(32) | FOREIGN KEY | 关联文件ID |
| user_id | VARCHAR(32) | | 用户ID |
| role_id | VARCHAR(32) | | 角色ID |
| permissions | VARCHAR(100) | NOT NULL | 权限列表 |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 创建时间 |

### 9.4 file_version（文件版本表）
| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| version_id | VARCHAR(32) | PRIMARY KEY | 版本ID |
| file_id | VARCHAR(32) | FOREIGN KEY | 关联文件ID |
| version_number | VARCHAR(20) | NOT NULL | 版本号 |
| storage_path | VARCHAR(500) | NOT NULL | 版本文件路径 |
| change_log | TEXT | | 变更说明 |
| created_by | VARCHAR(32) | FOREIGN KEY | 创建人ID |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 创建时间 |

---

## 十、产品基线库管理表

### 10.1 baseline_repository（基线库表）
| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| repo_id | VARCHAR(32) | PRIMARY KEY | 基线库ID |
| name | VARCHAR(100) | NOT NULL | 基线库名称 |
| description | TEXT | | 基线库描述 |
| product_id | VARCHAR(32) | FOREIGN KEY | 关联产品ID |
| scope_config | JSON | | 基线范围配置 |
| freeze_rule | JSON | | 冻结规则配置 |
| is_active | BOOLEAN | DEFAULT true | 是否启用 |
| created_by | VARCHAR(32) | FOREIGN KEY | 创建人ID |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 创建时间 |
| updated_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 更新时间 |

### 10.2 baseline_version（基线版本表）
| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| version_id | VARCHAR(32) | PRIMARY KEY | 基线版本ID |
| repo_id | VARCHAR(32) | FOREIGN KEY | 关联基线库ID |
| version_number | VARCHAR(50) | NOT NULL | 基线版本号 |
| status | VARCHAR(32) | DEFAULT 'DRAFT' | 状态 |
| baseline_date | TIMESTAMP | | 基线创建日期 |
| description | TEXT | | 版本描述 |
| contents | JSON | | 基线内容清单 |
| created_by | VARCHAR(32) | FOREIGN KEY | 创建人ID |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 创建时间 |

### 10.3 baseline_item（基线条目表）
| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| item_id | VARCHAR(32) | PRIMARY KEY | 条目ID |
| version_id | VARCHAR(32) | FOREIGN KEY | 关联基线版本ID |
| item_type | VARCHAR(32) | NOT NULL | 条目类型 |
| item_id_ref | VARCHAR(32) | NOT NULL | 关联项目ID |
| item_version | VARCHAR(20) | | 条目版本号 |
| added_by | VARCHAR(32) | FOREIGN KEY | 添加人ID |
| added_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 添加时间 |

### 10.4 change_request（变更请求表）
| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| request_id | VARCHAR(32) | PRIMARY KEY | 变更请求ID |
| repo_id | VARCHAR(32) | FOREIGN KEY | 关联基线库ID |
| baseline_version_id | VARCHAR(32) | FOREIGN KEY | 关联基线版本ID |
| title | VARCHAR(255) | NOT NULL | 变更标题 |
| description | TEXT | | 变更描述 |
| change_type | VARCHAR(32) | NOT NULL | 变更类型 |
| affected_items | JSON | | 受影响的条目列表 |
| impact_analysis | TEXT | | 影响分析报告 |
| status | VARCHAR(32) | DEFAULT 'DRAFT' | 状态 |
| created_by | VARCHAR(32) | FOREIGN KEY | 创建人ID |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 创建时间 |
| updated_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 更新时间 |

### 10.5 change_approval（变更审批表）
| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| approval_id | VARCHAR(32) | PRIMARY KEY | 审批ID |
| request_id | VARCHAR(32) | FOREIGN KEY | 关联变更请求ID |
| approver_id | VARCHAR(32) | FOREIGN KEY | 审批人ID |
| status | VARCHAR(32) | DEFAULT 'PENDING' | 审批状态 |
| comment | TEXT | | 审批意见 |
| approved_at | TIMESTAMP | | 审批时间 |

### 10.6 baseline_audit_log（基线审计日志表）
| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| log_id | VARCHAR(32) | PRIMARY KEY | 日志ID |
| repo_id | VARCHAR(32) | FOREIGN KEY | 关联基线库ID |
| version_id | VARCHAR(32) | FOREIGN KEY | 关联基线版本ID |
| operation_type | VARCHAR(32) | NOT NULL | 操作类型 |
| operation_desc | TEXT | | 操作描述 |
| operator_id | VARCHAR(32) | FOREIGN KEY | 操作人ID |
| operator_name | VARCHAR(100) | | 操作人姓名 |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 操作时间 |

---

## 十一、ISO9001文件管理合规表

### 11.1 file_distribution（文件分发记录表）
| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| distribution_id | VARCHAR(32) | PRIMARY KEY | 分发记录ID |
| file_id | VARCHAR(32) | FOREIGN KEY | 关联文件ID |
| recipient_id | VARCHAR(32) | FOREIGN KEY | 接收人ID |
| recipient_name | VARCHAR(100) | NOT NULL | 接收人姓名 |
| distribution_date | TIMESTAMP | NOT NULL | 分发日期 |
| received | BOOLEAN | DEFAULT false | 是否已接收 |
| received_date | TIMESTAMP | | 接收日期 |
| distribution_method | VARCHAR(32) | | 分发方式 |
| remarks | TEXT | | 备注 |
| created_by | VARCHAR(32) | FOREIGN KEY | 创建人ID |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 创建时间 |

### 11.2 file_audit_trail（文件审计追踪表）
| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| trail_id | VARCHAR(32) | PRIMARY KEY | 审计记录ID |
| file_id | VARCHAR(32) | FOREIGN KEY | 关联文件ID |
| operation_type | VARCHAR(32) | NOT NULL | 操作类型 |
| operator_id | VARCHAR(32) | FOREIGN KEY | 操作人ID |
| operator_name | VARCHAR(100) | NOT NULL | 操作人姓名 |
| operation_date | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 操作日期 |
| details | JSON | | 操作详情 |
| ip_address | VARCHAR(50) | | 操作IP地址 |

---

## 十二、任务模板管理表

### 12.1 task_template（任务模板表）
| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| template_id | VARCHAR(32) | PRIMARY KEY | 模板ID |
| name | VARCHAR(100) | NOT NULL | 模板名称 |
| description | TEXT | | 模板描述 |
| category | VARCHAR(50) | | 模板分类 |
| project_type | VARCHAR(50) | | 适用项目类型 |
| phase | VARCHAR(50) | | 适用项目阶段 |
| status | VARCHAR(32) | DEFAULT 'ACTIVE' | 状态 |
| parameters | JSON | | 参数配置 |
| created_by | VARCHAR(32) | FOREIGN KEY | 创建人ID |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 创建时间 |
| updated_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 更新时间 |

### 12.2 task_template_field（任务模板字段表）
| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| field_id | VARCHAR(32) | PRIMARY KEY | 字段ID |
| template_id | VARCHAR(32) | FOREIGN KEY | 关联模板ID |
| field_name | VARCHAR(100) | NOT NULL | 字段名称 |
| field_type | VARCHAR(32) | NOT NULL | 字段类型 |
| required | BOOLEAN | DEFAULT false | 是否必填 |
| default_value | TEXT | | 默认值 |
| options | JSON | | 选项列表 |
| sort_order | INT | DEFAULT 0 | 排序顺序 |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 创建时间 |

### 12.3 task_template_version（任务模板版本表）
| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| version_id | VARCHAR(32) | PRIMARY KEY | 版本ID |
| template_id | VARCHAR(32) | FOREIGN KEY | 关联模板ID |
| version_number | VARCHAR(20) | NOT NULL | 版本号 |
| change_log | TEXT | | 变更日志 |
| template_snapshot | JSON | | 模板快照 |
| created_by | VARCHAR(32) | FOREIGN KEY | 创建人ID |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 创建时间 |

---

## 十三、知识库管理表（类似Confluence）

### 13.1 knowledge_space（知识空间表）
| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| space_id | VARCHAR(32) | PRIMARY KEY | 空间ID |
| name | VARCHAR(100) | NOT NULL | 空间名称 |
| key | VARCHAR(50) | UNIQUE | 空间标识 |
| description | TEXT | | 空间描述 |
| icon | VARCHAR(255) | | 空间图标 |
| theme | VARCHAR(50) | | 空间主题 |
| status | VARCHAR(32) | DEFAULT 'ACTIVE' | 状态 |
| created_by | VARCHAR(32) | FOREIGN KEY | 创建人ID |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 创建时间 |
| updated_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 更新时间 |

### 13.2 knowledge_page（知识页面表）
| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| page_id | VARCHAR(32) | PRIMARY KEY | 页面ID |
| space_id | VARCHAR(32) | FOREIGN KEY | 关联空间ID |
| parent_id | VARCHAR(32) | FOREIGN KEY | 父页面ID |
| title | VARCHAR(255) | NOT NULL | 页面标题 |
| content | LONGTEXT | | 页面内容 |
| content_format | VARCHAR(20) | DEFAULT 'MARKDOWN' | 内容格式 |
| status | VARCHAR(32) | DEFAULT 'DRAFT' | 状态 |
| view_count | INT | DEFAULT 0 | 浏览次数 |
| created_by | VARCHAR(32) | FOREIGN KEY | 创建人ID |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 创建时间 |
| updated_by | VARCHAR(32) | FOREIGN KEY | 更新人ID |
| updated_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 更新时间 |

### 13.3 knowledge_page_version（页面版本表）
| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| version_id | VARCHAR(32) | PRIMARY KEY | 版本ID |
| page_id | VARCHAR(32) | FOREIGN KEY | 关联页面ID |
| version_number | VARCHAR(20) | NOT NULL | 版本号 |
| content | LONGTEXT | | 版本内容 |
| change_log | TEXT | | 变更说明 |
| created_by | VARCHAR(32) | FOREIGN KEY | 创建人ID |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 创建时间 |

### 13.4 knowledge_page_tag（页面标签表）
| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| tag_id | VARCHAR(32) | PRIMARY KEY | 标签ID |
| page_id | VARCHAR(32) | FOREIGN KEY | 关联页面ID |
| tag_name | VARCHAR(50) | NOT NULL | 标签名称 |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 创建时间 |

### 13.5 knowledge_page_attachment（页面附件表）
| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| attachment_id | VARCHAR(32) | PRIMARY KEY | 附件ID |
| page_id | VARCHAR(32) | FOREIGN KEY | 关联页面ID |
| file_name | VARCHAR(255) | NOT NULL | 文件名 |
| file_path | VARCHAR(500) | NOT NULL | 文件路径 |
| file_size | BIGINT | | 文件大小 |
| file_type | VARCHAR(100) | | 文件类型 |
| created_by | VARCHAR(32) | FOREIGN KEY | 创建人ID |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 创建时间 |

### 13.6 knowledge_page_comment（页面评论表）
| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| comment_id | VARCHAR(32) | PRIMARY KEY | 评论ID |
| page_id | VARCHAR(32) | FOREIGN KEY | 关联页面ID |
| parent_id | VARCHAR(32) | FOREIGN KEY | 父评论ID |
| content | TEXT | NOT NULL | 评论内容 |
| created_by | VARCHAR(32) | FOREIGN KEY | 创建人ID |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 创建时间 |

### 13.7 knowledge_page_favorite（页面收藏表）
| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| favorite_id | VARCHAR(32) | PRIMARY KEY | 收藏ID |
| page_id | VARCHAR(32) | FOREIGN KEY | 关联页面ID |
| user_id | VARCHAR(32) | FOREIGN KEY | 用户ID |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 创建时间 |

### 13.8 knowledge_page_permission（页面权限表）
| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| permission_id | VARCHAR(32) | PRIMARY KEY | 权限ID |
| page_id | VARCHAR(32) | FOREIGN KEY | 关联页面ID |
| user_id | VARCHAR(32) | | 用户ID |
| role_id | VARCHAR(32) | | 角色ID |
| permissions | VARCHAR(100) | NOT NULL | 权限列表 |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 创建时间 |

---

## 十四、数据报表插件表（类似Jira）

### 14.1 report_plugin（报表插件表）
| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| plugin_id | VARCHAR(32) | PRIMARY KEY | 插件ID |
| name | VARCHAR(100) | NOT NULL | 插件名称 |
| description | TEXT | | 插件描述 |
| type | VARCHAR(50) | | 插件类型 |
| icon | VARCHAR(255) | | 插件图标 |
| config | JSON | | 插件配置 |
| status | VARCHAR(32) | DEFAULT 'ENABLED' | 状态 |
| created_by | VARCHAR(32) | FOREIGN KEY | 创建人ID |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 创建时间 |
| updated_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 更新时间 |

### 14.2 report_template（报表模板表）
| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| template_id | VARCHAR(32) | PRIMARY KEY | 模板ID |
| name | VARCHAR(100) | NOT NULL | 模板名称 |
| description | TEXT | | 模板描述 |
| plugin_id | VARCHAR(32) | FOREIGN KEY | 关联插件ID |
| config | JSON | | 模板配置 |
| chart_type | VARCHAR(50) | | 图表类型 |
| category | VARCHAR(50) | | 模板分类 |
| status | VARCHAR(32) | DEFAULT 'ACTIVE' | 状态 |
| created_by | VARCHAR(32) | FOREIGN KEY | 创建人ID |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 创建时间 |

### 14.3 report_instance（报表实例表）
| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| instance_id | VARCHAR(32) | PRIMARY KEY | 实例ID |
| name | VARCHAR(100) | NOT NULL | 报表名称 |
| template_id | VARCHAR(32) | FOREIGN KEY | 关联模板ID |
| project_id | VARCHAR(32) | FOREIGN KEY | 关联项目ID |
| config | JSON | | 报表配置 |
| last_run_at | TIMESTAMP | | 最后运行时间 |
| last_run_status | VARCHAR(32) | | 最后运行状态 |
| schedule_config | JSON | | 调度配置 |
| created_by | VARCHAR(32) | FOREIGN KEY | 创建人ID |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 创建时间 |
| updated_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 更新时间 |

### 14.4 report_schedule（报表调度表）
| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| schedule_id | VARCHAR(32) | PRIMARY KEY | 调度ID |
| instance_id | VARCHAR(32) | FOREIGN KEY | 关联报表实例ID |
| cron_expression | VARCHAR(100) | NOT NULL | Cron表达式 |
| last_executed_at | TIMESTAMP | | 最后执行时间 |
| next_execution_at | TIMESTAMP | NOT NULL | 下次执行时间 |
| status | VARCHAR(32) | DEFAULT 'ACTIVE' | 状态 |
| recipients | JSON | | 接收人列表 |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 创建时间 |

---

## 十五、个性化看板表

### 15.1 kanban_board（看板表）
| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| board_id | VARCHAR(32) | PRIMARY KEY | 看板ID |
| name | VARCHAR(100) | NOT NULL | 看板名称 |
| project_id | VARCHAR(32) | FOREIGN KEY | 关联项目ID |
| description | TEXT | | 看板描述 |
| config | JSON | | 看板配置 |
| is_default | BOOLEAN | DEFAULT false | 是否默认看板 |
| created_by | VARCHAR(32) | FOREIGN KEY | 创建人ID |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 创建时间 |
| updated_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 更新时间 |

### 15.2 kanban_column（看板列表）
| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| column_id | VARCHAR(32) | PRIMARY KEY | 列ID |
| board_id | VARCHAR(32) | FOREIGN KEY | 关联看板ID |
| name | VARCHAR(100) | NOT NULL | 列名称 |
| field_key | VARCHAR(50) | | 关联字段键 |
| field_value | VARCHAR(100) | | 字段值 |
| position | INT | NOT NULL | 列位置顺序 |
| color | VARCHAR(20) | | 列颜色 |
| config | JSON | | 列配置 |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 创建时间 |

### 15.3 kanban_card_field（卡片字段配置表）
| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| field_id | VARCHAR(32) | PRIMARY KEY | 字段配置ID |
| board_id | VARCHAR(32) | FOREIGN KEY | 关联看板ID |
| field_key | VARCHAR(50) | NOT NULL | 字段键 |
| field_label | VARCHAR(100) | | 字段显示标签 |
| display_position | INT | NOT NULL | 显示位置 |
| visible | BOOLEAN | DEFAULT true | 是否可见 |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 创建时间 |

### 15.4 kanban_view（看板视图表）
| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| view_id | VARCHAR(32) | PRIMARY KEY | 视图ID |
| board_id | VARCHAR(32) | FOREIGN KEY | 关联看板ID |
| name | VARCHAR(100) | NOT NULL | 视图名称 |
| filter_config | JSON | | 筛选配置 |
| sort_config | JSON | | 排序配置 |
| is_default | BOOLEAN | DEFAULT false | 是否默认视图 |
| shared | BOOLEAN | DEFAULT false | 是否共享 |
| created_by | VARCHAR(32) | FOREIGN KEY | 创建人ID |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 创建时间 |

---

## 十六、硬件电子工程师管理表

### 16.1 hardware_project（硬件项目表）
| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| project_id | VARCHAR(32) | PRIMARY KEY | 项目ID |
| name | VARCHAR(100) | NOT NULL | 项目名称 |
| description | TEXT | | 项目描述 |
| status | VARCHAR(32) | DEFAULT 'PLANNING' | 状态 |
| created_by | VARCHAR(32) | FOREIGN KEY | 创建人ID |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 创建时间 |

### 16.2 hardware_schematic（原理图表）
| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| schematic_id | VARCHAR(32) | PRIMARY KEY | 原理图ID |
| project_id | VARCHAR(32) | FOREIGN KEY | 关联项目ID |
| name | VARCHAR(255) | NOT NULL | 文件名称 |
| file_path | VARCHAR(500) | NOT NULL | 文件路径 |
| file_type | VARCHAR(50) | | 文件类型 |
| version | VARCHAR(20) | DEFAULT '1.0' | 版本号 |
| status | VARCHAR(32) | DEFAULT 'DRAFT' | 状态 |
| thumbnail | VARCHAR(500) | | 缩略图路径 |
| created_by | VARCHAR(32) | FOREIGN KEY | 创建人ID |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 创建时间 |

### 16.3 hardware_pcb（PCB设计表）
| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| pcb_id | VARCHAR(32) | PRIMARY KEY | PCB ID |
| project_id | VARCHAR(32) | FOREIGN KEY | 关联项目ID |
| name | VARCHAR(255) | NOT NULL | 文件名称 |
| file_path | VARCHAR(500) | NOT NULL | 文件路径 |
| layers | INT | | 层数 |
| size | VARCHAR(100) | | 尺寸 |
| version | VARCHAR(20) | DEFAULT '1.0' | 版本号 |
| status | VARCHAR(32) | DEFAULT 'DRAFT' | 状态 |
| thumbnail | VARCHAR(500) | | 缩略图路径 |
| created_by | VARCHAR(32) | FOREIGN KEY | 创建人ID |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 创建时间 |

### 16.4 hardware_bom（硬件BOM表）
| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| bom_id | VARCHAR(32) | PRIMARY KEY | BOM ID |
| project_id | VARCHAR(32) | FOREIGN KEY | 关联项目ID |
| name | VARCHAR(100) | NOT NULL | BOM名称 |
| version | VARCHAR(20) | DEFAULT '1.0' | 版本号 |
| items | JSON | | BOM物料项 |
| status | VARCHAR(32) | DEFAULT 'ACTIVE' | 状态 |
| created_by | VARCHAR(32) | FOREIGN KEY | 创建人ID |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 创建时间 |

---

## 十七、结构工程师管理表

### 17.1 structure_project（结构项目表）
| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| project_id | VARCHAR(32) | PRIMARY KEY | 项目ID |
| name | VARCHAR(100) | NOT NULL | 项目名称 |
| description | TEXT | | 项目描述 |
| status | VARCHAR(32) | DEFAULT 'PLANNING' | 状态 |
| created_by | VARCHAR(32) | FOREIGN KEY | 创建人ID |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 创建时间 |

### 17.2 structure_3d_model（3D模型表）
| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| model_id | VARCHAR(32) | PRIMARY KEY | 模型ID |
| project_id | VARCHAR(32) | FOREIGN KEY | 关联项目ID |
| name | VARCHAR(255) | NOT NULL | 文件名称 |
| file_path | VARCHAR(500) | NOT NULL | 文件路径 |
| file_format | VARCHAR(50) | | 文件格式 |
| version | VARCHAR(20) | DEFAULT '1.0' | 版本号 |
| status | VARCHAR(32) | DEFAULT 'DRAFT' | 状态 |
| thumbnail | VARCHAR(500) | | 缩略图路径 |
| created_by | VARCHAR(32) | FOREIGN KEY | 创建人ID |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 创建时间 |

### 17.3 structure_2d_drawing（2D图纸表）
| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| drawing_id | VARCHAR(32) | PRIMARY KEY | 图纸ID |
| project_id | VARCHAR(32) | FOREIGN KEY | 关联项目ID |
| name | VARCHAR(255) | NOT NULL | 文件名称 |
| file_path | VARCHAR(500) | NOT NULL | 文件路径 |
| file_format | VARCHAR(50) | | 文件格式 |
| version | VARCHAR(20) | DEFAULT '1.0' | 版本号 |
| status | VARCHAR(32) | DEFAULT 'DRAFT' | 状态 |
| thumbnail | VARCHAR(500) | | 缩略图路径 |
| created_by | VARCHAR(32) | FOREIGN KEY | 创建人ID |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 创建时间 |

### 17.4 structure_bom（结构BOM表）
| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| bom_id | VARCHAR(32) | PRIMARY KEY | BOM ID |
| project_id | VARCHAR(32) | FOREIGN KEY | 关联项目ID |
| name | VARCHAR(100) | NOT NULL | BOM名称 |
| version | VARCHAR(20) | DEFAULT '1.0' | 版本号 |
| items | JSON | | BOM物料项 |
| status | VARCHAR(32) | DEFAULT 'ACTIVE' | 状态 |
| created_by | VARCHAR(32) | FOREIGN KEY | 创建人ID |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 创建时间 |

---

## 十八、索引设计

### 18.1 需求表索引
```sql
CREATE INDEX idx_requirements_project_id ON requirements(project_id);
CREATE INDEX idx_requirements_status ON requirements(status);
CREATE INDEX idx_requirements_type ON requirements(type);
```

### 18.2 任务表索引
```sql
CREATE INDEX idx_tasks_project_id ON tasks(project_id);
CREATE INDEX idx_tasks_status ON tasks(status);
CREATE INDEX idx_tasks_assignee_id ON tasks(assignee_id);
CREATE INDEX idx_tasks_sprint_id ON tasks(sprint_id);
```

### 18.3 追溯关系表索引
```sql
CREATE INDEX idx_trace_req_design_req ON traceability_req_design(req_id);
CREATE INDEX idx_trace_req_design_design ON traceability_req_design(design_id);
CREATE INDEX idx_trace_design_test_design ON traceability_design_test(design_id);
CREATE INDEX idx_trace_design_test_case ON traceability_design_test(case_id);
CREATE INDEX idx_trace_item_task_item ON traceability_item_task(item_type, item_id);
CREATE INDEX idx_trace_item_task_task ON traceability_item_task(task_id);
```

### 18.4 ASPICE表索引
```sql
CREATE INDEX idx_aspice_assessment_project ON aspice_assessment(project_id);
CREATE INDEX idx_aspice_assessment_domain ON aspice_assessment(domain_id);
CREATE INDEX idx_aspice_metric_project ON aspice_metric(project_id);
CREATE INDEX idx_aspice_metric_domain ON aspice_metric(domain_id);
CREATE INDEX idx_aspice_project_domain ON aspice_project_domain(project_id, domain_id);
```

### 18.5 审批流表索引
```sql
CREATE INDEX idx_approval_flow_template ON approval_flow(template_id);
CREATE INDEX idx_approval_flow_document ON approval_flow(document_id, document_type);
CREATE INDEX idx_approval_flow_status ON approval_flow(status);
CREATE INDEX idx_approval_node_flow ON approval_node(flow_id);
CREATE INDEX idx_approval_task_node ON approval_task(node_id);
CREATE INDEX idx_approval_task_assignee ON approval_task(assignee_id);
CREATE INDEX idx_approval_task_status ON approval_task(status);
CREATE INDEX idx_approval_delegation_delegator ON approval_delegation(delegator_id);
CREATE INDEX idx_approval_delegation_delegatee ON approval_delegation(delegatee_id);
CREATE INDEX idx_approval_delegation_active ON approval_delegation(is_active);
```

### 18.6 组织架构同步表索引
```sql
CREATE INDEX idx_organization_parent ON organization(parent_id);
CREATE INDEX idx_organization_type ON organization(type);
CREATE INDEX idx_organization_external ON organization(external_id);
CREATE INDEX idx_sync_config_type ON sync_config(type);
CREATE INDEX idx_sync_config_active ON sync_config(is_active);
CREATE INDEX idx_sync_task_config ON sync_task(config_id);
CREATE INDEX idx_sync_task_status ON sync_task(status);
CREATE INDEX idx_sync_log_task ON sync_log(task_id);
CREATE INDEX idx_sync_log_target ON sync_log(target_type, target_id);
```

### 18.7 AI模型配置表索引
```sql
CREATE INDEX idx_ai_model_provider ON ai_model(provider);
CREATE INDEX idx_ai_model_active ON ai_model(is_active);
CREATE INDEX idx_ai_generation_log_model ON ai_generation_log(model_id);
CREATE INDEX idx_ai_generation_log_type ON ai_generation_log(generation_type);
```

### 18.8 国际化表索引
```sql
CREATE INDEX idx_language_active ON language(is_active);
CREATE INDEX idx_user_language_lang ON user_language(lang_code);
```

### 18.9 开源网盘集成表索引
```sql
CREATE INDEX idx_storage_config_type ON storage_config(type);
CREATE INDEX idx_storage_config_active ON storage_config(is_active);
CREATE INDEX idx_storage_config_default ON storage_config(is_default);
CREATE INDEX idx_file_storage_config ON file_storage(config_id);
CREATE INDEX idx_file_storage_status ON file_storage(status);
CREATE INDEX idx_file_storage_created ON file_storage(created_by);
CREATE INDEX idx_file_storage_controlled ON file_storage(controlled);
CREATE INDEX idx_file_storage_iso ON file_storage(iso_file_number);
CREATE INDEX idx_file_permission_file ON file_permission(file_id);
CREATE INDEX idx_file_permission_user ON file_permission(user_id);
CREATE INDEX idx_file_permission_role ON file_permission(role_id);
CREATE INDEX idx_file_version_file ON file_version(file_id);
```

### 18.10 ISO9001文件管理合规表索引
```sql
CREATE INDEX idx_file_distribution_file ON file_distribution(file_id);
CREATE INDEX idx_file_distribution_recipient ON file_distribution(recipient_id);
CREATE INDEX idx_file_distribution_received ON file_distribution(received);
CREATE INDEX idx_file_audit_trail_file ON file_audit_trail(file_id);
CREATE INDEX idx_file_audit_trail_operator ON file_audit_trail(operator_id);
CREATE INDEX idx_file_audit_trail_operation ON file_audit_trail(operation_type);
```

### 18.11 任务模板管理表索引
```sql
CREATE INDEX idx_task_template_category ON task_template(category);
CREATE INDEX idx_task_template_project ON task_template(project_type);
CREATE INDEX idx_task_template_status ON task_template(status);
CREATE INDEX idx_task_template_field_template ON task_template_field(template_id);
CREATE INDEX idx_task_template_version_template ON task_template_version(template_id);
```

### 18.12 知识库管理表索引（类似Confluence）
```sql
CREATE INDEX idx_knowledge_space_key ON knowledge_space(key);
CREATE INDEX idx_knowledge_space_status ON knowledge_space(status);
CREATE INDEX idx_knowledge_page_space ON knowledge_page(space_id);
CREATE INDEX idx_knowledge_page_parent ON knowledge_page(parent_id);
CREATE INDEX idx_knowledge_page_status ON knowledge_page(status);
CREATE INDEX idx_knowledge_page_version_page ON knowledge_page_version(page_id);
CREATE INDEX idx_knowledge_page_tag_page ON knowledge_page_tag(page_id);
CREATE INDEX idx_knowledge_page_tag_name ON knowledge_page_tag(tag_name);
CREATE INDEX idx_knowledge_page_attachment_page ON knowledge_page_attachment(page_id);
CREATE INDEX idx_knowledge_page_comment_page ON knowledge_page_comment(page_id);
CREATE INDEX idx_knowledge_page_comment_parent ON knowledge_page_comment(parent_id);
CREATE INDEX idx_knowledge_page_favorite_user ON knowledge_page_favorite(user_id);
CREATE INDEX idx_knowledge_page_favorite_page ON knowledge_page_favorite(page_id);
CREATE INDEX idx_knowledge_page_permission_page ON knowledge_page_permission(page_id);
```

### 18.13 数据报表插件表索引（类似Jira）
```sql
CREATE INDEX idx_report_plugin_status ON report_plugin(status);
CREATE INDEX idx_report_plugin_type ON report_plugin(type);
CREATE INDEX idx_report_template_plugin ON report_template(plugin_id);
CREATE INDEX idx_report_template_category ON report_template(category);
CREATE INDEX idx_report_instance_template ON report_instance(template_id);
CREATE INDEX idx_report_instance_project ON report_instance(project_id);
CREATE INDEX idx_report_schedule_instance ON report_schedule(instance_id);
CREATE INDEX idx_report_schedule_status ON report_schedule(status);
```

### 18.14 个性化看板表索引
```sql
CREATE INDEX idx_kanban_board_project ON kanban_board(project_id);
CREATE INDEX idx_kanban_board_default ON kanban_board(is_default);
CREATE INDEX idx_kanban_column_board ON kanban_column(board_id);
CREATE INDEX idx_kanban_column_position ON kanban_column(position);
CREATE INDEX idx_kanban_card_field_board ON kanban_card_field(board_id);
CREATE INDEX idx_kanban_view_board ON kanban_view(board_id);
CREATE INDEX idx_kanban_view_shared ON kanban_view(shared);
```

### 18.15 硬件电子工程师管理表索引
```sql
CREATE INDEX idx_hardware_project_status ON hardware_project(status);
CREATE INDEX idx_hardware_schematic_project ON hardware_schematic(project_id);
CREATE INDEX idx_hardware_schematic_status ON hardware_schematic(status);
CREATE INDEX idx_hardware_pcb_project ON hardware_pcb(project_id);
CREATE INDEX idx_hardware_pcb_status ON hardware_pcb(status);
CREATE INDEX idx_hardware_bom_project ON hardware_bom(project_id);
CREATE INDEX idx_hardware_bom_status ON hardware_bom(status);
```

### 18.16 结构工程师管理表索引
```sql
CREATE INDEX idx_structure_project_status ON structure_project(status);
CREATE INDEX idx_structure_3d_model_project ON structure_3d_model(project_id);
CREATE INDEX idx_structure_3d_model_status ON structure_3d_model(status);
CREATE INDEX idx_structure_2d_drawing_project ON structure_2d_drawing(project_id);
CREATE INDEX idx_structure_2d_drawing_status ON structure_2d_drawing(status);
CREATE INDEX idx_structure_bom_project ON structure_bom(project_id);
CREATE INDEX idx_structure_bom_status ON structure_bom(status);
```

### 18.17 产品基线库管理表索引
```sql
CREATE INDEX idx_baseline_repo_product ON baseline_repository(product_id);
CREATE INDEX idx_baseline_repo_active ON baseline_repository(is_active);
CREATE INDEX idx_baseline_version_repo ON baseline_version(repo_id);
CREATE INDEX idx_baseline_version_status ON baseline_version(status);
CREATE INDEX idx_baseline_item_version ON baseline_item(version_id);
CREATE INDEX idx_baseline_item_type ON baseline_item(item_type);
CREATE INDEX idx_change_request_repo ON change_request(repo_id);
CREATE INDEX idx_change_request_status ON change_request(status);
CREATE INDEX idx_change_approval_request ON change_approval(request_id);
CREATE INDEX idx_change_approval_approver ON change_approval(approver_id);
CREATE INDEX idx_baseline_audit_repo ON baseline_audit_log(repo_id);
CREATE INDEX idx_baseline_audit_operation ON baseline_audit_log(operation_type);
```

---

## 十二、数据库初始化脚本

```sql
-- 创建数据库
CREATE DATABASE alm_db WITH ENCODING='UTF8';

-- 创建用户
CREATE USER alm_user WITH PASSWORD 'your_password';
GRANT ALL PRIVILEGES ON DATABASE alm_db TO alm_user;

-- 切换到alm_db
\c alm_db;

-- 创建表空间（可选）
CREATE TABLESPACE alm_tbs LOCATION '/var/lib/postgresql/alm_data';
```

---

**文档版本**: v0.2  
**文档类型**: DDD  
**创建日期**: 2026-05-11  
**作者**: ALM_Opensource Team