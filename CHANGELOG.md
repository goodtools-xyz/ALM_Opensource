
# 版本历史 / Changelog

---

## v1.0.0 (2024-XX-XX)

### 新增功能 / New Features

#### 1. 文档管理模块 / Document Management Module
- 实现文档导入功能，支持 Word (.docx) 和 Excel (.xlsx) 文件解析
- 支持文档条目化管理，将文档内容解析为结构化数据
- 实现文档预览功能，在线查看原始文档
- 支持文档属性信息显示
- Implemented document import function supporting Word (.docx) and Excel (.xlsx) file parsing
- Document itemization management, parsing document content into structured data
- Document preview function for online viewing of original documents
- Support for displaying document attribute information

#### 2. 受控体系文件 / Controlled System Files
- 将原"文档管理"模块重命名为"受控体系文件"
- 实现文件版本管理功能
- 支持文件审计追踪
- 文件归档功能
- Renamed "Document Management" to "Controlled System Files"
- Implemented document version management
- Support for file audit trails
- Document archiving function

#### 3. 项目资料库 / Project Library
- 基于 A-SPICE 过程域设计目录结构
- 初始化目录包括：项目管理、客户需求管理、产品设计和开发、过程设计和开发、项目质量、供应商管理、物流管理
- 产品设计和开发下建立二级目录：系统需求、系统设计、软件需求、软件系统设计、软件详细设计、软件单元测试、软件集成测试、软件合格性测试、系统集成测试、系统合格性测试、软件发布、硬件设计、结构设计、光学设计、机器学习、外包供应商管理、配置管理、研发质量管理
- 为目录设计图标系统，提升可视化体验
- Designed directory structure based on A-SPICE process domains
- Initial directories: Project Management, Customer Requirements Management, Product Design & Development, Process Design & Development, Project Quality, Supplier Management, Logistics Management
- Secondary directories under Product Design & Development with comprehensive coverage
- Icon system for directories to enhance visualization

#### 4. 产品库 / Product Library
- 新建产品库模块
- 支持产品文档分类管理
- 产品版本追溯功能
- New Product Library module
- Support for product document classification management
- Product version traceability

#### 5. 文件上传功能 / File Upload Functionality
- 实现文件上传 API
- 支持选择目标文件夹上传
- 文件元数据记录（文件名、大小、类型、版本等）
- Implemented file upload API
- Support for selecting target folder for upload
- File metadata recording (filename, size, type, version, etc.)

#### 6. 需求管理 / Requirement Management
- 需求导入和保存功能
- 需求状态追踪
- 支持从 Word/Excel 导入需求
- Requirement import and save functionality
- Requirement status tracking
- Support for importing requirements from Word/Excel

---

### 技术实现 / Technical Implementation

#### 后端技术 / Backend Technology
- Spring Boot 3.2.5 框架
- Spring Data JPA 数据访问
- H2 内存数据库（开发环境）
- POI 库进行文档解析
- RESTful API 设计
- Spring Boot 3.2.5 framework
- Spring Data JPA for data access
- H2 in-memory database (development)
- POI library for document parsing
- RESTful API design

#### 前端技术 / Frontend Technology
- Vue.js 3 + Vite 5.x
- Element Plus UI 组件库
- Axios HTTP 客户端
- 响应式设计
- Vue.js 3 + Vite 5.x
- Element Plus UI component library
- Axios HTTP client
- Responsive design

#### 架构设计 / Architecture Design
- 前后端分离架构
- Docker 容器化部署支持
- 模块化设计
- Separation of frontend and backend
- Docker containerization support
- Modular design

---

### Bug修复 / Bug Fixes

1. **导入失败问题** - 修复 InputStream 被多次读取导致的文件解析失败
   - Fixed document import failure caused by InputStream being read multiple times

2. **加载文件夹失败 (500错误)** - 修复 KnowledgeServiceImpl 中的方法调用错误 (getKey() → getSpaceKey())
   - Fixed folder loading 500 error caused by incorrect method call in KnowledgeServiceImpl

3. **Vite 代理配置错误** - 修复代理重写规则导致 API 路径错误
   - Fixed Vite proxy configuration issue causing incorrect API paths

4. **图标显示问题** - 修复非标准 Element Plus 图标导致的显示失败
   - Fixed icon display issues caused by non-standard Element Plus icons

5. **模板结构问题** - 修复 ProjectLibraryView.vue 中缺少的闭合标签
   - Fixed missing closing tags in ProjectLibraryView.vue

---

### 参考标准 / Reference Standards

- **A-SPICE**: 汽车软件过程改进和能力评定标准
- **ISO 26262**: 汽车功能安全标准
- **IATF 16949**: 汽车行业质量管理体系

---

### 部署方式 / Deployment

#### Docker 部署 / Docker Deployment
```bash
# 构建并启动
docker-compose up -d

# 停止服务
docker-compose down
```

#### 手动部署 / Manual Deployment
```bash
# 后端启动
cd backend
mvn spring-boot:run

# 前端启动
cd frontend
npm install
npm run dev
```

---

### 访问地址 / Access URLs

| 服务 / Service | URL |
|---------------|-----|
| 前端应用 / Frontend | http://localhost:3000 |
| 后端API / Backend API | http://localhost:8081 |
| H2数据库控制台 / H2 Console | http://localhost:8081/h2-console |

---

### 许可证 / License

MIT License - 详见 LICENSE 文件

---

*项目基于开源技术，由AI辅助编写*
*Built with open-source technologies, AI-assisted development*
