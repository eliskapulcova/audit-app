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
    const projectKey = getRouterParam(event, 'project_key')
    const projectSummary = projectSummaries.find((p) => p.id === projectKey)

    if (!projectSummary) {
      throw createError({
        statusCode: 404,
        statusMessage: 'Project ' + projectKey + ' not found',
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