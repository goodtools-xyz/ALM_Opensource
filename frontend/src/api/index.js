import axios from 'axios'

const baseURL = '/api'

const api = axios.create({
  baseURL: baseURL,
  timeout: 10000
})

export const hardwareAPI = {
  getProjects: () => api.get('/hardware/projects'),
  createProject: (data) => api.post('/hardware/projects', data),
  updateProject: (id, data) => api.put(`/hardware/projects/${id}`, data),
  deleteProject: (id) => api.delete(`/hardware/projects/${id}`),

  getSchematics: (projectId) => api.get(`/hardware/schematics?projectId=${projectId}`),
  createSchematic: (data) => api.post('/hardware/schematics', data),
  updateSchematic: (id, data) => api.put(`/hardware/schematics/${id}`, data),
  deleteSchematic: (id) => api.delete(`/hardware/schematics/${id}`),
  previewSchematic: (id) => api.get(`/hardware/schematics/${id}/preview`),

  getPcbs: (projectId) => api.get(`/hardware/pcbs?projectId=${projectId}`),
  createPcb: (data) => api.post('/hardware/pcbs', data),
  updatePcb: (id, data) => api.put(`/hardware/pcbs/${id}`, data),
  deletePcb: (id) => api.delete(`/hardware/pcbs/${id}`),
  previewPcb: (id) => api.get(`/hardware/pcbs/${id}/preview`)
}

export const structureAPI = {
  getProjects: () => api.get('/structure/projects'),
  createProject: (data) => api.post('/structure/projects', data),
  updateProject: (id, data) => api.put(`/structure/projects/${id}`, data),
  deleteProject: (id) => api.delete(`/structure/projects/${id}`),

  get3dModels: (projectId) => api.get(`/structure/models?projectId=${projectId}`),
  create3dModel: (data) => api.post('/structure/models', data),
  update3dModel: (id, data) => api.put(`/structure/models/${id}`, data),
  delete3dModel: (id) => api.delete(`/structure/models/${id}`),

  get2dDrawings: (projectId) => api.get(`/structure/drawings?projectId=${projectId}`),
  create2dDrawing: (data) => api.post('/structure/drawings', data),
  update2dDrawing: (id, data) => api.put(`/structure/drawings/${id}`, data),
  delete2dDrawing: (id) => api.delete(`/structure/drawings/${id}`)
}

export const knowledgeAPI = {
  getSpaces: () => api.get('/knowledge/spaces'),
  createSpace: (data) => api.post('/knowledge/spaces', data),
  updateSpace: (id, data) => api.put(`/knowledge/spaces/${id}`, data),
  deleteSpace: (id) => api.delete(`/knowledge/spaces/${id}`),

  getPages: (spaceId) => api.get(`/knowledge/spaces/${spaceId}/pages`),
  getPage: (pageId) => api.get(`/knowledge/pages/${pageId}`),
  createPage: (data) => api.post('/knowledge/pages', data),
  updatePage: (id, data) => api.put(`/knowledge/pages/${id}`, data),
  deletePage: (id) => api.delete(`/knowledge/pages/${id}`),

  getPageVersions: (pageId) => api.get(`/knowledge/pages/${pageId}/versions`)
}

export const reportAPI = {
  getTemplates: () => api.get('/report/templates'),
  createTemplate: (data) => api.post('/report/templates', data),
  updateTemplate: (id, data) => api.put(`/report/templates/${id}`, data),
  deleteTemplate: (id) => api.delete(`/report/templates/${id}`),

  generateReport: (templateId, params) => api.post(`/report/generate/${templateId}`, params),
  getReports: (templateId) => api.get(`/report/instances?templateId=${templateId}`),

  getKanbanBoards: () => api.get('/report/kanban'),
  getKanbanBoard: (boardId) => api.get(`/report/kanban/${boardId}`),
  createKanbanBoard: (data) => api.post('/report/kanban', data),
  updateKanbanBoard: (id, data) => api.put(`/report/kanban/${id}`, data),
  deleteKanban: (id) => api.delete(`/report/kanban/${id}`)
}

