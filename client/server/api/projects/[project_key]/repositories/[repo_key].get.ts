import type { RepositoryDetails } from '~/domain/types'

import {
  healthScore,
  kpis,
  sonarQubeData,
  semgrepData,
  phpcsData,
  phpstanData,
  trendData,
  topIssues,
  repositoryHealth,
  projectSummaries,
} from '~/mock/v2/mock-data'


export default defineEventHandler(
  async (event): Promise<RepositoryDetails> => {
    const repoKey = getRouterParam(event, 'repo_key')
    const repository = repositoryHealth.find(({ repository }) => repository === repoKey)

    if (!repository) {
      throw createError({
        statusCode: 404,
        statusMessage: 'Repository not found',
      })
    }

    return {
      healthScore,
      kpis,
      sonarQubeData,
      semgrepData,
      phpcsData,
      phpstanData,
      trendData,
      topIssues,
      repositorySummary: {
                id: repository.repository,
                name: repository.repository,
                description: repository.repository,
                healthScore: projectSummaries[0].healthScore,
                healthStatus: projectSummaries[0].healthStatus,
                grade: projectSummaries[0].grade,
                totalIssues: projectSummaries[0].totalIssues,
                criticalIssues: projectSummaries[0].criticalIssues,
                coverage: projectSummaries[0].coverage,
                lastScan: projectSummaries[0].lastScan,
                trend: projectSummaries[0].trend,
                tools: projectSummaries[0].tools,
             },
    }
  }
)
