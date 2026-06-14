package com.example.data

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CmsProjectDao {
    @Query("SELECT * FROM projects ORDER BY updatedAt DESC")
    fun getAllProjects(): Flow<List<CmsProject>>

    @Query("SELECT * FROM projects WHERE id = :id")
    fun getProjectById(id: Long): Flow<CmsProject?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProject(project: CmsProject): Long

    @Update
    suspend fun updateProject(project: CmsProject)

    @Delete
    suspend fun deleteProject(project: CmsProject)

    @Query("DELETE FROM projects WHERE id = :id")
    suspend fun deleteProjectById(id: Long)
}

@Database(entities = [CmsProject::class], version = 1, exportSchema = false)
abstract class CmsDatabase : RoomDatabase() {
    abstract fun projectDao(): CmsProjectDao

    companion object {
        @Volatile
        private var INSTANCE: CmsDatabase? = null

        fun getDatabase(context: Context): CmsDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CmsDatabase::class.java,
                    "meta_cms_builder_db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
