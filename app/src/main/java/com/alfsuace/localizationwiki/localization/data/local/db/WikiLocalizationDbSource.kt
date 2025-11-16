package com.alfsuace.localizationwiki.localization.data.local.db

import com.alfsuace.localizationwiki.app.domain.ErrorApp
import com.alfsuace.localizationwiki.app.domain.TIME_CACHE
import com.alfsuace.localizationwiki.localization.domain.WikiLocalization
import org.koin.core.annotation.Single

@Single
class WikiLocalizationDbSource(private val wikiLocalizationDao: WikiLocalizationDao) {

    suspend fun getAllWikis(): Result<List<WikiLocalization>> {
        val wikis = wikiLocalizationDao.getAllWikis()
        wikis.firstOrNull()?.let {
            return if (isInCacheTime(wikis.first().timeStamp)) {
                Result.success(wikis.map { it.toModel() })
            } else {
                Result.failure(ErrorApp.CacheExpiredErrorApp())
            }
        }
        return Result.failure(ErrorApp.DataErrorApp())
    }

    private fun isInCacheTime(date: Long): Boolean {
        return System.currentTimeMillis() - date < TIME_CACHE
    }

    suspend fun saveWikis(wikis: List<WikiLocalization>) {
        val date = System.currentTimeMillis()
        val entities = wikis.map { it.toEntity(date) }
        wikiLocalizationDao.insertWikis(*entities.toTypedArray())
    }

    suspend fun deleteAllWikis() {
        wikiLocalizationDao.deleteAllWikis()
    }
}