export const approvalAPI = {
  getFlows: () => api.get('/approval/flows'),
  createFlow: (data) => api.post('/approval/flows', data),
  updateFlow: (id, data) => api.put(`/approval/flows/${id}`, data),
  deleteFlow: (id) => api.delete(`/approval/flows/${id}`),

  getSteps: (flowId) => api.get(`/approval/flows/${flowId}/steps`),
  createStep: (data) => api.post('/approval/steps', data),
  updateStep: (id, data) => api.put(`/approval/steps/${id}`, data),
  deleteStep: (id) => api.delete(`/approval/steps/${id}`),

  createTask: (data) => api.post('/approval/tasks', data),
  getTask: (taskId) => api.get(`/approval/tasks/${taskId}`),
  getTasksByStatus: (status) => api.get(`/approval/tasks?status=${status}`),
  approveTask: (taskId, data) => api.post(`/approval/tasks/${taskId}/approve`, data),
  rejectTask: (taskId, data) => api.post(`/approval/tasks/${taskId}/reject`, data),
  addAdHocApprover: (taskId, data) => api.post(`/approval/tasks/${taskId}/adhoc`, data),
  delegateTask: (taskId, data) => api.post(`/approval/tasks/${taskId}/delegate`, data),
  withdrawTask: (taskId) => api.post(`/approval/tasks/${taskId}/withdraw`)
}

export const organizationAPI = {
  getOrganizations: () => api.get('/org/organizations'),
  createOrganization: (data) => api.post('/org/organizations', data),
  updateOrganization: (id, data) => api.put(`/org/organizations/${id}`, data),
  deleteOrganization: (id) => api.delete(`/org/organizations/${id}`),

  getEmployees: () => api.get('/org/employees'),
  getEmployee: (empId) => api.get(`/org/employees/${empId}`),
  getEmployeesByOrg: (orgId) => api.get(`/org/organizations/${orgId}/employees`),
  createEmployee: (data) => api.post('/org/employees', data),
  updateEmployee: (id, data) => api.put(`/org/employees/${id}`, data),
  deleteEmployee: (id) => api.delete(`/org/employees/${id}`),

  getRoles: () => api.get('/org/roles'),
  getRole: (roleId) => api.get(`/org/roles/${roleId}`),
  createRole: (data) => api.post('/org/roles', data),
  updateRole: (id, data) => api.put(`/org/roles/${id}`, data),
  deleteRole: (id) => api.delete(`/org/roles/${id}`),

  syncFromExternal: (sourceType, data) => api.post(`/org/sync/${sourceType}`, data)
}

export const storageAPI = {
  getFolders: () => api.get('/storage/folders'),
  createFolder: (data) => api.post('/storage/folders', data),
  updateFolder: (id, data) => api.put(`/storage/folders/${id}`, data),
  deleteFolder: (id) => api.delete(`/storage/folders/${id}`),

  getFiles: () => api.get('/storage/files'),
  getFile: (fileId) => api.get(`/storage/files/${fileId}`),
  getFilesByFolder: (folderId) => api.get(`/storage/folders/${folderId}/files`),
  createFile: (data) => api.post('/storage/files', data),
  uploadFile: (file, folderId, createdBy) => {
    const formData = new FormData()
    formData.append('file', file)
    formData.append('folderId', folderId)
    formData.append('createdBy', createdBy)
    return api.post('/storage/files/upload', formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    })
  },
  uploadFiles: (formData) => {
    return api.post('/storage/files/uploads', formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    })
  },
  updateFile: (id, data) => api.put(`/storage/files/${id}`, data),
  deleteFile: (id) => api.delete(`/storage/files/${id}`),
  archiveFile: (id) => api.post(`/storage/files/${id}/archive`),

  getFileAudit: (fileId) => api.get(`/storage/files/${fileId}/audit`),
  getPreviewUrl: (fileId) => api.get(`/storage/files/${fileId}/preview-url`),
  downloadFile: (fileId) => `/api/storage/files/${fileId}/download`
}

