package com.example.data

import kotlinx.coroutines.flow.Flow

class CmsRepository(private val dao: CmsProjectDao) {
    val allProjects: Flow<List<CmsProject>> = dao.getAllProjects()

    fun getProject(id: Long): Flow<CmsProject?> = dao.getProjectById(id)

    suspend fun insert(project: CmsProject): Long = dao.insertProject(project)

    suspend fun update(project: CmsProject) = dao.updateProject(project)

    suspend fun delete(project: CmsProject) = dao.deleteProject(project)

    suspend fun deleteById(id: Long) = dao.deleteProjectById(id)
}
