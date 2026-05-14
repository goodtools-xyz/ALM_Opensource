
# ALM-OpenSource - 车载电子项目管理系统

## 项目概述 / Project Overview

ALM-OpenSource 是一个基于开源技术构建的车载电子项目管理系统，旨在解决车载电子产品开发过程中文档管理、需求追溯、项目资料存档等方面的痛点问题。

ALM-OpenSource is an automotive electronics project management system built on open-source technologies. It aims to address pain points in document management, requirement traceability, and project data archiving during automotive electronics product development.

---

## 项目背景 / Project Background

### 行业痛点 / Industry Challenges

在车载电子产品开发过程中，传统的项目管理系统、文档管理系统、ALM和PLM系统面临以下问题：

During automotive electronics product development, traditional project management systems, document management systems, ALM, and PLM systems face the following challenges:

| 问题 / Issue | 描述 / Description |
|-------------|-------------------|
| 部门墙 / Siloed Departments | 不同部门使用独立系统，信息无法有效共享 |
| | Different departments use separate systems, preventing effective information sharing |
| 权限设置复杂 / Complex Permissions | 权限配置繁琐，导致文档访问受限 |
| | Cumbersome permission configuration limits document access |
| 资料存档缺失 / Missing Archives | 项目资料分散，难以追溯历史版本 |
| | Project documents are scattered, making historical version tracing difficult |
| 双向追溯不足 / Inadequate Traceability | 无法实现需求、设计、测试的双向追溯 |
| | Cannot achieve bidirectional traceability between requirements, design, and testing |
| 缺乏基线管理 / No Baseline Management | 没有区分资料库和基线库，无法有效管理发布版本 |
| | No distinction between library and baseline repository, making release version management ineffective |

---

## 项目目标 / Project Objectives

1. **统一平台**：提供统一的项目管理和文档管理平台
   - Unified platform for project and document management

2. **双向追溯**：实现需求、设计、测试用例的双向追溯
   - Bidirectional traceability between requirements, design, and test cases

3. **资料管理**：区分资料库和基线库，规范文档版本管理
   - Separate library and baseline repository for standardized document version management

4. **开放共享**：基于开源技术，任何人/企业均可免费部署使用
   - Open-source based, free for anyone/enterprise to deploy and use

---

## 参考标准 / Reference Standards

### A-SPICE (Automotive SPICE)
- 遵循汽车软件过程改进和能力评定标准
- 支持A-SPICE过程域的项目资料管理
- Follows Automotive SPICE standards
- Supports project document management for A-SPICE process domains

### ISO 26262
- 汽车功能安全标准
- 支持安全相关文档的管理和追溯
- Automotive functional safety standard
- Supports management and traceability of safety-related documents

### IATF 16949
- 汽车行业质量管理体系
- 支持质量文档的管理和审计追踪
- Automotive quality management system
- Supports quality document management and audit trails

---

## 技术方案 / Technical Architecture

### 技术栈 / Technology Stack

| 层级 / Tier | 技术 / Technology | 版本 / Version |
|------------|------------------|---------------|
| 前端框架 / Frontend | Vue.js | 3.x |
| UI组件 / UI Components | Element Plus | Latest |
| 构建工具 / Build Tool | Vite | 5.x |
| 后端框架 / Backend | Spring Boot | 3.2.x |
| 数据库 / Database | H2 (开发) / PostgreSQL (生产) | - |
| ORM框架 / ORM | Hibernate | 6.x |
| 容器化 / Container | Docker | Latest |

### 架构设计 / Architecture Design

```
┌─────────────────────────────────────────────────────────────┐
│                      前端层 / Frontend                      │
│  Vue.js 3 + Element Plus + Vite                            │
│  - 项目资料库 / Project Library                            │
│  - 产品库 / Product Library                                │
│  - 需求管理 / Requirement Management                       │
│  - 测试用例 / Test Case Management                         │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                      后端层 / Backend                       │
│  Spring Boot 3.2.x                                         │
│  - RESTful API                                              │
│  - 文档解析服务 (Word/Excel)                                │
│  - 文件存储服务                                             │
│  - 追溯关系服务                                             │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                     数据层 / Database                       │
│  H2 (开发环境) / PostgreSQL (生产环境)                     │
│  - 需求数据 / Requirements                                  │
│  - 设计数据 / Design Documents                              │
│  - 测试用例 / Test Cases                                   │
│  - 文件元数据 / File Metadata                               │
└─────────────────────────────────────────────────────────────┘
```

---

## 核心功能 / Core Features

### 1. 项目资料库 / Project Library
- 基于A-SPICE过程域的目录结构
- 支持文件上传、下载、版本管理
- A-SPICE process domain-based directory structure
- File upload, download, and version management

### 2. 产品库 / Product Library
- 产品文档分类管理
- 产品版本追溯
- Product document classification management
- Product version traceability

### 3. 需求管理 / Requirement Management
- 需求导入（支持Word/Excel）
- 需求条目化管理
- 需求状态追踪
- Requirement import (Word/Excel support)
- Requirement item management
- Requirement status tracking

### 4. 双向追溯 / Bidirectional Traceability
- 需求→设计→测试用例追溯
- 影响分析
- 追溯矩阵展示
- Requirement → Design → Test Case traceability
- Impact analysis
- Traceability matrix display

### 5. 受控体系文件 / Controlled Documents
- 文件版本管理
- 审计追踪
- 文件归档
- Document version management
- Audit trail
- Document archiving

---

## 快速开始 / Quick Start

### 环境要求 / Environment Requirements

- JDK 17+
- Node.js 18+
- Maven 3.8+
- Docker (可选)

### 使用Docker启动 / Start with Docker

```bash
# 构建Docker镜像
docker-compose build

# 启动服务
docker-compose up -d

# 停止服务
docker-compose down
```

### 手动启动 / Manual Start

**后端服务 / Backend:**
```bash
cd backend
mvn spring-boot:run
```

**前端服务 / Frontend:**
```bash
cd frontend
npm install
npm run dev
```

### 访问地址 / Access URLs

| 服务 / Service | URL |
|---------------|-----|
| 前端应用 / Frontend | http://localhost:3000 |
| 后端API / Backend API | http://localhost:8081 |
| H2数据库控制台 / H2 Console | http://localhost:8081/h2-console |

---

## 项目结构 / Project Structure

```
ALM_Opensource/
├── backend/                    # 后端服务
│   ├── src/main/java/         # Java源码
│   ├── src/main/resources/    # 配置文件
│   └── pom.xml               # Maven配置
├── frontend/                  # 前端应用
│   ├── src/                  # Vue源码
│   ├── public/               # 静态资源
│   ├── vite.config.js        # Vite配置
│   └── package.json          # npm配置
├── docker-compose.yml        # Docker Compose配置
├── Dockerfile.backend        # 后端Dockerfile
├── Dockerfile.frontend       # 前端Dockerfile
└── README.md                 # 项目说明文档
```

---

## 许可证 / License

MIT License

详见 LICENSE 文件。

See LICENSE file for details.

---

## 贡献 / Contributing

欢迎提交 Issue 和 Pull Request！

欢迎贡献代码，共同改进这个项目！

Contributions are welcome! Please submit issues and pull requests.

---

## 联系方式 / Contact

如有问题或建议，请通过以下方式联系：

- 提交 GitHub Issue
- 发送邮件至项目维护者

For questions or suggestions:
- Submit GitHub Issues
- Contact the maintainers

---

*项目基于开源技术，由AI辅助编写*
*Built with open-source technologies, AI-assisted development*
