import type { ProjectDetails } from '~/domain/types'

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
  async (event): Promise<ProjectDetails> => {
    const projectId = getRouterParam(event, 'project')
    const projectSummary = projectSummaries.find((p) => p.id === projectId)

    if (!projectSummary) {
      throw createError({
        statusCode: 404,
        statusMessage: 'Project ' + projectId + ' not found',
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
      repositoryHealth,
      projectSummary,
    }
  }
)