export const templateAPI = {
  getTemplates: () => api.get('/template/templates'),
  getTemplate: (templateId) => api.get(`/template/templates/${templateId}`),
  getTemplatesByCategory: (category) => api.get(`/template/templates/category/${category}`),
  createTemplate: (data) => api.post('/template/templates', data),
  updateTemplate: (id, data) => api.put(`/template/templates/${id}`, data),
  deleteTemplate: (id) => api.delete(`/template/templates/${id}`),

  getFields: (templateId) => api.get(`/template/templates/${templateId}/fields`),
  createField: (data) => api.post('/template/fields', data),
  updateField: (id, data) => api.put(`/template/fields/${id}`, data),
  deleteField: (id) => api.delete(`/template/fields/${id}`)
}

export const traceabilityAPI = {
  getRequirementTrace: (reqId) => api.get(`/traceability/requirement/${reqId}`),
  getDesignTrace: (designId) => api.get(`/traceability/design/${designId}`),
  getTestTrace: (caseId) => api.get(`/traceability/test/${caseId}`),
  getImpactAnalysis: (itemId) => api.get(`/traceability/impact/${itemId}`),
  getTraceMatrix: (projectId) => api.get(`/traceability/matrix?projectId=${projectId || ''}`),
  searchItems: (keyword, type) => api.get(`/traceability/search?keyword=${keyword || ''}&type=${type || ''}`),
  createRelation: (data) => api.post('/traceability/relation', data),
  deleteRelation: (id) => api.delete(`/traceability/relation/${id}`)
}

export const requirementAPI = {
  getAll: (params) => api.get('/requirement', { params }),
  getById: (id) => api.get(`/requirement/${id}`),
  getByReqId: (reqId) => api.get(`/requirement/req/${reqId}`),
  create: (data) => api.post('/requirement', data),
  update: (id, data) => api.put(`/requirement/${id}`, data),
  delete: (id) => api.delete(`/requirement/${id}`),
  generateId: () => api.get('/requirement/generate-id'),
  importPreview: (file) => {
    const formData = new FormData()
    formData.append('file', file)
    return api.post('/requirement/import/preview', formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    })
  },
  importSave: (file) => {
    const formData = new FormData()
    formData.append('file', file)
    return api.post('/requirement/import/save', formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    })
  },
  getImportRecords: () => api.get('/requirement/import/records'),
  getRequirementsByImportId: (importId) => api.get(`/requirement/import/${importId}`),
  previewDocument: (importId) => api.get(`/requirement/import/${importId}/preview`),
  downloadDocument: (importId) => `/api/requirement/import/${importId}/download`
}

export const designAPI = {
  getAll: (params) => api.get('/design', { params }),
  getById: (id) => api.get(`/design/${id}`),
  getByDesignId: (designId) => api.get(`/design/design/${designId}`),
  create: (data) => api.post('/design', data),
  update: (id, data) => api.put(`/design/${id}`, data),
  delete: (id) => api.delete(`/design/${id}`),
  generateId: () => api.get('/design/generate-id')
}

export const testCaseAPI = {
  getAll: (params) => api.get('/testcase', { params }),
  getById: (id) => api.get(`/testcase/${id}`),
  getByCaseId: (caseId) => api.get(`/testcase/case/${caseId}`),
  create: (data) => api.post('/testcase', data),
  update: (id, data) => api.put(`/testcase/${id}`, data),
  delete: (id) => api.delete(`/testcase/${id}`),
  generateId: () => api.get('/testcase/generate-id')
}

export const defectAPI = {
  getAll: (params) => api.get('/defect', { params }),
  getById: (id) => api.get(`/defect/${id}`),
  getByDefectId: (defectId) => api.get(`/defect/defect/${defectId}`),
  create: (data) => api.post('/defect', data),
  update: (id, data) => api.put(`/defect/${id}`, data),
  delete: (id) => api.delete(`/defect/${id}`),
  resolve: (id, resolvedBy) => api.post(`/defect/${id}/resolve`, resolvedBy),
  generateId: () => api.get('/defect/generate-id')
}

export const projectAPI = {
  getAll: () => api.get('/projects'),
  getById: (id) => api.get(`/projects/${id}`),
  getByCode: (code) => api.get(`/projects/code/${code}`),
  create: (data) => api.post('/projects', data),
  update: (id, data) => api.put(`/projects/${id}`, data),
  delete: (id) => api.delete(`/projects/${id}`),
  getRootFolder: (projectId) => api.get(`/projects/${projectId}/folder`)
}